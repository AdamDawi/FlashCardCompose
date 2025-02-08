package com.adamdawi.flashcardcompose

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.AnimationVector1D
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.TransformOrigin
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.adamdawi.flashcardcompose.ui.theme.Blue
import com.adamdawi.flashcardcompose.ui.theme.Green
import com.adamdawi.flashcardcompose.ui.theme.Red
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.math.roundToInt

private const val FLIP_ANIMATION_DURATION = 400
private const val ALPHA_ANIMATION_DURATION = FLIP_ANIMATION_DURATION - 150
private const val SWIPE_GESTURE_THRESHOLD = 70f
private val CARD_CORNER_RADIUS = 16.dp
private const val CARD_CAMERA_DISTANCE = 30f
private val CARD_HEIGHT = 480.dp
private val CARD_WIDTH = 320.dp
private const val SWIPE_ANIMATION_TIME = 200
private val BORDER_STROKE_WIDTH = 2.dp
private const val AFTER_SWIPE_DELAY = 70L

/**
 * Flashcard is a customizable component with flip and swipe animations.
 * It allows users to interact with the card by tapping to flip or swiping left/right.
 *
 * @param modifier Modifier to apply to the flashcard container.
 * @param frontText Text displayed on the front side of the card.
 * @param backText Text displayed on the back side of the card.
 * @param height Height of the flashcard.
 * @param width Width of the flashcard.
 * @param backgroundColor Background color of the flashcard.
 * @param textColor Color of the text on the flashcard.
 * @param borderStrokeWidth Width of the border stroke.
 * @param rightSwipeColor Color indicating a right swipe.
 * @param leftSwipeColor Color indicating a left swipe.
 * @param shape Shape of the flashcard.
 * @param topButtonRow Optional composable to display buttons at the top of the card.
 * @param flipDuration Duration of the flip animation in milliseconds.
 * @param alphaDuration Duration of the alpha animation in milliseconds.
 * @param swipeDuration Duration of the swipe animation in milliseconds.
 * @param swipeThreshold Threshold for detecting a swipe gesture.
 * @param onSwipeLeft Callback invoked when the card is swiped to the left.
 * @param onSwipeRight Callback invoked when the card is swiped to the right.
 * @param onRightSwipeApproach Callback invoked when the card is close to being swiped to the right.
 * @param onLeftSwipeApproach Callback invoked when the card is close to being swiped to the left.
 * @param onNeutralPosition Callback invoked when the card is in a neutral position.
 */
