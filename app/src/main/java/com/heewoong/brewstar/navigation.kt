package com.heewoong.brewstar

import android.content.ContentValues
import android.content.Intent
import android.os.Bundle
import android.util.Log
import com.google.android.material.tabs.TabLayout
import androidx.viewpager.widget.ViewPager
import androidx.appcompat.app.AppCompatActivity
import com.heewoong.brewstar.ui.main.SectionsPagerAdapter
import com.heewoong.brewstar.databinding.ActivityNavigationBinding
import com.kakao.sdk.user.UserApiClient

class navigation : AppCompatActivity() {


    private lateinit var binding: ActivityNavigationBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityNavigationBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val sectionsPagerAdapter = SectionsPagerAdapter(this, supportFragmentManager)
        val viewPager: ViewPager = binding.viewPager
        viewPager.adapter = sectionsPagerAdapter
        val tabs: TabLayout = binding.tabs
        tabs.setupWithViewPager(viewPager)

        binding.logout.setOnClickListener{
            UserApiClient.instance.logout { error: Throwable? ->
                if (error != null) {
                    Log.e(ContentValues.TAG, "로그아웃 실패, SDK에서 토큰 삭제됨", error)
                } else {
                    Log.e(ContentValues.TAG, "로그아웃 성공, SDK에서 토큰 삭제됨")
                }
            }
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)

        }
//        val fab: FloatingActionButton = binding.fab
//
//        fab.setOnClickListener { view ->
//            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                .setAction("Action", null).show()
//        }
    }
}