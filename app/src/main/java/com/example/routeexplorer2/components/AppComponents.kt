package com.example.routeexplorer2.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.routeexplorer2.R
import com.example.routeexplorer2.ui.theme.Pink80
import com.example.routeexplorer2.ui.theme.TextColor

@Composable
fun NormalTextComponent(value: String){
    Text(
        text=value,
        modifier= Modifier
            .fillMaxWidth()
            .heightIn(min = 80.dp),
        style = TextStyle(
            fontSize=24.sp,
            fontWeight = FontWeight.Normal,
            fontStyle= FontStyle.Normal
        )
    , color= colorResource(id = R.color.colorText),
        textAlign = TextAlign.Center



    )
}

@Composable
fun HeadingTextComponent(value: String){
    Text(
        text=value,
        modifier= Modifier
            .fillMaxWidth()
            .heightIn(min = 80.dp),
        style = TextStyle(
            fontSize=30.sp,
            fontWeight = FontWeight.Bold,
            fontStyle= FontStyle.Normal
        )
        , color= colorResource(id = R.color.colorText),
        textAlign = TextAlign.Center



    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MyTextFieldComponent(labelValue:String, painterResource: Painter){

    val textValue =remember{
        mutableStateOf("")
    }

    OutlinedTextField(
        modifier = Modifier.fillMaxWidth(),//.clip(componentShapes.small),
        label = {Text(text=labelValue)},

        colors=TextFieldDefaults.outlinedTextFieldColors(
            focusedBorderColor = Pink80,
            focusedLabelColor= Pink80,
            cursorColor= Pink80,
//            backgroundColor= TextColor //nece kod mene
        ),
        keyboardOptions = KeyboardOptions.Default,
        value=textValue.value,
        onValueChange={
            textValue.value = it
        },
        leadingIcon={
            Icon(painter= painterResource,contentDescription="")
        }
    )
}