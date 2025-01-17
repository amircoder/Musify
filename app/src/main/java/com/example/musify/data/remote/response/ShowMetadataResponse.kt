package com.example.musify.data.remote.response

import com.example.musify.data.utils.MapperImageSize
import com.example.musify.data.utils.getImageResponseForImageSize
import com.example.musify.domain.SearchResult

/**
 * A response object that contains metadata about a specific show.
 */
data class ShowMetadataResponse(
    val id: String,
    val name: String,
    val publisher: String,
    val images: List<ImageResponse>
)

/**
 * A mapper function used to map an instance of [ShowMetadataResponse] to
 * an instance of [SearchResult.PodcastSearchResult]. The [imageSize]
 * parameter determines the size of image to be used for the
 * [SearchResult.PodcastSearchResult] instance.
 */
fun ShowMetadataResponse.toPodcastSearchResult(imageSize: MapperImageSize) =
    SearchResult.PodcastSearchResult(
        id = id,
        name = name,
        nameOfPublisher = publisher,
        imageUrlString = images.getImageResponseForImageSize(imageSize).url
    )
