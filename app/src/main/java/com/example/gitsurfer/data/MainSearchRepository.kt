package com.example.gitsurfer.data

import com.example.gitsurfer.data.entities.Repository
import io.reactivex.Single

class MainSearchRepository(private val searchDataSource: SearchDataSource) : SearchRepository {
    override fun searchForRepos(query: String, page: Int, size: Int): Single<Response<List<Repository>>> {
        return searchDataSource.searchRepos(query, page, size)
            .map { Response.Success(it.data) }
    }
}