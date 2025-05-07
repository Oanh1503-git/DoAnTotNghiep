package com.example.laptopstore.views

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import com.example.app_e_commerce.model.BottomNavItem
import com.example.laptopstore.models.Screens

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Categories(navController: NavHostController) {
    var selectedBrand by remember { mutableStateOf("") }
    var selectedPriceRange by remember { mutableStateOf("") }
    var selectedUsage by remember { mutableStateOf("") }
    var sortOption by remember { mutableStateOf("Giá thấp đến cao") }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = "") },
                actions = { SearchField() }
            )
        },
        bottomBar = {
            MenuBottomNavBar(navController)
        }
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .background(Color.White),
            contentPadding = PaddingValues(16.dp)
        ) {
            item {
                BrandFilterSection(
                    selectedBrand = selectedBrand,
                    onBrandSelected = { selectedBrand = it }
                )
            }
            item {
                PriceRangeFilterSection(
                    selectedPriceRange = selectedPriceRange,
                    onPriceRangeSelected = { selectedPriceRange = it }
                )
            }
            item {
                UsageFilterSection(
                    selectedUsage = selectedUsage,
                    onUsageSelected = { selectedUsage = it }
                )
            }
            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 8.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Sản phẩm",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Button(
                        onClick = {
                            sortOption = when (sortOption) {
                                "Giá thấp đến cao" -> "Giá cao đến thấp"
                                else -> "Giá thấp đến cao"
                            }
                        },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color.White,
                            contentColor = Color.Black
                        ),
                        modifier = Modifier
                            .border(1.dp, Color.Gray, RoundedCornerShape(16.dp))
                            .height(36.dp)
                    ) {
                        Text(text = sortOption, fontSize = 14.sp)
                        Icon(
                            Icons.Default.ArrowDropDown,
                            contentDescription = "Sort",
                            modifier = Modifier.size(20.dp)
                        )
                    }
                }
            }
            items(products.chunked(2)) { productRow ->
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    productRow.forEach { product ->
                        ProductCard(
                            product = product,
                            navController = navController,
                            modifier = Modifier.weight(1f)
                        )
                    }
                    if (productRow.size == 1) {
                        Spacer(modifier = Modifier.weight(1f))
                    }
                }
            }
        }
    }
}

@Composable
fun BrandFilterSection(selectedBrand: String, onBrandSelected: (String) -> Unit) {
    val brands = listOf(
        "MacBook" to "https://i.postimg.cc/GhQDK6V9/Apple.png",
        "ASUS" to "https://i.postimg.cc/LsrghSYQ/ASUS.png",
        "Lenovo" to "https://i.postimg.cc/x15W0j7Y/Lenovo.png",
        "Dell" to "https://i.postimg.cc/jqGT95VF/Dell.png",
        "Acer" to "https://i.postimg.cc/4dYC2CWG/Acer.png",
        "MSI" to "https://i.postimg.cc/3rZWQmBc/MSI.png",
        "HP" to "https://i.postimg.cc/GphnYTYH/HP.png"
    )

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 16.dp)
    ) {
        Text(
            text = "Thương hiệu",
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 8.dp)
        )
        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            contentPadding = PaddingValues(horizontal = 8.dp)
        ) {
            items(brands) { (brand, logoUrl) ->
                BrandChip(
                    brand = brand,
                    logoUrl = logoUrl,
                    isSelected = selectedBrand == brand,
                    onClick = { onBrandSelected(brand) }
                )
            }
        }
    }
}

@Composable
fun BrandChip(brand: String, logoUrl: String, isSelected: Boolean, onClick: () -> Unit) {
    val borderColor by animateColorAsState(
        targetValue = if (isSelected) Color.Red else Color.Gray
    )
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(8.dp))
            .border(1.dp, borderColor, RoundedCornerShape(8.dp))
            .background(Color.White)
            .clickable { onClick() }
            .padding(8.dp)
    ) {
        AsyncImage(
            model = logoUrl,
            contentDescription = "$brand logo",
            modifier = Modifier.size(40.dp),
            contentScale = ContentScale.Fit
        )
    }
}

