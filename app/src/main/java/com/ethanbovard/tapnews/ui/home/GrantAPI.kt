package com.ethanbovard.tapnews.ui.home

import com.ethanbovard.tapnews.models.newsApi.NewsJSON
import retrofit2.http.GET

interface GrantAPI {

    @GET("/v1/latest-news?language=en&apiKey=7goxs3CKQUAJRGhquf6BovIkIToi_vj5Uii2lTrQCVE9EtY2")
    suspend fun getNews() : NewsJSON
}