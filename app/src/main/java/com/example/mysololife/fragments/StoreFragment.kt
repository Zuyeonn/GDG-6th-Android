package com.example.mysololife.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebView
import com.example.mysololife.R
import com.example.mysololife.databinding.FragmentStoreBinding


class StoreFragment : Fragment() {
    private lateinit var binding : FragmentStoreBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        // fragment_store 레이아웃을 inflate하여 view 객체 생성
        val view = inflater.inflate(R.layout.fragment_store, container, false)

        // WebView 객체를 찾아서 변수에 할당
        val webView: WebView = view.findViewById(R.id.storeWebView)

        // WebView에 URL을 로드하여 지정된 웹사이트를 표시
        webView.loadUrl("https://www.inflearn.com/")

        // Fragment의 View 반환
        return view
    }
}