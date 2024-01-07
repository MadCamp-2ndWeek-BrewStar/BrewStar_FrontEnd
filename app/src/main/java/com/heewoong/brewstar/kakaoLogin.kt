package com.heewoong.brewstar

import android.content.ContentValues
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.ImageButton
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
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


        //var binding = ActivityKakaoLoginBinding.inflate(layoutInflater)
        val tab1Btn = findViewById<Button>(R.id.btn_tab1)
        tab1Btn.setOnClickListener {
            Log.w("plz", "이건 눌렸을 텐데")
            setFragment("tab1", tab1())
        }

    }

    private fun setFragment(tag: String, fragment: Fragment) {
        val manager: FragmentManager = supportFragmentManager
        val fragTransaction = manager.beginTransaction()
        val tab1 = manager.findFragmentByTag("tab1")
        Log.w("plz", "여기까지 안 왔나?")

        if (manager.findFragmentByTag(tag) == null) {
            fragTransaction.add(R.id.mainFrameLayout, fragment)
        }

        if (tag == "tab1") {
            if (tab1 != null) {
                Log.w("plz", "여기까지 안 왔나?")
                fragTransaction.show(tab1)
            }
        }

        fragTransaction.commitAllowingStateLoss()
    }
}