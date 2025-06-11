package org.example

import java.util.UUID

class CartItem(
    val id: UUID = UUID.randomUUID(),
    val product: Product,
    val quantity: Int
)
