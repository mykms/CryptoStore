import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.material.Checkbox
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

class AppActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(ComposeView(this).apply {
            setContent {
                Screen()
            }
        })
        // or
        setContent {
            Screen()
        }
    }
}

@Composable
fun Screen() {
    Text("Home is here")
}

data class SomeInfo(
    var name: String,
    val description: String,
)


@OptIn(ExperimentalAnimationApi::class)
@Composable
fun HomeScreen(info: SomeInfo, showImage: Boolean) {
    var isImageShown by remember { mutableStateOf(showImage) }
    MaterialTheme {
        Box(
            modifier = Modifier
                .fillMaxHeight()
                .fillMaxWidth(),
            contentAlignment = Alignment.Center
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .background(Color.Yellow.copy(alpha = 0.2f))
                    .clickable {
                        isImageShown = isImageShown.not()
                    }
                    .padding(16.dp)
            ) {
                AnimatedVisibility(visible = isImageShown) {
                    Image(
                        imageVector = Icons.Default.Person,
                        contentDescription = null,
                        colorFilter = ColorFilter.tint(Color.Green),
                        modifier = Modifier
                            .padding(4.dp)
                            .border(border = BorderStroke(2.dp, Color.Gray))
                            .padding(4.dp)
                    )
                }
                Column(
                    modifier = Modifier.padding(4.dp),
                    verticalArrangement = Arrangement.SpaceAround
                ) {
                    Text(text = info.name, fontWeight = FontWeight.Bold)
                    Text(text = info.description, color = Color.Black.copy(alpha = 0.5f))
                }
            }
        }
    }
}

@Preview(
    backgroundColor = 0xFFFFFFFF,
    showBackground = true
)
@Composable
fun HomePreview() {
    HomeScreen(
        info = SomeInfo(
            name = "Andrey",
            description = "Android developer"
        ),
        showImage = false
    )
}

@Composable
fun FormsScreen(value: Boolean) {
    val (rememberedValue, setValue) = remember { mutableStateOf(value) }
    Checkbox(
        checked = rememberedValue,
        onCheckedChange = { newValue -> setValue(newValue) },
        modifier = Modifier.padding(8.dp)
    )
}

@Preview(
    backgroundColor = 0xFFFFFFFF,
    showBackground = true
)
@Composable
fun PreviewForm() {
    FormsScreen(true)
}




