package com.example.expensemanager.ui

import android.annotation.SuppressLint
import android.app.Dialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.View
import android.view.Window
import android.view.WindowManager
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.GridLayoutManager
import com.example.expensemanager.Adapter.CategoryAdapter
import com.example.expensemanager.Adapter.LibraryAdapter
import com.example.expensemanager.Interfaces.CategoryOnClick
import com.example.expensemanager.models.CategoryBody
import com.example.expensemanager.models.ResponseModel
import com.example.expensemanager.Network.ApiInterface
import com.example.expensemanager.Network.RetrofitHelper
import com.example.expensemanager.R
import com.example.expensemanager.Utility.Resource
import com.example.expensemanager.Utility.Status
import com.example.expensemanager.databinding.ActivityVirtualLibraryBinding
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class VirtualLibraryActivity : AppCompatActivity(), CategoryOnClick {
    private lateinit var binding: ActivityVirtualLibraryBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityVirtualLibraryBinding.inflate(layoutInflater)
        setContentView(binding.root)
        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN)

        binding.rv.layoutManager = GridLayoutManager(this, 3)
        try{
            checkAP()
        }
        catch (e : Exception){
            Toast.makeText(this, "Server down maybe", Toast.LENGTH_SHORT).show()
        }
        binding.back.setOnClickListener { finish() }
    }

    @SuppressLint
    fun checkApiRepo() : MutableLiveData<Resource<ResponseModel<CategoryBody>>> {
        val api = RetrofitHelper().getInstance().create(ApiInterface::class.java)

        val mutableLiveData = MutableLiveData<Resource<ResponseModel<CategoryBody>>>()
        mutableLiveData.value = Resource<ResponseModel<CategoryBody>>().loading()
        api.getAllCategories().subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                {
                    mutableLiveData.value = Resource<ResponseModel<CategoryBody>>().success(it)

                },
                {
                    mutableLiveData.value = Resource<ResponseModel<CategoryBody>>().error(it)
                }
            )

        return mutableLiveData
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
                        binding.rv.adapter = CategoryAdapter(it.data!!.body, this)
                    Log.d("response: ", it.message.toString())

                }
                Status.ERROR ->{
                    binding.loadingLayout.visibility = View.GONE
                    Log.d("error: ", it.message.toString())

                }
            }
        }
    }

    override fun categoryClickListener(position: Int, model: Any) {
        val builder: AlertDialog.Builder? = this?.let {
            AlertDialog.Builder(it)
        }

        builder!!.setMessage("Do you want to proceed??")
            .setTitle("Virtual Book Library")

        builder.apply {
            setPositiveButton("Ok") { dialog, id ->
                val selectedId = id
            }
            setNegativeButton("Cancel") { dialog, id ->
                val selectedId = id
            }
        }
        val dialog: AlertDialog? = builder.create()

        dialog!!.show()
    }
}