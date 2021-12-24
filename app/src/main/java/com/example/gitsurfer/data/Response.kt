package com.example.gitsurfer.data

import com.example.gitsurfer.data.entities.Repository
import com.google.gson.annotations.SerializedName

sealed class Response<out R> {

    data class Success<out T>(val data: T) : Response<T>()
    data class Error(val exception: Throwable) : Response<Nothing>()
    object Loading : Response<Nothing>()

    override fun toString(): String {
        return when (this) {
            is Success<*> -> "Successful result: $data]"
            is Error -> "Error occured: $exception"
            is Loading -> "Loading..."
        }
    }
    val succeeded
        get() = this is Success && data != null
}

data class BaseResponse<out T>(@SerializedName("items") val data: List<Repository>)
