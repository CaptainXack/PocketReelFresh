package com.captainxack.pocketreel

import android.content.Context
import android.net.Uri
import androidx.documentfile.provider.DocumentFile
import java.util.Locale
import java.util.UUID

object LocalScanner {
    private val videoExtensions = setOf("mp4", "mkv", "webm", "mov", "avi", "m4v")
    private val episodeRegex = Regex("""(?i)\bS(\d{1,2})E(\d{1,2})\b""")

    fun scan(context: Context, treeUriString: String): List<CatalogTitle> {
        val treeUri = Uri.parse(treeUriString)
        val root = DocumentFile.fromTreeUri(context, treeUri) ?: return emptyList()
        val items = mutableListOf<CatalogTitle>()
        walk(root, items)
        return items.sortedWith(compareBy<CatalogTitle>({ it.seriesTitle ?: it.title }, { it.seasonNumber ?: 0 }, { it.episodeNumber ?: 0 }, { it.title }))
    }

    private fun walk(node: DocumentFile, out: MutableList<CatalogTitle>) {
        if (node.isDirectory) {
            node.listFiles().forEach { walk(it, out) }
            return
        }
        if (!node.isFile) return
        val name = node.name ?: return
        val ext = name.substringAfterLast('.', "").lowercase(Locale.ROOT)
        if (ext !in videoExtensions) return

        val cleanName = cleanTitle(name)
        val episodeMatch = episodeRegex.find(name)

        if (episodeMatch != null) {
            val season = episodeMatch.groupValues[1].toIntOrNull()
            val episode = episodeMatch.groupValues[2].toIntOrNull()
            val seriesTitle = cleanName.substringBefore(episodeMatch.value, cleanName).trim().ifBlank { cleanName }
            out += CatalogTitle(
                key = UUID.randomUUID().toString(),
                title = episodeMatch.value.uppercase(Locale.ROOT),
                mediaKind = MediaKind.EPISODE,
                localUri = node.uri.toString(),
                seriesTitle = prettify(seriesTitle),
                seasonNumber = season,
                episodeNumber = episode,
                subtitle = "Season ${season ?: 0} • Episode ${episode ?: 0}",
                isLocal = true,
            )
        } else {
            out += CatalogTitle(
                key = UUID.randomUUID().toString(),
                title = prettify(cleanName),
                mediaKind = MediaKind.MOVIE,
                localUri = node.uri.toString(),
                subtitle = "Local file",
                isLocal = true,
            )
        }
    }

    private fun cleanTitle(fileName: String): String {
        return fileName.substringBeforeLast('.').replace('.', ' ').replace('_', ' ').replace('-', ' ').replace(Regex("""\s+"""), " ").trim()
    }

    private fun prettify(raw: String): String {
        return raw.replace(Regex("""(?i)\b(1080p|720p|2160p|x264|x265|h264|h265|webrip|web-dl|bluray|brrip|dvdrip|aac|ac3|hevc)\b"""), "").replace(Regex("""\s+"""), " ").trim()
    }
}
