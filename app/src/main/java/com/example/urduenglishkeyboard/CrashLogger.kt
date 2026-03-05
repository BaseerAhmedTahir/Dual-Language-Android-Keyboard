package com.example.urduenglishkeyboard

import android.content.Context
import java.io.PrintWriter
import java.io.StringWriter

class CrashLogger(private val context: Context) : Thread.UncaughtExceptionHandler {
    private val defaultHandler = Thread.getDefaultUncaughtExceptionHandler()

    override fun uncaughtException(t: Thread, e: Throwable) {
        try {
            val sw = StringWriter()
            val pw = PrintWriter(sw)
            e.printStackTrace(pw)
            val stackTrace = sw.toString()

            val prefs = context.getSharedPreferences("keyboard_prefs", Context.MODE_PRIVATE)
            prefs.edit().putString("last_crash", stackTrace).commit()
            
        } catch (ex: Exception) {
            // Ignore
        } finally {
            defaultHandler?.uncaughtException(t, e)
        }
    }
    
    companion object {
        fun init(context: Context) {
            Thread.setDefaultUncaughtExceptionHandler(CrashLogger(context.applicationContext))
        }
    }
}
