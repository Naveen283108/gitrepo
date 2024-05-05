package com.repo.gitrepo.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.repo.gitrepo.R
import com.repo.gitrepo.data.Repository

class RepositoryAdapter : ListAdapter<Repository, RepositoryAdapter.RepositoryViewHolder>(DIFF_CALLBACK) {

    private var onItemClickListener: ((Repository) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RepositoryViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_repository, parent, false)
        return RepositoryViewHolder(view)
    }

    override fun onBindViewHolder(holder: RepositoryViewHolder, position: Int) {
        val repository = getItem(position)
        holder.bind(repository)
    }

    fun setOnItemClickListener(listener: (Repository) -> Unit) {
        onItemClickListener = listener
    }

    inner class RepositoryViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val repositoryNameTextView: TextView = itemView.findViewById(R.id.repositoryNameTextView)

        fun bind(repository: Repository) {
            repositoryNameTextView.text = repository.name
            itemView.setOnClickListener {
                onItemClickListener?.invoke(repository)
            }
        }
    }

    companion object {
        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<Repository>() {
            override fun areItemsTheSame(oldItem: Repository, newItem: Repository): Boolean {
                return oldItem.name == newItem.name && oldItem.owner == newItem.owner
            }

            override fun areContentsTheSame(oldItem: Repository, newItem: Repository): Boolean {
                return oldItem == newItem
            }
        }
    }
}
