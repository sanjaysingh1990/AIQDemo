package com.wh.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

open class MyObseravble : ViewModel()
{
    val data = MutableLiveData<String>()
}