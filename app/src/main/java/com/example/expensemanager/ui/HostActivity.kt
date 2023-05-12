package com.example.expensemanager.ui

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.WindowManager
import androidx.databinding.DataBindingUtil
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import com.example.expensemanager.R
import com.example.expensemanager.databinding.ActivityHostBinding

class HostActivity : AppCompatActivity() {
    private lateinit var binding : ActivityHostBinding
    private lateinit var navHostFragment: NavHostFragment
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
//        binding.search.setOnClickListener {
//            navController.navigate(R.id.searchFragment)
//            binding.bottomNav.selectedItemId = R.id.Search
//        }

        binding.bottomNav.setOnItemSelectedListener {it ->
            when(it.itemId){
                R.id.profile -> {
                    navController.navigate(R.id.settingsFragment)
                    true}

                R.id.home -> {
                    navController.navigate(R.id.mainFragment)
                    true
                }
                R.id.Search -> {
                    navController.navigate(R.id.searchFragment)
                    true
                }
                else -> {false}
            }
        }
    }
}