@Composable
fun PriceRangeFilterSection(selectedPriceRange: String, onPriceRangeSelected: (String) -> Unit) {
    val priceRanges = listOf(
        "Dưới 10 triệu", "Từ 10 - 15 triệu", "Từ 15 - 20 triệu", "Từ 25 - 30 triệu"
    )

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 16.dp)
    ) {
        Text(
            text = "Phân khúc giá",
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 8.dp)
        )
        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            contentPadding = PaddingValues(horizontal = 8.dp)
        ) {
            items(priceRanges) { range ->
                FilterChip(
                    text = range,
                    isSelected = selectedPriceRange == range,
                    onClick = { onPriceRangeSelected(range) }
                )
            }
        }
    }
}

@Composable
fun UsageFilterSection(selectedUsage: String, onUsageSelected: (String) -> Unit) {
    val usages = listOf(
        Usage("Văn phòng", "https://i.postimg.cc/KjV6SpNP/asus-vivobook-go15.jpg", ""),
        Usage("Gaming", "https://i.postimg.cc/RVZxD87n/msi-modern-15.jpg", ""),
        Usage("Mỏng nhẹ", "https://i.postimg.cc/DyVrzkzt/hp-15-fc0085au-r5.jpg", ""),
        Usage("Cảm ứng", "https://i.postimg.cc/52b8wBLg/macbook-air-m4.jpg", ""),
        Usage("Laptop AI", "https://i.postimg.cc/RVZxD87n/msi-modern-15.jpg", "MỚI")
    )

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 16.dp)
    ) {
        Text(
            text = "Nhu cầu sử dụng",
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 8.dp)
        )
        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            contentPadding = PaddingValues(horizontal = 8.dp)
        ) {
            items(usages) { usage ->
                UsageChip(
                    usage = usage,
                    isSelected = selectedUsage == usage.name,
                    onClick = { onUsageSelected(usage.name) }
                )
            }
        }
    }
}

@Composable
fun FilterChip(text: String, isSelected: Boolean, onClick: () -> Unit) {
    val backgroundColor by animateColorAsState(
        targetValue = if (isSelected) Color.Red else Color.White
    )
    val textColor by animateColorAsState(
        targetValue = if (isSelected) Color.White else Color.Black
    )
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(16.dp))
            .background(backgroundColor)
            .border(1.dp, if (isSelected) Color.Red else Color.Gray, RoundedCornerShape(16.dp))
            .clickable { onClick() }
            .padding(horizontal = 12.dp, vertical = 6.dp)
    ) {
        Text(
            text = text,
            color = textColor,
            fontSize = 14.sp,
            fontWeight = FontWeight.Medium
        )
    }
}

