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
            name = entity.name,
            license = entity.license.name,
            description = entity.description,
            contributorsUrl = entity.contributors_url,
            forks = entity.forks_count,
            repoUrl = entity.html_url,
            owner = entity.owner,
            watchers = entity.watchers_count,
            language = entity.language,
            stargazers = entity.stargazers_count,
            openIssues = entity.open_issues_count
        )

    }

    override fun mapToEntity(domainModel: Repo): RepoDto {
        return RepoDto(
            id = domainModel.id,
            license = License(domainModel.license),
            description = domainModel.description,
            contributors_url = domainModel.contributorsUrl,
            forks_count = domainModel.forks,
            html_url = domainModel.repoUrl,
            owner = domainModel.owner,
            watchers_count = domainModel.watchers,
            open_issues_count = domainModel.openIssues,
            language = domainModel.language,
            name = domainModel.name,
            stargazers_count = domainModel.stargazers
        )
    }

    fun toRepoList(gitSearchRepositoryResponse: GitSearchRepositoryResponse) =
        gitSearchRepositoryResponse.repos.map {
            mapFromEntity(it)
        }

}

