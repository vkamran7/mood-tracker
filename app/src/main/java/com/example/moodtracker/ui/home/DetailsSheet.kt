package com.example.moodtracker.ui.home

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.moodtracker.domain.Tags

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailsSheet(
    selectedTags: Set<String>,
    note: String,
    onToggleTag: (String) -> Unit,
    onNoteChange: (String) -> Unit,
    onSave: () -> Unit,
    onDismiss: () -> Unit
) {
    ModalBottomSheet(
        onDismissRequest = onDismiss
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            Text("Details", style = MaterialTheme.typography.headlineSmall)

            Text("Tags", style = MaterialTheme.typography.titleMedium)
            LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                items(Tags.DEFAULT) { tag ->
                    FilterChip(
                        selected = selectedTags.contains(tag),
                        onClick = { onToggleTag(tag) },
                        label = { Text(tag) }
                    )
                }
            }

            Text("Note (optional)", style = MaterialTheme.typography.titleMedium)
            OutlinedTextField(
                value = note,
                onValueChange = onNoteChange,
                placeholder = { Text("What affected your mood? (max 120 chars)") },
                maxLines = 3,
                modifier = Modifier.fillMaxWidth()
            )

            Button(
                onClick = onSave,
                modifier = Modifier.fillMaxWidth()
            ) { Text("Save") }

            Spacer(modifier = Modifier.height(8.dp))
        }
    }
}
