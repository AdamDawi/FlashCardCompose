package com.adamdawi.flashcardcompose

import androidx.annotation.DrawableRes
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.TweenSpec
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.adamdawi.flashcardcompose.ui.theme.Blue
import com.adamdawi.flashcardcompose.ui.theme.DarkBlue
import com.adamdawi.flashcardcompose.ui.theme.FlashcardComposeTheme
import com.adamdawi.flashcardcompose.ui.theme.Green
import com.adamdawi.flashcardcompose.ui.theme.LightBlue
import com.adamdawi.flashcardcompose.ui.theme.Red

private val listOfFlashcardsTexts = mapOf(
    "hello" to "hola",
    "not much" to "no mucho",
    "fine" to "bien",
    "one" to "uno",
    "two" to "dos",
    "three" to "tres",
    "bathroom" to "El Baño",
    "kitchen" to "La cocina"
)

private const val COUNTERS_ANIMATION_TIME = 200

@Composable
fun MainScreen() {
    val currentFlashcardNumber = remember {
        mutableIntStateOf(0)
    }
    // Counters states section
    val correctCount = remember {
        mutableIntStateOf(0)
    }

    val incorrectCount = remember {
        mutableIntStateOf(0)
    }

    val correctBgColor = remember {
        mutableStateOf(Color.Transparent)
    }

    val incorrectBgColor = remember {
        mutableStateOf(Color.Transparent)
    }

    val animatedCorrectBg = animateColorAsState(
        targetValue = correctBgColor.value,
        animationSpec = TweenSpec(
            durationMillis = COUNTERS_ANIMATION_TIME
        ),
        label = ""
    )

    val animatedIncorrectBg = animateColorAsState(
        targetValue = incorrectBgColor.value,
        animationSpec = TweenSpec(
            durationMillis = COUNTERS_ANIMATION_TIME
        ),
        label = ""
    )

    val animatedCorrectAlpha = animateFloatAsState(
        targetValue = if (correctBgColor.value == Green) 1f else 0f,
        animationSpec = TweenSpec(
            durationMillis = COUNTERS_ANIMATION_TIME
        ),
        label = ""
    )

    val animatedIncorrectAlpha = animateFloatAsState(
        targetValue = if (incorrectBgColor.value == Red) 1f else 0f,
        animationSpec = TweenSpec(
            durationMillis = COUNTERS_ANIMATION_TIME
        ),
        label = ""
    )

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
            TopButtonsRow(
                currentFlashcardNumber = currentFlashcardNumber.intValue
            )
            HorizontalDivider(thickness = 3.dp, color = Blue)
            CountersRow(
                redCounter = incorrectCount.intValue,
                greenCounter = correctCount.intValue,
                greenCounterBg = animatedCorrectBg.value,
                redCounterBg = animatedIncorrectBg.value,
                redCounterAlpha = animatedIncorrectAlpha.value,
                greenCounterAlpha = animatedCorrectAlpha.value
            )
            Spacer(modifier = Modifier.height(22.dp))
            Column(
                modifier = Modifier
                    .fillMaxSize(),
                verticalArrangement = Arrangement.SpaceBetween,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Flashcard(
                    frontText = listOfFlashcardsTexts.keys.elementAt(currentFlashcardNumber.intValue % listOfFlashcardsTexts.size), //Avoided OutOfBounds index error with %
                    backText = listOfFlashcardsTexts.values.elementAt(currentFlashcardNumber.intValue % listOfFlashcardsTexts.size), //Avoided OutOfBounds index error with %
                    onSwipeLeft = {
                        incorrectCount.intValue++
                        correctBgColor.value = Color.Transparent
                        incorrectBgColor.value = Color.Transparent
                        currentFlashcardNumber.intValue++
                    },
                    onSwipeRight = {
                        correctCount.intValue++
                        correctBgColor.value = Color.Transparent
                        incorrectBgColor.value = Color.Transparent
                        currentFlashcardNumber.intValue++
                    },
                    onLeftSwipeApproach = {
                        incorrectBgColor.value = Red
                    },
                    onRightSwipeApproach = {
                        correctBgColor.value = Green
                    },
                    onNeutralPosition = {
                        correctBgColor.value = Color.Transparent
                        incorrectBgColor.value = Color.Transparent
                    },
                    topButtonRow = {
                        FunctionButtonRow(
                            firstIcon = R.drawable.sound_high,
                            secondIcon = R.drawable.baseline_star_outline_24
                        )
                    }
                )
                BottomButtons()
            }
        }
    }
}

@Composable
private fun TopButtonsRow(
    modifier: Modifier = Modifier,
    currentFlashcardNumber: Int
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
            text = "${currentFlashcardNumber+1}/88",
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
    redCounter: Int,
    greenCounter: Int,
    greenCounterBg: Color,
    redCounterBg: Color,
    redCounterAlpha: Float,
    greenCounterAlpha: Float
) {
    Row(
        modifier = Modifier.fillMaxWidth().padding(top = 16.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        CounterBox(
            counter = redCounter,
            counterBg = redCounterBg,
            counterAlpha = redCounterAlpha,
            textColor = Red,
            borderShape = RoundedCornerShape(
                topStart = 0.dp,
                topEnd = 100.dp,
                bottomStart = 0.dp,
                bottomEnd = 100.dp
            )
        )
        CounterBox(
            counter = greenCounter,
            counterBg = greenCounterBg,
            counterAlpha = greenCounterAlpha,
            textColor = Green,
            borderShape = RoundedCornerShape(
                topStart = 100.dp,
                topEnd = 0.dp,
                bottomStart = 100.dp,
                bottomEnd = 0.dp
            )
        )
    }
}

@Composable
private fun CounterBox(
    counter: Int,
    counterBg: Color,
    counterAlpha: Float,
    textColor: Color,
    borderShape: RoundedCornerShape
) {
    Box(
        modifier = Modifier
            .width(60.dp)
            .height(38.dp)
            .border(1.dp, textColor, borderShape)
            .clip(borderShape)
            .background(counterBg),
        contentAlignment = Alignment.Center
    ) {
        Box {
            Text(
                modifier = Modifier.alpha(counterAlpha),
                text = "+1",
                color = Color.White,
                textAlign = TextAlign.Center,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold
            )
            Text(
                modifier = Modifier.alpha(1f - counterAlpha), //inversed alpha
                text = counter.toString(),
                color = textColor,
                textAlign = TextAlign.Center,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold
            )
        }
    }
}
@Composable
fun FunctionButtonRow(
    modifier: Modifier = Modifier,
    @DrawableRes firstIcon: Int,
    @DrawableRes secondIcon: Int
) {
    Row(
        modifier = modifier
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Icon(
            modifier = Modifier
                .size(26.dp),
            painter = painterResource(firstIcon),
            contentDescription = null,
            tint = LightBlue
        )
        Icon(
            modifier = Modifier
                .size(26.dp),
            painter = painterResource(secondIcon),
            contentDescription = null,
            tint = LightBlue
        )
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
    FlashcardComposeTheme {
        MainScreen()
    }
}