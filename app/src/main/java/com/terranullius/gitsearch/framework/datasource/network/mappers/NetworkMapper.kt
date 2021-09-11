package com.terranullius.gitsearch.framework.datasource.network.mappers

import com.terranullius.gitsearch.business.domain.model.Repo
import com.terranullius.gitsearch.business.domain.util.EntityMapper
import com.terranullius.gitsearch.framework.datasource.network.model.allrepos.GitSearchRepositoryResponse
import com.terranullius.gitsearch.framework.datasource.network.model.allrepos.License
import com.terranullius.gitsearch.framework.datasource.network.model.allrepos.RepoDto

/**
 * Class to map RepoDto to Image domain model and vice-versa
 * */

class NetworkMapper : EntityMapper<RepoDto, Repo> {

    override fun mapFromEntity(entity: RepoDto): Repo {

        return Repo(
            id = entity.id,
            userName = entity.username,
            license = entity.license.name,
            description = entity.description,
            contributorsUrl = entity.contributorsUrl,
            forks = entity.forks,
            repoUrl = entity.repoUrl,
            owner = entity.owner,
            watchers = entity.watchers,
            language = entity.language,
            stargazers = entity.stargazers,
            openIssues = entity.openIssues
        )

    }

    override fun mapToEntity(domainModel: Repo): RepoDto {
        return RepoDto(
            id = domainModel.id,
            license = License(domainModel.license),
            description = domainModel.description,
            contributorsUrl = domainModel.contributorsUrl,
            forks = domainModel.forks,
            repoUrl = domainModel.repoUrl,
            owner = domainModel.owner,
            watchers = domainModel.watchers,
            openIssues = domainModel.openIssues,
            language = domainModel.language,
            username = domainModel.userName,
            stargazers = domainModel.stargazers
        )
    }

    fun toRepoList(gitSearchRepositoryResponse: GitSearchRepositoryResponse) =
        gitSearchRepositoryResponse.repos.map {
            mapFromEntity(it)
        }

}

