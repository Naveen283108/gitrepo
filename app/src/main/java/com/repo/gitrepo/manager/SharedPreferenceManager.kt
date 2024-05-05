package com.repo.gitrepo.manager

import android.content.Context
import android.content.SharedPreferences
import android.preference.PreferenceManager
import com.google.gson.Gson
import com.repo.gitrepo.data.Repository

class SharedPreferenceManager(context: Context) {
    private val sharedPreferences: SharedPreferences = PreferenceManager.getDefaultSharedPreferences(context)
    private val gson: Gson = Gson()

    fun saveSearchResults(repositories: List<Repository>) {
        val json = gson.toJson(repositories.take(10))
        sharedPreferences.edit().putString("search_results", json).apply()
    }

    fun getSavedSearchResults(): String? {
        return sharedPreferences.getString("search_results", null)
    }

    fun parseJson(json: String): List<Repository> {
        return gson.fromJson(json, Array<Repository>::class.java).toList()
    }
}
