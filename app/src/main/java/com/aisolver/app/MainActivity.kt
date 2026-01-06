package com.aisolver.app

import android.content.Intent
import android.content.SharedPreferences
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    private lateinit var apiKeyEditText: EditText
    private lateinit var saveButton: Button
    private lateinit var startServiceButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        apiKeyEditText = findViewById(R.id.editTextApiKey)
        saveButton = findViewById(R.id.buttonSave)
        startServiceButton = findViewById(R.id.buttonStart)

        // 读取已保存的 Key
        val prefs = getSharedPreferences("ai_solver_prefs", MODE_PRIVATE)
        val savedKey = prefs.getString("deepseek_api_key", "")
        apiKeyEditText.setText(savedKey)

        saveButton.setOnClickListener {
            val key = apiKeyEditText.text.toString().trim()
            if (key.isEmpty()) {
                Toast.makeText(this, "请输入 DeepSeek API Key", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            prefs.edit().putString("deepseek_api_key", key).apply()
            Toast.makeText(this, "API Key 已保存", Toast.LENGTH_SHORT).show()
        }

        startServiceButton.setOnClickListener {
            // 检查悬浮窗权限
            if (!Settings.canDrawOverlays(this)) {
                Toast.makeText(this, "请先开启悬浮窗权限", Toast.LENGTH_LONG).show()
                val intent = Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION, Uri.parse("package:$packageName"))
                startActivity(intent)
                return@setOnClickListener
            }

            // 启动悬浮球服务
            startService(Intent(this, FloatingBallService::class.java))
            Toast.makeText(this, "悬浮球已启动！去其他 App 点击蓝色小球试试", Toast.LENGTH_LONG).show()
        }
    }
}
