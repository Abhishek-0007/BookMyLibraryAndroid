package com.example.expensemanager.ui

import android.annotation.SuppressLint
import android.content.Intent
import android.content.Intent.getIntent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.expensemanager.Adapter.LibraryAdapter
import com.example.expensemanager.Adapter.PopularBookListAdapter
import com.example.expensemanager.Interfaces.BookOnCLick
import com.example.expensemanager.Network.ApiInterface
import com.example.expensemanager.Network.RetrofitHelper
import com.example.expensemanager.R
import com.example.expensemanager.Utility.Resource
import com.example.expensemanager.Utility.Status
import com.example.expensemanager.databinding.FragmentBookListBinding
import com.example.expensemanager.models.BookInfo
import com.example.expensemanager.models.BookModel
import com.example.expensemanager.models.LibraryBody
import com.example.expensemanager.models.ResponseModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers


class BookListFragment : Fragment(), BookOnCLick {

    private lateinit var binding : FragmentBookListBinding
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentBookListBinding.inflate(layoutInflater, container, false)
        binding.bookListRv.layoutManager = GridLayoutManager(requireContext(), 3)
        val genre = arguments?.getString("genre").toString()
        checkAP(genre)
        return binding.root
    }

    @SuppressLint
    fun checkApiRepo(code:String) : MutableLiveData<Resource<ResponseModel<BookModel>>> {
        val api = RetrofitHelper(requireContext()).getInstance().create(ApiInterface::class.java)

        val mutableLiveData = MutableLiveData<Resource<ResponseModel<BookModel>>>()
        mutableLiveData.value = Resource<ResponseModel<BookModel>>().loading()
        api.getListOfBooks(code).subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                {
                    mutableLiveData.value = Resource<ResponseModel<BookModel>>().success(it)

                },
                {
                    mutableLiveData.value = Resource<ResponseModel<BookModel>>().error(it)
                }
            )

        return mutableLiveData
    }
    fun checkAP(code: String)
    {
        checkApiRepo(code).observe(requireActivity()){
            when(it.status){
                Status.LOADING -> {
                }
                Status.SUCCESS -> {
                    val list = it.data!!.body.map {
                        BookInfo(it.title, it.author, "")
                    }
                        binding.bookListRv.adapter = PopularBookListAdapter(list,this)
                    Log.d("response: ", it.message.toString())

                }
                Status.ERROR ->{
                    Log.d("error: ", it.message.toString())
                }
            }
        }
    }

    override fun bookOnClickListener(position: Int, model: Any) {
        TODO("Not yet implemented")
    }
}