package com.captainxack.pocketreel

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent { PocketReelApp() }
    }
}

@Composable
private fun PocketReelApp() {
    var rootTab by rememberSaveable { mutableStateOf(RootTab.Local) }

    MaterialTheme {
        Scaffold(
            bottomBar = {
                NavigationBar {
                    RootTab.entries.forEach { tab ->
                        NavigationBarItem(
                            selected = rootTab == tab,
                            onClick = { rootTab = tab },
                            icon = {},
                            label = { Text(tab.name) }
                        )
                    }
                }
            }
        ) { innerPadding ->
            when (rootTab) {
                RootTab.Local -> LibraryScreen(
                    modifier = Modifier.padding(innerPadding),
                    title = "Local",
                    subtitle = "Your downloaded library",
                    movieRows = listOf(
                        "All Movies", "Recently Added", "Action", "Comedy", "Drama", "Horror", "Sci-Fi"
                    ),
                    seriesRows = listOf(
                        "All Series", "Continue Watching", "Recently Added", "Crime", "Comedy", "Drama", "Documentary"
                    )
                )

                RootTab.Online -> LibraryScreen(
                    modifier = Modifier.padding(innerPadding),
                    title = "Online",
                    subtitle = "TMDb browser shell",
                    movieRows = listOf(
                        "Popular Movies", "Trending Movies", "Top Rated Movies", "Upcoming Movies", "Action", "Comedy", "Horror", "Sci-Fi"
                    ),
                    seriesRows = listOf(
                        "Popular Series", "Trending Series", "Top Rated Series", "Airing Series", "Crime", "Comedy", "Drama", "Animation"
                    )
                )

                RootTab.Search -> SearchScreen(modifier = Modifier.padding(innerPadding))
                RootTab.Settings -> SettingsScreen(modifier = Modifier.padding(innerPadding))
            }
        }
    }
}

@Composable
private fun LibraryScreen(
    modifier: Modifier = Modifier,
    title: String,
    subtitle: String,
    movieRows: List<String>,
    seriesRows: List<String>
) {
    var mediaTab by rememberSaveable { mutableStateOf(MediaTab.Movies) }
    val rows = if (mediaTab == MediaTab.Movies) movieRows else seriesRows

    LazyColumn(
        modifier = modifier.fillMaxSize().background(Color(0xFF0B0B0B)),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(18.dp)
    ) {
        item {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        Brush.verticalGradient(
                            listOf(Color(0xFF1F3A5F), Color(0xFF111111))
                        )
                    )
                    .padding(20.dp)
            ) {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text(title, style = MaterialTheme.typography.headlineLarge, fontWeight = FontWeight.Bold)
                    Text(subtitle, color = Color.LightGray)
                }
            }
        }

        item {
            TabRow(selectedTabIndex = mediaTab.ordinal) {
                MediaTab.entries.forEach { tab ->
                    Tab(
                        selected = mediaTab == tab,
                        onClick = { mediaTab = tab },
                        text = { Text(tab.name) }
                    )
                }
            }
        }

        items(rows) { rowTitle ->
            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(1.dp)
                        .background(Color(0x22FFFFFF))
                )
                Text(rowTitle, style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)

                LazyRow(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    items((1..10).toList()) { index ->
                        Column(
                            modifier = Modifier.width(140.dp),
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Box(
                                modifier = Modifier
                                    .width(140.dp)
                                    .height(210.dp)
                                    .background(Color(0xFF232323)),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = "${if (mediaTab == MediaTab.Movies) "Movie" else "Series"} $index",
                                    modifier = Modifier.padding(12.dp),
                                    maxLines = 3,
                                    overflow = TextOverflow.Ellipsis
                                )
                            }
                            Text(
                                text = "${if (mediaTab == MediaTab.Movies) "Movie" else "Series"} $index",
                                fontWeight = FontWeight.Bold,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )
                            Text(
                                text = rowTitle,
                                color = Color.LightGray,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun SearchScreen(modifier: Modifier = Modifier) {
    var query by rememberSaveable { mutableStateOf("") }

    Column(
        modifier = modifier.fillMaxSize().background(Color(0xFF0B0B0B)).padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text("Search", style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.Bold)
        OutlinedTextField(
            value = query,
            onValueChange = { query = it },
            modifier = Modifier.fillMaxWidth(),
            label = { Text("Search local and online") },
            singleLine = true
        )
        Text(
            "Next step is real local + TMDb search here.",
            color = Color.LightGray
        )
    }
}

@Composable
private fun SettingsScreen(modifier: Modifier = Modifier) {
    var onlineEnabled by rememberSaveable { mutableStateOf(true) }
    var allDebridKey by rememberSaveable { mutableStateOf("") }
    var rssFeeds by rememberSaveable { mutableStateOf("") }

    LazyColumn(
        modifier = modifier.fillMaxSize().background(Color(0xFF0B0B0B)),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            Text("Settings", style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.Bold)
        }

        item {
            Card {
                Column(
                    modifier = Modifier.fillMaxWidth().padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Text("Online", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text("Enable online section", fontWeight = FontWeight.Bold)
                            Text("Turns on the Online tab", color = Color.Gray)
                        }
                        Switch(checked = onlineEnabled, onCheckedChange = { onlineEnabled = it })
                    }

                    OutlinedTextField(
                        value = allDebridKey,
                        onValueChange = { allDebridKey = it },
                        modifier = Modifier.fillMaxWidth(),
                        label = { Text("AllDebrid API key") },
                        singleLine = true
                    )

                    OutlinedTextField(
                        value = rssFeeds,
                        onValueChange = { rssFeeds = it },
                        modifier = Modifier.fillMaxWidth(),
                        label = { Text("RSS feeds") },
                        minLines = 5,
                        supportingText = { Text("One feed URL per line") }
                    )

                    Text(
                        "Active feeds: " + rssFeeds.lines().map { it.trim() }.count { it.isNotBlank() },
                        color = Color.Gray
                    )
                }
            }
        }
    }
}
