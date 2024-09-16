package com.example.routeexplorer2.screens.mapScreen

import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.routeexplorer2.components.ImagePicker
import com.example.routeexplorer2.viewModels.MarkerViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddPlaceModal(
    context: Context,
    markerViewModel: MarkerViewModel,
    onDismiss: () -> Unit
){
    var isLoading by remember { mutableStateOf(false) }

    var expanded by remember { mutableStateOf(false) }

    val options = listOf(
        "Gradska Liga",
        "Okruzna Liga",
        "Zona Zapad",
        "Zona Istok",
        "Sprska Liga Istok",
        "Prva Liga"
    )


    AlertDialog(
        onDismissRequest = {
            onDismiss()
            //markerViewModel.resetState()
        },
        title = { Text("Add field") },
        text = {
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                if (isLoading) {
                    LinearProgressIndicator(
                        modifier = Modifier.width(128.dp),
                        color = MaterialTheme.colorScheme.secondary,
                        trackColor = MaterialTheme.colorScheme.surfaceVariant,
                    )

                    Spacer(modifier = Modifier.height(4.dp))
                }
                Text(text = markerViewModel.address)

                Spacer(modifier = Modifier.height(8.dp))

                OutlinedTextField(
                    value = markerViewModel.name,
                    onValueChange = { markerViewModel.name = it },
                    label = { Text("Enter Name") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth(),
                )

                Spacer(modifier = Modifier.height(16.dp))


                ExposedDropdownMenuBox(
                    expanded = expanded,
                    onExpandedChange = { expanded = it }
                ) {
                    OutlinedTextField(
                        value = markerViewModel.name,//selectedOption,
                        onValueChange = { /* No-op */ },
                        //label = { Text("Select Option") },
                        readOnly = true,
                        trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
                        singleLine = true,
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { expanded = true }
                            .menuAnchor()
                    )

                    ExposedDropdownMenu(
                        expanded = expanded,
                        onDismissRequest = { expanded = false },
                    ) {
                        options.forEach { option ->
                            DropdownMenuItem(
                                onClick = {
                                  //  markerViewModel.selectedOption = option
                                    expanded = false
                                },
                                text = {
                                    Text(option)
                                },
                                contentPadding = ExposedDropdownMenuDefaults.ItemContentPadding,
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                ImagePicker(markerViewModel.imageUri) { newUri ->
                    markerViewModel.imageUri = newUri // Ažurira sliku kada korisnik izabere novu
                }
            }
        },
        confirmButton = {
            Button(onClick = {
                isLoading = true
                markerViewModel.createMarker { success, toastMsg ->
                    isLoading = false
                    if (success) {
                        //markerViewModel.resetState()
                        onDismiss()
                        Toast.makeText(context, toastMsg, Toast.LENGTH_SHORT).show()
                    } else {
                        Toast.makeText(context, toastMsg, Toast.LENGTH_SHORT).show()
                    }

                }

            }) {
                Text("Save")
            }
        },
        dismissButton = {
            Button(onClick = {
                onDismiss()
                //markerViewModel.resetState()
            }) {
                Text("Cancel")
            }
        }
    )
}