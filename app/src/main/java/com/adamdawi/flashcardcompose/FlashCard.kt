package com.adamdawi.flashcardcompose

import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.animateOffsetAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.TransformOrigin
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.adamdawi.flashcardcompose.ui.theme.Blue
import com.adamdawi.flashcardcompose.ui.theme.Green
import com.adamdawi.flashcardcompose.ui.theme.LightBlue
import com.adamdawi.flashcardcompose.ui.theme.Red
import kotlin.math.roundToInt

private const val FLIP_CARD_ANIMATION_TIME = 400
private const val OFFSET_LIMIT = 40
private val ROUNDED_CORNER_RADIUS = 16.dp

@Composable
fun FlashCard(
    modifier: Modifier = Modifier,
    frontText: String,
    backText: String,
    onLeftSwipe: () -> Unit = {},
    onRightSwipe: () -> Unit = {},
    height: Dp = 500.dp,
    onCloseToRight: () -> Unit = {},
    onCloseToLeft: () -> Unit = {},
    onNeutral: () -> Unit = {}
) {
    val isCardFlipped = remember {
        mutableStateOf(false)
    }
    val cardBorderColor = remember {
        mutableStateOf(Color.Transparent)
    }
    val cardOffset = remember {
        mutableStateOf(Offset.Zero)
    }
    val alphaImage = buildAlphaAnimation(targetValue = if (!isCardFlipped.value) 1f else 0f)
    val alphaDescription = buildAlphaAnimation(targetValue = if (isCardFlipped.value) 1f else 0f)
    val rotationFrontCardY = buildRotationAnimation(targetValue = if (!isCardFlipped.value) 0f else 180f)
    val rotationBackCardY =
        buildRotationAnimation(targetValue = if (!isCardFlipped.value) -180f else 0f)
//    var animatedCardOffsetX = Animatable(0f)
//    var animatedCardOffsetY = Animatable(0f)
    var animatedCardOffset = buildOffsetAnimation(targetValue = cardOffset.value)

//    LaunchedEffect(cardOffset.value) {
//        if (cardOffset.value.x > 800f || cardOffset.value.x < -800f) {
//            kotlinx.coroutines.delay(300)
//            animatedCardOffset.value = Offset.Zero
//        }
//    }
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(height)
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(start = 25.dp, end = 25.dp, top = 25.dp)
                .clip(
                    RoundedCornerShape(12.dp)
                )
                .background(Blue)
        )

        Box(modifier = modifier
            .offset {
                IntOffset(
                    animatedCardOffset.value.x.roundToInt(),
                    animatedCardOffset.value.y.roundToInt()
                )
            }
            .graphicsLayer {
                rotationZ = cardOffset.value.x / 100 //TODO a lot of recompositions
            }
            .fillMaxSize()
            .padding(start = 25.dp, end = 25.dp, top = 25.dp)
            .border(
                BorderStroke(2.dp, cardBorderColor.value),
                shape = RoundedCornerShape(ROUNDED_CORNER_RADIUS)
            )
            .pointerInput(Unit) {
                detectDragGestures(
                    onDragEnd = {
                        //Swipe right
                        if (cardOffset.value.x > OFFSET_LIMIT) {
//                            cardOffset.value = Offset(1200f, 0f)
                            cardBorderColor.value = Color.Transparent
                            onRightSwipe()

                            cardOffset.value = Offset.Zero
                        }
                        //Swipe left
                        else if (cardOffset.value.x < -OFFSET_LIMIT) {
                            cardOffset.value = Offset.Zero
                            cardBorderColor.value = Color.Transparent
                            onLeftSwipe()
                        } else cardOffset.value = Offset.Zero
                    },
                    onDragCancel = {
                        //Swipe right
                        if (cardOffset.value.x > OFFSET_LIMIT) {
                            cardOffset.value = Offset.Zero
                            cardBorderColor.value = Color.Transparent
                            onRightSwipe()
                        }
                        //Swipe left
                        else if (cardOffset.value.x < -OFFSET_LIMIT) {
                            cardOffset.value = Offset.Zero
                            cardBorderColor.value = Color.Transparent
                            onLeftSwipe()
                        } else cardOffset.value = Offset.Zero
                    },
                    onDrag = { change, dragAmount ->
                        change.consume()
                        cardOffset.value = Offset(
                            cardOffset.value.x + dragAmount.x,
                            cardOffset.value.y + dragAmount.y
                        )
                        //close to right
                        if (cardOffset.value.x > OFFSET_LIMIT) {
                            cardBorderColor.value = Green
                            onCloseToRight()
                        }
                        //close to left
                        else if (cardOffset.value.x < -OFFSET_LIMIT) {
                            cardBorderColor.value = Red
                            onCloseToLeft()
                        }
                        //neutral
                        else {
                            cardBorderColor.value = Color.Transparent
                            onNeutral()
                        }
                    }
                )
            }
            //clicking without ripple
            .pointerInput(Unit) {
                detectTapGestures {
                    isCardFlipped.value = !isCardFlipped.value
                }
            }
        ) {
            Box(modifier = Modifier
                .fillMaxSize()
                .graphicsLayer {
                    transformOrigin = TransformOrigin.Center
                    rotationY = rotationFrontCardY.value
                    cameraDistance = 30f
                }
                .alpha(alphaImage.value)
                .clip(RoundedCornerShape(ROUNDED_CORNER_RADIUS))
                .background(Blue)
                .padding(32.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .align(Alignment.TopCenter),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Icon(
                        modifier = Modifier
                            .size(26.dp),
                        painter = painterResource(R.drawable.sound_high),
                        contentDescription = null,
                        tint = LightBlue
                    )
                    Icon(
                        modifier = Modifier
                            .size(26.dp),
                        painter = painterResource(R.drawable.baseline_star_outline_24),
                        contentDescription = null,
                        tint = LightBlue
                    )
                }
                Text(
                    text = frontText,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp)
                        .align(Alignment.Center),
                    fontSize = 22.sp,
                    textAlign = TextAlign.Center,
                    color = Color.White
                )
            }

        Box(modifier = Modifier
            .fillMaxSize()
            .graphicsLayer {
                transformOrigin = TransformOrigin.Center
                rotationY = rotationBackCardY.value
                cameraDistance = 30f
            }
            .alpha(alphaDescription.value)
            .clip(RoundedCornerShape(ROUNDED_CORNER_RADIUS))
            .background(Blue)
            .padding(32.dp),
            contentAlignment = Alignment.Center
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.TopCenter),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Icon(
                    modifier = Modifier
                        .size(26.dp),
                    painter = painterResource(R.drawable.sound_high),
                    contentDescription = null,
                    tint = LightBlue
                )
                Icon(
                    modifier = Modifier
                        .size(26.dp),
                    painter = painterResource(R.drawable.baseline_star_outline_24),
                    contentDescription = null,
                    tint = LightBlue
                )
            }
            Text(
                text = backText,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp)
                    .align(Alignment.Center),
                fontSize = 22.sp,
                textAlign = TextAlign.Center,
                color = Color.White
            )
        }

        }
    }
}

@Composable
fun buildAlphaAnimation(targetValue: Float) =
    animateFloatAsState(
        targetValue = targetValue,
        animationSpec = tween(
            durationMillis = FLIP_CARD_ANIMATION_TIME - 150,
            easing = LinearOutSlowInEasing
        ),
        label = ""
    )

@Composable
fun buildRotationAnimation(targetValue: Float) =
    animateFloatAsState(
        targetValue = targetValue,
        animationSpec = tween(
            durationMillis = FLIP_CARD_ANIMATION_TIME,
            easing = LinearOutSlowInEasing
        ),
        label = ""
    )

@Composable
fun buildOffsetAnimation(targetValue: Offset) =
    animateOffsetAsState(
        targetValue = targetValue,
        label = ""
    )

@Preview
@Composable
private fun FlashCardPreview() {
    FlashCard(
        frontText = "Eat",
        backText = "Cool"
    )
}