@Composable
fun Flashcard(
    modifier: Modifier = Modifier,
    frontText: String,
    backText: String,
    height: Dp = CARD_HEIGHT,
    width: Dp = CARD_WIDTH,
    borderStrokeWidth: Dp = BORDER_STROKE_WIDTH,
    rightSwipeColor: Color = Green,
    leftSwipeColor: Color = Red,
    backgroundColor: Color = Blue,
    textColor: Color = Color.White,
    shape: Shape = RoundedCornerShape(CARD_CORNER_RADIUS),
    topButtonRow: @Composable (() -> Unit)? = null,
    flipDuration: Int = FLIP_ANIMATION_DURATION,
    alphaDuration: Int = ALPHA_ANIMATION_DURATION,
    swipeDuration: Int = SWIPE_ANIMATION_TIME,
    swipeThreshold: Float = SWIPE_GESTURE_THRESHOLD,
    onSwipeLeft: () -> Unit = {},
    onSwipeRight: () -> Unit = {},
    onRightSwipeApproach: () -> Unit = {},
    onLeftSwipeApproach: () -> Unit = {},
    onNeutralPosition: () -> Unit = {}
) {
    val screenWidthDp = LocalConfiguration.current.screenWidthDp
    val screenWidthPx = with(LocalDensity.current) { screenWidthDp.dp.toPx() }
    val scope = rememberCoroutineScope()
    val isCardFlipped = remember { mutableStateOf(false) }
    val cardBorderColor = remember { mutableStateOf(Color.Transparent) }
    val cardSidesAlpha =
        animateAlphaAsState(targetValue = if (!isCardFlipped.value) 1f else 0f, alphaDuration)
    val cardRotationY = remember {
        Animatable(0f)
    }
    val cardOffsetX = remember {
        Animatable(0f)
    }
    val cardOffsetY = remember {
        Animatable(0f)
    }
    Box(
        modifier = Modifier
            .width(width)
            .height(height)
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .clip(shape)
                .background(backgroundColor)
        )
        Box(modifier = modifier
            .offset {
                IntOffset(
                    cardOffsetX.value.roundToInt(),
                    cardOffsetY.value.roundToInt()
                )
            }
            .graphicsLayer {
                rotationZ = cardOffsetX.value / 100
            }
            .fillMaxSize()
            .border(
                BorderStroke(borderStrokeWidth, cardBorderColor.value),
                shape = shape
            )
            .pointerInput(Unit) {
                detectDragGestures(
                    onDragEnd = {
                        handleSwipeGestureCompletion(
                            scope = scope,
                            animatedCardOffsetX = cardOffsetX,
                            animatedCardOffsetY = cardOffsetY,
                            cardRotationY = cardRotationY,
                            updateIsCardFlipped = {
                                isCardFlipped.value = it
                            },
                            onSwipeLeft = onSwipeLeft,
                            onSwipeRight = onSwipeRight,
                            updateCardBorderColor = {
                                cardBorderColor.value = it
                            },
                            swipeThreshold = swipeThreshold,
                            screenWidth = screenWidthPx,
                            swipeDuration = swipeDuration
                        )
                    },
                    onDragCancel = {
                        handleSwipeGestureCompletion(
                            scope = scope,
                            animatedCardOffsetX = cardOffsetX,
                            animatedCardOffsetY = cardOffsetY,
                            cardRotationY = cardRotationY,
                            updateIsCardFlipped = {
                                isCardFlipped.value = it
                            },
                            onSwipeLeft = onSwipeLeft,
                            onSwipeRight = onSwipeRight,
                            updateCardBorderColor = {
                                cardBorderColor.value = it
                            },
                            swipeThreshold = swipeThreshold,
                            screenWidth = screenWidthPx,
                            swipeDuration = swipeDuration
                        )
                    },
                    onDrag = { change, dragAmount ->
                        change.consume()
                        scope.launch {
                            cardOffsetX.snapTo(cardOffsetX.value + dragAmount.x)
                            cardOffsetY.snapTo(cardOffsetY.value + dragAmount.y)
                        }
                        //approaching to right swipe
                        if (cardOffsetX.value > SWIPE_GESTURE_THRESHOLD) {
                            cardBorderColor.value = rightSwipeColor
                            onRightSwipeApproach()
                        }
                        //approaching to left swipe
                        else if (cardOffsetX.value < -SWIPE_GESTURE_THRESHOLD) {
                            cardBorderColor.value = leftSwipeColor
                            onLeftSwipeApproach()
                        }
                        //neutral
                        else {
                            cardBorderColor.value = Color.Transparent
                            onNeutralPosition()
                        }
                    }
                )
            }
            //clicking without ripple
            .pointerInput(Unit) {
                detectTapGestures {
                    isCardFlipped.value = !isCardFlipped.value
                    scope.launch {
                        cardRotationY.animateTo(
                            targetValue = if (!isCardFlipped.value) 0f else 180f,
                            animationSpec = tween(
                                durationMillis = flipDuration,
                                easing = LinearOutSlowInEasing
                            )
                        )
                    }
                }
            }
        ) {
            //front side of card
            FlashcardSide(
                text = frontText,
                rotationY = cardRotationY.value,
                alpha = cardSidesAlpha.value,
                shape = shape,
                topButtonRow = topButtonRow,
                backgroundColor = backgroundColor,
                textColor = textColor
            )
            //back side of card
            FlashcardSide(
                text = backText,
                rotationY = -180f + cardRotationY.value,
                alpha = 1f - cardSidesAlpha.value,
                shape = shape,
                topButtonRow = topButtonRow,
                backgroundColor = backgroundColor,
                textColor = textColor
            )
        }
    }
}

