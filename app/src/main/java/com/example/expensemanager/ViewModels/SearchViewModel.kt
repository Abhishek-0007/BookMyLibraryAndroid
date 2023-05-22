package com.example.expensemanager.ViewModels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.expensemanager.models.BookInfo
import com.example.expensemanager.models.GenreInfo
import com.example.expensemanager.models.SearchModel

class SearchViewModel:ViewModel() {
    val mutableLiveData = MutableLiveData<List<SearchModel>>()

}