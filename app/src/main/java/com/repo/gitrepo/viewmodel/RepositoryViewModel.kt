package com.repo.gitrepo.viewmodel

import android.net.Uri
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.repo.gitrepo.data.Repository
import com.repo.gitrepo.data.RepositoryDetails
import com.repo.gitrepo.ui.RepositoryRepository
import kotlinx.coroutines.launch

class RepositoryViewModel : ViewModel() {
    private val repositoryRepository = RepositoryRepository()

    private val _repositories = MutableLiveData<List<Repository>>()
    val repositories: LiveData<List<Repository>> get() = _repositories

    private val _repositoryDetails = MutableLiveData<RepositoryDetails>()
    val repositoryDetails: LiveData<RepositoryDetails> get() = _repositoryDetails

    private val _repositoryLink = MutableLiveData<Uri?>()
    val repositoryLink: MutableLiveData<Uri?> get() = _repositoryLink

    fun searchRepositories(query: String) {
        viewModelScope.launch {
            val repositories = repositoryRepository.searchRepositories(query)
            _repositories.postValue(repositories)
        }
    }

    fun getRepositoryDetails(owner: String, repo: String) {
        viewModelScope.launch {
            try {
                val details = repositoryRepository.getRepositoryDetails(owner, repo)
                _repositoryDetails.postValue(details)

                val html = repositoryRepository.getProjectLink(owner,repo)
                _repositoryLink.postValue(html)

            } catch (e: Exception) {
                Log.e("project", "Error getting repository details: ${e.message}", e)
            }
        }
    }
}
