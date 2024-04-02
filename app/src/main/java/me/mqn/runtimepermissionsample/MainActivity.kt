package me.mqn.runtimepermissionsample

import android.app.AlarmManager
import android.content.Intent
import android.hardware.SensorPrivacyManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.provider.Settings.ACTION_REQUEST_SCHEDULE_EXACT_ALARM
import android.view.ViewGroup
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.content.getSystemService
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import me.mqn.runtimepermissionsample.Utils.addButton

class MainActivity : AppCompatActivity() {

    @RequiresApi(Build.VERSION_CODES.R)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Check device support
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            val sensorPrivacyManager =
                applicationContext.getSystemService(SensorPrivacyManager::class.java) as SensorPrivacyManager

            val supportsMicrophoneToggle =
                sensorPrivacyManager
                    .supportsSensorToggle(SensorPrivacyManager.Sensors.MICROPHONE)
            val supportsCameraToggle =
                sensorPrivacyManager
                    .supportsSensorToggle(SensorPrivacyManager.Sensors.CAMERA)
        }

        // Register the permissions callback, which handles the user's response to the
        // system permissions dialog. Save the return value, an instance of
        // ActivityResultLauncher. You can use either a val, as shown in this snippet,
        // or a lateinit var in your onAttach() or onCreate() method.
        val requestPermissionLauncher =
            registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted: Boolean ->
                if (isGranted) {
                    // Permission is granted. Continue the action or workflow in your
                    // app.
                } else {
                    // Explain to the user that the feature is unavailable because the
                    // feature requires a permission that the user has denied. At the
                    // same time, respect the user's decision. Don't link to system
                    // settings in an effort to convince the user to change their
                    // decision.
                }
            }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            val alarmManager = getSystemService<AlarmManager>()!!
            when {
                // if permission is granted, proceed with scheduling exact alarms…
                alarmManager.canScheduleExactAlarms() -> {
                    // alarmManager.setExact(...)
                }
                else                                  -> {
                    // ask users to grant the permission in the corresponding settings page
                    startActivity(Intent(ACTION_REQUEST_SCHEDULE_EXACT_ALARM))
                }
            }
        }

        val root = findViewById<ViewGroup>(R.id.main)

        root.addButton("Runtime Permission") {
            val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).apply {
                data = Uri.fromParts("package", this@MainActivity.packageName, null)
            }
            ContextCompat.startActivity(this@MainActivity, intent, null)
        }

        root.addButton("Display Overlays") {
            val intent = Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION).apply {
                data = Uri.fromParts("package", this@MainActivity.packageName, null)
            }
            ContextCompat.startActivity(this@MainActivity, intent, null)
        }

        root.addButton("All files access") {
            val intent = Intent(Settings.ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION).apply {
                data = Uri.fromParts("package", this@MainActivity.packageName, null)
            }
            ContextCompat.startActivity(this@MainActivity, intent, null)
        }

        if (savedInstanceState == null) {
            supportFragmentManager
                .beginTransaction()
                .replace(R.id.main, MainFragment.newInstance())
                .commitNow()
        }
    }
}