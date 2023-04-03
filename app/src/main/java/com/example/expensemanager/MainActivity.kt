package com.example.expensemanager
import android.Manifest
import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationManager
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.view.WindowCompat
import androidx.databinding.DataBindingUtil
import com.example.expensemanager.databinding.ActivityMainBinding
import com.example.expensemanager.ui.PhysicalLibraryActivity
import com.example.expensemanager.ui.VirtualLibraryActivity
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.tasks.Task
import com.google.android.material.bottomsheet.BottomSheetDialog
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers


class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    var selectedBtn = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        WindowCompat.setDecorFitsSystemWindows(window, false)
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        setContentView(binding.root)

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
}