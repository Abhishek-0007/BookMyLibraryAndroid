package com.example.expensemanager

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.MutableLiveData
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.expensemanager.Adapter.GenreListAdapter
import com.example.expensemanager.Adapter.PopularBookListAdapter
import com.example.expensemanager.Adapter.SliderAdapter
import com.example.expensemanager.Interfaces.BookOnCLick
import com.example.expensemanager.Network.ApiInterface
import com.example.expensemanager.Network.RetrofitHelper
import com.example.expensemanager.Utility.Resource
import com.example.expensemanager.Utility.Status
import com.example.expensemanager.databinding.FragmentMainBinding
import com.example.expensemanager.extensions.ExtensionMethods
import com.example.expensemanager.models.BookInfo
import com.example.expensemanager.models.GenreInfo
import com.example.expensemanager.models.ResponseModel
import com.example.expensemanager.ui.PhysicalLibraryActivity
import com.example.expensemanager.ui.VirtualLibraryActivity
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers


class MainFragment : Fragment(), BookOnCLick  {
    private lateinit var binding : FragmentMainBinding
    val handler = Handler()
    var origPosition: Int = 0
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentMainBinding.inflate(inflater, container, false)
        binding.booksRv.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        binding.genresRv.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        binding.slider.adapter = SliderAdapter(imageSetter())
        binding.search.setOnClickListener { findNavController().navigate(R.id.searchFragment) }

        val delay:Long = 3000 // 1000 milliseconds == 1 second

        handler.postDelayed(object : Runnable {
            override fun run() {
                if (origPosition >= 6) origPosition = 0

                binding.slider.setCurrentItem(origPosition)
                origPosition++
                handler.postDelayed(this, delay)
            }
        }, delay)

        checkAPIBookInfo()
        checkAPIGenreInfo()
        binding.physicalCard.setOnClickListener { findNavController().navigate(R.id.physicalLibraryActivity) }
        binding.virtualCard.setOnClickListener { findNavController().navigate(R.id.virtualLibraryActivity) }
        return binding.root
    }

    private fun imageSetter():List<Int>{
        val response = ArrayList<Int>()
        response.add(R.drawable.one)
        response.add(R.drawable.two)
        response.add(R.drawable.three)
        response.add(R.drawable.four)
        response.add(R.drawable.five)
        response.add(R.drawable.six)
        response.add(R.drawable.seven)
        return response
    }

    fun checkAPIBookInfo()
    {
        checkApiRepo<BookInfo>(BookInfo::class.java).observe(requireActivity()){
            when(it.status){
                Status.LOADING -> {
                }
                Status.SUCCESS -> {
                        binding.booksRv.adapter = PopularBookListAdapter(it.data!!.body, this)
                    Log.d("response: ", it.data.toString())

                }
                Status.ERROR ->{
                    Log.d("error: ", it.message.toString())
                }
            }
        }
    }

    fun checkAPIGenreInfo()
    {
        checkApiRepo<GenreInfo>(GenreInfo::class.java).observe(requireActivity()){
            when(it.status){
                Status.LOADING -> {
                }
                Status.SUCCESS -> {
                    binding.genresRv.adapter = GenreListAdapter(it.data!!.body)
                    Log.d("response: ", it.data.toString())

                }
                Status.ERROR ->{
                    Log.d("error: ", it.message.toString())
                }
            }
        }
    }

    @SuppressLint
    fun <T : Any>checkApiRepo(klass: Class<T>) : MutableLiveData<Resource<ResponseModel<T>>> {
        val api = RetrofitHelper(requireContext()).getInstance().create(ApiInterface::class.java)

        val mutableLiveData = MutableLiveData<Resource<ResponseModel<T>>>()
        mutableLiveData.value = Resource<ResponseModel<T>>().loading()
        when {
            klass.isAssignableFrom(GenreInfo::class.java) -> {
                api.getListOfGenres().subscribeOn(Schedulers.io())
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

    override fun bookOnClickListener(position: Int, model: Any) {
        val item = model as BookInfo
        ExtensionMethods().showBottomSheetOnBookCLick(requireContext(), item)
    }

}