package com.aar.tryhuawei.auth

import android.content.Context

data class Session(
    val idToken: String,
    val displayName: String,
    val email: String,
    val openId: String,
    val unionId: String
)

class SessionManager(context: Context) {
    private val pref = context.getSharedPreferences("session", Context.MODE_PRIVATE)

    val isLogin: Boolean
        get() = pref.getBoolean("is_login", false)

    val session: Session
        get() {
            return Session(
                idToken = pref.getString("id_token", "") ?: "",
                displayName = pref.getString("disp_name", "") ?: "",
                email = pref.getString("email", "") ?: "",
                openId = pref.getString("open_id", "") ?: "",
                unionId = pref.getString("union_id", "") ?: ""
            )
        }

    fun setSession(session: Session) {
        pref.edit()
            .putBoolean("is_login", true)
            .putString("id_token", session.idToken)
            .putString("disp_name", session.displayName)
            .putString("email", session.email)
            .putString("open_id", session.openId)
            .putString("union_id", session.unionId)
            .apply()
    }

    fun clearSession() {
        pref.edit().clear().apply()
    }
}