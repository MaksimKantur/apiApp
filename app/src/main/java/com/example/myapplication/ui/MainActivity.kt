package com.example.myapplication.ui
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import coil.compose.rememberImagePainter
import com.example.myapplication.ui.theme.MyApplicationTheme
import com.example.myapplication.viewmodel.RecipeViewModel

class MainActivity : ComponentActivity() {
    private val viewModel by viewModels<RecipeViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            MyApplicationTheme {
                Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) {
                    MainContent(viewModel)
                }
            }
        }
    }

    override fun onBackPressed() {
        if (::navController.isInitialized && !navController.popBackStack()) {
            super.onBackPressed()
        }
    }
}

@Composable
fun MainContent(viewModel: RecipeViewModel) {
    val navController = rememberNavController()
    NavGraph(navController, viewModel)
}

@Composable
fun LoadNetworkImage(url: String, modifier: Modifier = Modifier) {
    val painter: Painter = rememberImagePainter(url)
    Image(
        painter = painter,
        contentDescription = null,
        modifier = modifier
    )
}

@Composable
fun RecipeDetailScreen(navController: NavHostController, viewModel: RecipeViewModel) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp)
                .clip(shape = RoundedCornerShape(8.dp))
                .background(color = Color.LightGray)
        ) {
            LoadNetworkImage(
                url = viewModel.recipes.value?.get(0)!!.recipe.image,
                modifier = Modifier.fillMaxSize()
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            modifier = Modifier.padding(start = 16.dp, end = 16.dp),
            textAlign = TextAlign.Center,
            style = TextStyle(fontSize = 24.sp, fontWeight = FontWeight.Bold),
            text = viewModel.recipes.value?.get(0)!!.recipe.label,
            maxLines = 2,
            overflow = TextOverflow.Ellipsis
        )

        Spacer(modifier = Modifier.height(16.dp))

        Column(
            modifier = Modifier
                .padding(horizontal = 16.dp)
                .verticalScroll(rememberScrollState())
        ) {
            Text(
                style = TextStyle(fontSize = 20.sp, fontWeight = FontWeight.Bold),
                text = "Ingredients:"
            )
            Spacer(modifier = Modifier.height(8.dp))
            viewModel.recipes.value?.get(0)!!.recipe.ingredients.forEach {
                Text(
                    modifier = Modifier.padding(start = 8.dp),
                    text = it.text,
                    style = TextStyle(fontSize = 16.sp)
                )
            }
        }
    }
}


@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun RecipeSearchScreen(
    navController: NavHostController,
    viewModel: RecipeViewModel = ViewModelProvider(LocalContext.current as ComponentActivity).get(
        RecipeViewModel::class.java
    )
) {
    var ingredient by remember { mutableStateOf("") }
    val context = LocalContext.current
    val keyboardController = LocalSoftwareKeyboardController.current

    Column(
        modifier = Modifier
            .padding(16.dp)
            .fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        OutlinedTextField(
            value = ingredient,
            onValueChange = { ingredient = it },
            label = { Text("Enter main ingredient") },
            singleLine = true,
            keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Search),
            keyboardActions = KeyboardActions(
                onSearch = {
                    viewModel.searchRecipes(ingredient, context)
                    keyboardController?.hide()
                }
            ),
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        Button(onClick = { viewModel.searchRecipes(ingredient, context) }) {
            Text("Search")
        }

        Spacer(modifier = Modifier.height(16.dp))

        Box(){
            val recipes by viewModel.recipes.observeAsState()
            val isLoading by viewModel.isLoading.observeAsState()
            if (isLoading == true) {
                CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
            } else {
                if (recipes != null && recipes!!.isNotEmpty()) {
                    navController.navigate(Screen.RecipeDetailScreen.route)
                }
                else{
                    Toast.makeText(context,"Error", Toast.LENGTH_SHORT)
                }
            }
        }
    }
}
