package com.example.musicapplication.util

sealed class DataState<out R> {

    data class Success<out T>(val data: T) : DataState<T>()
    object Loading : DataState<Nothing>()
    object Error : DataState<Nothing>()

}