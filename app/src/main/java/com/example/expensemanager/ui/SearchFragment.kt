package com.example.expensemanager.ui

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.os.IBinder
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.MutableLiveData
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.expensemanager.Adapter.SearchAdapter
import com.example.expensemanager.Network.ApiInterface
import com.example.expensemanager.Network.RetrofitHelper
import com.example.expensemanager.Utility.Resource
import com.example.expensemanager.Utility.Status
import com.example.expensemanager.ViewModels.SearchViewModel
import com.example.expensemanager.databinding.FragmentSearchBinding
import com.example.expensemanager.models.ResponseModel
import com.example.expensemanager.models.SearchModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers


class SearchFragment : Fragment() {
    private lateinit var binding : FragmentSearchBinding
    private val viewModel: SearchViewModel by viewModels()
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSearchBinding.inflate(inflater, container, false)
        bindAdapter()
        binding.search.requestFocus()
//        showKeyboard(requireContext())

        binding.search.doOnTextChanged {text, start, count, after ->
            if(text!!.isNotEmpty())
                checkAPI(key = text.toString())
            else
                viewModel.mutableLiveData.value = arrayListOf()
        }

        binding.back.setOnClickListener {
            findNavController().navigateUp()
            hideKeyboard(requireContext(), it.windowToken)
        }
        binding.searchBar.setEndIconOnClickListener{
            binding.search.setText("")
            viewModel.mutableLiveData.value = arrayListOf()
        }
        return binding.root
    }

    fun showKeyboard(context: Context ) {
        (context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager).toggleSoftInput(
            InputMethodManager.SHOW_FORCED,
            InputMethodManager.HIDE_IMPLICIT_ONLY
        )
    }
    fun hideKeyboard(context: Context, windowToken: IBinder) {
        context.let { view ->
            val imm = view.getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
            imm?.hideSoftInputFromWindow(windowToken, 0)
        }

    }
    fun checkAPI(key : String)
    {
        checkApiRepo(key).observe(requireActivity()){
            when(it.status){
                Status.LOADING -> {
                }
                Status.SUCCESS -> {
                    if (it.data?.body.isNullOrEmpty()) {
                        viewModel.mutableLiveData.value  = arrayListOf()
                    } else {
                        viewModel.mutableLiveData.value = it.data?.body
                    }

                }
                Status.ERROR ->{
                    Log.d("error: ", it.message.toString())
                }
            }
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    fun bindAdapter(){
        val list = arrayListOf<SearchModel>()
        viewModel.mutableLiveData.observe(viewLifecycleOwner) {
            list.clear()
            list.addAll(it)
            if (binding.searchRv.layoutManager == null)
                 binding.searchRv.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
            if (binding.searchRv.adapter == null)
                binding.searchRv.adapter = SearchAdapter(list, requireContext())

            binding.searchRv.adapter!!.notifyDataSetChanged()

            if(list.size == 0)
                binding.searchRv.visibility = View.GONE
            else
                binding.searchRv.visibility = View.VISIBLE

        }
    }

    @SuppressLint
    fun checkApiRepo(key: String) : MutableLiveData<Resource<ResponseModel<SearchModel>>> {
        val mutableLiveData = MutableLiveData<Resource<ResponseModel<SearchModel>>>()
        val api = RetrofitHelper(requireContext()).getInstance().create(ApiInterface::class.java)
        mutableLiveData.value = Resource<ResponseModel<SearchModel>>().loading()
        api.getSearchResult(key).subscribeOn(Schedulers.io())
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

}