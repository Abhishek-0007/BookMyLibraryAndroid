package com.example.expensemanager.ui

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.WindowManager
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.GridLayoutManager
import com.example.expensemanager.Adapter.CategoryAdapter
import com.example.expensemanager.Adapter.SeatAdapter
import com.example.expensemanager.Interfaces.SeatsOnClick
import com.example.expensemanager.Network.ApiInterface
import com.example.expensemanager.Network.RetrofitHelper
import com.example.expensemanager.Utility.Resource
import com.example.expensemanager.Utility.Status
import com.example.expensemanager.databinding.ActivitySeatBookingBinding
import com.example.expensemanager.extensions.ExtensionMethods
import com.example.expensemanager.models.*
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class SeatBookingActivity : AppCompatActivity(), SeatsOnClick {
    private lateinit var binding:ActivitySeatBookingBinding
    var code = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySeatBookingBinding.inflate(layoutInflater)
        setContentView(binding.root)

        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN)


        binding.back.setOnClickListener { finish() }

        binding.rv.layoutManager = GridLayoutManager(this, 5)

        val intent = getIntent()
        code = intent.getStringExtra("code").toString()
        Log.d("code", code)
        try {
            checkAPI(code)
        }
        catch (e:Exception)
        {}

    }
    @SuppressLint
    fun checkApiRepo(code:String) : MutableLiveData<Resource<ResponseModel<SeatViewModel>>> {
        val api = RetrofitHelper(this).getInstance().create(ApiInterface::class.java)

        val mutableLiveData = MutableLiveData<Resource<ResponseModel<SeatViewModel>>>()
        mutableLiveData.value = Resource<ResponseModel<SeatViewModel>>().loading()
        api.getSeatsByLibraryCode(code).subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                {
                    mutableLiveData.value = Resource<ResponseModel<SeatViewModel>>().success(it)

                },
                {
                    mutableLiveData.value = Resource<ResponseModel<SeatViewModel>>().error(it)
                }
            )

        return mutableLiveData
    }

    @SuppressLint
    fun bookSeatApiRepo(row:Int, seat: Int , code:String) : MutableLiveData<Resource<SeatResponse>> {
        val api = RetrofitHelper(this).getInstance().create(ApiInterface::class.java)
        val req = SeatRequestModel(row = row, seatNum = seat, libraryCode = code);
        val mutableLiveData = MutableLiveData<Resource<SeatResponse>>()
        mutableLiveData.value = Resource<SeatResponse>().loading()
        api.bookSeats(req).subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                {
                    mutableLiveData.value = Resource<SeatResponse>().success(it)

                },
                {
                    mutableLiveData.value = Resource<SeatResponse>().error(it)
                }
            )
        return mutableLiveData
    }

    fun checkAPI(code: String)
    {
        checkApiRepo(code).observe(this){
            when(it.status){
                Status.LOADING -> {
                }
                Status.SUCCESS -> {
                    if(it.code == 200)
                        binding.rv.adapter = SeatAdapter(it.data!!.body, this)
                    Log.d("response: ", it.message.toString())

                }
                Status.ERROR ->{
                    Log.d("error: ", it.message.toString())

                }
            }
        }
    }

    fun bookSeat(row: Int, code: String, seat: Int)
    {
        bookSeatApiRepo(row, seat, code).observe(this){
            when(it.status){
                Status.LOADING -> {
                }
                Status.SUCCESS -> {
                    if(it.code == 200){
                        checkAPI(code)
                    }
                    Log.d("response: ", it.message.toString())
                }
                Status.ERROR ->{
                    Log.d("error: ", it.message.toString())

                }
            }
        }
    }

    override fun onSeatClick(seatNumber: Int, row: Int) {
        val builder: AlertDialog.Builder = this.let {
            AlertDialog.Builder(it)
        }

        builder.setMessage("Do you want to proceed??")
            .setTitle("Please confirm your seat booking")

        builder.apply {
            setPositiveButton("Confirm") { dialog, id ->
                val selectedId = id
                bookSeat(row, code, seatNumber)
                ExtensionMethods().showBottomSheetAfterSeatBooking(this@SeatBookingActivity, "${row}-${seatNumber}", code)
            }
            setNegativeButton("Cancel") { dialog, id ->
                val selectedId = id
            }
        }
        val dialog: AlertDialog = builder.create()

        dialog.show()
    }
}