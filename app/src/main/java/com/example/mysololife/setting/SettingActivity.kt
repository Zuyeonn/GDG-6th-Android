package com.example.mysololife.setting

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.mysololife.R
import com.example.mysololife.auth.IntroActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class SettingActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_setting)

        auth = Firebase.auth

        // logoutBtn 클릭 리스너 설정
        val logoutBtn: Button = findViewById(R.id.logoutBtn)
        logoutBtn.setOnClickListener {

            // Firebase 로그아웃
            auth.signOut()

            // 로그아웃 완료 토스트 메시지
            Toast.makeText(this, "로그아웃", Toast.LENGTH_LONG).show()

            // IntroActivity로 화면 전환
            val intent = Intent(this, IntroActivity::class.java)

            // Intent 플래그 설정: 새로운 작업으로 시작하고, 기존의 모든 Activity를 종료
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)  // IntroActivity 시작
        }
    }
}