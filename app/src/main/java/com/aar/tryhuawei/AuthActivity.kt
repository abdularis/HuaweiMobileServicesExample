package com.aar.tryhuawei

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import com.huawei.hms.support.hwid.HuaweiIdAuthManager
import com.huawei.hms.support.hwid.request.HuaweiIdAuthParams
import com.huawei.hms.support.hwid.request.HuaweiIdAuthParamsHelper
import kotlinx.android.synthetic.main.activity_auth.*

class AuthActivity : AppCompatActivity() {

    companion object {
        private const val TAG = "LoginActivity"
    }

    private lateinit var sessionManager: SessionManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_auth)

        sessionManager = SessionManager(this)

        btnLogin.setOnClickListener { loginWithHuaweiID() }
        btnLogout.setOnClickListener { logoutHuaweiID() }
    }

    override fun onStart() {
        super.onStart()
        updateTextStatus()
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

                    val session = Session(
                        idToken = huaweiAccount.idToken,
                        displayName = huaweiAccount.displayName,
                        email = huaweiAccount.email
                    )
                    sessionManager.setSession(session)
                    updateTextStatus()
                    Toast.makeText(this, "Login successful", Toast.LENGTH_SHORT).show()

                    Log.d(TAG, "id token ${huaweiAccount.idToken}")
                    Log.d(TAG, "access token ${huaweiAccount.accessToken}")
                    Log.d(TAG, "display name ${huaweiAccount.displayName}")
                    Log.d(TAG, "email ${huaweiAccount.email}")
                    Log.d(TAG, "profile ${huaweiAccount.avatarUriString}")
                    Log.d(TAG, "$huaweiAccount")
                } else {
                    Toast.makeText(this, "Failed to auth with Huawei ID", Toast.LENGTH_SHORT).show()
                    Log.d(TAG, "Failed to auth with Huawei ID")
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
            Toast.makeText(this@AuthActivity, "Logout successful", Toast.LENGTH_SHORT).show()
        }
        logoutTask.addOnFailureListener {
            Toast.makeText(this@AuthActivity, "Logout failed", Toast.LENGTH_SHORT).show()
        }

        sessionManager.clearSession()
        updateTextStatus()
    }

    @SuppressLint("SetTextI18n")
    private fun updateTextStatus() {
        sessionManager.isLogin.let {
            if (it) {
                val loggedInText =
                    "You're logged in\n\n" +
                            "id token: ${sessionManager.session.idToken}"
                textStatus.text = loggedInText
            } else {
                textStatus.text = "You're not logged in"
            }

            btnLogin.visibility = if (it) View.GONE else View.VISIBLE
            btnLogout.visibility = if (!it) View.GONE else View.VISIBLE
        }
    }
}