@Composable
fun FlashcardSide(
    text: String,
    rotationY: Float,
    alpha: Float,
    shape: Shape,
    backgroundColor: Color,
    textColor: Color,
    topButtonRow: @Composable (() -> Unit)? = null
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .graphicsLayer {
                transformOrigin = TransformOrigin.Center
                this.rotationY = rotationY
                cameraDistance = CARD_CAMERA_DISTANCE
            }
            .alpha(alpha)
            .clip(shape)
            .background(backgroundColor)
            .padding(32.dp),
        contentAlignment = Alignment.TopCenter
    ) {
        topButtonRow?.invoke()
        Text(
            modifier = Modifier.align(Alignment.Center),
            text = text,
            fontSize = 22.sp,
            textAlign = TextAlign.Center,
            color = textColor
        )
    }
}

private fun handleSwipeGestureCompletion(
    scope: CoroutineScope,
    animatedCardOffsetX: Animatable<Float, AnimationVector1D>,
    animatedCardOffsetY: Animatable<Float, AnimationVector1D>,
    cardRotationY: Animatable<Float, AnimationVector1D>,
    updateIsCardFlipped: (Boolean) -> Unit,
    updateCardBorderColor: (Color) -> Unit,
    onSwipeLeft: () -> Unit,
    onSwipeRight: () -> Unit,
    swipeThreshold: Float,
    screenWidth: Float,
    swipeDuration: Int
) {
    scope.launch {
        when {
            animatedCardOffsetX.value > swipeThreshold -> {
                updateIsCardFlipped(false)
                launch {
                    cardRotationY.snapTo(0f)
                }
                animatedCardOffsetX.animateTo(
                    targetValue = screenWidth * 1.5f,
                    animationSpec = tween(
                        durationMillis = swipeDuration,
                        easing = LinearEasing
                    )
                )
                onSwipeRight()
                delay(AFTER_SWIPE_DELAY)
                resetCardPositionAndColor(
                    animatedCardOffsetX,
                    animatedCardOffsetY,
                    updateCardBorderColor
                )
            }

            animatedCardOffsetX.value < -swipeThreshold -> {
                updateIsCardFlipped(false)
                launch {
                    cardRotationY.snapTo(0f)
                }
                animatedCardOffsetX.animateTo(
                    targetValue = -screenWidth * 1.5f,
                    animationSpec = tween(
                        durationMillis = swipeDuration,
                        easing = LinearEasing
                    )
                )
                onSwipeLeft()
                delay(AFTER_SWIPE_DELAY)
                resetCardPositionAndColor(
                    animatedCardOffsetX,
                    animatedCardOffsetY,
                    updateCardBorderColor
                )
            }

            else -> {
                scope.launch {
                    animatedCardOffsetX.animateTo(0f)
                }
                scope.launch {
                    animatedCardOffsetY.animateTo(0f)
                }
            }
        }
    }
}

private suspend fun resetCardPositionAndColor(
    animatedCardOffsetX: Animatable<Float, AnimationVector1D>,
    animatedCardOffsetY: Animatable<Float, AnimationVector1D>,
    changeCardBorderColor: (Color) -> Unit
) {
    animatedCardOffsetX.snapTo(0f)
    animatedCardOffsetY.snapTo(0f)
    changeCardBorderColor(Color.Transparent)
}

@Composable
private fun animateAlphaAsState(targetValue: Float, durationMillis: Int) =
    animateFloatAsState(
        targetValue = targetValue,
        animationSpec = tween(
            durationMillis = durationMillis,
            easing = LinearOutSlowInEasing
        ),
        label = ""
    )

@Preview
@Composable
private fun FlashcardPreview() {
    Flashcard(
        frontText = "Eat",
        backText = "Cool"
    )
}