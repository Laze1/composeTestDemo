package com.vecentek.decorelinkdemo

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material.Button
import androidx.compose.material.DropdownMenu
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp


@Composable
fun MyButton(msg: String, click: () -> Unit) {
    Button(
        modifier = Modifier.padding(10.dp),
        onClick = click
    ) {
        Text(text = msg)
    }
}


@Composable
fun DropDownUrl(onClick: (String) -> Unit) {
//    val context = LocalContext.current
    var expanded = remember {
        mutableStateOf(false)
    }
    Box(
        Modifier
            .padding(10.dp)
            .wrapContentSize(Alignment.TopStart)
    ) {
        TextButton(
            onClick = {
                expanded.value = true
            },
            modifier = Modifier.background(Color.LightGray)
        ) {
            Text(text = "选择url")
        }

        DropdownMenu(expanded = expanded.value, onDismissRequest = { expanded.value = false }) {
            urlList.forEachIndexed { index, s ->
                DropdownMenuItem(onClick = {
                    expanded.value = false
                    DLMain.instance.url = s
                    onClick(s)
                }) {
                    Text(text = s)
                }
            }
        }
    }
}

@Composable
fun DropDownUser(onClick: (String) -> Unit) {
//    val context = LocalContext.current
    var expanded = remember {
        mutableStateOf(false)
    }
    Box(
        Modifier
            .padding(10.dp)
            .wrapContentSize(Alignment.TopStart)
    ) {
        TextButton(
            onClick = {
                expanded.value = true
            },
            modifier = Modifier.background(Color.LightGray)
        ) {
            Text(text = "选择user")
        }

        DropdownMenu(expanded = expanded.value, onDismissRequest = { expanded.value = false }) {
            userList.forEachIndexed { index, s ->
                DropdownMenuItem(onClick = {
                    expanded.value = false
                    DLMain.instance.vin = s.third
                    DLMain.instance.phone = s.first
                    onClick("${s.first} - ${s.third}")
                }) {
                    Text(text = "${s.first} - ${s.third}")
                }
            }
        }
    }
}