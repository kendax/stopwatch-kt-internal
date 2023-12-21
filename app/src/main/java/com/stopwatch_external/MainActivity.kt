package com.stopwatch_external

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay
import androidx.compose.ui.graphics.Color
import androidx.compose.material3.IconButton
import androidx.compose.ui.res.painterResource

class Stopwatch {
    private var isRunning = false
    private var startTime: Long = 0
    private var elapsedTime: Long = 0

    fun start() {
        if (!isRunning) {
            isRunning = true
            startTime = System.currentTimeMillis() - elapsedTime
        }
    }

    fun pause() {
        if (isRunning) {
            isRunning = false
            elapsedTime = System.currentTimeMillis() - startTime
        }
    }

    fun reset() {
        isRunning = false
        elapsedTime = 0
    }

    fun getCurrentTime(): Long {
        return if (isRunning) {
            val currentTime = System.currentTimeMillis() - startTime
            if (currentTime >= 3600000) {
                pause()
                currentTime
            } else {
                currentTime
            }
        } else {
            elapsedTime
        }
    }
}

@Composable
fun StopwatchScreen(stopwatch: Stopwatch) {
    var elapsedTime by remember { mutableStateOf(0L) }
    var isRunning by remember { mutableStateOf(false) }

    LaunchedEffect(key1 = stopwatch) {
        while (true) {
            if (isRunning) {
                elapsedTime = stopwatch.getCurrentTime()
            }
            delay(10) // Update every 10 milliseconds (1 centisecond)
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "STOPWATCH",
            fontSize = 36.sp,
            modifier = Modifier.padding(bottom = 16.dp),
            color = Color(2, 132, 199, 255),
        )
        Spacer(modifier = Modifier.height(250.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center
        ) {
            Text(
                text = formatTime(elapsedTime),
                fontSize = 72.sp
            )
        }
        Spacer(modifier = Modifier.height(250.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center
        ) {
            IconButton(
                onClick = {
                    isRunning = !isRunning
                    if (isRunning) {
                        stopwatch.start()
                    } else {
                        stopwatch.pause()
                    }
                }
            ) {
                Icon(
                    painter = if (isRunning) painterResource(R.drawable.pause_24px) else painterResource(R.drawable.play_arrow_24px),
                    contentDescription = "play/pause icon",
                    modifier = Modifier.size(40.dp),
                    tint = Color(2, 132, 199, 255)
                )
            }
            Spacer(modifier = Modifier.width(16.dp))
            IconButton(
                onClick = {
                    stopwatch.reset()
                    elapsedTime = 0
                    isRunning = false
                }
            ) {
                Icon(
                    painter = painterResource(R.drawable.replay_24px),
                    contentDescription = "reset icon",
                    modifier = Modifier.size(33.dp),
                    tint = Color(2, 132, 199, 255)
                )
            }
        }
    }
}

private fun formatTime(timeInMillis: Long): String {
    val centiseconds = timeInMillis / 10
    val seconds = centiseconds / 100
    val minutes = seconds / 60

    return String.format("%02d:%02d.%02d", minutes, seconds % 60, centiseconds % 100)
}

class MainActivity : ComponentActivity() {
    private val stopwatch = Stopwatch()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            StopwatchApp()
        }
    }

    @SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    fun StopwatchApp() {
        Scaffold(
            content = {
                StopwatchScreen(stopwatch = stopwatch)
            }
        )
    }
}
