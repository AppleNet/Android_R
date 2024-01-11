package com.example.llc.android_r.text

import android.animation.ObjectAnimator
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.llc.android_r.R

/**
 * com.example.llc.android_r.text.TextViewActivity
 * @author liulongchao
 * @since 2024/1/10
 */
class TextViewActivity: AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_textview)
        val customTextView = findViewById<CustomTextView>(R.id.customTextView)
        customTextView.postDelayed({ animation(customTextView) }, 2000)
        customTextView.setOnClickListener {
            startActivity(Intent(this, ViewPagerActivity::class.java))
        }
    }

    private fun animation(view: CustomTextView) {
        ObjectAnimator.ofFloat(view, "percent", 0f, 1f).setDuration(5000).start()
    }
}