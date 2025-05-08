package com.example.laptopstore.views

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import com.example.laptopstore.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Categories(navController: NavHostController) {
    var selectedBrand by remember { mutableStateOf("") }
    var selectedPriceRange by remember { mutableStateOf("") }
    var selectedUsage by remember { mutableStateOf("") }
    var selectedChip by remember { mutableStateOf("") }
    var selectedScreenSize by remember { mutableStateOf("") }

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
                ChipFilterSection(
                    selectedChip = selectedChip,
                    onChipSelected = { selectedChip = it }
                )
            }
            item {
                ScreenSizeFilterSection(
                    selectedScreenSize = selectedScreenSize,
                    onScreenSizeSelected = { selectedScreenSize = it }
                )
            }
        }
    }
}

@Composable
fun BrandFilterSection(selectedBrand: String, onBrandSelected: (String) -> Unit) {
    val brands = listOf(
        "MacBook" to R.drawable.ap,
        "ASUS" to R.drawable.asus,
        "Lenovo" to R.drawable.lenovo,
        "Dell" to R.drawable.dell,
        "Acer" to R.drawable.acer,
        "HP" to R.drawable.hp,
        "MSI" to R.drawable.msi

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
fun BrandChip(brand: String, logoUrl: Int, isSelected: Boolean, onClick: () -> Unit) {
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
        Usage("Laptop AI", "https://i.postimg.cc/RVZxD87n/msi-modern-15.jpg", "MỚI"),
        Usage("Mac CTO - Nâng cấp theo cách của bạn", "https://i.postimg.cc/KjV6SpNP/asus-vivobook-go15.jpg", "HOT")
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
fun ChipFilterSection(selectedChip: String, onChipSelected: (String) -> Unit) {
    val chips = listOf("Intel Core i3", "Intel Core i5", "Intel Core i7", "AMD Ryzen 5", "AMD Ryzen 7", "Apple M1", "Apple M2")

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 16.dp)
    ) {
        Text(
            text = "Chip",
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 8.dp)
        )
        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            contentPadding = PaddingValues(horizontal = 8.dp)
        ) {
            items(chips) { chip ->
                FilterChip(
                    text = chip,
                    isSelected = selectedChip == chip,
                    onClick = { onChipSelected(chip) }
                )
            }
        }
    }
}

@Composable
fun ScreenSizeFilterSection(selectedScreenSize: String, onScreenSizeSelected: (String) -> Unit) {
    val screenSizes = listOf("13 inch", "14 inch", "15 inch", "16 inch", "17 inch")

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 16.dp)
    ) {
        Text(
            text = "Kích thước màn hình",
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 8.dp)
        )
        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            contentPadding = PaddingValues(horizontal = 8.dp)
        ) {
            items(screenSizes) { size ->
                FilterChip(
                    text = size,
                    isSelected = selectedScreenSize == size,
                    onClick = { onScreenSizeSelected(size) }
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
