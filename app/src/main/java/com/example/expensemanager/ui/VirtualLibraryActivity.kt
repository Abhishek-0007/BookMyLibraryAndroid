package com.example.expensemanager.ui

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.GridLayoutManager
import com.example.expensemanager.Adapter.CategoryAdapter
import com.example.expensemanager.Adapter.LibraryAdapter
import com.example.expensemanager.models.CategoryBody
import com.example.expensemanager.models.ResponseModel
import com.example.expensemanager.Network.ApiInterface
import com.example.expensemanager.Network.RetrofitHelper
import com.example.expensemanager.Utility.Resource
import com.example.expensemanager.Utility.Status
import com.example.expensemanager.databinding.ActivityVirtualLibraryBinding
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class VirtualLibraryActivity : AppCompatActivity() {
    private lateinit var binding: ActivityVirtualLibraryBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityVirtualLibraryBinding.inflate(layoutInflater)
        setContentView(binding.root)
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
                }
                Status.SUCCESS -> {
                    if(it.code == 200)
                        binding.rv.adapter = CategoryAdapter(it.data!!.body)
                    Log.d("response: ", it.message.toString())

                }
                Status.ERROR ->{
                    Log.d("error: ", it.message.toString())

                }
            }
        }
    }
}