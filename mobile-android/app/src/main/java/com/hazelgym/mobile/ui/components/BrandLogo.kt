package com.hazelgym.mobile.ui.components

import androidx.compose.foundation.Image
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import com.hazelgym.mobile.R

@Composable
fun HazelGymLogo(
    modifier: Modifier = Modifier,
    tint: Color? = Color.White
) {
    Image(
        painter = painterResource(id = R.drawable.logo_hazelgym_no_back_1),
        contentDescription = "Hazel Gym",
        modifier = modifier,
        contentScale = ContentScale.Fit,
        colorFilter = tint?.let { ColorFilter.tint(it) }
    )
}
