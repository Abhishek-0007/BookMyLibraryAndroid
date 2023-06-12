package com.example.expensemanager.ui

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.WindowManager
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.GridLayoutManager
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
import java.time.LocalDate
import java.time.format.DateTimeFormatter

class SeatBookingActivity : AppCompatActivity(), SeatsOnClick {
    private lateinit var binding:ActivitySeatBookingBinding
    var hallId = 0
    var slot = 0
    var date = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySeatBookingBinding.inflate(layoutInflater)
        setContentView(binding.root)

        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN)

        binding.back.setOnClickListener { finish() }

        binding.rv.layoutManager = GridLayoutManager(this, 5)

        val intent = getIntent()
        hallId = intent.getIntExtra("hallId",0)
        slot = intent.getIntExtra("slot",0)
        date = intent.getStringExtra("date").toString()
        if (date.isEmpty()) date = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MMM-dd"));
        println("date: $date")
        try {
            checkAPI()
        }
        catch (e:Exception)
        {}

    }
    @SuppressLint
    fun seatAPI() : MutableLiveData<Resource<ResponseModel<SeatViewModel>>> {
        val api = RetrofitHelper(this).getInstance().create(ApiInterface::class.java)

        val mutableLiveData = MutableLiveData<Resource<ResponseModel<SeatViewModel>>>()
        mutableLiveData.value = Resource<ResponseModel<SeatViewModel>>().loading()
        api.getSeatsByLibraryCode(hallId, slot, date).subscribeOn(Schedulers.io())
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
    fun bookSeatApiRepo( model: SeatBookRequestModel) : MutableLiveData<Resource<SeatResponse>> {
        val api = RetrofitHelper(this).getInstance().create(ApiInterface::class.java)
        val mutableLiveData = MutableLiveData<Resource<SeatResponse>>()
        mutableLiveData.value = Resource<SeatResponse>().loading()
        api.bookSeats(model).subscribeOn(Schedulers.io())
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

    @SuppressLint("NotifyDataSetChanged")
    fun checkAPI()
    {
        seatAPI().observe(this){
            when(it.status){
                Status.LOADING -> {
                }
                Status.SUCCESS -> {
                    if(it.code == 200)
                        binding.rv.adapter = SeatAdapter(it.data!!.body, this)
                    binding.rv.adapter?.notifyDataSetChanged()
                    Log.d("response: ", it.message.toString())
                }
                Status.ERROR ->{
                    Log.d("error: ", it.message.toString())
                }
            }
        }
    }

    fun bookSeat(model: SeatBookRequestModel)
    {
        bookSeatApiRepo(model).observe(this){
            when(it.status){
                Status.LOADING -> {
                }
                Status.SUCCESS -> {
                    if(it.code == 200){
                        checkAPI()
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
//        val inputFormatter = DateTimeFormatter.ofPattern("yyyy-MMM-dd")
//        val outputFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")

//        val localDate = LocalDate.parse(date, inputFormatter)
//        val outputDate = localDate.format(outputFormatter)
        var d = date.substring(5, (date.substring(5,date.length)).indexOf('-')+5)
        var t = ExtensionMethods().TryGetMonthNumber(d)
        if(t.isNullOrEmpty()) t = "06"
        date = date.replace(d,t)
        val model = SeatBookRequestModel(
            hall_id = hallId.toInt(), seat_id = (row*5+seatNumber), slot_id = slot.toInt(), "test", date
        )
        println(model)
        val builder: AlertDialog.Builder = this.let {
            AlertDialog.Builder(it)
        }

        builder.setMessage("Do you want to proceed??")
            .setTitle("Please confirm your seat booking")

        builder.apply {
            setPositiveButton("Confirm") { dialog, id ->
                val selectedId = id
                bookSeat(model)
                ExtensionMethods().showBottomSheetAfterSeatBooking(this@SeatBookingActivity, "${row}-${seatNumber}", "Library")
            }
            setNegativeButton("Cancel") { dialog, id ->
                val selectedId = id
            }
        }
        val dialog: AlertDialog = builder.create()

        dialog.show()
    }
}