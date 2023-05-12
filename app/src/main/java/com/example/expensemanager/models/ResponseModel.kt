package com.example.expensemanager.models

data class ResponseModel<T> (
    val code : String,
    var body : List<T>
)