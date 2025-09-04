package com.amritthakur.newsapp.presentation.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.amritthakur.newsapp.presentation.R

@Composable
fun ErrorView(
    message: String,
    onTryAgain: (() -> Unit)? = null
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Icon(
            imageVector = Icons.Default.Warning,
            contentDescription = stringResource(R.string.error),
            modifier = Modifier.size(64.dp),
            tint = MaterialTheme.colorScheme.error
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = stringResource(R.string.something_went_wrong),
            color = MaterialTheme.colorScheme.error,
            style = MaterialTheme.typography.headlineSmall,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = message,
            style = MaterialTheme.typography.bodyMedium,
            textAlign = TextAlign.Center
        )

        onTryAgain?.let {

            Spacer(modifier = Modifier.height(24.dp))

            Button(
                onClick = it
            ) {
                Text(
                    text = stringResource(R.string.try_again)
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ErrorViewPreview() {
    MaterialTheme {
        ErrorView(
            message = "Internal server error",
            onTryAgain = {}
        )
    }
}

@Preview(showBackground = true)
@Composable
fun ErrorViewWithoutRetryPreview() {
    MaterialTheme {
        ErrorView(
            message = "Internal server error"
        )
    }
}
