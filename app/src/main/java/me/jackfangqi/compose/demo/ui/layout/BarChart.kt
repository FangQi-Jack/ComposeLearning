package me.jackfangqi.compose.demo.ui.layout

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.layout.HorizontalAlignmentLine
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import kotlin.math.max
import kotlin.math.min
import kotlin.math.roundToInt

/**
 * create by FangQi on 20:11, 周二, 2021/9/7
 * fangqi.jack@bytedance.com
 *
 * 描述：--
 **/

val MaxChartValue = HorizontalAlignmentLine(merger = { old, new -> min(old, new) })

val MinChartValue = HorizontalAlignmentLine(merger = { old, new -> max(old, new) })

@Composable
fun BarChart(
    dataPoints: List<Int>,
    modifier: Modifier = Modifier
) {
    val maxValue: Float = remember(dataPoints) { (dataPoints.maxOrNull() ?: 0) * 1.2f }
    val maxDataPoint: Int = remember(dataPoints) { dataPoints.maxOrNull() ?: 0 }
    val minDataPoint: Int = remember(dataPoints) { dataPoints.minOrNull() ?: 0 }

    var maxValueBaseline by remember { mutableStateOf(Float.MAX_VALUE) }
    var minValueBaseline by remember { mutableStateOf(Float.MIN_VALUE) }

    var maxTopOffset by remember { mutableStateOf(Offset(0f, 0f)) }
    var minTopOffset by remember { mutableStateOf(Offset(0f, 0f)) }

    Layout(
        content = {
            BoxWithConstraints(propagateMinConstraints = true) {
                val density = LocalDensity.current
                with(density) {
                    val yPositionRatio = remember(density, maxHeight, maxValue) {
                        maxHeight.toPx() / maxValue
                    }
                    val xPositionRatio = remember(density, maxWidth, dataPoints) {
                        maxWidth.toPx() / (dataPoints.size + 1)
                    }
                    val xOffset = remember(density) { xPositionRatio / dataPoints.size }

                    Canvas(modifier = Modifier) {
                        dataPoints.forEachIndexed { index, dataPoint ->
                            val rectSize = Size(width = 60f, height = dataPoint * yPositionRatio)
                            val topLeftOffset = Offset(
                                x = xPositionRatio * (index + 1) - xOffset,
                                y = (maxValue - dataPoint) * yPositionRatio
                            )
                            drawRect(Color(0xFF3DDC84), topLeftOffset, rectSize)

                            if (maxValueBaseline == Float.MAX_VALUE && dataPoint == maxDataPoint) {
                                maxValueBaseline = topLeftOffset.y
                                maxTopOffset = topLeftOffset
                            }
                            if (minValueBaseline == Float.MIN_VALUE && dataPoint == minDataPoint) {
                                minValueBaseline = topLeftOffset.y
                                minTopOffset = topLeftOffset
                            }
                        }
                        drawLine(
                            color = Color(0xFF073042),
                            start = Offset(0f, 0f),
                            end = Offset(0f, maxHeight.toPx()),
                            strokeWidth = 6f
                        )
                        drawLine(
                            color = Color(0xFF073042),
                            start = Offset(0f, maxHeight.toPx()),
                            end = Offset(maxWidth.toPx(), maxHeight.toPx()),
                            strokeWidth = 6f
                        )
                        drawLine(
                            color = Color.Red,
                            start = Offset(0f, maxTopOffset.y),
                            end = Offset(maxTopOffset.x, maxTopOffset.y),
                            strokeWidth = 6f,
                            pathEffect = PathEffect.dashPathEffect(
                                intervals = floatArrayOf(10f, 10f)
                            )
                        )
                        drawLine(
                            color = Color.Red,
                            start = Offset(0f, minTopOffset.y),
                            end = Offset(minTopOffset.x, minTopOffset.y),
                            strokeWidth = 6f,
                            pathEffect = PathEffect.dashPathEffect(
                                intervals = floatArrayOf(10f, 10f)
                            )
                        )
                    }
                }
            }
        },
        modifier = modifier
    ) { measureables, constraints ->
        check(measureables.size == 1)
        val placeable = measureables[0].measure(constraints)

        layout(
            width = constraints.maxWidth,
            height = constraints.maxHeight,
            alignmentLines = mapOf(
                MinChartValue to minValueBaseline.roundToInt(),
                MaxChartValue to maxValueBaseline.roundToInt()
            )
        ) {
            placeable.placeRelative(0, 0)
        }
    }
}

@Composable
fun BarChartMinMax(
    dataPoints: List<Int>,
    maxText: @Composable () -> Unit,
    minText: @Composable () -> Unit,
    modifier: Modifier = Modifier
) {
    Layout(
        content = {
            maxText()
            minText()
            BarChart(dataPoints = dataPoints, modifier = Modifier.size(300.dp))
        },
        modifier = modifier
    ) { measureables, constraints ->
        check(measureables.size == 3)
        val placeables = measureables.map { measurable ->
            measurable.measure(constraints = constraints.copy(minWidth = 0, minHeight = 0))
        }

        val maxTextPlaceable = placeables[0]
        val minTextPlaceable = placeables[1]
        val barChartPlaceable = placeables[2]

        val minValueBaseline = barChartPlaceable[MinChartValue]
        val maxValueBaseline = barChartPlaceable[MaxChartValue]

        layout(constraints.maxWidth, constraints.maxHeight) {
            maxTextPlaceable.placeRelative(
                x = 0,
                y = maxValueBaseline - (maxTextPlaceable.height / 2)
            )
            minTextPlaceable.placeRelative(
                x = 0,
                y = minValueBaseline - (minTextPlaceable.height / 2)
            )
            barChartPlaceable.placeRelative(
                x = max(maxTextPlaceable.width, minTextPlaceable.width) + 20,
                y = 0
            )
        }
    }
}