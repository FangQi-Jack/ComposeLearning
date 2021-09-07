package me.jackfangqi.compose.demo

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import me.jackfangqi.compose.demo.ui.layout.BarChartMinMax

/**
 * create by FangQi on 20:49, 周二, 2021/9/7
 * fangqi.jack@bytedance.com
 *
 * 描述：--
 **/
class BarChartActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            BarChartContent()
        }
    }
}

@Composable
fun BarChartContent() {
    MaterialTheme {
        BarChartMinMax(
            dataPoints = listOf(20, 15, 18, 25, 30),
            maxText = { Text(text = "Max") },
            minText = { Text(text = "Min") },
            modifier = Modifier.padding(24.dp)
        )
    }
}

@Preview(showBackground = true)
@Composable
fun BarCharContentPreview() {
    BarChartContent()
}