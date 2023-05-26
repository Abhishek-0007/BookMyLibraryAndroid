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
import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.expensemanager.Adapter.CategoryAdapter
import com.example.expensemanager.Adapter.LibraryAdapter
import com.example.expensemanager.Adapter.PopularBookListAdapter
import com.example.expensemanager.Interfaces.BookOnCLick
import com.example.expensemanager.Network.ApiInterface
import com.example.expensemanager.Network.RetrofitHelper
import com.example.expensemanager.R
import com.example.expensemanager.Utility.Resource
import com.example.expensemanager.Utility.Status
import com.example.expensemanager.databinding.FragmentBookListBinding
import com.example.expensemanager.extensions.ExtensionMethods
import com.example.expensemanager.models.*
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers


class BookListFragment : Fragment(), BookOnCLick {

    private lateinit var binding : FragmentBookListBinding
    private var genre = ""
    private var otp = "${(Math.random() * 9000).toInt() + 1000}";
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentBookListBinding.inflate(layoutInflater, container, false)
        binding.bookListRv.layoutManager = GridLayoutManager(requireContext(), 3)
         genre = arguments?.getString("genre").toString()
        checkAP(genre)

        binding.back.setOnClickListener { findNavController().navigateUp() }

        binding.goVirtual.setOnClickListener {
            checkAPI()
        }
        return binding.root
    }
    fun checkAPI() {
        checkApiRepoVirtual().observe(requireActivity()) {
            when (it.status) {
                Status.LOADING -> {
                    binding.loadingLayout.visibility = View.VISIBLE
                }
                Status.SUCCESS -> {
                    Handler(Looper.getMainLooper()).postDelayed({
                        binding.loadingLayout.visibility = View.GONE
                    }, 1000)
                    ExtensionMethods().showBottomSheetAfterVirtual(requireContext(), "Virtual Library", otp)
                    Log.d("response: ", it.message.toString())

                }
                Status.ERROR -> {
                    binding.loadingLayout.visibility = View.GONE
                    Log.d("error: ", it.message.toString())
                }
            }
        }
    }
    @SuppressLint
    fun checkApiRepoVirtual() : MutableLiveData<Resource<SeatResponse>> {
        val api = RetrofitHelper(requireContext()).getInstance().create(ApiInterface::class.java)

        val mutableLiveData = MutableLiveData<Resource<SeatResponse>>()
        mutableLiveData.value = Resource<SeatResponse>().loading()
        api.addGenreToDB(otp, "ab@ab.com", genre).subscribeOn(Schedulers.io())
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
    @SuppressLint
    fun checkApiRepo(code:String) : MutableLiveData<Resource<ResponseModel<SearchModel>>> {
        val api = RetrofitHelper(requireContext()).getInstance().create(ApiInterface::class.java)

        val mutableLiveData = MutableLiveData<Resource<ResponseModel<SearchModel>>>()
        mutableLiveData.value = Resource<ResponseModel<SearchModel>>().loading()
        api.getSearchResult(code).subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                {
                    mutableLiveData.value = Resource<ResponseModel<SearchModel>>().success(it)

                },
                {
                    mutableLiveData.value = Resource<ResponseModel<SearchModel>>().error(it)
                }
            )

        return mutableLiveData
    }
    fun checkAP(code: String)
    {
        checkApiRepo(code).observe(requireActivity()){
            when(it.status){
                Status.LOADING -> {
                    binding.loadingLayout.visibility = View.VISIBLE
                }
                Status.SUCCESS -> {
                    Handler(Looper.getMainLooper()).postDelayed({
                        binding.loadingLayout.visibility = View.GONE
                    }, 1000)
                    val list = it.data!!.body.map {
                        BookInfo(it.title, it.author, it.imageUrl)
                    }
                        binding.bookListRv.adapter = PopularBookListAdapter(list,this)
                    Log.d("response: ", it.message.toString())

                }
                Status.ERROR ->{
                    binding.loadingLayout.visibility = View.GONE
                    Log.d("error: ", it.message.toString())
                }
            }
        }
    }

    override fun bookOnClickListener(position: Int, model: Any) {
        Toast.makeText(requireContext(), "Downloading PDF", Toast.LENGTH_SHORT).show()
    }
}