package me.jackfangqi.compose.demo

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.rememberImagePainter
import me.jackfangqi.compose.demo.ui.theme.ComposeDemoTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MyApp(
                title = getString(R.string.app_name),
                contents = listOf(
                    { Greeting(name = "World") },
                    { Counter() },
                    { MyBoxScreen() },
                    { PhotographerCard() },
                    {
                        OpenComposeActivity {
                            startActivity(Intent(this, ComposeActivity::class.java))
                        }
                    }
                )
            )
        }

        registerForActivityResult()
    }
}

@Composable
fun MyApp(title: String, contents: List<@Composable () -> Unit>) {
    ComposeDemoTheme {
        // A surface container using the 'background' color from the theme
        Column {
            TopAppBar(
                title = {
                    Text(text = title)
                }
            )
            Surface(color = MaterialTheme.colors.background) {
                MyAppScreen(contents = contents)
            }
        }
    }
}

@Composable
fun MyAppScreen(contents: List<@Composable () -> Unit>) {
    LazyColumn {
        items(items = contents) { content ->
            content()
            Divider(color = Color.LightGray, thickness = 0.5.dp)
        }
    }
}

@Composable
fun MyBoxScreen() {
    Box(modifier = Modifier.padding(16.dp)) {
        Text(text = "first text in box", modifier = Modifier.background(color = Color.Red))
        Text(text = "second text in box")
    }
}

@Composable
fun PhotographerCard(modifier: Modifier = Modifier) {
    Row(
        modifier = modifier
            .padding(8.dp)
            .clip(RoundedCornerShape(4.dp))
            .background(color = MaterialTheme.colors.surface)
            .clickable(onClick = {})
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        Surface(
            modifier = Modifier.size(50.dp),
            shape = CircleShape,
            color = MaterialTheme.colors.onSurface.copy(alpha = 0.2f)
        ) {
            // loading real image
            ImageItem()
        }
        Column(
            modifier = Modifier
                .padding(start = 8.dp)
                .align(Alignment.CenterVertically)
        ) {
            Text(text = "JetPack Compose", fontWeight = FontWeight.Bold)
            CompositionLocalProvider(LocalContentAlpha provides ContentAlpha.medium) {
                Text(text = "3 minutes ago", style = MaterialTheme.typography.body2)
            }
        }
    }
}

@Composable
fun ImageItem() {
    Image(
        painter = rememberImagePainter(
            data = "https://developer.android.com/images/brand/Android_Robot.png"
        ),
        contentDescription = "Photographer Image"
    )
}

@Composable
fun OpenComposeActivity(onClick: () -> Unit) {
    Button(onClick = onClick, modifier = Modifier.padding(8.dp)) {
        Text(text = "open ComposeActivity")
    }
}

@Composable
fun Greeting(name: String) {
    var isSelected by remember {
        mutableStateOf(false)
    }
    val backgroundColor by animateColorAsState(targetValue = if (isSelected) Color.Red else Color.Transparent)

    Text(
        text = "Hello $name!",
        modifier = Modifier
            .padding(16.dp)
            .background(color = backgroundColor)
            .clickable(onClick = { isSelected = !isSelected })
    )
}

@Composable
fun Counter() {
    val count = remember {
        mutableStateOf(0)
    }

    Button(
        onClick = { count.value++ },
        modifier = Modifier
            .padding(8.dp)
            .height(48.dp)
            .fillMaxWidth()
    ) {
        Text(text = "Button clicked ${count.value} times")
    }
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    PhotographerCard()
//    MyBoxScreen()
//    MyApp(
//        title = "ComposeDemo",
//        contents = listOf { Greeting(name = "Compose") }
//    )
}