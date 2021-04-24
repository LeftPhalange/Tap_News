package com.ethanbovard.tapnews.models.newsApi

data class New(
        val author: String,
        val category: List<String>,
        val description: String,
        val id: String,
        val image: String,
        val language: String,
        val published: String,
        val title: String,
        val url: String
)