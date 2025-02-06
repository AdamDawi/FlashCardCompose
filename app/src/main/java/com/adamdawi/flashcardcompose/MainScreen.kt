package com.adamdawi.flashcardcompose

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.adamdawi.flashcardcompose.ui.theme.Blue
import com.adamdawi.flashcardcompose.ui.theme.DarkBlue
import com.adamdawi.flashcardcompose.ui.theme.FlashCardComposeTheme
import com.adamdawi.flashcardcompose.ui.theme.Green
import com.adamdawi.flashcardcompose.ui.theme.Red

@Composable
fun MainScreen() {
    val redCounter = remember {
        mutableIntStateOf(0)
    }

    val greenCounter = remember {
        mutableIntStateOf(0)
    }
    Scaffold(
        modifier = Modifier
            .fillMaxSize(),
        containerColor = DarkBlue
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            TopButtons()
            HorizontalDivider(thickness = 3.dp, color = Blue)
            CountersRow(
                redCounter = redCounter.intValue,
                greenCounter = greenCounter.intValue
            )
            Column(
                modifier = Modifier
                    .fillMaxSize(),
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                FlashCard(
                    onLeftSwipe = {
                        redCounter.intValue++
                    },
                    onRightSwipe = {
                        greenCounter.intValue++
                    }
                )
                BottomButtons()
            }
        }
    }
}

@Composable
private fun TopButtons(
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(8.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        IconButton(
            onClick = {}
        ) {
            Icon(
                modifier = Modifier
                    .size(26.dp),
                imageVector = Icons.Default.Clear,
                contentDescription = null,
                tint = Color.White
            )
        }
        Text(
            text = "1/88",
            color = Color.White,
            textAlign = TextAlign.Center,
            fontSize = 18.sp,
            fontWeight = FontWeight.SemiBold
        )
        IconButton(
            onClick = {}
        ) {
            Icon(
                modifier = Modifier
                    .size(26.dp),
                imageVector = Icons.Default.Settings,
                contentDescription = null,
                tint = Color.White
            )
        }
    }
}

@Composable
private fun CountersRow(
    modifier: Modifier = Modifier,
    redCounter: Int,
    greenCounter: Int
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(top = 16.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Box(
            modifier = Modifier
                .width(60.dp)
                .height(38.dp)
                .border(
                    width = 1.dp,
                    color = Red,
                    RoundedCornerShape(
                        topStart = 0.dp,
                        topEnd = 100.dp,
                        bottomStart = 0.dp,
                        bottomEnd = 100.dp
                    )
                ),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = redCounter.toString(),
                color = Red,
                textAlign = TextAlign.Center,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold
            )
        }

        Box(
            modifier = Modifier
                .width(60.dp)
                .height(38.dp)
                .border(
                    width = 1.dp,
                    color = Green,
                    RoundedCornerShape(
                        topStart = 100.dp,
                        topEnd = 0.dp,
                        bottomStart = 100.dp,
                        bottomEnd = 0.dp
                    )
                ),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = greenCounter.toString(),
                color = Green,
                textAlign = TextAlign.Center,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold
            )
        }
    }
}

@Composable
private fun BottomButtons(
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(12.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        IconButton(
            onClick = {}
        ) {
            Icon(
                modifier = Modifier
                    .size(26.dp),
                painter = painterResource(R.drawable.return_ic),
                contentDescription = null,
                tint = Color.White
            )
        }
        IconButton(
            onClick = {}
        ) {
            Icon(
                modifier = Modifier
                    .size(26.dp),
                imageVector = Icons.Default.PlayArrow,
                contentDescription = null,
                tint = Color.White
            )
        }
    }
}


@Preview
@Composable
private fun MainScreenPreview() {
    FlashCardComposeTheme {
        MainScreen()
    }
}