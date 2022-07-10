package com.example.musify.data.dto

import com.example.musify.data.utils.MapperImageSize
import com.example.musify.domain.MusicSummary
import com.example.musify.domain.SearchResults
import com.fasterxml.jackson.annotation.JsonProperty

/**
 * A DTO that contains the results of a search operation.
 * All the properties are nullable because a search operation
 * for just [tracks],[albums],[artists] or [playlists], or
 * a combination of any of the above can be made, in which
 * case, the other properties will be null.
 */
data class SearchResultsDTO(
    val tracks: Tracks?,
    val albums: Albums?,
    val artists: Artists?,
    val playlists: Playlists?
) {
    data class Tracks(@JsonProperty("items") val value: List<TrackDTOWithAlbumMetadata>)
    data class Albums(@JsonProperty("items") val value: List<AlbumMetadataDTO>)
    data class Artists(@JsonProperty("items") val value: List<ArtistDTO>)
    data class Playlists(@JsonProperty("items") val value: List<PlaylistMetadataDTO>)
}

/**
 * A mapper function used to map an instance of [SearchResultsDTO] to
 * an instance of [SearchResults]. The [imageSize] parameter describes
 * the size of image to be used for the for associated [MusicSummary]
 * instances excluding [SearchResults.playlists].
 */
fun SearchResultsDTO.toSearchResults(imageSize: MapperImageSize) = SearchResults(
    tracks = tracks?.value?.map { it.toTrackSearchResult(imageSize) } ?: emptyList(),
    albums = albums?.value?.map { it.toAlbumSearchResult(imageSize) } ?: emptyList(),
    artists = artists?.value?.map { it.toArtistSearchResult(imageSize) } ?: emptyList(),
    playlists = playlists?.value?.map { it.toPlaylistSearchResult() } ?: emptyList()
)

