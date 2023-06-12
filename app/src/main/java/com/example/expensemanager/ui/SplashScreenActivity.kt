package com.example.expensemanager.ui

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.WindowManager
import android.view.animation.Animation
import androidx.activity.viewModels
import com.app.lets_go_splash.OnAnimationListener
import com.app.lets_go_splash.StarterAnimation
import com.example.expensemanager.MainActivity
import com.example.expensemanager.R
import com.example.expensemanager.ViewModels.LiveDataViewModel
import com.example.expensemanager.databinding.ActivitySplashScreenBinding

@SuppressLint("CustomSplashScreen")
class SplashScreenActivity : AppCompatActivity() {
    private lateinit var binding : ActivitySplashScreenBinding
    private val viewModel: LiveDataViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashScreenBinding.inflate(layoutInflater)
        setContentView(binding.root)

        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN)

        val quotes = arrayListOf<String>("No two persons ever read the same book.",
                                                        "If we encounter a man of rare intellect, we should ask him what books he reads.",
                                        "Always read something that will make you look good if you die in the middle of it.",
                        "Anyone who says they have only one life to live must not know how to read a book.",
            "I think of life as a good book. The further you get into it, the more it begins to make sense.",
            "There are many little ways to enlarge your child’s world. Love of books is the best of all.",
            "You can’t buy happiness but you can buy a book poster, and that’s kind of the same thing.",
            "The book you don’t read won’t help.",
            "Great books help you understand, and they help you feel understood.",
            "A good novel tells us the truth about its hero; but a bad novel tells us the truth about its author.",
            "In a good book the best is between the lines.",
                    "Are we not like two volumes of one book?",
            "The books that the world calls immoral are books that show the world its own shame.",
            "The only important thing in a book is the meaning that it has for you.",
            "Reading a book is like re-writing it for yourself.",
            "There’s nothing wrong with reading a book you love over and over."
            )

        binding.tv.setText(quotes[(0..quotes.size-1).random()])

        Handler(Looper.getMainLooper()).postDelayed({
            val intent = Intent(this@SplashScreenActivity, HostActivity::class.java)
            startActivity(intent)
            finish()
        }, 5000)
    }
}