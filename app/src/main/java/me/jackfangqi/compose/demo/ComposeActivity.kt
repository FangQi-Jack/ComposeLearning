package me.jackfangqi.compose.demo

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.outlined.Favorite
import androidx.compose.runtime.Composable
import androidx.compose.runtime.produceState
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.ImagePainter
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import me.jackfangqi.compose.demo.ui.theme.ComposeDemoTheme

class ComposeActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MyComposeScreen()
        }
    }
}

@Composable
fun MyComposeScreen() {
    val snackbarHostState = remember { SnackbarHostState() }
    ComposeDemoTheme {
        val scope = rememberCoroutineScope()
        Scaffold(
            scaffoldState = rememberScaffoldState(snackbarHostState = snackbarHostState),
            topBar = {
                TopAppBar(
                    title = {
                        Text(text = "ComposeActivity")
                    },
                    actions = {
                        IconButton(onClick = { /*TODO*/ }) {
                            Icon(imageVector = Icons.Filled.Favorite, contentDescription = null)
                        }
                    },
                    navigationIcon = {
                        IconButton(onClick = { /*TODO*/ }) {
                            Icon(imageVector = Icons.Filled.Menu, contentDescription = null)
                        }
                    }
                )
            },
            floatingActionButton = {
                FloatingActionButton(
                    onClick = {
                        scope.launch {
                            snackbarHostState.showSnackbar(message = "FloatingActionBar clicked")
                        }
                    },
                    backgroundColor = Color.Blue
                ) {
                    Icon(
                        imageVector = Icons.Filled.Add,
                        contentDescription = null,
                        tint = Color.White
                    )
                }
            }
        ) { padding ->
            BodyContent(
                Modifier
                    .padding(padding)
                    .padding(8.dp)
            )
        }
    }
}

@Composable
fun ShowSnackBar(message: String) {
    Snackbar {
        Text(text = message)
    }
}

@Composable
fun BodyContent(modifier: Modifier = Modifier) {
//    SimpleList()
    LazyList()
}

@Composable
fun LazyList() {
    val listSize = 100

    val scrollState = rememberLazyListState()

    val scope = rememberCoroutineScope()

    Column {
        Row {
            Button(onClick = {
                scope.launch { scrollState.animateScrollToItem(0) }
            }, modifier = Modifier.weight(1f)) {
                Text(text = "Scroll to the top")
            }

            Button(onClick = {
                scope.launch { scrollState.animateScrollToItem(listSize - 1) }
            }, modifier = Modifier.weight(1f)) {
                Text(text = "Scroll to the bottom")
            }
        }

        LazyColumn(state = scrollState) {
            items(count = listSize) {
                Text(
                    text = "Text $it",
                    modifier = Modifier
                        .padding(8.dp)
                        .fillMaxWidth()
                )
            }
        }
    }
}

@Composable
fun SimpleList() {
    val scrollState = rememberScrollState()

    Column(modifier = Modifier.verticalScroll(scrollState)) {
        repeat(100) {
            Text(
                text = "Text $it",
                modifier = Modifier
                    .padding(8.dp)
                    .fillMaxWidth()
            )
        }
    }
}

@Preview
@Composable
fun ComposeScreenPreview() {
    MyComposeScreen()
}