package com.example.gitsurfer.data

import com.example.gitsurfer.data.entities.Repository
import io.reactivex.Single

interface SearchRepository {
    fun searchForRepos(query: String, page: Int, size: Int): Single<Response<List<Repository>>>
}