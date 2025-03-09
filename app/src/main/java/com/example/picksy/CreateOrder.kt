package com.example.picksy

import java.time.LocalDate

data class CreateOrder(
    val productName: String,
    val price: Int,
    var amount: Int,
    val userId: String,
    val address: String,
    val status: String,
    val dispatchDate: LocalDate? = null,
    val deliveryDate: LocalDate? = null,
    val orderDate: LocalDate? = null,
    val paymentMethod: String,
    val paymentStatus: String,
    val customerName: String,
    val customerNo: String,
    val landmark: String,
    val pincode: String,
    val url: String
)
{
    val totalCost: Int
        get() = price * amount

    init {
        require(amount >= 0) { "Amount cannot be negative" }
        require(price >= 0) { "Price cannot be negative" }
    }
}
