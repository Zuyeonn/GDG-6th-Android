package com.example.mysololife.contentsList

import android.os.Bundle
import android.webkit.WebView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.example.mysololife.R

class ContentShowActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_content_show)

        val getUrl = intent.getStringExtra("url") // url을 넘겨받는다. 잘 받았는지 확인하기 위해서 getUrl이라는 변수 생성.
        //Toast.makeText(this, getUrl, Toast.LENGTH_LONG).show() // 잘 받았는지 getUrl 토스트 메시지 띄움

        val webView : WebView = findViewById(R.id.webView)
        webView.loadUrl(getUrl.toString())  // webView 안에 받아온 getUrl을 집어넣음
    }
}