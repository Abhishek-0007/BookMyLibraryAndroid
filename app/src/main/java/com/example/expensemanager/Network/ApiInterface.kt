package com.example.expensemanager.Network

import com.example.expensemanager.models.*
import io.reactivex.Single
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface ApiInterface {
    @GET("Library/get-all-libraries")
     fun getAllLibraries() : Single<ResponseModel<LibraryBody>>

    @GET("Books/get-categories")
    fun getAllCategories() : Single<ResponseModel<String>>


    @GET("Seat/get-seats-by-hallID")
    fun getSeatsByLibraryCode(@Query("hallId") hallId: Int, @Query("slotId") slotId: Int,
                              @Query("date") date: String) : Single<ResponseModel<SeatViewModel>>

    @POST("Seat/book-seat")
    fun bookSeats(@Body model: SeatBookRequestModel) : Single<SeatResponse>

    @GET("Books/get-popular-books")
    fun getListOfPopularBooks() : Single<ResponseModel<BookInfo>>

    @GET("Books/get-genres")
    fun getListOfGenres() : Single<ResponseModel<GenreInfo>>

    @GET("Books/get-books-for-genre")
    fun getListOfBooks(@Query("genre") genre: String) : Single<ResponseModel<BookModel>>

    @GET("Books/search-books")
    fun getSearchResult(@Query("search") code: String) : Single<ResponseModel<SearchModel>>

    @GET("Books/get-books-for-genre")
    fun getBooksForCategory(@Query("genre") code: String) : Single<ResponseModel<String>>

    @POST("VirtualWeb/add-go-virtual")
    fun addGenreToDB(@Query("name") name: String, @Query("email") email: String, @Query("genreCode") genreCode: String) : Single<SeatResponse>

}