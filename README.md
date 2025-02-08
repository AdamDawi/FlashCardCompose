# ScratchCardCompose
This project provides a customizable flashcard component in Jetpack Compose. The flashcard supports interactive features such as flipping on tap and swiping left or right.

## ⭐️Features
### Flip Animation:
Uses rotation along the Y-axis to create a flipping effect with ``cameraDistance``.
- Rotation values range from 0f to 180f to simulate a full flip.
- Alpha blending ensures only one side is visible at a time when the animation is complete.

### Swipe Animation: 
Smooth swipe animations with rotation on the Z-axis.
- **Card Slide Out of View:** Cards smoothly slide out of the screen when swiped.
- **Border Color Indications:** Highlights the card border when approaching a swipe threshold.

### Customizable Design:
Modify text, colors, shapes, and more to match your app's theme.

## 🎥Here are some overview videos:

## ⚙️Installation
To use this component in your Jetpack Compose project, simply copy the ``Flashcard`` composable into your project and customize it with your own text and styling.

## 📝Usage Example
Here’s an example of how to use the ScratchCard component:
```kotlin
  Flashcard(
    frontText = "Front of the card",
    backText = "Back of the card",
    height = 480.dp,
    width = 320.dp,
    onSwipeLeft = { /* Handle swipe left */ },
    onSwipeRight = { /* Handle swipe right */ }
)
```

## 🔍How it works
The flashcard component is built using a layered rendering approach:
1. **Flip Animation (Front and Back Sides):**
- **Rotation (Y-Axis):**
  -  The card rotates around the Y-axis when flipped. Initially, the card shows the front side, and upon flip, it rotates 180°, revealing the **back side**.
- **Alpha Animation (Opacity Transition):**
  - The **front side** fades out (opacity 0) as the card rotates.
  - Simultaneously, the back side fades in (opacity 1), creating a seamless transition between the two sides.
2. **Swipe Animation::**
- **Drag Detection:**
  - The card’s position (``cardOffsetX``, ``cardOffsetY``) is updated using ``detectDragGestures``.
  - The border color changes based on drag direction, and the swipe direction (left or right) is determined by the drag threshold (``SWIPE_GESTURE_THRESHOLD``).
- **Swipe Completion:**
  - If the drag exceeds the threshold, the card moves off-screen horizontally by 1.5 times the ``screenWidth`` and rotates back to 0°. The appropriate callback (``onSwipeLeft`` or ``onSwipeRight``) is triggered, and the card resets.
- **Reset Position:**
  - After the swipe, the card’s position and border color reset to their initial state.

## 🎨Customize Options

## 🎨 Customize Options

| Parameter                      | Description                                                                                                   | Default                      |
|---------------------------------|---------------------------------------------------------------------------------------------------------------|------------------------------|
| `modifier`                     | Modifier applied to the flashcard container.                                                                  | `Modifier`                   |
| `frontText`                    | Text displayed on the front side of the card.                                                                  | _Required_                   |
| `backText`                     | Text displayed on the back side of the card.                                                                   | _Required_                   |
| `height`                       | Height of the flashcard.                                                                                      | `480.dp`                |
| `width`                        | Width of the flashcard.                                                                                       | `320.dp`                 |
| `borderStrokeWidth`            | Width of the border stroke.                                                                                   | `2.dp`        |
| `rightSwipeColor`              | Color indicating a right swipe.                                                                               | `Green`                      |
| `leftSwipeColor`               | Color indicating a left swipe.                                                                                | `Red`                        |
| `backgroundColor`              | Background color of the flashcard.                                                                            | `Blue`                       |
| `textColor`                    | Text color on the flashcard.                                                                                  | `Color.White`                |
| `shape`                        | Shape of the flashcard (e.g., rounded corners).                                                               | `RoundedCornerShape(16.dp)` |
| `topButtonRow`                 | Optional composable to display buttons at the top of the card.                                                 | _None_                       |
| `flipDuration`                 | Duration of the flip animation in milliseconds.                                                               | `400`    |
| `alphaDuration`                | Duration of the alpha animation in milliseconds.                                                              | `250`   |
| `swipeDuration`                | Duration of the swipe animation in milliseconds.                                                              | `200`       |
| `swipeThreshold`               | Threshold for detecting a swipe gesture.                                                                      | `70f`    |
| `onSwipeLeft`                  | Callback triggered when the card is swiped to the left.                                                       | `{}` (empty lambda)          |
| `onSwipeRight`                 | Callback triggered when the card is swiped to the right.                                                      | `{}` (empty lambda)          |
| `onRightSwipeApproach`         | Callback triggered when the card is near a right swipe threshold.                                             | `{}` (empty lambda)          |
| `onLeftSwipeApproach`          | Callback triggered when the card is near a left swipe threshold.                                              | `{}` (empty lambda)          |
| `onNeutralPosition`            | Callback triggered when the card is in a neutral (centered) position.                                          | `{}` (empty lambda)          |


## 📋Requirements
Minimum version: Android 7.0 (API level 24) or later📱

Target version: Android 14 (API level 34) or later📱

## ✍️Author
Adam Dawidziuk🧑‍💻
