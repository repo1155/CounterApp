package com.example.counterapp

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(navController: NavHostController, vm: CounterViewModel) {
    var dailyGoal by remember { mutableStateOf(vm.dailyGoal()) }

    Scaffold(
        topBar = { TopAppBar(title = { Text("Settings") }) }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text("Settings Content", style = MaterialTheme.typography.titleLarge)

            // Example: Daily Goal setting
            Text("Daily Goal: $dailyGoal clicks")
            Row(verticalAlignment = androidx.compose.ui.Alignment.CenterVertically) {
                Button(onClick = {
                    dailyGoal += 1
                    // Optionally, update in repo or VM
                }) { Text("+") }
                Spacer(modifier = Modifier.width(8.dp))
                Button(onClick = {
                    if (dailyGoal > 1) dailyGoal -= 1
                    // Optionally, update in repo or VM
                }) { Text("-") }
            }
        }
    }
}
