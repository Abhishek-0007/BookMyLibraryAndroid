package com.example.expensemanager.ui

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.View
import android.view.WindowManager
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.expensemanager.Adapter.LibraryAdapter
import com.example.expensemanager.Interfaces.LibraryOnClick
import com.example.expensemanager.MainActivity
import com.example.expensemanager.models.LibraryBody
import com.example.expensemanager.models.ResponseModel
import com.example.expensemanager.Network.ApiInterface
import com.example.expensemanager.Network.RetrofitHelper
import com.example.expensemanager.Utility.Resource
import com.example.expensemanager.Utility.Status
import com.example.expensemanager.databinding.ActivityPhysicalLibraryBinding
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.Runnable
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class PhysicalLibraryActivity : AppCompatActivity(),LibraryOnClick {
    private lateinit var binding : ActivityPhysicalLibraryBinding
    private var locationByGps:Location = Location("")
    var locationByNetwork:Location = Location("")
    var latitude:Double = 28.4276740745365
    var longitude:Double = 77.52783498236961

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPhysicalLibraryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN)


        binding.rv.layoutManager= LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)

        try {
            if (ContextCompat.checkSelfPermission(applicationContext,
                    Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
            ) {
                ActivityCompat.requestPermissions(this,
                    arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                    101)
                checkAP()
            }
            else{
                checkAP()
            }
        } catch (e: Exception) {

            e.printStackTrace()
        }
        binding.back.setOnClickListener { finish() }

//        try
//        {
//
//        }
//        catch (e : Exception) {
//            Toast.makeText(this, "Check if server is down. Message : ${e.message}", Toast.LENGTH_SHORT).show()
//        }
    }

    fun checkAP()
    {
        checkApiRepo().observe(this){
            when(it.status){
                Status.LOADING -> {
                    binding.loadingLayout.visibility = View.VISIBLE
                }
                Status.SUCCESS -> {
                    Handler(Looper.getMainLooper()).postDelayed({
                        binding.loadingLayout.visibility = View.GONE
                    }, 2000)
                    if(it.code == 200)
                        binding.rv.adapter = LibraryAdapter(it.data!!.body, this, this@PhysicalLibraryActivity, this)
                    Log.d("response: ", it.message.toString())

                }
                Status.ERROR ->{
                    Log.d("error: ", it.message.toString())
                    binding.loadingLayout.visibility = View.GONE
                }
            }
        }
    }

    @SuppressLint
    fun checkApiRepo() : MutableLiveData<Resource<ResponseModel<LibraryBody>>> {
        val api = RetrofitHelper().getInstance().create(ApiInterface::class.java)

        val mutableLiveData = MutableLiveData<Resource<ResponseModel<LibraryBody>>>()
        mutableLiveData.value = Resource<ResponseModel<LibraryBody>>().loading()
        api.getAllLibraries().subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                {
                    mutableLiveData.value = Resource<ResponseModel<LibraryBody>>().success(it)

                },
                {
                    mutableLiveData.value = Resource<ResponseModel<LibraryBody>>().error(it)
                }
            )

        return mutableLiveData
    }

    override fun onCardListener(position: Int, item: Any) {
        val intent = Intent(this, SeatBookingActivity::class.java)
        var model = item as LibraryBody
        intent.putExtra("code", model.libraryCode)
        startActivity(intent)
    }

}