package com.example.counterapp

import android.content.Context
import android.widget.Toast

object NotificationHelper {
    fun send(context: Context, title: String, text: String) {
        Toast.makeText(context, "$title: $text", Toast.LENGTH_SHORT).show()
    }
}
