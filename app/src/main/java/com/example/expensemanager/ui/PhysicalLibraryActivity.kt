package com.example.expensemanager.ui

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.expensemanager.Adapter.LibraryAdapter
import com.example.expensemanager.models.LibraryBody
import com.example.expensemanager.models.ResponseModel
import com.example.expensemanager.Network.ApiInterface
import com.example.expensemanager.Network.RetrofitHelper
import com.example.expensemanager.Utility.Resource
import com.example.expensemanager.Utility.Status
import com.example.expensemanager.databinding.ActivityPhysicalLibraryBinding
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class PhysicalLibraryActivity : AppCompatActivity() {
    private lateinit var binding : ActivityPhysicalLibraryBinding
    private var locationByGps:Location = Location("")
    var locationByNetwork:Location = Location("")
    var latitude:Double = 28.4276740745365
    var longitude:Double = 77.52783498236961

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPhysicalLibraryBinding.inflate(layoutInflater)
        setContentView(binding.root)
        try {
            if (ContextCompat.checkSelfPermission(applicationContext,
                    Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
            ) {
                ActivityCompat.requestPermissions(this,
                    arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                    101)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        binding.rv.layoutManager= LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        binding.back.setOnClickListener { finish() }

        try
        {
            checkAP()
        }
        catch (e : Exception) {
            Toast.makeText(this, "Check if server is down. Message : ${e.message}", Toast.LENGTH_SHORT).show()
        }
    }
    fun getLocation(){
        try {
            if (ContextCompat.checkSelfPermission(applicationContext,
                    Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
            ) {
                ActivityCompat.requestPermissions(this,
                    arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                    101)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }

        var currentLocation: Location? = null
        lateinit var locationManager: LocationManager
        locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager

        val hasGps = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
//------------------------------------------------------//
        val hasNetwork = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)

        val gpsLocationListener: LocationListener = object : LocationListener {
            override fun onLocationChanged(location: Location) {
                locationByGps= location
            }

            override fun onStatusChanged(provider: String, status: Int, extras: Bundle) {}
            override fun onProviderEnabled(provider: String) {}
            override fun onProviderDisabled(provider: String) {}
        }
//------------------------------------------------------//
        val networkLocationListener: LocationListener = object : LocationListener {
            override fun onLocationChanged(location: Location) {
                locationByNetwork= location
            }

            override fun onStatusChanged(provider: String, status: Int, extras: Bundle) {}
            override fun onProviderEnabled(provider: String) {}
            override fun onProviderDisabled(provider: String) {}
        }

        if (hasGps) {
            locationManager.requestLocationUpdates(
                LocationManager.GPS_PROVIDER,
                5000,
                0F,
                gpsLocationListener
            )
        }
//------------------------------------------------------//
        if (hasNetwork) {
            locationManager.requestLocationUpdates(
                LocationManager.NETWORK_PROVIDER,
                5000,
                0F,
                networkLocationListener
            )
        }

        val lastKnownLocationByGps =
            locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER)
        lastKnownLocationByGps?.let {
            locationByGps = lastKnownLocationByGps
        }
//------------------------------------------------------//
        val lastKnownLocationByNetwork =
            locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER)
        lastKnownLocationByNetwork?.let {
            locationByNetwork = lastKnownLocationByNetwork
        }
//------------------------------------------------------//
        if (locationByGps != null && locationByNetwork != null) {
            if (locationByGps.accuracy > locationByNetwork!!.accuracy) {
                currentLocation = locationByGps
                latitude = currentLocation.latitude
                longitude = currentLocation.longitude
                // use latitude and longitude as per your need
            } else {
                currentLocation = locationByNetwork
                latitude = currentLocation.latitude
                longitude = currentLocation.longitude
                // use latitude and longitude as per your need
            }
        }

    }

    fun checkAP()
    {
        checkApiRepo().observe(this){
            when(it.status){
                Status.LOADING -> {
                }
                Status.SUCCESS -> {
                    if(it.code == 200)
                        binding.rv.adapter = LibraryAdapter(it.data!!.body, this, this@PhysicalLibraryActivity)
                    Log.d("response: ", it.message.toString())

                }
                Status.ERROR ->{
                    Log.d("error: ", it.message.toString())

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

}