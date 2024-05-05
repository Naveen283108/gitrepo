package com.repo.gitrepo.ui

import android.app.Application
import android.net.Uri
import android.util.Log
import com.repo.gitrepo.util.RetrofitClient
import com.repo.gitrepo.data.Repository
import com.repo.gitrepo.data.RepositoryDetails

class RepositoryRepository : Application(){
    private val githubApi = RetrofitClient.githubApiService

    suspend fun searchRepositories(query: String): List<Repository> {
        return githubApi.searchRepositories(query).items
    }

    suspend fun getRepositoryDetails(owner: String, repo: String): RepositoryDetails {
        try {
            val repository = githubApi.getRepository(owner, repo)
            val contributors = githubApi.getContributors(owner, repo)
            return RepositoryDetails(repository, contributors[0])
        } catch (e: Exception) {
            Log.e("project", "Error getting repository details: ${e.message}", e)
            throw e
        }
    }

   fun getProjectLink(owner: String, repo: String): Uri? {
        return Uri.parse("https://github.com/${owner}/${repo}")
    }

}

