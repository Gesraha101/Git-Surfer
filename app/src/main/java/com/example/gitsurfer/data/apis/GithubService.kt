package com.example.gitsurfer.data.apis

import com.example.gitsurfer.data.BaseResponse
import com.example.gitsurfer.data.entities.Repository
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Query

interface GithubService {

    @GET("search/repositories")
    fun searchRepos(
        @Query("q") query: String,
        @Query("page") page: Int,
        @Query("per_page") size: Int
    ): Single<BaseResponse<List<Repository>>>
}