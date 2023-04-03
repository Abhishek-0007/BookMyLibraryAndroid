package com.example.expensemanager.extensions

import android.Manifest
import android.app.Activity
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationManager
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.View
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.RelativeLayout
import android.widget.TextView
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.ContextCompat.getSystemService
import androidx.core.content.ContextCompat.startActivity
import com.example.expensemanager.R
import com.example.expensemanager.models.LibraryBody
import com.example.expensemanager.models.LocationCoordinates
import com.example.expensemanager.models.SeatViewModel
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.tasks.Task
import com.google.android.material.bottomsheet.BottomSheetDialog

public class ExtensionMethods {

    fun convertToList(model: SeatViewModel):Array<Int>{
        val result : Array<Int> = arrayOf(0,0,0,0,0)
        result[0] = model.firstSeat
        result[1] = model.secondSeat
        result[2] = model.thirdSeat
        result[3] = model.fourthSeat
        result[4] = model.fifthSeat
        return result
    }
    fun showBottomSheetAfterSeatBooking(context: Context, seatNumber: String, code:String) {
        val dialog = BottomSheetDialog(context)
        dialog.setContentView(R.layout.bottom_sheet_layout)
        val seatTextView= dialog.findViewById<TextView>(R.id.seatIdDesc)
        val libCode= dialog.findViewById<TextView>(R.id.libCode)
        val share= dialog.findViewById<LinearLayout>(R.id.share)
        val copy= dialog.findViewById<LinearLayout>(R.id.copy)
        val copyInLine= dialog.findViewById<ImageView>(R.id.copyInLine)
        val loadingLayout= dialog.findViewById<RelativeLayout>(R.id.loadingLayout)
        seatTextView?.setText(seatNumber)
        libCode?.setText(code)

        share?.setOnClickListener {
            val myIntent = Intent(Intent.ACTION_SEND);
            myIntent.setType("text/plain");
            val body = "123456";
            val sub = "Here is your OTP for library";
            myIntent.putExtra(Intent.EXTRA_SUBJECT,sub);
            myIntent.putExtra(Intent.EXTRA_TEXT,body);
            startActivity(context, Intent.createChooser(myIntent, "Share Using"), null)
            dialog.show()
        }

        copy?.setOnClickListener {
            val clipboard = context.applicationContext.getSystemService(android.content.Context.CLIPBOARD_SERVICE) as ClipboardManager?
            val clip = ClipData.newPlainText("label", "123456")
            clipboard!!.setPrimaryClip(clip)
            Toast.makeText(context, "OTP Copied", Toast.LENGTH_SHORT).show()
        }

        copyInLine?.setOnClickListener {
            val clipboard = context.applicationContext.getSystemService(android.content.Context.CLIPBOARD_SERVICE) as ClipboardManager?
            val clip = ClipData.newPlainText("label", "123456")
            clipboard!!.setPrimaryClip(clip)
            Toast.makeText(context, "OTP Copied", Toast.LENGTH_SHORT).show()
        }

        dialog.show()
        loadingLayout?.visibility = View.VISIBLE
        Handler(Looper.getMainLooper()).postDelayed({
            loadingLayout?.visibility = View.GONE
        }, 2000)
    }

    fun showBottomSheetAfterCategory(context: Context, category: String) {
        val dialog = BottomSheetDialog(context)
        dialog.setContentView(R.layout.bottom_sheet_category)
        val seatTextView= dialog.findViewById<TextView>(R.id.seatIdDesc)
        val share= dialog.findViewById<LinearLayout>(R.id.share)
        val copy= dialog.findViewById<LinearLayout>(R.id.copy)
        val copyInLine= dialog.findViewById<ImageView>(R.id.copyInLine)
        val loadingLayout= dialog.findViewById<RelativeLayout>(R.id.loadingLayout)
        seatTextView?.setText(category)

        share?.setOnClickListener {
            val myIntent = Intent(Intent.ACTION_SEND);
            myIntent.setType("text/plain");
            val body = "123456";
            val sub = "Here is your OTP for library";
            myIntent.putExtra(Intent.EXTRA_SUBJECT,sub);
            myIntent.putExtra(Intent.EXTRA_TEXT,body);
            startActivity(context, Intent.createChooser(myIntent, "Share Using"), null)
            dialog.show()
        }

        copy?.setOnClickListener {
            val clipboard = context.applicationContext.getSystemService(android.content.Context.CLIPBOARD_SERVICE) as ClipboardManager?
            val clip = ClipData.newPlainText("label", "123456")
            clipboard!!.setPrimaryClip(clip)
            Toast.makeText(context, "OTP Copied", Toast.LENGTH_SHORT).show()
        }

        copyInLine?.setOnClickListener {
            val clipboard = context.applicationContext.getSystemService(android.content.Context.CLIPBOARD_SERVICE) as ClipboardManager?
            val clip = ClipData.newPlainText("label", "123456")
            clipboard!!.setPrimaryClip(clip)
            Toast.makeText(context, "OTP Copied", Toast.LENGTH_SHORT).show()
        }

        dialog.show()
        loadingLayout?.visibility = View.VISIBLE
        Handler(Looper.getMainLooper()).postDelayed({
            loadingLayout?.visibility = View.GONE
        }, 2000)
    }
    fun toGetDistance(item: LibraryBody, locationCoordinates: LocationCoordinates): String {
        try{
            var e: Location = Location(LocationManager.GPS_PROVIDER)
            val latitude: Double = e.latitude
//        val latitude:Double = 28.50684271601254
            val longitude: Double = e.longitude
//        val longitude:Double = 77.40796074372773

            val res = FloatArray(10)
            Location.distanceBetween(latitude,
                longitude,
                item.latitude as Double,
                item.latitude as Double,
                res)
            var distance = (res[0] / 1000000).toString().substring(0, 1)
            return "${distance[0]} kms"
        }
        catch (e:Exception){
            return "4 kms"
        }

    }

    fun toGetCurrentLocation(context: Context, activity: Activity): LocationCoordinates {
        try {
            if (ContextCompat.checkSelfPermission(context,
                    Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
            ) {
                ActivityCompat.requestPermissions(activity,
                    arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                    101)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        var locationCoordinates: LocationCoordinates = LocationCoordinates()
        lateinit var fusedLocationClient: FusedLocationProviderClient
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(context)

        var task: Task<Location> = fusedLocationClient.lastLocation

        if (ActivityCompat.checkSelfPermission(context,
                Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                context,
                Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED
        ) {
            return locationCoordinates
        }
        task.addOnSuccessListener {
            try {
                locationCoordinates.latitude = it.latitude
                locationCoordinates.longitude = it.longitude
                Log.d("lat", it.latitude.toString())
                Log.d("lon", it.longitude.toString())
            } catch (e: Exception) {
                Log.d("location fetch error: ", e.message.toString())
            }
        }
        return locationCoordinates
    }
}