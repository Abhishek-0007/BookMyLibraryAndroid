package com.example.expensemanager.ui

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.WindowManager
import androidx.databinding.DataBindingUtil
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import com.example.expensemanager.R
import com.example.expensemanager.databinding.ActivityHostBinding

class HostActivity : AppCompatActivity() {
    private lateinit var binding : ActivityHostBinding
    private lateinit var navController: NavController
    @SuppressLint("CommitTransaction", "ResourceType")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHostBinding.inflate(layoutInflater)
        val navHostFragment = supportFragmentManager
            .findFragmentById(R.id.navHostFragment) as NavHostFragment
        navController = navHostFragment.navController
        navController.navigate(R.id.mainFragment)
        setContentView(binding.root)
        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN)
        setupBottomBar()
    }
    fun setupBottomBar(){
         binding.bottomBar.setOnItemSelectedListener{
            println("id: $it")
            when(it){
                0 ->
                {
                    navController.navigate(R.id.mainFragment)
                }

                1 -> {
                    navController.navigate(R.id.virtualLibraryActivity)

                }
                2 -> {
                    navController.navigate(R.id.physicalLibraryActivity)

                }
                else -> {navController.navigate(R.id.settingsFragment)}
            }
        }

    }
}