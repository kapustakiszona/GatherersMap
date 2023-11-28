package com.example.gatherersmap.presentation.components.reusables

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.Measurable
import androidx.compose.ui.layout.Placeable
import androidx.compose.ui.layout.SubcomposeLayout
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

/**
Makes it possible to define the same size for elements in one line
 **/
@Composable
fun SubcomposeRow(
    modifier: Modifier = Modifier,
    paddingBetween: Dp = 0.dp,
    content: @Composable () -> Unit = {},
) {
    val density = LocalDensity.current

    SubcomposeLayout(modifier = modifier) { constraints ->

        var subcomposeIndex = 0

        val spaceBetweenButtons = with(density) {
            paddingBetween.roundToPx()
        }

        var placeables: List<Placeable> = subcompose(subcomposeIndex++, content)
            .map {
                it.measure(constraints)
            }

        var maxWidth = 0
        var maxHeight = 0
        var layoutWidth = 0

        placeables.forEach { placeable: Placeable ->
            maxWidth = placeable.width.coerceAtLeast(maxWidth)
                .coerceAtMost(((constraints.maxWidth - spaceBetweenButtons) / 2))
            maxHeight = placeable.height.coerceAtLeast(maxHeight)
        }


        layoutWidth = maxWidth

        // Remeasure every element using width of longest item using it as min width
        // Our max width is half of the remaining area after we subtract space between buttons
        // and we constraint its maximum width to half width minus space between
        if (placeables.isNotEmpty() && placeables.size > 1) {
            placeables = subcompose(subcomposeIndex, content).map { measurable: Measurable ->
                measurable.measure(
                    constraints.copy(
                        minWidth = maxWidth,
                        maxWidth = ((constraints.maxWidth - spaceBetweenButtons) / 2)
                            .coerceAtLeast(maxWidth)
                    )
                )
            }

            layoutWidth = (placeables.sumOf { it.width } + spaceBetweenButtons)
                .coerceAtMost(constraints.maxWidth)

            maxHeight = placeables.maxOf { it.height }
        }

        layout(layoutWidth, maxHeight) {
            var xPos = 0
            placeables.forEach { placeable: Placeable ->
                placeable.placeRelative(xPos, 0)
                xPos += placeable.width + spaceBetweenButtons
            }
        }
    }
}