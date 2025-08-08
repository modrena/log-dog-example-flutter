package com.modrena.log_dog_flutter.log_dog_flutter

import io.flutter.embedding.android.FlutterActivity
import io.flutter.embedding.engine.FlutterEngine
import android.os.Bundle
import android.util.Log
import com.modrena.logdog.LogDog
import com.modrena.logdog.LogDogConfig
import com.modrena.logdog.LogLevel
import com.modrena.logdog.ui.openLogDogSheet

import io.flutter.plugin.common.MethodChannel

class MainActivity : FlutterActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d("MainActivity", "🔥 onCreate called!")
        if (BuildConfig.LOGDOG_API_KEY == "") {
            throw Exception("Please provide an api-key from your gradle.properties file. Example: LOGDOG_API_KEY=ld-prod-XXX-XXX")
        }
        LogDog.initialize(this)
        val config = LogDogConfig(apiKey =  BuildConfig.LOGDOG_API_KEY,
            logs = true,
            network = true,
            events = true,
            logLevel =  LogLevel.VERBOSE)
        LogDog.start(config)
        LogDog.i("LogDog","Hello from LogDog!")
    }

    override fun configureFlutterEngine(flutterEngine: FlutterEngine) {
        super.configureFlutterEngine(flutterEngine)

        MethodChannel(
            flutterEngine.dartExecutor.binaryMessenger,
            "logdog"
        ).setMethodCallHandler { call, result ->
            when (call.method) {
                "openDebugSheet" -> {
                    openLogDogSheet()
                    result.success(null)
                }
                "closeDebugSheet" -> {
                    result.success(null)
                }
                else -> result.notImplemented()
            }
        }
    }
}

