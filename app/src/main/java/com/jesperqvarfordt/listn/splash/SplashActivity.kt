package com.jesperqvarfordt.listn.splash

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import com.jesperqvarfordt.listn.home.HomeActivity


class SplashActivity: Activity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val intent = Intent(this, HomeActivity::class.java)
        startActivity(intent)
        finish()
    }
}