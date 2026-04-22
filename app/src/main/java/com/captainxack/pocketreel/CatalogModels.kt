package com.captainxack.pocketreel

enum class RootTab { Local, Online, Search, Settings }
enum class MediaTab { Movies, Series }

enum class MediaKind {
    MOVIE,
    EPISODE,
}

data class GenreChip(
    val id: Int,
    val name: String,
)

data class CatalogTitle(
    val key: String,
    val title: String,
    val mediaKind: MediaKind,
    val posterUrl: String? = null,
    val backdropUrl: String? = null,
    val overview: String? = null,
    val year: String? = null,
    val genres: List<String> = emptyList(),
    val localUri: String? = null,
    val seriesTitle: String? = null,
    val seasonNumber: Int? = null,
    val episodeNumber: Int? = null,
    val subtitle: String? = null,
    val isLocal: Boolean = false,
)

data class SeriesGroup(
    val title: String,
    val posterUrl: String?,
    val backdropUrl: String?,
    val overview: String?,
    val genres: List<String>,
    val episodes: List<CatalogTitle>,
) {
    val episodeCount: Int get() = episodes.size
    val seasonCount: Int get() = episodes.mapNotNull { it.seasonNumber }.distinct().size
}

data class UiState(
    val treeUri: String? = null,
    val tmdbBearer: String = "",
    val rssFeedsText: String = "",
    val preferredAudioLanguage: String = "en",
    val preferredSubtitleLanguage: String = "en",
    val localItems: List<CatalogTitle> = emptyList(),
    val onlineMovies: List<CatalogTitle> = emptyList(),
    val onlineSeries: List<CatalogTitle> = emptyList(),
    val movieGenres: List<GenreChip> = emptyList(),
    val tvGenres: List<GenreChip> = emptyList(),
    val selectedMovieGenre: GenreChip? = null,
    val selectedSeriesGenre: GenreChip? = null,
    val onlineSearchResults: List<CatalogTitle> = emptyList(),
    val status: String = "Pick a folder and add a TMDb token when you want online browsing.",
    val loading: Boolean = false,
)
