CounterAppCompose - Jetpack Compose version

How to run:
1. Open Android Studio (Electric Eel/Flamingo or later recommended).
2. File -> Open... -> select the folder '/app' inside the project root.
3. Let Gradle sync. If asked install missing SDK components (API 34).
4. Run the app on an emulator or device (minSdk 24).
5. Grant notification permission when prompted on Android 13+.

Notes:
- This project embeds MPAndroidChart via AndroidView for the weekly chart.
- Settings persist in SharedPreferences and alarm uses AlarmManager.
