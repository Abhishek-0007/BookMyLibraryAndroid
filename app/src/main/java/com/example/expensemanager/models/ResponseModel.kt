package com.example.expensemanager.models

data class ResponseModel<T> (
    val code : String,
    val  body : List<T>
)