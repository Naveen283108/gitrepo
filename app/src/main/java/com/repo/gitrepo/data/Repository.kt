package com.repo.gitrepo.data

data class Repository(
    val name: String,
    val owner: Owner,
    val description: String?,
    val stars: Int,
    val forks: Int
)




