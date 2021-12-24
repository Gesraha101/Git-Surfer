package com.example.gitsurfer.ui

import androidx.lifecycle.ViewModel
import com.example.gitsurfer.data.MainSearchRepository
import com.example.gitsurfer.data.Response
import com.example.gitsurfer.utils.Constants.PAGE_SIZE
import com.example.gitsurfer.utils.addToBag
import com.example.gitsurfer.utils.networkBind
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.subjects.BehaviorSubject
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(private val repo: MainSearchRepository) : ViewModel() {
    private val bag = CompositeDisposable()
    private var page = 1

    val responseSubject: BehaviorSubject<Response<*>> = BehaviorSubject.create()

    fun searchForRepos(query: String) {
        responseSubject.onNext(Response.Loading)
        repo.searchForRepos(query, page, PAGE_SIZE)
            .networkBind()
            .subscribe({
                if (it.succeeded) {
                    responseSubject.onNext(it)
                    page++
                }
            }, { e -> responseSubject.onNext(Response.Error(e))})
            .addToBag(bag)
    }

    fun refresh(query: String) {
        page = 1
        searchForRepos(query)
    }

    override fun onCleared() {
        super.onCleared()
        bag.clear()
    }
}