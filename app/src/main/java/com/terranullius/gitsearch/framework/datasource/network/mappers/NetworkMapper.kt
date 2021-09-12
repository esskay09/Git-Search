package com.terranullius.gitsearch.framework.datasource.network.mappers

import com.terranullius.gitsearch.business.domain.model.Repo
import com.terranullius.gitsearch.business.domain.model.User
import com.terranullius.gitsearch.business.domain.util.EntityMapper
import com.terranullius.gitsearch.framework.datasource.network.model.UserDto
import com.terranullius.gitsearch.framework.datasource.network.model.allrepos.GitSearchRepositoryResponse
import com.terranullius.gitsearch.framework.datasource.network.model.allrepos.License
import com.terranullius.gitsearch.framework.datasource.network.model.allrepos.RepoDto

/**
 * Class to map RepoDto to Image domain model and vice-versa
 * */

class NetworkMapper : EntityMapper<RepoDto, Repo> {

    override fun mapFromEntity(entity: RepoDto): Repo {

        return Repo(
            id = entity.id!!.mod(10000),
            name = entity.name!!,
            fullName = entity.fullName!!,
            license = entity.license?.name!!,
            description = entity.description!!,
            contributorsUrl = entity.contributorsUrl!!,
            forks = entity.forks!!,
            repoUrl = entity.repoUrl!!,
            owner = entity.owner?.toUser()!!,
            watchers = entity.watchers!!,
            language = entity.language!!,
            stargazers = entity.stargazers!!,
            openIssues = entity.openIssues!!
        )

    }

    override fun mapToEntity(domainModel: Repo): RepoDto {
        return RepoDto(
            idR = domainModel.id,
            licenseR = License(domainModel.license),
            descriptionR = domainModel.description,
            contributorsUrlR = domainModel.contributorsUrl,
            forksR = domainModel.forks,
            repoUrlR = domainModel.repoUrl,
            ownerR = domainModel.owner.toUserDto(),
            watchersR = domainModel.watchers,
            openIssuesR = domainModel.openIssues,
            languageR = domainModel.language,
            nameR = domainModel.name,
            fullNameR = domainModel.fullName,
            stargazersR = domainModel.stargazers
        )
    }

    fun toRepoList(gitSearchRepositoryResponse: GitSearchRepositoryResponse) =
        gitSearchRepositoryResponse.repos.map {
            mapFromEntity(it)
        }
}

fun UserDto.toUser() = User(
    id = this.idR!!,
    avatarUrl = this.avatarUrl!!,
    username = this.username!!,
    reposUrl = this.reposUrl!!
)

fun User.toUserDto() = UserDto(
    idR = this.id,
    avatarUrlR = this.avatarUrl,
    usernameR = this.username,
    reposUrlR = this.reposUrl
)

