package com.terranullius.gitsearch.framework.datasource.network.model.allrepos

import com.squareup.moshi.Json
import com.terranullius.gitsearch.framework.datasource.network.model.UserDto
import java.util.*


data class RepoDto(

    @Json(name = "id")
    val idR: Int?,

    @Json(name = "contributors_url")
    val contributorsUrlR: String?,

    @Json(name = "name")
    val nameR: String?,

    @Json(name = "full_name")
    val fullNameR: String?,

    @Json(name = "description")
    val descriptionR: String?,

    @Json(name = "forks_count")
    val forksR: Int?,

    @Json(name = "html_url")
    val repoUrlR: String?,

    @Json(name = "language")
    val languageR: String?,

    @Json(name = "license")
    val licenseR: License?,

    @Json(name = "open_issues_count")
    val openIssuesR: Int?,

    @Json(name = "owner")
    val ownerR: UserDto?,

    @Json(name = "stargazers_count")
    val stargazersR: Int?,

    @Json(name = "watchers_count")
    val watchersR: Int?
) {


    val id = idR
        get() = field ?: Random().nextInt(10000)


    val contributorsUrl = contributorsUrlR
        get() = field ?: ""


    val name = nameR
        get() = field ?: ""

    val fullName = fullNameR
        get() = field ?: ""


    val description = descriptionR
        get() = field ?: ""


    val forks = forksR
        get() = field ?: 0


    val repoUrl = repoUrlR
        get() = field ?: ""


    val language = languageR
        get() = field ?: ""


    val license = licenseR
        get() = field ?: License()


    val openIssues = openIssuesR
        get() = field ?: 0


    val owner = ownerR
        get() = field ?: UserDto()


    val stargazers = stargazersR
        get() = field ?: 0

    val watchers = watchersR
        get() = field ?: 0


}