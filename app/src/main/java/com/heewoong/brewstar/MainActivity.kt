package com.heewoong.brewstar
//import com.kakao.sdk.user.model.User
//import android.view.View
//import android.widget.ImageView
//import android.widget.TextView
//
//import com.bumptech.glide.Glide
//import com.bumptech.glide.RequestManager
//import com.google.android.gms.common.internal.TelemetryLoggingClient
//import com.kakao.sdk.user.model.User

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.heewoong.brewstar.databinding.ActivityMainBinding
import com.kakao.sdk.auth.model.OAuthToken
import com.kakao.sdk.common.KakaoSdk
import com.kakao.sdk.common.model.AuthErrorCause
import com.kakao.sdk.common.model.ClientError
import com.kakao.sdk.common.model.ClientErrorCause
import com.kakao.sdk.common.util.Utility
import com.kakao.sdk.user.Constants.TAG
import com.kakao.sdk.user.UserApiClient
//import kotlin.jvm.functions.Function1
//import kotlin.jvm.functions.Function2


class MainActivity : AppCompatActivity() {

//    private lateinit var kakaoLoginButton: ImageButton
//
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_main)
//
//        kakaoLoginButton = findViewById(R.id.btnKakaoLogin)
//        val context = this
//        kakaoLoginButton.setOnClickListener {
//            // 카카오 로그인 요청
//            lifecycleScope.launch {
//                try {
//                    // 서비스 코드에서는 간단하게 로그인 요청하고 oAuthToken을 받아올 수 있다.
//                    val oAuthToken = UserApiClient.loginWithKakao(context)
//                    Log.d("MainActivity", "beanbean > $oAuthToken")
//                } catch (error: Throwable) {
//                    if (error is ClientError && error.reason == ClientErrorCause.Cancelled) {
//                        Log.d("MainActivity", "사용자가 명시적으로 취소")
//                    } else {
//                        Log.e("MainActivity", "인증 에러 발생", error)
//                    }
//                }
//            }
//        }







     //새로운 사람 해보기

    private lateinit var binding : ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        // R.layout.activity_main == binding.root 임
        setContentView(binding.root)

        val keyHash = Utility.getKeyHash(this)
        Log.e("Hash", "keyHash: $keyHash")
        /** KakaoSDK init */
        KakaoSdk.init(this, this.getString(R.string.kakao_native_app_key))

        binding.btnKakaoLogin.setOnClickListener {
            kakaoLogin()
        }
        /*
        binding.btnStartKakaoLogout.setOnClickListener {
            kakaoLogout() // 로그아웃
        }
        binding.btnStartKakaoUnlink.setOnClickListener {
            kakaoUnlink() // 연결해제
        }
         */
    }
    private fun kakaoLogin() {
        UserApiClient.instance.accessTokenInfo { tokenInfo, error ->
            if (error != null) {
                // 로그인 필요?
                Toast.makeText(this, "토큰 정보 보기 실패", Toast.LENGTH_SHORT).show()
            } else if (tokenInfo != null) {
                // 토큰 유효성 체크 성공 (필요 시 토큰 갱신됨)
                Toast.makeText(this, "토큰 정보 보기 성공", Toast.LENGTH_SHORT).show()
                UserApiClient.instance.me {user, error ->
                    if(error != null) {
                        Log.e(TAG, "사용자 정보 요청 실패 $error")
                    } else if (user != null) {
                        Log.e(TAG, "로그인 성공했냐? $user")
                        val intent = Intent(this, kakaoLogin::class.java)
                        startActivity(intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP))
                        finish()
                    }
                }
//                val intent = Intent(this, kakaoLogin::class.java)
//                startActivity(intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP))
//                finish()
            }
        }
        val callback: (OAuthToken?, Throwable?) -> Unit = { token, error ->
            if (error != null) {
                when {
                    error.toString() == AuthErrorCause.AccessDenied.toString() -> {
                        Toast.makeText(this, "접근이 거부 됨(동의 취소)", Toast.LENGTH_SHORT).show()
                    }

                    error.toString() == AuthErrorCause.InvalidClient.toString() -> {
                        Toast.makeText(this, "유효하지 않은 앱", Toast.LENGTH_SHORT).show()
                    }

                    error.toString() == AuthErrorCause.InvalidGrant.toString() -> {
                        Toast.makeText(this, "인증 수단이 유효하지 않아 인증할 수 없는 상태", Toast.LENGTH_SHORT)
                            .show()
                    }

                    error.toString() == AuthErrorCause.InvalidRequest.toString() -> {
                        Toast.makeText(this, "요청 파라미터 오류", Toast.LENGTH_SHORT).show()
                    }

                    error.toString() == AuthErrorCause.InvalidScope.toString() -> {
                        Toast.makeText(this, "유효하지 않은 scope ID", Toast.LENGTH_SHORT).show()
                    }

                    error.toString() == AuthErrorCause.ServerError.toString() -> {
                        Toast.makeText(this, "서버 내부 에러", Toast.LENGTH_SHORT).show()
                    }

                    error.toString() == AuthErrorCause.Unauthorized.toString() -> {
                        Toast.makeText(this, "앱이 요청 권한이 없음", Toast.LENGTH_SHORT).show()
                    }

                    else -> { // Unknown
                        Toast.makeText(this, "기타 에러", Toast.LENGTH_SHORT).show()
                    }
                }
            } else if (token != null) {
                Toast.makeText(this, "로그인에 성공하였습니다.", Toast.LENGTH_SHORT).show()
                Log.e(TAG, "로그인 성공 ${token.accessToken}")
                val intent = Intent(this, kakaoLogin::class.java)
                startActivity(intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP))
                finish()
            }
        }
        // 카카오톡이 설치되어 있으면 카카오톡으로 로그인, 아니면 카카오계정으로 로그인
        if (UserApiClient.instance.isKakaoTalkLoginAvailable(this)) {
            UserApiClient.instance.loginWithKakaoTalk(this) { token, error ->
                if (error != null) {
                    // 로그인 실패 부분
                    Toast.makeText(this, "토큰 정보 보기 실패", Toast.LENGTH_SHORT).show()
                    // TextMsg(this, "카카오톡으로 로그인 실패 : ${error}")

                    // 사용자가 카카오톡 설치 후 디바이스 권한 요청 화면에서 로그인을 취소한 경우,
                    // 의도적인 로그인 취소로 보고 카카오계정으로 로그인 시도 없이 로그인 취소로 처리 (예: 뒤로 가기)
                    if (error is ClientError && error.reason == ClientErrorCause.Cancelled) {
                        return@loginWithKakaoTalk
                    }

                    // 카카오톡에 연결된 카카오계정이 없는 경우, 카카오계정(이메일)으로 로그인 시도
                    UserApiClient.instance.loginWithKakaoAccount(this, callback = callback)

                } else if (token != null) {
                    // 로그인 성공 부분
                    //Log.e(TAG, "로그인 성공 ${token.accessToken}")
                    Toast.makeText(this, "토큰 정보 보기 성공", Toast.LENGTH_SHORT).show()
                    startActivity(intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP))
                    finish()
                }
            }
        } else {
            // 카카오 이메일 로그인 (위랑 같음)
            UserApiClient.instance.loginWithKakaoAccount(this, callback = callback)
        }
    }
}