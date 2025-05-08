package com.example.laptopstore.models

 open class Screens (val route:String){
        object HOMEPAGE:Screens("home_page")
        object CATAGORIES:Screens("categories")
        object CARTSCREENS:Screens("cartscreens")
        object ACCOUNTSCREENS:Screens("account")
        object PRODUCTDETAIL:Screens("productdetail")
        object CHECKOUTSCREENS : Screens("checkout/{totalPrice}/{cartItems}")
 }
