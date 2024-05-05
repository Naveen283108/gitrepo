package com.repo.gitrepo.ui

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.widget.SearchView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.MenuItemCompat
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.repo.gitrepo.R
import com.repo.gitrepo.manager.SharedPreferenceManager
import com.repo.gitrepo.ui.adapter.RepositoryAdapter
import com.repo.gitrepo.util.PhoneStateUtil
import com.repo.gitrepo.viewmodel.RepositoryViewModel

class MainActivity : AppCompatActivity() {
    private lateinit var viewModel: RepositoryViewModel
    private lateinit var adapter: RepositoryAdapter
    private lateinit var sharedPreferences: SharedPreferenceManager
    private lateinit var phoneStateUtil: PhoneStateUtil

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        viewModel = ViewModelProvider(this).get(RepositoryViewModel::class.java)

        supportActionBar?.title = "GitRepo"
        sharedPreferences = SharedPreferenceManager(applicationContext)
        phoneStateUtil = PhoneStateUtil(applicationContext)

        val recyclerView: RecyclerView = findViewById(R.id.recyclerView)

        adapter = RepositoryAdapter()
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = adapter


        if (phoneStateUtil.isNetworkConnected()) {
            viewModel.repositories.observe(this) { repositories ->
                adapter.submitList(repositories)
                sharedPreferences.saveSearchResults(repositories)
            }
        } else {
            val savedResults = sharedPreferences.getSavedSearchResults()
            if (savedResults != null) {
                val savedRepositories = sharedPreferences.parseJson(savedResults)
                adapter.submitList(savedRepositories)
            } else {
                Toast.makeText(this, "No internet connection", Toast.LENGTH_SHORT).show()
            }
        }

        // Handle item click event
        adapter.setOnItemClickListener { repository ->
            navigateToRepositoryDetails(repository.owner.login, repository.name)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)

        val searchViewItem = menu.findItem(R.id.action_search)
        val searchView = MenuItemCompat.getActionView(searchViewItem) as SearchView

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                searchView.clearFocus()
                if (phoneStateUtil.isNetworkConnected()){
                    viewModel.searchRepositories(query)
                }
                return true
            }

            override fun onQueryTextChange(newText: String): Boolean {
                return false
            }
        })
        return super.onCreateOptionsMenu(menu)
    }

    private fun navigateToRepositoryDetails(owner: String, repo: String) {
        val intent = Intent(this, RepositoryDetailsActivity::class.java)
        intent.putExtra("owner", owner)
        intent.putExtra("repo", repo)
        startActivity(intent)
    }

}
