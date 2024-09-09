package com.example.routeexplorer2.components

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.ClickableText
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.Visibility
import com.example.routeexplorer2.R
import com.example.routeexplorer2.ui.theme.Pink80
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.withStyle
import com.example.routeexplorer2.ui.theme.Pink40


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
            Icon(painter = painterResource,contentDescription="")
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable

fun PasswordFieldComponent(labelValue:String, painterResource: Painter){

    val password =remember{
        mutableStateOf("")
    }

    val passwordVisible = remember {
        mutableStateOf(false)
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
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password ),
        value=password.value,
        onValueChange={
            password.value = it
        },
        leadingIcon={
            Icon(painter = painterResource,contentDescription="")
        },
        trailingIcon = {
            val iconImage =if(passwordVisible.value){
                Icons.Filled.Visibility
            }else{
                Icons.Filled.VisibilityOff
            }

            var description =if(passwordVisible.value){
//                "Hide password"
                stringResource(id=R.string.hide_password)
            } else{
//                "Show password"
                stringResource(id=R.string.show_password)
            }
            
            IconButton(onClick = { passwordVisible.value = !passwordVisible.value }) {
                Icon(imageVector=iconImage, contentDescription = description)
            }
        },

        visualTransformation =
        if(passwordVisible.value)
            VisualTransformation.None
        else
            PasswordVisualTransformation()

    )
}

@Composable
fun CheckboxComponent(value:String, onTextSelected:(String) -> Unit){
    Row(modifier= Modifier
        .fillMaxWidth()
        .heightIn(56.dp),
//        .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically,
    ){
        val checkedState = remember {
            mutableStateOf(false)
        }
        Checkbox(checked =checkedState.value,
            onCheckedChange={
                checkedState.value!=checkedState.value
            })
        ClickableTextComponent(value=value,onTextSelected)
    }
}

@Composable
fun ClickableTextComponent(value:String, onTextSelected:(String) -> Unit){

    val initialText="By continuing you accept our"
    val privacyPolicyText=" Privacy Policy"
    val andText=" and"
    val termsAndConditionsText=" Term of Use"
    val annotatedString = buildAnnotatedString {
        append(initialText)
        withStyle(style=SpanStyle(color= Pink80)){
            pushStringAnnotation(tag=privacyPolicyText, annotation = privacyPolicyText)
            append(privacyPolicyText)
        }
        append(andText)
        withStyle(style=SpanStyle(color= Pink80)){
            pushStringAnnotation(tag=termsAndConditionsText, annotation = termsAndConditionsText)
            append(termsAndConditionsText)
        }
    }
    ClickableText(text =annotatedString ,onClick={offset ->
        annotatedString.getStringAnnotations(offset,offset)
            .firstOrNull()?.also {span ->
                Log.d("ClickableTextComponent","{$span}")

                if((span.item ==termsAndConditionsText) || (span.item==privacyPolicyText)){
                    onTextSelected(span.item)
                }

            }
    })
}

@Composable
fun ButtonComponent(value:String){
    Button(
        onClick={},
        modifier= Modifier
            .fillMaxWidth()
            .heightIn(48.dp),
        contentPadding= PaddingValues(),
        colors = ButtonDefaults.buttonColors(Color.Transparent)

    ){
        Box(modifier=Modifier
            .fillMaxWidth()
            .heightIn(48.dp)
            .background(
                brush= Brush.horizontalGradient(listOf(Pink80, Pink40)),
                shape= RoundedCornerShape(50.dp)
            ),
            contentAlignment=Alignment.Center
        ){
            Text(
                text=value,
                fontSize=18.sp,
                fontWeight=FontWeight.Bold

            )
        }
    }
}


@Composable
fun ClickableLoginTextComponent(onTextSelected: (String) -> Unit){

    val initialText="Already have an account? "
    val loginText="Login"

    val annotatedString = buildAnnotatedString {
        append(initialText)
        withStyle(style=SpanStyle(color= Pink80)){
            pushStringAnnotation(tag=loginText, annotation = loginText)
            append(loginText)
        }

    }

    ClickableText(text =annotatedString ,onClick={offset ->
        annotatedString.getStringAnnotations(offset,offset)
            .firstOrNull()?.also {span ->
                Log.d("ClickableTextComponent","{$span}")

                if((span.item ==loginText) || (span.item==loginText)){
                    onTextSelected(span.item)
                }

            }
    })
}


