package com.example.gitsurfer.data.sources

import com.example.gitsurfer.data.BaseResponse
import com.example.gitsurfer.data.SearchDataSource
import com.example.gitsurfer.data.apis.GithubService
import com.example.gitsurfer.data.entities.Repository
import io.reactivex.Single
import javax.inject.Inject

class RemoteDataSource @Inject constructor(
    private val githubService: GithubService
) : SearchDataSource {
    override fun searchRepos(
        query: String,
        page: Int,
        size: Int
    ): Single<BaseResponse<List<Repository>>> {
        return githubService.searchRepos(query, page, size)
    }
}