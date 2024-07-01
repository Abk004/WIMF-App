package com.example.wimf1

import java.time.LocalDate

data class FridgeStructure(
    var name: String = "",
    var description: String = ""
)

data class GroceryStructure(
    var name: String = "",
    var expirationDate: String = LocalDate.now().toString(),
    var quantity: String = "",
    var description: String
)

