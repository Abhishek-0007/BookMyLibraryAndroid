package com.example.expensemanager.ui

import android.annotation.SuppressLint
import android.app.Dialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.*
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.lifecycle.MutableLiveData
import androidx.navigation.fragment.findNavController
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
import com.example.expensemanager.extensions.ExtensionMethods
import com.example.expensemanager.models.GenreInfo
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class VirtualLibraryActivity : Fragment(), CategoryOnClick {
    private lateinit var binding: ActivityVirtualLibraryBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = ActivityVirtualLibraryBinding.inflate(inflater, container, false)
        binding.back.setOnClickListener { findNavController().navigateUp() }
        binding.rv.layoutManager = GridLayoutManager(requireContext(), 3)
        try{
            checkAP()
        }
        catch (e : Exception){
            Toast.makeText(requireContext(), "Server down maybe", Toast.LENGTH_SHORT).show()
        }

//        binding.goVirtual.setOnClickListener {
//            binding.loadingLayout.visibility = View.VISIBLE
////            findNavController().navigate(1) // yet to impl
//        }
        return binding.root
    }

    @SuppressLint
    fun checkApiRepo() : MutableLiveData<Resource<ResponseModel<String>>> {
        val api = RetrofitHelper(requireContext()).getInstance().create(ApiInterface::class.java)

        val mutableLiveData = MutableLiveData<Resource<ResponseModel<String>>>()
        mutableLiveData.value = Resource<ResponseModel<String>>().loading()
        api.getAllCategories().subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                {
                    mutableLiveData.value = Resource<ResponseModel<String>>().success(it)

                },
                {
                    mutableLiveData.value = Resource<ResponseModel<String>>().error(it)
                }
            )

        return mutableLiveData
    }
    @SuppressLint
    fun checkApiRepoForBooks(string: String) : MutableLiveData<Resource<ResponseModel<String>>> {
        val api = RetrofitHelper(requireContext()).getInstance().create(ApiInterface::class.java)

        val mutableLiveData = MutableLiveData<Resource<ResponseModel<String>>>()
        mutableLiveData.value = Resource<ResponseModel<String>>().loading()
        api.getBooksForCategory(string).subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                {
                    mutableLiveData.value = Resource<ResponseModel<String>>().success(it)

                },
                {
                    mutableLiveData.value = Resource<ResponseModel<String>>().error(it)
                }
            )

        return mutableLiveData
    }

    fun checkAP() {
        checkApiRepo().observe(requireActivity()) {
            when (it.status) {
                Status.LOADING -> {
                    binding.loadingLayout.visibility = View.VISIBLE
                }
                Status.SUCCESS -> {
                    Handler(Looper.getMainLooper()).postDelayed({
                        binding.loadingLayout.visibility = View.GONE
                    }, 2000)
                    if (it.code == 200)
                        binding.rv.adapter = CategoryAdapter(it.data!!.body, this)
                    Log.d("response: ", it.message.toString())

                }
                Status.ERROR -> {
                    binding.loadingLayout.visibility = View.GONE
                    Log.d("error: ", it.message.toString())

                }
            }
        }
    }

        fun checkAPIForBooks(string: String)
        {
            checkApiRepoForBooks(string).observe(requireActivity()){
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
        val item = model as String
        val bundle = Bundle()
        bundle.putString("genre",item)
        findNavController().navigate(R.id.bookListFragment, bundle)
    }
}
