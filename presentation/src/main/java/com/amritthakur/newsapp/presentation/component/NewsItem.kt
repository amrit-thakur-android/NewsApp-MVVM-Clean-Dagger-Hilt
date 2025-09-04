package com.amritthakur.newsapp.presentation.component

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import com.amritthakur.newsapp.domain.entity.Article
import com.amritthakur.newsapp.presentation.R

@Composable
fun NewsItem(
    article: Article,
    onNews: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onNews() },
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        shape = RoundedCornerShape(8.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            if (article.urlToImage.isNotEmpty()) {
                AsyncImage(
                    model = article.urlToImage,
                    contentDescription = stringResource(R.string.article_image),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(160.dp)
                        .clip(RoundedCornerShape(8.dp)),
                    contentScale = ContentScale.Crop,
                )

                Spacer(modifier = Modifier.height(12.dp))
            }

            Text(
                text = article.title,
                style = MaterialTheme.typography.titleMedium,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = article.description,
                style = MaterialTheme.typography.bodyMedium,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = stringResource(R.string.source_format, article.sourceName),
                style = MaterialTheme.typography.bodySmall,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun NewsItemWithImagePreview() {
    MaterialTheme {
        NewsItem(
            article = Article(
                sourceId = "bbc-news",
                sourceName = "BBC News",
                author = "John Doe",
                title = "Breaking News: Important Development in Technology Sector",
                description = "This is a sample news description that shows how the news item will look in the app. It provides a brief overview of the article content.",
                url = "https://example.com/news",
                urlToImage = "https://via.placeholder.com/400x200",
                publishedAt = "2023-12-01T10:00:00Z",
                content = "Full news content here with more details about the story."
            ),
            onNews = { }
        )
    }
}

@Preview(showBackground = true)
@Composable
fun NewsItemWithoutImagePreview() {
    MaterialTheme {
        NewsItem(
            article = Article(
                sourceId = "reuters",
                sourceName = "Reuters",
                author = "Jane Smith",
                title = "Another Important News Story Without Image",
                description = "This news item demonstrates how the component looks when there is no image available for the article.",
                url = "https://example.com/news-no-image",
                urlToImage = "",
                publishedAt = "2023-12-01T11:30:00Z",
                content = "Complete article content without an accompanying image."
            ),
            onNews = { }
        )
    }
}
