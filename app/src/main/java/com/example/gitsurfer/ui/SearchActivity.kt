package com.example.gitsurfer.ui


import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.gitsurfer.data.Response
import com.example.gitsurfer.data.entities.Repository
import com.example.gitsurfer.databinding.ActivitySearchBinding
import com.example.gitsurfer.utils.addToBag
import com.example.gitsurfer.utils.bind
import com.example.gitsurfer.utils.getValue
import com.jakewharton.rxbinding3.widget.afterTextChangeEvents
import dagger.hilt.android.AndroidEntryPoint
import io.reactivex.disposables.CompositeDisposable
import java.util.concurrent.TimeUnit


const val EXTRA_QUERY = "query"

@AndroidEntryPoint
class SearchActivity : AppCompatActivity() {

    val viewModel: SearchViewModel by viewModels()
    private val bag = CompositeDisposable()
    private val viewsBag = CompositeDisposable()
    private val mAdapter by lazy { ReposAdapter() }
    private lateinit var binding: ActivitySearchBinding
    private val query
    get() = binding.etRepoQuery.text.toString()

    init {
        getValue()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySearchBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val lastQuery = savedInstanceState?.getString(EXTRA_QUERY) ?: ""


            initObservables()

        initViews(lastQuery)
    }

    private fun initObservables() {
        viewModel.responseSubject.bind()
            .subscribe { response ->
                when (response) {
                    is Response.Success -> {
                        binding.pbLoading.isVisible = false
                        (response.data as? List<Repository>)?.let {
                            mAdapter.addPage(it)
                        }
                    }
                    is Response.Error -> {
                        binding.pbLoading.isVisible = false
                        Toast.makeText(this, response.exception.toString(), Toast.LENGTH_SHORT).show()
                    }
                    is Response.Loading -> {
                        binding.pbLoading.isVisible = true
                    }
                    else -> {}
                }
            }.addToBag(bag)
    }

    private fun setPagination(recyclerView: RecyclerView) {
        val layoutManager = recyclerView.layoutManager as LinearLayoutManager
        recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {

            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                if (dy > 0) {
                    if (viewModel.responseSubject.value != Response.Loading) {
                        val visibleItemCount = layoutManager.childCount
                        val totalItemCount = layoutManager.itemCount
                        val pastVisibleItems = layoutManager.findFirstVisibleItemPosition()
                        if (visibleItemCount + pastVisibleItems >= totalItemCount)
                            viewModel.searchForRepos(query)
                    }
                }
            }
        })
    }

    private fun initViews(lastQuery: String) {
        binding.etRepoQuery.afterTextChangeEvents()
            .skipInitialValue()
            .filter {
                it.editable.toString() != lastQuery
            }
            .debounce(700, TimeUnit.MILLISECONDS)
            .bind()
            .subscribe {
                it.editable?.toString().let { query ->
                    if (!query.isNullOrBlank()) {
                        mAdapter.refresh()
                        viewModel.refresh(query)
                    }
                }
            }
            .addToBag(viewsBag)
        binding.rvRepos.adapter = mAdapter
        setPagination(binding.rvRepos)
        setSwipeReset()
    }

    private fun setSwipeReset() {
        binding.swipeLayoutRepos.setOnRefreshListener {
            mAdapter.refresh()
            viewModel.refresh(query)
            binding.swipeLayoutRepos.isRefreshing = false
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString(EXTRA_QUERY, query)
    }

    override fun onDestroy() {
        super.onDestroy()
        bag.clear()
    }
}