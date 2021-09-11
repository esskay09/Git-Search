package com.terranullius.gitsearch.framework.datasource.network.model.allrepos

import com.squareup.moshi.Json

data class License(

    @Json(name = "name")
    val nameR: String? = ""
){

    val name = nameR
        get() = field ?: ""

}