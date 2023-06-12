package com.example.expensemanager.extensions

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Context.WIFI_SERVICE
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationManager
import android.net.wifi.WifiManager
import android.os.Handler
import android.os.Looper
import android.text.format.Formatter
import android.util.Base64
import android.util.Log
import android.view.View
import android.widget.*
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.ContextCompat.getSystemService
import androidx.core.content.ContextCompat.startActivity
import androidx.lifecycle.MutableLiveData
import com.bumptech.glide.Glide
import com.example.expensemanager.Network.ApiInterface
import com.example.expensemanager.Network.RetrofitHelper
import com.example.expensemanager.R
import com.example.expensemanager.Utility.Resource
import com.example.expensemanager.models.*
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.tasks.Task
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.imageview.ShapeableImageView
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.*


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

    fun showBottomSheetAfterVirtual(context: Context, librarycode:String, otp:String) {
        val dialog = BottomSheetDialog(context)
        dialog.setContentView(R.layout.bottom_sheet_virtual_layout)
        val seatTextView= dialog.findViewById<TextView>(R.id.seatIdDesc)
        val tv1= dialog.findViewById<TextView>(R.id.tv1)
        val tv2= dialog.findViewById<TextView>(R.id.tv2)
        val tv3= dialog.findViewById<TextView>(R.id.tv3)
        val tv4= dialog.findViewById<TextView>(R.id.tv4)
        val share= dialog.findViewById<LinearLayout>(R.id.share)
        val copy= dialog.findViewById<LinearLayout>(R.id.copy)
        val copyInLine= dialog.findViewById<ImageView>(R.id.copyInLine)
        val loadingLayout= dialog.findViewById<RelativeLayout>(R.id.loadingLayout)
        tv1?.setText(otp.get(0).toString())
        tv2?.setText(otp.get(1).toString())
        tv3?.setText(otp.get(2).toString())
        tv4?.setText(otp.get(3).toString())

        share?.setOnClickListener {
            val myIntent = Intent(Intent.ACTION_SEND);
            myIntent.setType("text/plain");
            val sub = "Here is your OTP for virtual library";
            myIntent.putExtra(Intent.EXTRA_SUBJECT,sub);
            myIntent.putExtra(Intent.EXTRA_TEXT,otp);
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

    fun showBottomSheetOnBookCLick(context: Context, book: BookInfo) {
        val dialog = BottomSheetDialog(context)
        dialog.setContentView(R.layout.bottom_sheet_book)
        val seatTextView= dialog.findViewById<TextView>(R.id.title)
        val seatTextView1= dialog.findViewById<TextView>(R.id.libCode)
        val share= dialog.findViewById<LinearLayout>(R.id.share)
        val copy= dialog.findViewById<LinearLayout>(R.id.copy)
        val image= dialog.findViewById<ShapeableImageView>(R.id.qr)
        val copyInLine= dialog.findViewById<ImageView>(R.id.copyInLine)
        val loadingLayout= dialog.findViewById<RelativeLayout>(R.id.loadingLayout)
        seatTextView?.setText(book.bookName)
        seatTextView1?.setText(book.bookAuthor)
        val imageByteArray: ByteArray = Base64.decode(book.bookImage, Base64.DEFAULT)
        Glide.with(context)
            .asBitmap()
            .load(imageByteArray)
            .into(image!!);

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
//        loadingLayout?.visibility = View.VISIBLE
//        Handler(Looper.getMainLooper()).postDelayed({
//            loadingLayout?.visibility = View.GONE
//        }, 2000)
    }
    fun tryGetDistance(item: LibraryBody, locationCoordinates: LocationCoordinates): String {
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
    fun tryGetCurrentLocation(context: Context, activity: Activity): LocationCoordinates {
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
    fun tryGetIPAddress(context: Context) : String{
        val wifiMgr = context.getSystemService(Context.WIFI_SERVICE) as WifiManager
        val wifiInfo = wifiMgr!!.connectionInfo
        val ip = wifiInfo.ipAddress
        return Formatter.formatIpAddress(wifiMgr.connectionInfo.ipAddress)
    }

    fun <T : Any>checkApiRepo(klass: Class<T>, context: Context, key:String) : MutableLiveData<Resource<ResponseModel<T>>> {
        val api = RetrofitHelper(context).getInstance().create(ApiInterface::class.java)

        val mutableLiveData = MutableLiveData<Resource<ResponseModel<T>>>()
        mutableLiveData.value = Resource<ResponseModel<T>>().loading()
        when {
            klass.isAssignableFrom(GenreInfo::class.java) -> {
                api.getSearchResult(key).subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(
                        {
                            mutableLiveData.value = Resource<ResponseModel<T>>().success(it as ResponseModel<T>)

                        },
                        {
                            mutableLiveData.value = Resource<ResponseModel<T>>().error(it)
                        }
                    )
            }
            else -> {
                api.getListOfPopularBooks().subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(
                        {
                            mutableLiveData.value = Resource<ResponseModel<T>>().success(it as ResponseModel<T>)

                        },
                        {
                            mutableLiveData.value = Resource<ResponseModel<T>>().error(it)
                        }
                    )
            }
        }


        return mutableLiveData
    }

    @SuppressLint("SimpleDateFormat")
    fun getDate(calendar: Calendar) : DateModel{
        val date = SimpleDateFormat("dd")
        val date_string = date.format(calendar.getTime())

        val day = SimpleDateFormat("EEE")
        val day_string = day.format(calendar.getTime())

        val currentMonth = LocalDate.now().format(DateTimeFormatter.ofPattern("MMMM"))

        return DateModel(day_string, date_string, currentMonth)
    }
    @SuppressLint("SimpleDateFormat")
    fun Get7Dates(): List<DateModel>{
        val calendar = Calendar.getInstance()
        val datee = SimpleDateFormat("dd")
        val date_string = datee.format(calendar.getTime())
        val month = SimpleDateFormat("MM")
        val month_string = month.format(calendar.getTime()).toInt()
        var i = 0
        val list = arrayListOf<DateModel>()
        while (i < 8)
        {
            val d= date_string.toInt()+i
            calendar.set(2023,month_string,d)
            val date = getDate(calendar)
            i++
            list.add(date)
        }
        return list
    }

    fun Get7DatesStatic(): List<DateModel>{
        val list = arrayListOf<DateModel>()
        val dateFormatter = DateTimeFormatter.ofPattern("dd")
        val dayFormatter = DateTimeFormatter.ofPattern("E")
        val monthFormatter = DateTimeFormatter.ofPattern("MMMM")

        val currentDate = LocalDate.now()

        for (i in 0 until 7) {
            val date = currentDate.plusDays(i.toLong())
            val day = date.format(dayFormatter)
            val formattedDate = date.format(dateFormatter)
            val formattedMonth = date.format(monthFormatter)

            val model = DateModel(day, formattedDate, formattedMonth)
            list.add(model)
        }

        return list
    }
    fun TryGetMonthNumber(month:String) : String?{
        val monthMap = hashMapOf<String, String>(
            "January" to "01",
            "February" to "02",
            "March" to "03",
            "April" to "04",
            "May" to "05",
            "June" to "06",
            "July" to "07",
            "August" to "08",
            "September" to "09",
            "October" to "10",
            "November" to "11",
            "December" to "12"
        )
        return monthMap.get(month)
    }

}