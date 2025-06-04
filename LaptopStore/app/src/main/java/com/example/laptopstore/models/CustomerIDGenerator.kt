package com.example.laptopstore.models

import java.time.DayOfWeek
import java.time.LocalDateTime
import java.util.Locale

object CustomerIDGenerator {
    private val dayOfWeekMap = mapOf(
        DayOfWeek.MONDAY to "MO",
        DayOfWeek.TUESDAY to "TU",
        DayOfWeek.WEDNESDAY to "WE",
        DayOfWeek.THURSDAY to "TH",
        DayOfWeek.FRIDAY to "FR",
        DayOfWeek.SATURDAY to "SA",
        DayOfWeek.SUNDAY to "SU"
    )

    fun generateCustomerId(customerCount: Int): String {
        val now = LocalDateTime.now()

        // Prefix KH
        val prefix = "KH"

        // Số thứ tự khách hàng, đảm bảo luôn có 3 chữ số
        val memberNumber = String.format("%03d", customerCount + 1)

        // Lấy 2 ký tự đầu của thứ trong tuần bằng tiếng Anh
        val dayOfWeek = dayOfWeekMap[now.dayOfWeek] ?: "UN"

        // Giờ và phút hiện tại
        val hour = String.format("%02d", now.hour)
        val minute = String.format("%02d", now.minute)

        // Kết hợp tất cả
        return "$prefix$memberNumber$dayOfWeek$hour$minute"
    }

    // Hàm kiểm tra tính hợp lệ của mã khách hàng
    fun isValidCustomerId(customerId: String): Boolean {
        // Format: KH + 3 số + 2 chữ + 2 số (giờ) + 2 số (phút)
        val regex = "^KH\\d{3}[A-Z]{2}\\d{4}$".toRegex()
        return regex.matches(customerId)
    }
}