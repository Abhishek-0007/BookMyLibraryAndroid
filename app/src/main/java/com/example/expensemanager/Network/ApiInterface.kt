package com.example.expensemanager.Network

import com.example.expensemanager.models.*
import io.reactivex.Single
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface ApiInterface {
    @GET("get-all-libraries")
     fun getAllLibraries() : Single<ResponseModel<LibraryBody>>

    @GET("get-categories")
    fun getAllCategories() : Single<ResponseModel<CategoryBody>>


    @GET("get-seats")
    fun getSeatsByLibraryCode(@Query("code") code: String) : Single<ResponseModel<SeatViewModel>>

    @POST("update-seats")
    fun bookSeats(@Body model: SeatRequestModel) : Single<SeatResponse>


}