package com.vecentek.decorelinkdemo.view

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.tooling.preview.Preview

//@Preview(showBackground = true, widthDp = 160, heightDp = 160)
@Composable
fun DrawLine(){
    Canvas(modifier = Modifier.fillMaxSize()){
        drawLine(
            start = Offset.Zero,
            end = Offset(size.width,size.height),
            color = Color.Blue,
            strokeWidth = 5f
        )
        drawLine(
            start = Offset(size.width,0f),
            end = Offset(0f,size.height),
            color = Color.Blue,
            strokeWidth = 5f
        )
    }
}

@Preview(showBackground = true, widthDp = 160, heightDp = 160)
@Composable
fun DrawPath() {
    Canvas(modifier = Modifier.fillMaxSize()) {
        val canvasWidth = size.width
        val canvasHeight = size.height
        val path = Path()
        path.addArc(
            oval = Rect(0f, 0f, canvasWidth, canvasHeight),
            0f,
            180f)
        drawPath(
            path = path,
            brush = Brush.linearGradient(colors = listOf(Color.Blue, Color.Cyan, Color.LightGray))
        )
    }
}
