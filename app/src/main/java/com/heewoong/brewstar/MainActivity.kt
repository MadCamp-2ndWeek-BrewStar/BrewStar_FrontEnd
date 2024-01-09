package com.heewoong.brewstar

import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
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


class MainActivity : AppCompatActivity() {

    private lateinit var binding : ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater) // binding 만들기
        // R.layout.activity_main == binding.root 임
        setContentView(binding.root)

        supportActionBar?.hide()

        val keyHash = Utility.getKeyHash(this) // hash key 선언
        Log.e("Hash", "keyHash: $keyHash")

        /** KakaoSDK init */
        KakaoSdk.init(this, this.getString(R.string.kakao_native_app_key)) // 초기화, but 뭐하는건진 모르겠음
        // 근데 이 초기화를 내 코드 상 GlobalApplication에서도 하는데, 두 개 중 어떤 게 진짜로 초기화 역할을 하는 건 진 모르겠음
        // GlobalApplication을 없애도 잘 작동하는 걸로 봐서는, 여기서도 초기화 역할을 잘 해주는 듯

        binding.btnKakaoLogin.setOnClickListener {
            kakaoLogin()
        }
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
                        Log.e(TAG, "로그인 성공했냐? $user") // 이 부분은 이미 로그인 정보가 담겨있어서 버튼을 누르면 다시 로그인이 되었을 때 뜸
                        val intent = Intent(this, navigation::class.java)
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
                Log.e(TAG, "로그인 성공 ${token.accessToken}") // 이 부분은 이미 로그인 된 상황에서 뒤로가기 버튼 눌렀다가 다시 kakaoAccount로 continue했을 때 나타남

                UserApiClient.instance.accessTokenInfo { tokenInfo, error ->
                    if (error != null) {
                        Log.e(ContentValues.TAG, "토큰 정보 가져오기 실패 $error")
                    } else if (tokenInfo != null) {
                        Log.i(
                            ContentValues.TAG, "토큰 정보 보기 성공" +
                                    "\n회원번호: ${tokenInfo.id}" +
                                    "\n만료시간: ${tokenInfo.expiresIn} 초"
                        )
                            // SharedPreferences 객체 가져오기
                            val sharedPref = getSharedPreferences("getTokenId", Context.MODE_PRIVATE)
                            val editor = sharedPref.edit()
                            // 데이터 저장
                            editor.putString("tokenId", "${tokenInfo.id}")
                            editor.apply() // 변경 사항을 저장
                    }
                }
                // 결국에는 화면에 전환되는 과정 후에 토큰 정보가 불러와져서, 그래서 전환된 화면에
                // 토큰이 전달이 안 되었던 것인듯.
                Handler(Looper.getMainLooper()).postDelayed({
                    val intent = Intent(this, navigation::class.java)
                    startActivity(intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP))
                    finish()
                }, 1000)
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
                    Handler(Looper.getMainLooper()).postDelayed({
                        startActivity(intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP))
                    }, 1000)
                    UserApiClient.instance.accessTokenInfo { tokenInfo, error ->
                        if (error != null) {
                            Log.e(ContentValues.TAG, "토큰 정보 가져오기 실패 $error")
                        } else if (tokenInfo != null) {
                            Log.i(
                                ContentValues.TAG, "토큰 정보 보기 성공" +
                                        "\n회원번호: ${tokenInfo.id}" +
                                        "\n만료시간: ${tokenInfo.expiresIn} 초"
                            )
                            // SharedPreferences 객체 가져오기
                            val sharedPref = getSharedPreferences("getTokenId", Context.MODE_PRIVATE)
                            val editor = sharedPref.edit()
                            // 데이터 저장
                            editor.putString("tokenId", "${tokenInfo.id}")
                            editor.apply() // 변경 사항을 저장
                        }
                    }
                    finish()
                }
            }
        } else {
            // 카카오 이메일 로그인 (위랑 같음)
            UserApiClient.instance.loginWithKakaoAccount(this, callback = callback)
        }
    }
}