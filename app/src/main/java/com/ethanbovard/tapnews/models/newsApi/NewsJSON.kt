package com.ethanbovard.tapnews.models.newsApi

data class NewsJSON(
        val news: List<New>,
        val page: Int,
        val status: String
)