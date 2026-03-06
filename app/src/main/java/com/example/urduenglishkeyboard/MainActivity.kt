package com.example.urduenglishkeyboard

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.provider.Settings
import android.view.inputmethod.InputMethodManager
import android.widget.TextView
import android.Manifest
import android.content.pm.PackageManager
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.card.MaterialCardView
import com.google.android.material.switchmaterial.SwitchMaterial

class MainActivity : AppCompatActivity() {

    private lateinit var prefs: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        CrashLogger.init(this)
        setContentView(R.layout.activity_main)

        prefs = getSharedPreferences("keyboard_prefs", Context.MODE_PRIVATE)
        val lastCrash = prefs.getString("last_crash", null)
        if (lastCrash != null) {
            android.app.AlertDialog.Builder(this)
                .setTitle("Keyboard Crash Detected")
                .setMessage("Please send this to the developer:\n\n$lastCrash")
                .setPositiveButton("Clear & Copy") { _, _ -> 
                    val clipboard = getSystemService(Context.CLIPBOARD_SERVICE) as android.content.ClipboardManager
                    val clip = android.content.ClipData.newPlainText("Crash Log", lastCrash)
                    clipboard.setPrimaryClip(clip)
                    prefs.edit().remove("last_crash").apply() 
                }
                .setCancelable(false)
                .show()
        }

        if (intent.getBooleanExtra("request_mic_permission", false)) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.RECORD_AUDIO), 101)
            }
        }

        prefs = getSharedPreferences("keyboard_prefs", Context.MODE_PRIVATE)

        val cardEnable = findViewById<MaterialCardView>(R.id.card_enable)
        val cardSelect = findViewById<MaterialCardView>(R.id.card_select)
        
        val switchSound = findViewById<SwitchMaterial>(R.id.switch_sound)
        val switchVibration = findViewById<SwitchMaterial>(R.id.switch_vibration)

        switchSound.isChecked = prefs.getBoolean("sound_enabled", true)
        switchVibration.isChecked = prefs.getBoolean("vibration_enabled", true)

        switchSound.setOnCheckedChangeListener { _, isChecked ->
            prefs.edit().putBoolean("sound_enabled", isChecked).apply()
        }

        switchVibration.setOnCheckedChangeListener { _, isChecked ->
            prefs.edit().putBoolean("vibration_enabled", isChecked).apply()
        }

        cardEnable.setOnClickListener {
            val intent = Intent(Settings.ACTION_INPUT_METHOD_SETTINGS)
            startActivity(intent)
        }

        cardSelect.setOnClickListener {
            val imm = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
            imm.showInputMethodPicker()
        }
    }

    override fun onResume() {
        super.onResume()
        updateSetupStatus()
    }

    override fun onNewIntent(newIntent: Intent) {
        super.onNewIntent(newIntent)
        intent = newIntent
        if (intent.getBooleanExtra("request_mic_permission", false)) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.RECORD_AUDIO), 101)
            }
        }
    }

    private fun updateSetupStatus() {
        val imm = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
        val enabledInputMethodIds = imm.enabledInputMethodList.map { it.id }
        
        val myInputMethodId = "${packageName}/.UrduEnglishKeyboardService"
        
        val isEnabled = enabledInputMethodIds.contains(myInputMethodId)
        val isSelected = Settings.Secure.getString(contentResolver, Settings.Secure.DEFAULT_INPUT_METHOD) == myInputMethodId

        val tvEnableStatus = findViewById<TextView>(R.id.tv_enable_status)
        val tvSelectStatus = findViewById<TextView>(R.id.tv_select_status)

        if (isEnabled) {
            tvEnableStatus.text = "Enabled"
            tvEnableStatus.setTextColor(getColor(android.R.color.holo_green_dark))
        } else {
            tvEnableStatus.text = "Required"
            tvEnableStatus.setTextColor(getColor(android.R.color.darker_gray))
        }

        if (isSelected) {
            tvSelectStatus.text = "Selected (Active)"
            tvSelectStatus.setTextColor(getColor(android.R.color.holo_green_dark))
        } else {
            tvSelectStatus.text = "Required"
            tvSelectStatus.setTextColor(getColor(android.R.color.darker_gray))
        }
    }
}
