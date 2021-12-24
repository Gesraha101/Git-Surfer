package com.example.gitsurfer.data

import com.example.gitsurfer.data.entities.Repository
import io.reactivex.Single

interface SearchDataSource {
    fun searchRepos(query: String, page: Int, size: Int): Single<BaseResponse<List<Repository>>>
}