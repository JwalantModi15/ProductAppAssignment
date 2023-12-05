package com.example.lokalappassignment

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.LifecycleOwner
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import coil.compose.AsyncImage
import com.example.lokalappassignment.data.Product
import com.example.lokalappassignment.ui.theme.LokalAppAssignmentTheme
import com.example.lokalappassignment.viewmodel.ProductsViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    private val productsViewModel: ProductsViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            LokalAppAssignmentTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val navController = rememberNavController()
                    SetUpNavigation(navHostController = navController, productsViewModel, this)
                }
            }
        }
    }
}

@Composable
fun SetUpNavigation(
    navHostController: NavHostController,
    productsViewModel: ProductsViewModel,
    lifecycleOwner: LifecycleOwner
) {
    NavHost(navController = navHostController, startDestination = "List") {
        composable(route = "List") {

            ListScreen(
                productsViewModel = productsViewModel,
                lifecycleOwner = lifecycleOwner,
                navHostController)
        }

        composable(route = "Product/{id}", arguments = listOf(navArgument("id") {
            type = NavType.IntType
        })) { navBackStackEntry ->
            val id = navBackStackEntry.arguments?.getInt("id")
            if (id != null) {
                val product = productsViewModel.allProducts.value?.products?.get(id - 1)
                if (product != null) {
                    ProductScreen(product = product)
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ListScreen(
    productsViewModel: ProductsViewModel,
    lifecycleOwner: LifecycleOwner,
    navController: NavHostController) {

    Scaffold(topBar = {
        TopAppBar(
            title = { Text(text = "All Products") },
            colors = TopAppBarDefaults.smallTopAppBarColors(
                titleContentColor = Color.White,
                containerColor = MaterialTheme.colorScheme.primary
            )
        )
    }) {

        val list = remember {
            mutableStateOf<List<Product>>(listOf())
        }
        productsViewModel.getAllProducts()
        productsViewModel.allProducts.observe(lifecycleOwner) {
            list.value = it.products
        }

        ShowProducts(products = list.value, navController, it.calculateTopPadding())

    }

}

@Composable
fun ShowProducts(
    products: List<Product>,
    navController: NavHostController,
    calculateTopPadding: Dp) {
    LazyColumn(Modifier.padding(top = calculateTopPadding)) {
        items(products) {
            Item(it, navController)
        }
    }

}

@Composable
fun Item(product: Product, navController: NavHostController) {
    Row(modifier = Modifier
        .fillMaxWidth()
        .clickable {
            navController.navigate("Product/${product.id}")
        }
        .padding(4.dp)) {
        AsyncImage(
            model = product.images[0],
            contentDescription = null,
            modifier = Modifier.size(70.dp),
            contentScale = ContentScale.Crop
        )
        Column(Modifier.padding(start = 4.dp, end = 4.dp)) {
            Text(text = product.title, fontSize = 16.sp, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(1.dp))
            Text(
                text = product.description,
                fontSize = 12.sp,
                overflow = TextOverflow.Ellipsis,
                maxLines = 1
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProductScreen(product: Product) {
    val imageIndex = remember {
        mutableIntStateOf(0)
    }

    Scaffold(topBar = {
        TopAppBar(
            title = { Text(text = product.title) },
            colors = TopAppBarDefaults.smallTopAppBarColors(
                titleContentColor = Color.White,
                containerColor = MaterialTheme.colorScheme.primary
            )
        )
    }) {
        it
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    top = it
                        .calculateTopPadding()
                        .plus(4.dp), start = 8.dp, end = 8.dp, bottom = 8.dp
                )
        ) {

            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                Text(text = "Brand: ${product.brand}", color = MaterialTheme.colorScheme.primary)
                Text(text = "Rating: ${product.rating}", color = Color.Red)
            }
            Text(text = product.title, fontSize = 16.sp, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(4.dp))

            Box{
                AsyncImage(
                    model = product.images[imageIndex.intValue],
                    contentDescription = null,
                    modifier = Modifier
                        .fillMaxWidth()
                        .aspectRatio(1f),
                    contentScale = ContentScale.FillBounds
                )

                Row(modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.Center), horizontalArrangement = Arrangement.SpaceBetween) {
                    Image(painter = painterResource(id = R.drawable.ic_left), contentDescription = null, modifier = Modifier
                        .clickable {
                            if (imageIndex.intValue - 1 < 0) {
                                imageIndex.intValue = product.images.size - 1
                            } else {
                                imageIndex.intValue = (imageIndex.intValue - 1)
                            }
                        }
                        .background(Color.White))
                    Image(painter = painterResource(id = R.drawable.ic_right), contentDescription = null, modifier = Modifier
                        .clickable {
                            imageIndex.intValue = (imageIndex.intValue + 1) % product.images.size
                        }
                        .background(Color.White))
                }
            }


            Spacer(modifier = Modifier.height(4.dp))

            val discountedPrice =
                (product.price - (product.price * (product.discountPercentage / 100))).toInt()
            Text(text = "-${product.discountPercentage}%  ₹$discountedPrice", fontSize = 24.sp)
            Text(text = "M.R.P.: ${product.price}", textDecoration = TextDecoration.LineThrough)
            Spacer(modifier = Modifier.height(4.dp))

            Text(text = "Description", fontWeight = FontWeight.Bold, fontSize = 16.sp)
            Text(text = product.description)
        }
    }

}


