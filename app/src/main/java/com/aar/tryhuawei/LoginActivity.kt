package com.aar.tryhuawei

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.huawei.hms.support.hwid.HuaweiIdAuthManager
import com.huawei.hms.support.hwid.request.HuaweiIdAuthParams
import com.huawei.hms.support.hwid.request.HuaweiIdAuthParamsHelper
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        btnLogin.setOnClickListener { loginWithHuaweiID() }
        btnLogout.setOnClickListener { logoutHuaweiID() }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 1000) {
            if (resultCode == Activity.RESULT_CANCELED) {
                Toast.makeText(this, "Login Cancelled", Toast.LENGTH_SHORT).show()
            } else if (resultCode == Activity.RESULT_OK) {
                val authHuaweiIdTask = HuaweiIdAuthManager.parseAuthResultFromIntent(data)
                if (authHuaweiIdTask.isSuccessful) {
                    val huaweiAccount = authHuaweiIdTask.result
                    Log.d("TestMe", "id token ${huaweiAccount.idToken}")
                    Log.d("TestMe", "access token ${huaweiAccount.accessToken}")
                    Log.d("TestMe", "display name ${huaweiAccount.displayName}")
                    Log.d("TestMe", "email ${huaweiAccount.email}")
                    Log.d("TestMe", "profile ${huaweiAccount.avatarUriString}")

                    Log.d("TestMe", "$huaweiAccount")
                    Toast.makeText(this, "Login successful", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(this, "Failed to auth with Huawei ID", Toast.LENGTH_SHORT).show()
                    Log.d("TestMe", "Failed to auth with Huawei ID")
                }
            }
        }
    }

    private fun loginWithHuaweiID() {
        val authParams = HuaweiIdAuthParamsHelper(HuaweiIdAuthParams.DEFAULT_AUTH_REQUEST_PARAM)
            .setEmail()
            .setAccessToken()
            .setProfile()
            .setIdToken()
            .createParams()

        val authService = HuaweiIdAuthManager.getService(this, authParams)
        startActivityForResult(authService.signInIntent, 1000)
    }

    private fun logoutHuaweiID() {
        val authParams = HuaweiIdAuthParamsHelper(HuaweiIdAuthParams.DEFAULT_AUTH_REQUEST_PARAM)
            .createParams()
        val authService = HuaweiIdAuthManager.getService(this, authParams)
        val logoutTask = authService.signOut()
        logoutTask.addOnSuccessListener {
            Toast.makeText(this@LoginActivity, "Logout successful", Toast.LENGTH_SHORT).show()
        }
        logoutTask.addOnFailureListener {
            Toast.makeText(this@LoginActivity, "Logout failed", Toast.LENGTH_SHORT).show()
        }
    }
}
