package com.example.counterapp

import android.media.RingtoneManager
import android.os.Build
import android.os.VibrationEffect
import android.os.Vibrator
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.data.*
import androidx.compose.ui.viewinterop.AndroidView

@Composable
fun HomeScreen(navController: NavHostController, vm: CounterViewModel) {
    val ctx = LocalContext.current
    val today by vm.today.collectAsState()
    val weekly by vm.weekly.collectAsState()

    Scaffold { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp)
        ) {
            Card(shape = RoundedCornerShape(8.dp), modifier = Modifier.fillMaxWidth()) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text("Today: $today clicks", style = MaterialTheme.typography.titleLarge)
                    Spacer(modifier = Modifier.height(8.dp))
                    LinearProgressIndicator(
                        progress = (today.toFloat() / vm.dailyGoal().toFloat()).coerceAtMost(1f)
                    )
                    Spacer(modifier = Modifier.height(12.dp))

                    Button(
                        onClick = {
                            val vib = ctx.getSystemService(android.content.Context.VIBRATOR_SERVICE) as Vibrator
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                                vib.vibrate(VibrationEffect.createOneShot(40, VibrationEffect.DEFAULT_AMPLITUDE))
                            } else {
                                @Suppress("DEPRECATION")
                                vib.vibrate(40)
                            }

                            val notif = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
                            RingtoneManager.getRingtone(ctx, notif).play()

                            vm.addClick()
                        },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("Click Me")
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Text("Weekly", style = MaterialTheme.typography.titleMedium)
            Spacer(modifier = Modifier.height(8.dp))

            AndroidView(
                factory = { ctx ->
                    BarChart(ctx).apply {
                        layoutParams = android.view.ViewGroup.LayoutParams(
                            android.view.ViewGroup.LayoutParams.MATCH_PARENT,
                            400
                        )
                    }
                },
                update = { chart ->
                    val entries = ArrayList<BarEntry>()
                    val values = weekly.values.toList()
                    for (i in values.indices) entries.add(BarEntry(i.toFloat(), values[i].toFloat()))
                    val set = BarDataSet(entries, "Clicks")
                    val data = BarData(set)
                    chart.data = data
                    chart.invalidate()
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(400.dp)
            )
        }
    }
}