@Composable
fun UsageChip(usage: Usage, isSelected: Boolean, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .clip(RoundedCornerShape(8.dp))
            .clickable { onClick() }
            .shadow(if (isSelected) 4.dp else 2.dp, RoundedCornerShape(8.dp)),
        shape = RoundedCornerShape(8.dp)
    ) {
        Column(
            modifier = Modifier
                .background(Color.White)
                .padding(8.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(
                modifier = Modifier
                    .size(100.dp)
                    .clip(RoundedCornerShape(8.dp))
            ) {
                AsyncImage(
                    model = usage.imageUrl,
                    contentDescription = usage.name,
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop
                )
            }
            Text(
                text = usage.name,
                fontSize = 14.sp,
                fontWeight = FontWeight.Medium,
                modifier = Modifier.padding(top = 4.dp)
            )
            if (usage.label.isNotEmpty()) {
                Box(
                    modifier = Modifier
                        .align(Alignment.End)
                        .padding(top = 2.dp)
                        .background(Color.Red, RoundedCornerShape(4.dp))
                        .padding(horizontal = 4.dp, vertical = 2.dp)
                ) {
                    Text(
                        text = usage.label,
                        color = Color.White,
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}

data class Usage(
    val name: String,
    val imageUrl: String,
    val label: String
)

@Composable
fun ProductCard(product: Product, navController: NavHostController, modifier: Modifier = Modifier) {
    Card(
        modifier = modifier
            .shadow(4.dp, RoundedCornerShape(8.dp))
            .clickable {
                // Điều hướng đến màn hình chi tiết sản phẩm
                // navController.navigate("product_detail/${product.id}")
            },
        shape = RoundedCornerShape(8.dp)
    ) {
        Column(
            modifier = Modifier
                .background(Color.White)
                .padding(8.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(
                modifier = Modifier
                    .size(150.dp)
                    .clip(RoundedCornerShape(8.dp))
            ) {
                AsyncImage(
                    model = product.imageUrl,
                    contentDescription = product.name,
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop
                )
                if (product.discount > 0) {
                    Box(
                        modifier = Modifier
                            .align(Alignment.TopStart)
                            .padding(4.dp)
                            .background(Color.Red, RoundedCornerShape(4.dp))
                            .padding(horizontal = 6.dp, vertical = 2.dp)
                    ) {
                        Text(
                            text = "-${product.discount}%",
                            color = Color.White,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
            Text(
                text = product.name,
                fontSize = 14.sp,
                fontWeight = FontWeight.Medium,
                maxLines = 2,
                modifier = Modifier
                    .padding(top = 8.dp)
                    .height(40.dp)
            )
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.padding(top = 4.dp)
            ) {
                Text(
                    text = "${(product.price-((product.price * product.discount) / 100)) / 1000}.000 VNĐ",
                    fontSize = 16.sp,
                    color = Color.Red,
                    fontWeight = FontWeight.Bold
                )
                if (product.discount > 0) {
                    Text(
                        text = "${product.price / 1000}.000 VNĐ",
                        fontSize = 12.sp,
                        color = Color.Gray,
                        textDecoration = TextDecoration.LineThrough
                    )
                }
            }
        }
    }
}

data class Product(
    val id: Int,
    val name: String,
    val price: Int,
    val imageUrl: String,
    val discount: Int = 0
)

private val products = listOf(
    Product(
        id = 1,
        name = "Laptop gaming ASUS TUF Gaming F15",
        price = 29990000,
        imageUrl = "https://i.postimg.cc/KjV6SpNP/asus-vivobook-go15.jpg",
        discount = 10
    ),
    Product(
        id = 2,
        name = "Laptop MSI Modern 14",
        price = 8990000,
        imageUrl = "https://i.postimg.cc/RVZxD87n/msi-modern-15.jpg"
    ),
    Product(
        id = 3,
        name = "Laptop ASUS Vivobook 16X",
        price = 17290000,
        imageUrl = "https://i.postimg.cc/DyVrzkzt/hp-15-fc0085au-r5.jpg",
        discount = 5
    ),
    Product(
        id = 4,
        name = "Laptop ASUS Vivobook 14 OLED",
        price = 17390000,
        imageUrl = "https://i.postimg.cc/52b8wBLg/macbook-air-m4.jpg"
    )
)

@Composable
fun MenuBottomNavBar(navController: NavHostController) {
    val items = listOf(
        BottomNavItem("Home", Icons.Default.Home, Screens.HOMEPAGE.route),
        BottomNavItem("Categories", Icons.Default.List, Screens.CATAGORIES.route),
        BottomNavItem("Cart", Icons.Default.ShoppingCart, Screens.CARTSCREENS.route),
        BottomNavItem("Account", Icons.Default.Person, Screens.ACCOUNTSCREENS.route)
    )

    var selectedItem by remember { mutableStateOf(1) }

    NavigationBar(containerColor = Color.White) {
        items.forEachIndexed { index, item ->
            NavigationBarItem(
                icon = {
                    Icon(
                        imageVector = item.icon,
                        contentDescription = item.title,
                        modifier = Modifier.size(24.dp),
                        tint = if (selectedItem == index) Color.Black else Color.Gray
                    )
                },
                label = {
                    Text(
                        text = item.title,
                        fontSize = 12.sp,
                        color = if (selectedItem == index) Color.Black else Color.Gray
                    )
                },
                selected = selectedItem == index,
                onClick = {
                    selectedItem = index
                    navController.navigate(item.route) {
                        popUpTo(Screens.HOMEPAGE.route) { inclusive = false }
                        launchSingleTop = true
                    }
                }
            )
        }
    }
}