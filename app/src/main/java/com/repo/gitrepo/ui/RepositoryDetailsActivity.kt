package com.repo.gitrepo.ui

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.repo.gitrepo.R
import com.repo.gitrepo.viewmodel.RepositoryViewModel

class RepositoryDetailsActivity : AppCompatActivity() {

    private lateinit var repositoryNameTextView: TextView
    private lateinit var ownerTextView: TextView
    private lateinit var descriptionTextView: TextView
    private lateinit var starsTextView: TextView
    private lateinit var forksTextView: TextView
    private lateinit var contributor: TextView
    private lateinit var viewModel: RepositoryViewModel
    private lateinit var linkButton:Button
    private lateinit var project_avatar:ImageView
    private lateinit var projectLink:TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_repository_details)
        // Initialize views
        repositoryNameTextView = findViewById(R.id.repositoryNameTextView)
        ownerTextView = findViewById(R.id.ownerTextView)
        descriptionTextView = findViewById(R.id.descriptionTextView)
        starsTextView = findViewById(R.id.starsTextView)
        forksTextView = findViewById(R.id.forksTextView)
        contributor = findViewById(R.id.contributions)

        projectLink = findViewById(R.id.projectLink)
        linkButton = findViewById(R.id.linkButton)


        viewModel = ViewModelProvider(this).get(RepositoryViewModel::class.java)

        val owner = intent.getStringExtra("owner") ?: ""
        val repo = intent.getStringExtra("repo") ?: ""

        Log.d("Naveen","owner $owner repo$repo")

        viewModel.getRepositoryDetails(owner, repo)

        viewModel.repositoryLink.observe(this) { link ->
            link?.let {
                projectLink.text = "Project Link: ${link}"

            }
        }

        linkButton.setOnClickListener {
            val url = projectLink.text.toString().substringAfter("Project Link: ")
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
            startActivity(intent)

        }

        viewModel.repositoryDetails.observe(this) { details ->
            repositoryNameTextView.text = "Project Name : ${details.repository.name}"
            ownerTextView.text = "Owner : ${details.repository.owner.login}"
            descriptionTextView.text = "Description : ${details.repository.description}"
            starsTextView.text = "${details.repository.stars} stars"
            forksTextView.text = "${details.repository.forks} forks"
            contributor.text = "Contributions : ${details.contributors.contributions}"
            project_avatar = findViewById(R.id.project_avatar)

            Glide.with(this)
                .load(details.repository.owner.avatar_url)
                .into(project_avatar)

        }
    }
}
