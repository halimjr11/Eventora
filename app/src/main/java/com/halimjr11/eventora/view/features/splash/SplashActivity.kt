package com.halimjr11.eventora.view.features.splash

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.halimjr11.eventora.databinding.ActivitySplashBinding
import com.halimjr11.eventora.view.MainActivity

@SuppressLint("CustomSplashScreen")
class SplashActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySplashBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashBinding.inflate(layoutInflater)
        enableEdgeToEdge()
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(binding.main.id)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        binding.splashLogo.run {
            translationY = -500f
            alpha = 0f
            scaleX = 0.6f
            scaleY = 0.6f

            animate()
                .translationY(0f)
                .alpha(1f)
                .scaleX(1f)
                .scaleY(1f)
                .setInterpolator(android.view.animation.BounceInterpolator())
                .setDuration(2000L)
                .withEndAction {
                    animate()
                        .alpha(0f)
                        .scaleX(0.3f)
                        .scaleY(0.3f)
                        .setStartDelay(1000L)
                        .setDuration(700L)
                        .withEndAction {
                            Intent(this@SplashActivity, MainActivity::class.java).run {
                                flags =
                                    Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                                startActivity(this)
                            }
                        }
                        .start()
                }
                .start()
        }

    }
}