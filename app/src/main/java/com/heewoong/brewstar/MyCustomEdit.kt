package com.heewoong.brewstar

import android.content.Intent
import android.os.Bundle
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity

class MyCustomEdit : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_my_custom_edit)

        val intent: Intent = getIntent()
        val oldName = intent.getStringExtra("old name")

        var tv_name = findViewById<EditText>(R.id.editPopupName)
        tv_name.setText(oldName)
    }
}