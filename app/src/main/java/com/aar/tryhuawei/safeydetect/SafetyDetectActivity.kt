package com.aar.tryhuawei.safeydetect

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.aar.tryhuawei.R
import com.huawei.hms.support.api.entity.safetydetect.UserDetectResponse
import com.huawei.hms.support.api.safetydetect.SafetyDetect
import kotlinx.android.synthetic.main.activity_safety_detect.*

class SafetyDetectActivity : AppCompatActivity() {

    companion object {
        private const val APP_ID = "101977939"
        private const val TAG = "SafetyDetectActivity"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_safety_detect)

        btnLogin.setOnClickListener {
            login()
        }
    }

    @SuppressLint("SetTextI18n")
    private fun login() {
        val sd = SafetyDetect.getClient(this)
        SafetyDetect.getClient(this).userDetection(APP_ID)
            .addOnSuccessListener {
                val userDetectResponse = it as UserDetectResponse
                textCaptchaToken.text = "captcha response token:\n${userDetectResponse.responseToken}"
                Toast.makeText(this@SafetyDetectActivity, "fake user detection success", Toast.LENGTH_SHORT).show()
                Log.d(TAG, "resp token: ${userDetectResponse.responseToken}")
            }
            .addOnFailureListener {
                Toast.makeText(this@SafetyDetectActivity, "User Detection Failed", Toast.LENGTH_SHORT).show()
                Log.d(TAG, "user detect failed: $it")
            }
    }
}
