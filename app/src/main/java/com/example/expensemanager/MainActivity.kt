package com.example.expensemanager

import android.content.Intent
import android.os.Bundle
import android.view.Gravity
import android.view.MenuItem
import android.view.WindowManager
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.core.view.WindowCompat
import androidx.navigation.NavController
import androidx.navigation.ui.AppBarConfiguration
import com.example.expensemanager.databinding.ActivityMainBinding
import com.example.expensemanager.ui.OrderActivity
import com.example.expensemanager.ui.PhysicalLibraryActivity
import com.example.expensemanager.ui.VirtualLibraryActivity


class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var navController: NavController
    private lateinit var actionBarDrawerToggle: ActionBarDrawerToggle
    var selectedBtn = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        WindowCompat.setDecorFitsSystemWindows(window, false)
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN)

        binding.bottomNav.setOnItemSelectedListener {
            when (it.itemId) {
                R.id.activity_virtual_library -> {
                    startActivity(Intent(this, VirtualLibraryActivity::class.java))
                    true
                }
                R.id.physicalFragment -> {
                    startActivity(Intent(this, PhysicalLibraryActivity::class.java))
                    true
                }
                R.id.orderFragment -> {
                    startActivity(Intent(this, OrderActivity::class.java))
                    true
                }
                else -> {
                    true
                }
            }
        }

        actionBarDrawerToggle =
            ActionBarDrawerToggle(this, binding.myDrawerLayout, R.string.nav_open, R.string.nav_close)

        binding.myDrawerLayout.addDrawerListener(actionBarDrawerToggle)
        actionBarDrawerToggle.syncState()
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        binding.menu.setOnClickListener{
            if(!binding.myDrawerLayout.isDrawerOpen(GravityCompat.START)) binding.myDrawerLayout.openDrawer(
                GravityCompat.START);
            else binding.myDrawerLayout.closeDrawer(GravityCompat.END);
        }

        binding.virtualBtn.setOnClickListener {
            if(selectedBtn.isNotEmpty() && selectedBtn.equals("physical"))
                binding.physicallBtn.setBackgroundResource(R.drawable.gray_round_corner)

            binding.virtualBtn.setBackgroundResource(R.drawable.cyan_round_corner)
            selectedBtn = "virtual"
        }


        binding.physicallBtn.setOnClickListener {
            if(selectedBtn.isNotEmpty() && selectedBtn.equals("virtual"))
                binding.virtualBtn.setBackgroundResource(R.drawable.gray_round_corner)

            binding.physicallBtn.setBackgroundResource(R.drawable.cyan_round_corner)
            selectedBtn = "physical"
        }

        binding.submitBtn.setOnClickListener{
            if (selectedBtn.isNullOrEmpty())
                Toast.makeText(this, "Please pick one of the above options", Toast.LENGTH_SHORT).show()

            when(selectedBtn){
                "physical" -> {startActivity(Intent(this@MainActivity, PhysicalLibraryActivity::class.java))}
                "virtual" -> {startActivity(Intent(this@MainActivity, VirtualLibraryActivity::class.java))}
                
                else -> {
                    Toast.makeText(this, "No options selected", Toast.LENGTH_SHORT).show()}
            }
        }
    }
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return if (actionBarDrawerToggle.onOptionsItemSelected(item)) {
            true
        } else super.onOptionsItemSelected(item)
    }
}