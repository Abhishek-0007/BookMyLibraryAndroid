package com.example.expensemanager.Network

import com.example.expensemanager.models.CategoryBody
import com.example.expensemanager.models.LibraryBody
import com.example.expensemanager.models.ResponseModel
import io.reactivex.Single
import retrofit2.http.GET

interface ApiInterface {
    @GET("get-all-libraries")
     fun getAllLibraries() : Single<ResponseModel<LibraryBody>>

    @GET("get-categories")
    fun getAllCategories() : Single<ResponseModel<CategoryBody>>
}