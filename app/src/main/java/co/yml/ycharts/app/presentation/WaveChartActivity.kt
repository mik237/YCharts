package co.yml.ycharts.app.presentation

import android.graphics.Typeface
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import co.yml.charts.axis.AxisData
import co.yml.charts.common.extensions.formatToSinglePrecision
import co.yml.charts.common.model.Point
import co.yml.charts.common.utils.DataUtils
import co.yml.charts.ui.linechart.model.*
import co.yml.charts.ui.wavechart.WaveChart
import co.yml.charts.ui.wavechart.model.Wave
import co.yml.charts.ui.wavechart.model.WaveChartData
import co.yml.charts.ui.wavechart.model.WaveFillColor
import co.yml.charts.ui.wavechart.model.WavePlotData
import co.yml.ycharts.app.R
import co.yml.ycharts.app.ui.compositions.AppBarWithBackButton
import co.yml.ycharts.app.ui.theme.YChartsTheme

class WaveChartActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            YChartsTheme {
                Scaffold(modifier = Modifier.fillMaxSize(),
                    backgroundColor = YChartsTheme.colors.background,
                    topBar = {
                        AppBarWithBackButton(
                            stringResource(id = R.string.title_wave_chart),
                            onBackPressed = {
                                onBackPressed()
                            })
                    })
                {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(it),
                        contentAlignment = Alignment.TopCenter
                    ) {
                        LazyColumn(content = {
                            items(3) { item ->
                                when (item) {
                                    0 -> WaveGraph1(
                                        DataUtils.getWaveChartData(
                                            100,
                                            start = -50,
                                            maxRange = 10
                                        )
                                    )
                                    1 -> WaveGraph2(
                                        DataUtils.getLineChartData(
                                            50,
                                            start = -10,
                                            maxRange = 50
                                        )
                                    )
                                    2 -> WaveGraph3(
                                        DataUtils.getLineChartData(
                                            100,
                                            start = -10,
                                            maxRange = 10
                                        )
                                    )
                                }
                            }
                        })
                    }
                }
            }
        }
    }
}

@Composable
private fun WaveGraph1(pointsData: List<Point>) {
    val steps = 5
    val xAxisData = AxisData.Builder()
        .axisStepSize(30.dp)
        .steps(pointsData.size - 1)
        .labelData { i -> i.toString() }
        .labelAndAxisLinePadding(15.dp)
        .build()
    val yAxisData = AxisData.Builder()
        .steps(steps)
        .labelAndAxisLinePadding(20.dp)
        .labelData { i ->
            // Add yMin to get the negative axis values to the scale
            val yMin = pointsData.minOf { it.y }
            val yMax = pointsData.maxOf { it.y }
            val yScale = (yMax - yMin) / steps
            ((i * yScale) + yMin).formatToSinglePrecision()
        }.build()
    val data = WaveChartData(
        wavePlotData = WavePlotData(
            lines = listOf(
                Wave(
                    dataPoints = pointsData,
                    LineStyle(),
                    IntersectionPoint(),
                    SelectionHighlightPoint(),
                    ShadowUnderLine(),
                    SelectionHighlightPopUp(),
                    WaveFillColor(topColor = Color.Yellow, bottomColor = Color.Blue)
                )
            )
        ),
        xAxisData = xAxisData,
        yAxisData = yAxisData,
        gridLines = GridLines()
    )
    WaveChart(
        modifier = Modifier
            .fillMaxWidth()
            .height(300.dp),
        waveChartData = data
    )
}

@Composable
private fun WaveGraph2(pointsData: List<Point>) {
    val xAxisData = AxisData.Builder()
        .axisStepSize(40.dp)
        .steps(pointsData.size - 1)
        .labelData { i -> (1900 + i).toString() }
        .axisLabelAngle(20f)
        .labelAndAxisLinePadding(15.dp)
        .axisLabelColor(Color.Blue)
        .axisLineColor(Color.DarkGray)
        .typeFace(Typeface.DEFAULT_BOLD)
        .build()
    val yAxisData = AxisData.Builder()
        .steps(10)
        .labelData { i ->
            // Add yMin to get the negative axis values to the scale
            val yMin = pointsData.minOf { it.y }
            val yMax = pointsData.maxOf { it.y }
            val yScale = (yMax - yMin) / 10
            ((i * yScale) + yMin).formatToSinglePrecision()
        }
        .labelAndAxisLinePadding(30.dp)
        .axisLabelColor(Color.Blue)
        .axisLineColor(Color.DarkGray)
        .typeFace(Typeface.DEFAULT_BOLD)
        .build()
    val data = WaveChartData(
        wavePlotData = WavePlotData(
            lines = listOf(
                Wave(
                    dataPoints = pointsData,
                    waveStyle = LineStyle(lineType = LineType.SmoothCurve(), color = Color.Blue),
                    intersectionPoint = IntersectionPoint(color = Color.Red),
                    selectionHighlightPopUp = SelectionHighlightPopUp(popUpLabel = { x, y ->
                        val xLabel = "x : ${(1900 + x).toInt()} "
                        val yLabel = "y : ${String.format("%.2f", y)}"
                        "$xLabel $yLabel"
                    })
                )
            )
        ),
        xAxisData = xAxisData,
        yAxisData = yAxisData
    )
    WaveChart(
        modifier = Modifier
            .fillMaxWidth()
            .height(300.dp),
        waveChartData = data
    )
}

@Composable
private fun WaveGraph3(pointsData: List<Point>) {
    val steps = 10
    val xAxisData = AxisData.Builder()
        .axisStepSize(40.dp)
        .steps(pointsData.size - 1)
        .labelData { i -> i.toString() }
        .labelAndAxisLinePadding(15.dp)
        .axisLineColor(Color.Red)
        .build()
    val yAxisData = AxisData.Builder()
        .steps(steps)
        .labelData { i ->
            val yMin = pointsData.minOf { it.y }
            val yMax = pointsData.maxOf { it.y }
            val yScale = (yMax - yMin) / steps
            ((i * yScale) + yMin).formatToSinglePrecision()
        }
        .axisLineColor(Color.Red)
        .labelAndAxisLinePadding(20.dp)
        .bottomPadding(15.dp)
        .build()
    val data = WaveChartData(
        wavePlotData = WavePlotData(
            lines = listOf(
                Wave(
                    dataPoints = pointsData,
                    waveStyle = LineStyle(
                        lineType = LineType.SmoothCurve(isDotted = true),
                        color = Color.Green
                    ),
                    shadowUnderLine = ShadowUnderLine(
                        brush = Brush.verticalGradient(
                            listOf(
                                Color.Green,
                                Color.Transparent
                            )
                        ), alpha = 0.3f
                    ),
                    selectionHighlightPoint = SelectionHighlightPoint(
                        color = Color.Green
                    ),
                    selectionHighlightPopUp = SelectionHighlightPopUp(
                        backgroundColor = Color.Black,
                        backgroundStyle = Stroke(2f),
                        labelColor = Color.Red,
                        labelTypeface = Typeface.DEFAULT_BOLD
                    )
                )
            )
        ),
        xAxisData = xAxisData,
        yAxisData = yAxisData
    )
    WaveChart(
        modifier = Modifier
            .fillMaxWidth()
            .height(300.dp),
        waveChartData = data
    )
}
