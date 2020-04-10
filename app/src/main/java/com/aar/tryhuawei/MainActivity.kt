package com.aar.tryhuawei

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.aar.tryhuawei.locationkit.LocationKitActivity
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        btnAccountKit.setOnClickListener {
            startActivity(Intent(this@MainActivity, LoginActivity::class.java))
        }

        btnLocationKit.setOnClickListener {
            startActivity(Intent(this@MainActivity, LocationKitActivity::class.java))
        }
    }
}
