package com.example.mysololife

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.util.Log
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.mysololife.auth.IntroActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class SplashActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_splash)

        auth = Firebase.auth

        // 현재 유저의 UID 값을 가져와서 로그인 상태 확인
        Handler().postDelayed({
            if (auth.currentUser?.uid == null) {
                // 로그인하지 않은 상태 -> IntroActivity로 이동
                Log.d("SplashActivity", "User not logged in")
                startActivity(Intent(this, IntroActivity::class.java))
            } else {
                // 로그인한 상태 -> MainActivity로 이동
                Log.d("SplashActivity", "User logged in")
                startActivity(Intent(this, MainActivity::class.java))
            }
            finish() // SplashActivity 종료
        }, 3000) // 3초 후 이동
    }
}


//        Handler().postDelayed({
//            startActivity(Intent(this, IntroActivity::class.java))
//            finish()
//        }, 3000)   // Spalsh 화면에서 3초 있다가, Intro 화면으로 이동
//    }
//}