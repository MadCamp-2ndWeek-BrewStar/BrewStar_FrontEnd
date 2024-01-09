package com.heewoong.brewstar

import android.content.ContentValues.TAG
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.ProgressBar
import android.widget.TextView
import com.google.gson.JsonArray
import com.google.gson.JsonObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class Kotlinflask : AppCompatActivity() {

    val api = RetrofitInterface.create()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_kotlinflask)

        val textView = findViewById<TextView>(R.id.textView)
        val progressBar = findViewById<ProgressBar>(R.id.progressBar)
        val button1 = findViewById<Button>(R.id.button1)

        fun getData() {
            api.getMilk().enqueue(object : Callback<JsonArray> {
                override fun onResponse(call: Call<JsonArray>, response: Response<JsonArray>) {
                    if (response.isSuccessful) {
                        val result = response.body()
                        textView.text = result.toString()
                    } else {
                        // HTTP 요청이 실패한 경우의 처리
                        Log.e(TAG, "HTTP 요청 실패: ${response.code()}")
                    }
                }

                override fun onFailure(call: Call<JsonArray>, t: Throwable) {
                    // 네트워크 오류 또는 요청 실패 시 처리할 코드

                    Log.e(TAG, "네트워크 오류: ${t.message}")
                }
            })
        }

        button1.setOnClickListener {
            getData()
            Log.d("DDDD", api.getMilk().toString())
        }
    }
}