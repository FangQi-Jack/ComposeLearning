package me.jackfangqi.compose.demo

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.layoutId
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.ConstraintSet
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import me.jackfangqi.compose.demo.ui.layout.Chip
import me.jackfangqi.compose.demo.ui.layout.MyBasicColumn
import me.jackfangqi.compose.demo.ui.layout.StaggeredGrid
import me.jackfangqi.compose.demo.ui.layout.firstBaselineToTop
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
        val snackbarHostState = remember { SnackbarHostState() }
        val scope = rememberCoroutineScope()
        var job: Job? = null
        Scaffold(
            scaffoldState = rememberScaffoldState(snackbarHostState = snackbarHostState),
            topBar = {
                TopAppBar(
                    title = {
                        Text(text = "CustomLayoutActivity")
                    }
                )
            }
        ) {
            Column(modifier = Modifier.verticalScroll(rememberScrollState())) {
                FirstBaselineToTopText(firstBaselineToTop = 32.dp)
                ProvideLineDivider()
                MyBasicColumn(modifier = Modifier.padding(8.dp)) {
                    Text(text = "first text in MyBasicColumn")
                    Text(text = "second text in MyBasicColumn")
                    Text(text = "third text in MyBasicColumn")
                }
                ProvideLineDivider()
                GridBodyContent { topic ->
                    job?.cancel()
                    job = scope.launch {
                        snackbarHostState.showSnackbar(message = topic)
                    }
                }
                ProvideLineDivider()
                ConstraintLayoutBodyContent()
                ProvideLineDivider()
                DecoupledConstraintLayout()
                ProvideLineDivider()
                IntrinsicDemo()
            }
        }
    }
}

@Composable
fun IntrinsicDemo() {
    Row(modifier = Modifier.height(IntrinsicSize.Min)) {
        Text(
            text = "text on left",
            modifier = Modifier
                .weight(1f)
                .padding(4.dp)
                .wrapContentWidth(Alignment.CenterHorizontally)
        )
        Divider(
            color = Color.Black, modifier = Modifier
                .fillMaxHeight()
                .width(1.dp)
        )
        Text(
            text = "text on right",
            modifier = Modifier
                .weight(1f)
                .padding(4.dp)
                .wrapContentWidth(Alignment.CenterHorizontally)
        )
    }
}

private fun decoupledConstraints(margin: Dp): ConstraintSet {
    return ConstraintSet {
        val button = createRefFor("button")
        val text = createRefFor("text")

        constrain(button) {
            top.linkTo(parent.top, margin = margin)
        }
        constrain(text) {
            top.linkTo(button.bottom, margin = margin)
        }
    }
}

@Composable
fun DecoupledConstraintLayout() {
    BoxWithConstraints {
        val constraints = if (maxWidth < maxHeight) {
            decoupledConstraints(margin = 16.dp)
        } else {
            decoupledConstraints(margin = 32.dp)
        }

        ConstraintLayout(constraintSet = constraints) {
            Button(onClick = { /*TODO*/ }, modifier = Modifier.layoutId("button")) {
                Text(text = "Button")
            }

            Text(text = "Text", Modifier.layoutId("text"))
        }
    }
}

@Composable
fun ConstraintLayoutBodyContent() {
    ConstraintLayout(modifier = Modifier.fillMaxWidth()) {
        val (button, text) = createRefs()

        Button(onClick = { /*TODO*/ }, modifier = Modifier.constrainAs(button) {
            top.linkTo(parent.top, margin = 8.dp)
        }) {
            Text(text = "Button in ConstraintLayout")
        }

        Text(text = "Text in ConstraintLayout", modifier = Modifier.constrainAs(text) {
            top.linkTo(button.bottom, margin = 8.dp)
            centerHorizontallyTo(parent)
        })
    }
}

@Composable
fun GridBodyContent(modifier: Modifier = Modifier, onChipClick: (String) -> Unit) {
    val topics = listOf(
        "Arts & Crafts", "Beauty", "Books", "Business", "Comics", "Culinary",
        "Design", "Fashion", "Film", "History", "Maths", "Music", "People", "Philosophy",
        "Religion", "Social sciences", "Technology", "TV", "Writing"
    )
    Row(modifier = modifier.horizontalScroll(rememberScrollState())) {
        StaggeredGrid {
            for (topic in topics) {
                Chip(modifier = Modifier.padding(8.dp), text = topic) {
                    onChipClick.invoke(topic)
                }
            }
        }
    }
}

@Composable
fun ProvideLineDivider() {
    Divider(
        modifier = Modifier.padding(top = 8.dp, bottom = 8.dp),
        color = Color.LightGray,
        thickness = 0.5.dp
    )
}

@Composable
fun FirstBaselineToTopText(firstBaselineToTop: Dp) {
    Text(
        text = "Hi there!",
        modifier = Modifier.firstBaselineToTop(firstBaselineToTop)
    )
}

@Preview(showBackground = true)
@Composable
fun CustomLayoutScreenPreview() {
    CustomLayoutScreen()
}
