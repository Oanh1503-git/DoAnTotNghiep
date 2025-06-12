package com.example.laptopstore.models

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun DiaChiItemOnCheckout(
    diaChi: DiaChi,
    onToggleMacDinh: ((Boolean) -> Unit)? = null, // <--- Thêm callback này
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (diaChi.MacDinh == 1) MaterialTheme.colorScheme.primary.copy(alpha = 0.1f)
            else MaterialTheme.colorScheme.surface
        )
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = diaChi.TenNguoiNhan,
                    style = MaterialTheme.typography.titleMedium
                )
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Checkbox(
                        checked = diaChi.MacDinh == 1,
                        onCheckedChange = { isChecked ->
                            onToggleMacDinh?.invoke(isChecked)
                        }
                    )
                    Text("Mặc định", style = MaterialTheme.typography.bodySmall)
                }
            }
            Text("SĐT: ${diaChi.SoDienThoai}", style = MaterialTheme.typography.bodyMedium)
            Text("Địa chỉ: ${diaChi.ThongTinDiaChi}", style = MaterialTheme.typography.bodyMedium)
        }
    }
}
