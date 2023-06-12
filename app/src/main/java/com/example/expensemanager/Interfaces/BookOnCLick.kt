package com.example.expensemanager.Interfaces

import android.view.View

interface BookOnCLick {
    fun bookOnClickListener(position: Int, model : Any, x : View, y : String)
}