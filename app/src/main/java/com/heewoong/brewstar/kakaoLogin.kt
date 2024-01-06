package com.heewoong.brewstar

import android.content.ContentValues
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.ImageButton
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import com.kakao.sdk.user.UserApiClient

class kakaoLogin : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_kakao_login)

        val logoutBtn = findViewById<ImageButton>(R.id.logout)

        logoutBtn.setOnClickListener{
            UserApiClient.instance.logout { error: Throwable? ->
                if (error != null) {
                    Log.e(ContentValues.TAG, "로그아웃 실패, SDK에서 토큰 삭제됨", error)
                } else {
                    Log.e(ContentValues.TAG, "로그아웃 성공, SDK에서 토큰 삭제됨")
                }
            }
            val intent = Intent(this, MainActivity::class.java)

            // Intent에 필요한 데이터 전달 (옵션)


            // 액티비티 시작
            startActivity(intent)

        }
    }
}