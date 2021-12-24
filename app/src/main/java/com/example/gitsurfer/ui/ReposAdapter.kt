package com.example.gitsurfer.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.gitsurfer.data.entities.Repository
import com.example.gitsurfer.databinding.ItemRepoBinding
import com.example.gitsurfer.utils.load

class ReposAdapter: RecyclerView.Adapter<ReposAdapter.EmployeeViewHolder>(){

    inner class EmployeeViewHolder(val binding: ItemRepoBinding):RecyclerView.ViewHolder(binding.root)

    private val diffCallback = object : DiffUtil.ItemCallback<Repository>(){
        override fun areItemsTheSame(oldItem: Repository, newItem: Repository): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Repository, newItem: Repository): Boolean {
            return oldItem.hashCode() == newItem.hashCode()
        }
    }

    private val differ = AsyncListDiffer(this,diffCallback)

    fun addPage(list: List<Repository>) = submitList(
        arrayListOf<Repository>().apply {
            addAll(differ.currentList)
            addAll(list)
        }
    )

    fun refresh() = submitList(emptyList())

    private fun submitList(list: List<Repository>) = differ.submitList(list)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EmployeeViewHolder {
        return EmployeeViewHolder(
            ItemRepoBinding.inflate(LayoutInflater.from(
                parent.context
            ),
                parent,
                false
            )
        )
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    override fun onBindViewHolder(holder: EmployeeViewHolder, position: Int) {

        val item = differ.currentList[position]
        holder.binding.apply {
            item.owner?.avatarUrl?.let {
                ivRepoImage.load(it)
            }
            tvRepoName.text = item.name
            tvRepoDescription.text = item.description
        }
    }
}