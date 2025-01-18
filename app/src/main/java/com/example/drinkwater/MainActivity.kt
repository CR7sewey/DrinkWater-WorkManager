package com.example.drinkwater

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.work.WorkManager
import com.example.drinkwater.ui.theme.BluePrimary
import com.example.drinkwater.ui.theme.BlueSecondary
import com.example.drinkwater.ui.theme.DrinkWaterTheme
import androidx.work.Constraints
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.NetworkType
import androidx.work.PeriodicWorkRequestBuilder
import java.util.UUID
import java.util.concurrent.TimeUnit

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            DrinkWaterTheme {
                val context = LocalContext.current

                DrinkWater { text ->

                    val workManager = WorkManager.getInstance(context)
                    val constraints = Constraints.Builder().setRequiredNetworkType(NetworkType.CONNECTED).build()
                    var interval: Long
                    try {
                        interval = text.text.toLong()
                    }
                    catch (e: NumberFormatException) {
                        interval = 15
                    }

                    val repeatingRequest = PeriodicWorkRequestBuilder<NotificationWorker>(interval,
                        TimeUnit.SECONDS).setConstraints(constraints).build()//.setConstrains(
                    val workId = UUID.randomUUID().toString()

                    workManager.enqueueUniquePeriodicWork(workId, ExistingPeriodicWorkPolicy.KEEP, repeatingRequest)
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DrinkWater(onClick: (text: TextFieldValue) -> Unit) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Drink Water",
                        color = Color.White,
                        fontSize = 20.sp
                    )
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = BluePrimary
                )
            )
        },
        content = { paddingValues ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(BlueSecondary)
                    .padding(paddingValues)
            ) {
                Spacer(modifier = Modifier.height(32.dp))
                NotificationContent(onClick)
            }
        }
    )
}

@Composable
fun NotificationContent(onClick: (text: TextFieldValue) -> Unit) {
    val intervalState = remember { mutableStateOf(TextFieldValue()) }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            painter = painterResource(id = R.drawable.baseline_water),
            contentDescription = null,
            modifier = Modifier
                .size(150.dp)

        )
        Text(
            text = "Minutes till reminder...",
            fontSize = 18.sp,
            color = Color.White
        )
        Spacer(modifier = Modifier.height(8.dp))
        BasicTextField(
            value = intervalState.value,
            onValueChange = { intervalState.value = it },
            modifier = Modifier
                .background(Color.White)
                .padding(8.dp)
                .fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(16.dp))
        Button(
            colors = ButtonDefaults.buttonColors(containerColor = BluePrimary),
            onClick = { onClick(intervalState.value) },
            modifier = Modifier.align(Alignment.CenterHorizontally)
        ) {
            Text(text = "More Water")
        }
    }
}