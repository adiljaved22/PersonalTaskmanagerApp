package com.example.personaltaskmanager

data class Category(
    val title: String,
    val location: String
)
data class CategoryResponse(
    val categories: List<Category>
)