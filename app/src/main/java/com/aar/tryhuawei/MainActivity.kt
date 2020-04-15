package com.aar.tryhuawei

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.aar.tryhuawei.auth.AuthActivity
import com.aar.tryhuawei.auth.SessionManager
import com.aar.tryhuawei.locationkit.LocationKitActivity
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private lateinit var sessionManager: SessionManager

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        sessionManager = SessionManager(this)

        btnAccountKit.setOnClickListener {
            startActivity(Intent(this@MainActivity, AuthActivity::class.java))
        }

        btnLocationKit.setOnClickListener {
            startActivity(Intent(this@MainActivity, LocationKitActivity::class.java))
        }

        GetTokenAction().getToken(this) {
            textToken.text = "Your push kit token: $it"
            Log.d("TestMe", "hms token: $it")
        }
    }

    override fun onStart() {
        super.onStart()
        updateAccountStatus()
    }

    @SuppressLint("SetTextI18n")
    private fun updateAccountStatus() {
        if (sessionManager.isLogin) {
            val session = sessionManager.session
            val text = "You're logged in\n" +
                    "name: ${session.displayName}\n" +
                    "email: ${session.email}\n" +
                    "openId: ${session.openId}\n" +
                    "unionId: ${session.unionId}"
            textAccount.text = text
        } else {
            textAccount.text = "You're not logged in to huawei ID"
        }
    }
}
