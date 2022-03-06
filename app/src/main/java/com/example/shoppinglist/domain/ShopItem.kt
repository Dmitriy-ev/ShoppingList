package com.example.shoppinglist.domain

/**
 * Основа бизнес-логики
 */
data class ShopItem(
    val id: Int,
    val name: String,
    val count: Int,
    val enabled: Boolean
)
