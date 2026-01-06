package com.example.moodtracker.ui.home

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.moodtracker.domain.Tags
import kotlinx.coroutines.CancellationException

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
    val sheetState = rememberModalBottomSheetState(
        skipPartiallyExpanded = true
    )

    // ✅ On first show, immediately expand fully
    LaunchedEffect(Unit) {
        try {
            sheetState.expand()
        } catch (_: CancellationException) {
            // ignore if sheet dismissed quickly
        }
    }

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp, vertical = 12.dp)
                // ✅ Avoid overlap with system nav + keyboard
                .windowInsetsPadding(WindowInsets.safeDrawing)
                .imePadding(),
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
                onValueChange = { onNoteChange(it.take(120)) },
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
