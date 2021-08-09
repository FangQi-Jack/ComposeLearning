package me.jackfangqi.compose.demo

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Divider
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.AlignmentLine
import androidx.compose.ui.layout.FirstBaseline
import androidx.compose.ui.layout.layout
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import me.jackfangqi.compose.demo.ui.theme.ComposeDemoTheme

/**
 * create by FangQi on 17:53, 周五, 2021/8/6
 * fangqi.jack@bytedance.com
 *
 * 描述：--
 **/
class CustomLayoutActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            CustomLayoutScreen()
        }
    }
}

@Composable
fun CustomLayoutScreen() {
    ComposeDemoTheme {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = {
                        Text(text = "CustomLayoutActivity")
                    }
                )
            }
        ) {
            Column {
                FirstBaselineToTopText(firstBaselineToTop = 32.dp)
                Divider(
                    modifier = Modifier.padding(top = 8.dp, bottom = 8.dp),
                    color = Color.LightGray
                )
            }
        }
    }
}

@Composable
fun FirstBaselineToTopText(firstBaselineToTop: Dp) {
    Text(
        text = "Hi there!",
        modifier = Modifier
            .firstBaselineToTop(firstBaselineToTop)
    )
}

@Preview(showBackground = true)
@Composable
fun CustomLayoutScreenPreview() {
    CustomLayoutScreen()
}

fun Modifier.firstBaselineToTop(
    firstBaselineToTop: Dp
) = this.then(
    layout { measurable, constraints ->
        val placeable = measurable.measure(constraints = constraints)

        check(placeable[FirstBaseline] != AlignmentLine.Unspecified)
        val firstBaseline = placeable[FirstBaseline]

        val placeableY = firstBaselineToTop.roundToPx() - firstBaseline
        val height = placeable.height + placeableY
        layout(placeable.width, height) {
            placeable.placeRelative(0, placeableY)
        }
    }
)