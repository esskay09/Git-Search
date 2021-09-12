package com.terranullius.gitsearch.framework.datasource.cache.mappers

import com.terranullius.gitsearch.business.domain.model.Repo
import com.terranullius.gitsearch.business.domain.model.User
import com.terranullius.gitsearch.business.domain.util.EntityMapper
import com.terranullius.gitsearch.framework.datasource.cache.model.RepoCacheEntity
import kotlin.random.Random

class CacheMapper : EntityMapper<RepoCacheEntity, Repo> {

    override fun mapFromEntity(entity: RepoCacheEntity): Repo {
        return Repo(
            id = entity.id,
            license = entity.license,
            contributorsUrl = entity.contributorsUrl,
            description = entity.description,
            forks = entity.forks,
            repoUrl = entity.repoUrl,
            owner = User(
                avatarUrl = entity.ownerAvatarUrl,
                username = entity.ownerUserName,
                id = Random.nextInt(),
                reposUrl = ""
            ),
            watchers = entity.watchers,
            language = entity.language,
            stargazers = entity.stargazers,
            name = entity.name,
            fullName = entity.fullName,
            openIssues = entity.openIssues
        )
    }

    override fun mapToEntity(domainModel: Repo): RepoCacheEntity {
        return RepoCacheEntity(
            id = domainModel.id,
            license = domainModel.license,
            contributorsUrl = domainModel.contributorsUrl,
            description = domainModel.description,
            forks = domainModel.forks,
            repoUrl = domainModel.repoUrl,
            watchers = domainModel.watchers,
            language = domainModel.language,
            stargazers = domainModel.stargazers,
            name = domainModel.name,
            fullName = domainModel.fullName,
            openIssues = domainModel.openIssues,
            ownerAvatarUrl = domainModel.owner.avatarUrl,
            ownerUserName = domainModel.owner.username
        )
    }

    fun mapListEntityToListDomain(listEntity: List<RepoCacheEntity>) = listEntity.map {
        mapFromEntity(it)
    }

    fun mapListDomainToListEntity(listDomain: List<Repo>) = listDomain.map {
        mapToEntity(it)
    }

}