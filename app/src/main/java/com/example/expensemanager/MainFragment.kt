package com.example.expensemanager

import android.annotation.SuppressLint
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.MutableLiveData
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.expensemanager.Adapter.GenreListAdapter
import com.example.expensemanager.Adapter.PopularBookListAdapter
import com.example.expensemanager.Adapter.SliderAdapter
import com.example.expensemanager.Interfaces.BookOnCLick
import com.example.expensemanager.Interfaces.GenreOnCLick
import com.example.expensemanager.Network.ApiInterface
import com.example.expensemanager.Network.RetrofitHelper
import com.example.expensemanager.Utility.Resource
import com.example.expensemanager.Utility.Status
import com.example.expensemanager.ViewModels.MainFragmentViewModel
import com.example.expensemanager.databinding.FragmentMainBinding
import com.example.expensemanager.extensions.ExtensionMethods
import com.example.expensemanager.models.BookInfo
import com.example.expensemanager.models.GenreInfo
import com.example.expensemanager.models.ResponseModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers


class MainFragment : Fragment(), BookOnCLick, GenreOnCLick  {
    private lateinit var binding : FragmentMainBinding
    private lateinit var navHostFragment: NavHostFragment
    private lateinit var navController: NavController
    val handler = Handler()
    var origPosition: Int = 0
    private val viewModel: MainFragmentViewModel by viewModels()
    private var countLive = MutableLiveData<Int>()
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentMainBinding.inflate(inflater, container, false)
        binding.booksRv.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        binding.genresRv.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        binding.slider.adapter = SliderAdapter(imageSetter())
        binding.search.setOnClickListener { findNavController().navigate(R.id.searchFragment) }
        val delay:Long = 1500 // 1000 milliseconds == 1 second
        var countint = 0
        if (countLive.value == null)countLive.value = 0

        handler.postDelayed(object : Runnable {
            override fun run() {
                if (countint >= 6) countint = 0
                countLive.value = (countint++)
                handler.postDelayed(this, delay)
            }
        }, delay)

        checkAPIBookInfo()
        checkAPIGenreInfo()
        bindAdapter()
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
                    viewModel.mutableLiveDataBook.value = it.data!!.body
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
                    viewModel.mutableLiveDataGenre.value = it.data!!.body
                    Log.d("response: ", it.data.toString())

                }
                Status.ERROR ->{
                    Log.d("error: ", it.message.toString())
                }
            }
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    fun bindAdapter(){
        val book_list = arrayListOf<BookInfo>()
        viewModel.mutableLiveDataBook.observe(viewLifecycleOwner) {
            book_list.clear()
            book_list.addAll(it)
            if (binding.booksRv.layoutManager == null)
                binding.booksRv.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
            if (binding.booksRv.adapter == null)
                binding.booksRv.adapter = PopularBookListAdapter(book_list, this)

            binding.booksRv.adapter!!.notifyDataSetChanged()

            if(book_list.size == 0)
                binding.booksRv.visibility = View.GONE
            else
                binding.booksRv.visibility = View.VISIBLE
        }

        val genre_list = arrayListOf<GenreInfo>()
        viewModel.mutableLiveDataGenre.observe(viewLifecycleOwner) {
            genre_list.clear()
            genre_list.addAll(it)
            if (binding.genresRv.layoutManager == null)
                binding.genresRv.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
            if (binding.genresRv.adapter == null)
                binding.genresRv.adapter = GenreListAdapter(genre_list, this)

            binding.genresRv.adapter!!.notifyDataSetChanged()

            if(book_list.size == 0)
                binding.genresRv.visibility = View.GONE
            else
                binding.genresRv.visibility = View.VISIBLE
        }

        countLive.observe(requireActivity()){
            binding.slider.setCurrentItem(it)
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
    override fun bookOnClickListener(position: Int, model: Any, x : View, y : String) {
        val item = model as BookInfo
        ExtensionMethods().showBottomSheetOnBookCLick(requireContext(), item)
    }

    override fun genreOnClickListener(position: Int, model: Any) {
        val item = model as GenreInfo
        val bundle = Bundle()
        bundle.putString("genre",item.genreTitle)
        findNavController().navigate(R.id.bookListFragment, bundle)
    }

}