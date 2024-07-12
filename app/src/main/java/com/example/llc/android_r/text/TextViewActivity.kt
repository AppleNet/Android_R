package com.example.llc.android_r.text

import android.animation.ObjectAnimator
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
        val customTextView = findViewById<DynamicMarqueeTextView>(R.id.customTextView)
//        customTextView.postDelayed({ animation(customTextView) }, 2000)
//        customTextView.setOnClickListener {
//            startActivity(Intent(this, ViewPagerActivity::class.java))
//        }
        val text1 = "这是一个演示如何在超过两行后，开头以省略号显示，并逐字显示在结尾的实时字幕，请看效果，是否符合预期!"
        val text2 = "追加的文本内容会逐字显示直到结束。"
        customTextView.setTextWithDynamicMarquee(text1, 200) // 200ms 的延迟时间

//        customTextView.postDelayed(Runnable {
//
//        }, 3000)
//        for (i in 1..text2.length) {
//            val t = text1.substring(0, i)
//            customTextView.setTextWithDynamicMarquee(t, 0) // 200ms 的延迟时间
//            Thread.sleep(10)
//        }



        // 模拟新的文本追加，延迟几秒后追加新的文本
//        val text2 = "追加的文本内容会逐字显示直到结束。"
        // 模拟新的文本追加，延迟几秒后追加新的文本
//        customTextView.postDelayed(Runnable {
//            customTextView.setTextWithDynamicMarquee(
//                text2,
//                200
//            )
//        }, 1000) // 延迟5秒后追加新的文本

    }

    private fun animation(view: CustomTextView) {
        ObjectAnimator.ofFloat(view, "percent", 0f, 1f).setDuration(5000).start()
    }
}