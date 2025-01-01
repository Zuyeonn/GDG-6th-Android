package com.example.mysololife.utils

import android.icu.util.Calendar
import com.google.firebase.auth.FirebaseAuth
import java.text.SimpleDateFormat
import java.util.Locale

class FBAuth {

    companion object{

        private lateinit var auth: FirebaseAuth

        fun getUid() : String {

            auth = FirebaseAuth.getInstance()

            return auth.currentUser?.uid.toString()    // 현재 유저의 uid값을 리턴
        }

        fun getTime() : String {    // 시간 값 가져옴

            val currentDataTime = Calendar.getInstance().time
            val dataFormat = SimpleDateFormat("yyyy.MM.dd HH:mm:ss", Locale.KOREA).format(currentDataTime)

            return dataFormat

        }
    }
}