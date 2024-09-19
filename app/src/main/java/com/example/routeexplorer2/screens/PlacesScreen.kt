package com.example.routeexplorer2.screens

import android.util.Log
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import coil.compose.rememberAsyncImagePainter
import com.example.routeexplorer2.R
import com.example.routeexplorer2.Screens
import com.example.routeexplorer2.data.model.Place
import com.example.routeexplorer2.screens.mapScreen.FilterPlaceModal
import com.example.routeexplorer2.utils.extractAddressPart
import com.example.routeexplorer2.utils.formatDate
import com.example.routeexplorer2.viewModels.MarkerViewModel
import com.example.routeexplorer2.viewModels.PlaceViewModel
import com.example.routeexplorer2.viewModels.UserViewModel


@Composable
fun PlacesScreen(
    navController: NavHostController,
    userViewModel: UserViewModel,
    markerViewModel: MarkerViewModel,
    placeViewModel: PlaceViewModel,
    selectPlace: (Place) -> Unit,
    modifier: Modifier = Modifier
) {

    Log.d("selectPlace","$selectPlace")
    //2024-09-19 13:05:39.473 21883-21883 selectPlace             com.example.routeexplorer2           D  Function1<com.example.routeexplorer2.data.model.Place, kotlin.Unit>
    val context = LocalContext.current

    val currentUserLocation by userViewModel.currentUserLocation.collectAsState()

    var isFilteredModalOpen by remember { mutableStateOf(false) }

    val filteredMarkers by markerViewModel.filteredMarkers.collectAsState()
    val markers by markerViewModel.markers.collectAsState(emptyList())//bilo je navedeno empty list

    Box(modifier = modifier.fillMaxSize()) {
        LazyColumn {
            val markersToDisplay =
                if (filteredMarkers.isNotEmpty()) filteredMarkers else markers
            items(markersToDisplay) { marker ->
                Log.d("marker","$marker")
                /*
                Place(id=8n48SWO5nK776gyyaCtv, name=Aleksinac, type=, address=, longitude=21.711555272340775, latitude=43.53981752159446, selectedOption=Car, icon=0, reviews=[], avgRating=0.0, reviewCount=0, photo=, timeCreated=Timestamp(seconds=1726744630, nanoseconds=821000000), author=)
                Place(id=8n48SWO5nK776gyyaCtv, name=Aleksinac, type=, address=, longitude=21.711555272340775, latitude=43.53981752159446, selectedOption=Car, icon=0, reviews=[], avgRating=0.0, reviewCount=0, photo=, timeCreated=Timestamp(seconds=1726744630, nanoseconds=821000000), author=)
                Place(id=9VFb9MHs1pnzlmRIqgw9, name=Palanka, type=, address=, longitude=22.29458637535572, latitude=43.23669847107124, selectedOption=Car, icon=0, reviews=[], avgRating=0.0, reviewCount=0, photo=, timeCreated=Timestamp(seconds=1726744630, nanoseconds=823000000), author=)
                Place(id=9VFb9MHs1pnzlmRIqgw9, name=Palanka, type=, address=, longitude=22.29458637535572, latitude=43.23669847107124, selectedOption=Car, icon=0, reviews=[], avgRating=0.0, reviewCount=0, photo=, timeCreated=Timestamp(seconds=1726744630, nanoseconds=823000000), author=)
                Place(id=UE0uyKMgmCcq8R3i4duC, name=Nis, type=, address=, longitude=21.885009706020355, latitude=43.33866089077561, selectedOption=Run, icon=0, reviews=[], avgRating=0.0, reviewCount=0, photo=, timeCreated=Timestamp(seconds=1726744630, nanoseconds=825000000), author=)
                Place(id=UE0uyKMgmCcq8R3i4duC, name=Nis, type=, address=, longitude=21.885009706020355, latitude=43.33866089077561, selectedOption=Run, icon=0, reviews=[], avgRating=0.0, reviewCount=0, photo=, timeCreated=Timestamp(seconds=1726744630, nanoseconds=825000000), author=)
                Place(id=pIzaYl5ZpasQ7ZcSUwT2, name=Knjazevac, type=, address=, longitude=22.269599922001362, latitude=43.619452657606175, selectedOption=Car, icon=0, reviews=[], avgRating=0.0, reviewCount=0, photo=, timeCreated=Timestamp(seconds=1726744630, nanoseconds=826000000), author=)
                Place(id=pIzaYl5ZpasQ7ZcSUwT2, name=Knjazevac, type=, address=, longitude=22.269599922001362, latitude=43.619452657606175, selectedOption=Car, icon=0, reviews=[], avgRating=0.0, reviewCount=0, photo=, timeCreated=Timestamp(seconds=1726744630, nanoseconds=826000000), author=)
                 */
                PlaceCard(
                    place = marker,
                    navController = navController,
                    selectPlace = { selectPlace(it) }
                )
            }
        }

        if (isFilteredModalOpen) {
            FilterPlaceModal(
                context = context,
                currentUserLocation = currentUserLocation,
                markerViewModel = markerViewModel,
                onDismiss = { isFilteredModalOpen = false }
            )
        }


        Button(
            onClick = { isFilteredModalOpen = true },
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(16.dp)
                .size(56.dp), // Set the size to be circular
            elevation = ButtonDefaults.buttonElevation(
                defaultElevation = 20.dp
            ),
            shape = RoundedCornerShape(16.dp),
            contentPadding = PaddingValues(10.dp) // No extra padding inside the button
        ) {
            Icon(
                imageVector = Icons.Default.Search, // Replace with your desired icon
                contentDescription = "Filter",
                tint = Color.White // Adjust icon color if needed
            )
        }


        if (filteredMarkers.isNotEmpty()) {
            IconButton(
                onClick = { markerViewModel.removeFilters() },
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(
                        top = 16.dp,
                        bottom = 24.dp,
                        start = 130.dp
                    ) // Adjust padding as needed
                    .size(40.dp) // Larger size for the button
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_search_off_24),
                    contentDescription = "Remove filters",
                    tint = MaterialTheme.colorScheme.primary, // Adjust icon color if needed
                    modifier = Modifier.size(32.dp)
                )
            }
        }
    }
}


