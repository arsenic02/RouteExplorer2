package com.example.routeexplorer2.screens.mapScreen

import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.Star
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import com.example.routeexplorer2.viewModels.PlaceViewModel


@Composable
fun ReviewDialog(
    onDismissRequest: () -> Unit,
    placeId: String,
    placeViewModel: PlaceViewModel,
) {

    val context = LocalContext.current


    AlertDialog(
        onDismissRequest = {
            onDismissRequest()
            placeViewModel.resetReviewData()
        },
        title = { Text("Add Review") },
        text = {
            Column {
                // Star rating
                Row(
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp)
                ) {
                    // Display stars based on rating
                    (1..5).forEach { index ->
                        IconButton(
                            onClick = {
                                placeViewModel.selectedStars = index
                            },
                            modifier = Modifier.size(24.dp)
                        ) {
                            Icon(
                                imageVector = if (index <= placeViewModel.selectedStars) Icons.Filled.Star else Icons.Outlined.Star,
                                tint = if (index <= placeViewModel.selectedStars) Color.Yellow else Color.Gray,
                                contentDescription = null,
                            )
                        }
                    }
                }

                // Comment text field
                TextField(
                    value = placeViewModel.comment,
                    onValueChange = {
                        placeViewModel.comment = it
                    },
                    placeholder = { Text("Add a comment") },
                    maxLines = 3,
                    keyboardOptions = KeyboardOptions.Default.copy(
                        imeAction = ImeAction.Done
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp)
                )
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    placeViewModel.createReview(placeId) { isNotReviewed ->
                        if (isNotReviewed) {
                            Toast.makeText(
                                context,
                                "Review created successfully",
                                Toast.LENGTH_SHORT
                            ).show()
                            placeViewModel.resetReviewData()
                        } else {
                            Toast.makeText(
                                context,
                                "Failed to create review or already reviewed",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
                    onDismissRequest()
                }
            ) {
                Text("Add Review")
            }
        },
        dismissButton = {
            Button(
                onClick = {
                    onDismissRequest()
                    placeViewModel.resetReviewData()
                }
            ) {
                Text("Cancel")
            }
        }
    )
}