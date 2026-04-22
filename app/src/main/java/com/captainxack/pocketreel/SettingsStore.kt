package com.captainxack.pocketreel

import android.content.Context

class SettingsStore(context: Context) {
    private val prefs = context.getSharedPreferences("reel_settings", Context.MODE_PRIVATE)

    fun getTreeUri(): String? = prefs.getString("tree_uri", null)
    fun setTreeUri(value: String?) = prefs.edit().putString("tree_uri", value).apply()

    fun getTmdbBearer(): String = prefs.getString("tmdb_bearer", "") ?: ""
    fun setTmdbBearer(value: String) = prefs.edit().putString("tmdb_bearer", value.trim()).apply()

    fun getRssFeedsText(): String = prefs.getString("rss_feeds_text", "") ?: ""
    fun setRssFeedsText(value: String) = prefs.edit().putString("rss_feeds_text", value).apply()

    fun getPreferredAudioLanguage(): String = prefs.getString("preferred_audio_language", "en") ?: "en"
    fun setPreferredAudioLanguage(value: String) = prefs.edit().putString("preferred_audio_language", value.trim().ifBlank { "en" }).apply()

    fun getPreferredSubtitleLanguage(): String = prefs.getString("preferred_subtitle_language", "en") ?: "en"
    fun setPreferredSubtitleLanguage(value: String) = prefs.edit().putString("preferred_subtitle_language", value.trim().ifBlank { "en" }).apply()
}
