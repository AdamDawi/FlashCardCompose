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
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlin.math.roundToInt

private const val FLIP_CARD_ANIMATION_TIME = 400
private const val OFFSET_LIMIT = 120

@Composable
fun MealCard(
    modifier: Modifier = Modifier
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
    val alphaImage = buildAlphaAnimation(targetValue = if(!isCardFlipped.value) 1f else 0f)
    val alphaDescription = buildAlphaAnimation(targetValue = if(isCardFlipped.value) 1f else 0f)
    val rotateImageY = buildRotationAnimation(targetValue = if(!isCardFlipped.value) 0f else 180f)
    val rotateDescriptionY = buildRotationAnimation(targetValue = if(!isCardFlipped.value) -180f else 0f)
    val animatedCardOffset = buildOffsetAnimation(targetValue = cardOffset.value)

    Box(modifier = modifier
        .offset {
            IntOffset(
                animatedCardOffset.x.roundToInt(),
                animatedCardOffset.y.roundToInt()
            )
        }
        .fillMaxSize()
        .padding(top = 150.dp, bottom = 150.dp, start = 50.dp, end = 50.dp)
        .border(
            BorderStroke(4.dp, cardBorderColor.value),
            shape = RoundedCornerShape(12.dp)
        )
        .pointerInput(Unit) {
            detectDragGestures(
                onDragEnd = {
                    //Swipe right
                    if(cardOffset.value.x> OFFSET_LIMIT){
//                        getRandomFood()
                        cardOffset.value = Offset.Zero
                        cardBorderColor.value = Color.Transparent
//                        _rightSwipeCounter.intValue++
                    }
                    //Swipe left
                    else if(cardOffset.value.x<-OFFSET_LIMIT){
//                        getRandomFood()
                        cardOffset.value = Offset.Zero
                        cardBorderColor.value = Color.Transparent
//                        _leftSwipeCounter.intValue++
                    }
                    else cardOffset.value = Offset.Zero
                },
                onDragCancel = {
                    //Swipe right
                    if(cardOffset.value.x> OFFSET_LIMIT){
//                        getRandomFood()
                        cardOffset.value = Offset.Zero
                        cardBorderColor.value = Color.Transparent
//                        _rightSwipeCounter.intValue++
                    }
                    //Swipe left
                    else if(cardOffset.value.x<-OFFSET_LIMIT){
//                        getRandomFood()
                        cardOffset.value = Offset.Zero
                        cardBorderColor.value = Color.Transparent
//                        _leftSwipeCounter.intValue++
                    }
                    else cardOffset.value = Offset.Zero
                },
                onDrag = { change, dragAmount ->
                    change.consume()
                    cardOffset.value = Offset(
                        cardOffset.value.x+dragAmount.x,
                        cardOffset.value.y+dragAmount.y
                    )
                    //close to right
                    if(cardOffset.value.x>OFFSET_LIMIT){
                        cardBorderColor.value = Color.Green
//                        _state.value = _state.value.copy(isSwipeToRightShaking = true)
                    }
                    //close to left
                    else if(cardOffset.value.x<-OFFSET_LIMIT){
                        cardBorderColor.value = Color.Red
//                        _state.value = _state.value.copy(isSwipeToLeftShaking = true)
                    }
                    //neutral
                    else {
                        cardBorderColor.value = Color.Transparent
//                        _state.value = _state.value.copy(isSwipeToRightShaking = false, isSwipeToLeftShaking = false)
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
    ){
            Box(modifier = Modifier
                .fillMaxSize()
                .graphicsLayer {
                    transformOrigin = TransformOrigin.Center
                    rotationY = rotateImageY
                    cameraDistance = 30f
                }
                .alpha(alphaImage)
                .clip(RoundedCornerShape(12.dp))
                .background(Color.LightGray)
            ){
                LazyColumn(modifier = Modifier
                    .fillMaxSize()
                    .align(Alignment.Center)
                    .padding(12.dp),
                    userScrollEnabled = isCardFlipped.value
                ){
                    item{
                        Text(
                            text = "Eat",
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(8.dp),
                            fontSize = 22.sp,
                            fontWeight = FontWeight.ExtraBold,
                            textAlign = TextAlign.Center
                        )
                    }
                }
            }
            Box(modifier = Modifier
                .fillMaxSize()
                .graphicsLayer {
                    transformOrigin = TransformOrigin.Center
                    rotationY = rotateDescriptionY
                    cameraDistance = 30f
                }
                .alpha(alphaDescription)
                .clip(RoundedCornerShape(12.dp))
                .background(Color.LightGray)
            ) {
                LazyColumn(modifier = Modifier
                    .fillMaxSize()
                    .align(Alignment.Center)
                    .padding(12.dp),
                    userScrollEnabled = isCardFlipped.value
                ){
                    item{
                        Text(
                            text = "Cool",
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(8.dp),
                            fontSize = 22.sp,
                            fontWeight = FontWeight.ExtraBold,
                            textAlign = TextAlign.Center
                        )
                    }
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
    ).value

@Composable
fun buildRotationAnimation(targetValue: Float) =
    animateFloatAsState(
        targetValue = targetValue,
        animationSpec = tween(
            durationMillis = FLIP_CARD_ANIMATION_TIME,
            easing = LinearOutSlowInEasing
        ),
        label = ""
    ).value

@Composable
fun buildOffsetAnimation(targetValue: Offset) =
    animateOffsetAsState(
        targetValue = targetValue,
        label = ""
    ).value