@Composable
fun PlaceCard(
    place: Place,
    navController: NavHostController,
    selectPlace: (Place) -> Unit,
    modifier: Modifier = Modifier
) {

    //ovde treba da pzozovem place iz viewModela
    Log.d("place: ","$place")
    //place: Place(id=8n48SWO5nK776gyyaCtv, name=Aleksinac, type=, address=, longitude=21.711555272340775, latitude=43.53981752159446, selectedOption=Car, icon=0, reviews=[], avgRating=0.0, reviewCount=0, photo=, timeCreated=Timestamp(seconds=1726744188, nanoseconds=826000000), author=)
    //place: Place(id=9VFb9MHs1pnzlmRIqgw9, name=Palanka, type=, address=, longitude=22.29458637535572, latitude=43.23669847107124, selectedOption=Car, icon=0, reviews=[], avgRating=0.0, reviewCount=0, photo=, timeCreated=Timestamp(seconds=1726744188, nanoseconds=827000000), author=)
//    place: Place(id=UE0uyKMgmCcq8R3i4duC, name=Nis, type=, address=, longitude=21.885009706020355, latitude=43.33866089077561, selectedOption=Run, icon=0, reviews=[], avgRating=0.0, reviewCount=0, photo=, timeCreated=Timestamp(seconds=1726744188, nanoseconds=829000000), author=)
//    place: Place(id=pIzaYl5ZpasQ7ZcSUwT2, name=Knjazevac, type=, address=, longitude=22.269599922001362, latitude=43.619452657606175, selectedOption=Car, icon=0, reviews=[], avgRating=0.0, reviewCount=0, photo=, timeCreated=Timestamp(seconds=1726744188, nanoseconds=831000000), author=)
    var expanded by remember { mutableStateOf(false) }

    val formattedDate = formatDate(place.timeCreated)
    val formattedAddress = extractAddressPart(place.address)


    Box(
        modifier = modifier.padding(12.dp)
    ) {
        Card(
            modifier = modifier.clickable {
                selectPlace(place)
                navController.navigate(Screens.Place.name)
            }
        ) {
            Column(
                modifier = Modifier
                    .animateContentSize(
                        animationSpec = spring(
                            dampingRatio = Spring.DampingRatioNoBouncy,
                            stiffness = Spring.StiffnessMedium
                        )
                    )
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp)
                ) {
                    val painter = rememberAsyncImagePainter(place.photo)
                    Image(
                        modifier = modifier
                            .size(64.dp)
                            .padding(8.dp)
                            .clip(MaterialTheme.shapes.small),
                        contentScale = ContentScale.Crop,
                        painter = painter,

                        // Content Description is not needed here - image is decorative, and setting a null content
                        // description allows accessibility services to skip this element during navigation.

                        contentDescription = null
                    )
                    Column(modifier = modifier) {
                        Text(
                            text = place.name,
                            style = MaterialTheme.typography.displayMedium,
                            modifier = Modifier.padding(top = 8.dp)
                        )
                        Text(

                            text = formattedAddress,
                            style = MaterialTheme.typography.bodyLarge
                        )
                    }

                    Row(
                        modifier = modifier
                            .padding(start = 0.dp, top = 0.dp, end = 24.dp, bottom = 0.dp),
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically

                    ) {
                        Text(text = "Rating: ${place.avgRating} (Reviews: ${place.reviewCount})")
                    }


                }
                if (expanded) {
                    Row(
                        modifier = modifier.padding(8.dp)
                    ) {
                        Text(
                            // ovo sam menjao
                            text = "by ${place.author}",
                            style = MaterialTheme.typography.labelSmall,
                            modifier = Modifier.padding(horizontal = 8.dp)
                        )


                        Text(
                            text = "at ${formattedDate}",
                            style = MaterialTheme.typography.bodyLarge
                        )

                    }
                }
            }
        }
        IconButton(
            onClick = { expanded = !expanded },
            modifier = Modifier
                .align(Alignment.TopEnd)
        ) {
            Icon(
                imageVector = if (expanded) Icons.Filled.KeyboardArrowUp else Icons.Filled.KeyboardArrowDown,
                contentDescription = "Expading Icon",
                tint = MaterialTheme.colorScheme.primary
            )
        }

    }
}


