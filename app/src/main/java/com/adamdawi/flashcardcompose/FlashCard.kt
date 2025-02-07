package com.adamdawi.flashcardcompose

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
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
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
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
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
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
    val scope = rememberCoroutineScope()
    val isCardFlipped = remember {
        mutableStateOf(false)
    }
    val cardBorderColor = remember {
        mutableStateOf(Color.Transparent)
    }
    val alphaImage = buildAlphaAnimation(targetValue = if (!isCardFlipped.value) 1f else 0f)
    val alphaDescription = buildAlphaAnimation(targetValue = if (isCardFlipped.value) 1f else 0f)
    val rotationFrontCardY = buildRotationAnimation(targetValue = if (!isCardFlipped.value) 0f else 180f)
    val rotationBackCardY =
        buildRotationAnimation(targetValue = if (!isCardFlipped.value) -180f else 0f)
    var animatedCardOffsetX = remember {
        Animatable(0f)
    }
    var animatedCardOffsetY = remember {
        Animatable(0f)
    }
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
                    animatedCardOffsetX.value.roundToInt(),
                    animatedCardOffsetY.value.roundToInt()
                )
            }
            .graphicsLayer {
                rotationZ = animatedCardOffsetX.value / 100 //TODO a lot of recompositions
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
                        if (animatedCardOffsetX.value > OFFSET_LIMIT) {
                            scope.launch {
                                animatedCardOffsetX.animateTo(1200f)
                                delay(70)
                                animatedCardOffsetX.snapTo(0f)
                                animatedCardOffsetY.snapTo(0f)
                            }
                            cardBorderColor.value = Color.Transparent
                            onRightSwipe()
                        }
                        //Swipe left
                        else if (animatedCardOffsetX.value < -OFFSET_LIMIT) {
                            scope.launch {
                                animatedCardOffsetX.animateTo(-1200f)
                                delay(70)
                                animatedCardOffsetX.snapTo(0f)
                                animatedCardOffsetY.snapTo(0f)
                            }
                            cardBorderColor.value = Color.Transparent
                            onLeftSwipe()
                        } else {
                            scope.launch {
                                animatedCardOffsetX.snapTo(0f)
                                animatedCardOffsetY.animateTo(0f)
                            }
                        }
                    },
                    onDragCancel = {
                        //Swipe right
                        if (animatedCardOffsetX.value > OFFSET_LIMIT) {
                            scope.launch {
                                animatedCardOffsetX.animateTo(1200f)
                                delay(70)
                                animatedCardOffsetY.animateTo(0f)
                                animatedCardOffsetY.snapTo(0f)
                            }
                            cardBorderColor.value = Color.Transparent
                            onRightSwipe()
                        }
                        //Swipe left
                        else if (animatedCardOffsetX.value < -OFFSET_LIMIT) {
                            scope.launch {
                                animatedCardOffsetX.animateTo(-1200f)
                                delay(70)
                                animatedCardOffsetY.animateTo(0f)
                                animatedCardOffsetY.snapTo(0f)
                            }
                            cardBorderColor.value = Color.Transparent
                            onLeftSwipe()
                        } else {
                            scope.launch {
                                animatedCardOffsetX.animateTo(0f)
                                animatedCardOffsetY.animateTo(0f)
                            }
                        }
                    },
                    onDrag = { change, dragAmount ->
                        change.consume()
                        scope.launch {
                            animatedCardOffsetX.snapTo(animatedCardOffsetX.value + dragAmount.x)
                            animatedCardOffsetY.snapTo(animatedCardOffsetY.value + dragAmount.y)
                        }
                        //close to right
                        if (animatedCardOffsetX.value > OFFSET_LIMIT) {
                            cardBorderColor.value = Green
                            onCloseToRight()
                        }
                        //close to left
                        else if (animatedCardOffsetX.value < -OFFSET_LIMIT) {
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
                FunctionButtonRow(
                    modifier = Modifier
                        .align(Alignment.TopCenter)
                )
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
            FunctionButtonRow(
                modifier = Modifier
                    .align(Alignment.TopCenter)
            )
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
fun FunctionButtonRow(
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth(),
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

@Preview
@Composable
private fun FlashCardPreview() {
    FlashCard(
        frontText = "Eat",
        backText = "Cool"
    )
}