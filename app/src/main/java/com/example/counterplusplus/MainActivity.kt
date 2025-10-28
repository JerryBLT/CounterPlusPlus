package com.example.counterplusplus

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.counterplusplus.ui.theme.CounterPlusPlusTheme
import kotlinx.coroutines.launch

// MainActivity is the entry point to the app that sets the Compose UI content
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            CounterPlusPlusTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    CounterScreen()
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
// Composable function rendering the main counter UI
// It observes the counter state from the ViewModel and reacts to state changes
@Composable
fun CounterScreen(viewModel: CounterViewModel = viewModel()) {
    // Collect StateFlow from ViewModel as Compose State for reactivity
    val count by viewModel.counter.collectAsState(initial = 0)
    val autoMode by viewModel.isAutoMode.collectAsState(initial = false)
    val interval by viewModel.interval.collectAsState(initial = 3)
    val showSettings by viewModel.showSettings.collectAsState(initial = false)

    // Snackbar host state for showing brief messages to user
    val snackbarHostState = remember { SnackbarHostState() }
    val coroutineScope = rememberCoroutineScope()

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        topBar = { TopAppBar(title = { Text("Counter++ – Reactive Counter") }) }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Display current count
            Text(text = "Count: $count", style = MaterialTheme.typography.headlineMedium)
            Spacer(modifier = Modifier.height(8.dp))

            // Display current Auto Mode status
            Text(text = "Auto Mode: ${if (autoMode) "ON" else "OFF"}")
            Spacer(modifier = Modifier.height(16.dp))

            // Buttons for manual increment, decrement, and reset
            Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                Button(onClick = { viewModel.increment() }) { Text("+1") }
                Button(onClick = { viewModel.decrement() }) { Text("-1") }
                Button(onClick = { viewModel.reset() }) { Text("Reset") }
            }
            Spacer(modifier = Modifier.height(16.dp))

            // Button to toggle Auto Mode
            Button(
                onClick = {
                    viewModel.toggleAutoMode()
                    coroutineScope.launch {
                        snackbarHostState.showSnackbar(
                            if (autoMode) "Auto mode OFF" else "Auto mode ON"
                        )
                    }
                }
            ) {
                Text(if (autoMode) "Stop Auto Mode" else "Start Auto Mode")
            }
            Spacer(modifier = Modifier.height(16.dp))

            // Button to open/close settings screen
            Button(onClick = { viewModel.toggleSettings() }) {
                Text("Settings")
            }

            // Show settings UI if toggled on
            if (showSettings) {
                AutoIncrementSettings(viewModel = viewModel, interval = interval)
            }
        }
    }
}

// Composable rendering the settings UI for adjusting auto-increment interval
@Composable
fun AutoIncrementSettings(viewModel: CounterViewModel, interval: Int) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Spacer(modifier = Modifier.height(12.dp))
        Text("Auto Increment Interval (seconds): $interval")
        Row(
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Decrease interval
            Button(onClick = { viewModel.decreaseInterval() }) { Text("-") }
            // Increase interval
            Button(onClick = { viewModel.increaseInterval() }) { Text("+") }
        }
    }
}
