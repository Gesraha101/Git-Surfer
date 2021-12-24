package com.example.gitsurfer.data.sources

import android.content.Context
import androidx.annotation.RawRes
import com.example.gitsurfer.R
import com.example.gitsurfer.data.BaseResponse
import com.example.gitsurfer.data.SearchDataSource
import com.example.gitsurfer.data.entities.Repository
import com.example.gitsurfer.utils.fromJson
import com.example.gitsurfer.utils.openAsset
import com.google.gson.reflect.TypeToken
import dagger.hilt.android.qualifiers.ApplicationContext
import io.reactivex.Single
import javax.inject.Inject

class LocalDataSource @Inject constructor(
    @ApplicationContext private val context: Context
) : SearchDataSource {
    override fun searchRepos(
        query: String,
        page: Int,
        size: Int
    ): Single<BaseResponse<List<Repository>>> {
        val jsonRes = when (page % 5) {
            1 -> R.raw.repos_1
            2 -> R.raw.repos_2
            3 -> R.raw.repos_3
            4 -> R.raw.repos_4
            else -> R.raw.repos_5
        }
        val formattedResponse = getJsonDataFromAsset(jsonRes)?.fromJson<BaseResponse<List<Repository>>>(object :
            TypeToken<BaseResponse<List<Repository>>>() {}.type)
        return if (formattedResponse != null)
            Single.just(formattedResponse)
        else Single.error(RuntimeException("Invalid json file format or file not found"))
    }

    private fun getJsonDataFromAsset(@RawRes res: Int): String? {
        return try {
            context.openAsset(res).use { it.readText() }
        } catch (exception: Exception) {
            exception.printStackTrace()
            null
        }
    }
}