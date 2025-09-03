package com.amritthakur.newsapp.data.remote.response

import com.amritthakur.newsapp.data.remote.dto.SourceDto
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class SourcesResponse(
    val sources: List<SourceDto>?
)
