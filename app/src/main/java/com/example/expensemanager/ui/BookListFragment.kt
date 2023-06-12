package com.example.expensemanager.ui

import android.annotation.SuppressLint
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.transition.Fade
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.ActivityOptionsCompat
import androidx.core.view.ViewCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.MutableLiveData
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.expensemanager.Adapter.PopularBookListAdapter
import com.example.expensemanager.Interfaces.BookOnCLick
import com.example.expensemanager.Network.ApiInterface
import com.example.expensemanager.Network.RetrofitHelper
import com.example.expensemanager.R
import com.example.expensemanager.Utility.Resource
import com.example.expensemanager.Utility.Status
import com.example.expensemanager.databinding.FragmentBookListBinding
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
        binding.bookListRv.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
         genre = arguments?.getString("genre").toString()
        checkAP(genre)
        val fade = Fade()
        activity?.window?.setEnterTransition(fade)
        activity?.window?.setExitTransition(fade)
        fade.excludeTarget(android.R.id.statusBarBackground, true);
        fade.excludeTarget(android.R.id.navigationBarBackground, true);

        binding.back.setOnClickListener { findNavController().navigateUp() }

        binding.goVirtual.setOnClickListener {
            val bundle = Bundle()
            bundle.putString("genre",genre)
            findNavController().navigate(R.id.qrScreenFragment, bundle)
        }
        return binding.root
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

    override fun bookOnClickListener(position: Int, model: Any, x : View, y : String) {
        val item = model as BookInfo
        val bundle = Bundle()
        bundle.putString("bookImage",item.bookImage)
        bundle.putString("bookTitle",item.bookName)
        bundle.putString("bookAuthor",item.bookAuthor)

        val options = ActivityOptionsCompat.makeSceneTransitionAnimation(
            requireActivity(), x, ViewCompat.getTransitionName(x)!!
        )

        bundle.putBundle("x", options.toBundle())

        findNavController().navigate(R.id.bookDetailFragment, bundle)
    }
}