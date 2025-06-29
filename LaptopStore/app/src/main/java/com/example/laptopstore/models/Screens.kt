package com.example.laptopstore.models

sealed class Screens(val route: String) {
    object FAVORITEPRODUCTS : Screens("favorite_products")
    object HOMEPAGE : Screens("HOMEPAGE")
    object ACCOUNTSCREENS : Screens("ACCOUNTSCREENS")
    object ACCOUNTDETAIL : Screens("account_detail/{maKhachHang}") {
        fun createRoute(maKhachHang: String) = "account_detail/$maKhachHang"
    }
    object ADDRESS : Screens("address?fromCheckout={fromCheckout}") {
        fun createRoute(fromCheckout: Boolean = false): String = "address?fromCheckout=$fromCheckout"
    }
    object Login_Screens : Screens("Login_Screens")
    object REGISTERSCREEN : Screens("REGISTERSCREEN")
    object CATAGORIES:Screens("categories")
    object CARTSCREENS:Screens("cartscreens")
    object PRODUCTDETAIL:Screens("productdetail")
    object CHECKOUTSCREENS : Screens("checkout/{totalPrice}/{cartItems}")
    object VERIFYEMAILSCREEN : Screens("VERIFYEMAILSCREEN/{email}/{username}") {
        fun createRoute(email: String, username: String): String {
            return "VERIFYEMAILSCREEN/$email/$username"
        }
    }
    object VERIFYOTPSCREENFORGOTPASSWORD:Screens("VERIFYOTPSCREENFORGOTPASSWORD")
    object RESETPASSWORDSCREEN : Screens("RESETPASSWORDSCREEN/{username}") {
        fun createRoute(username: String): String = "RESETPASSWORDSCREEN/$username"
    }
    object ORDERSUCCESSSCREEN:Screens("ORDERSUCCESSSCREEN")
    object ORDERSTATUSSCREEN:Screens("ORDERSTATUSSCREEN/{maKhachHang}")
    object SEACHSCREENS:Screens("SEACHSCREENS")
    object ORDERDELIVEREDSCREEN : Screens("ORDERDELIVEREDSCREEN/{maKhachHang}") {
        fun createRoute(maKhachHang: String): String = "ORDERDELIVEREDSCREEN/$maKhachHang"
    }


}
