package com.ethanbovard.tapnews.ui.home

import android.content.Context
import android.util.Log
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

const val BASE_URL = "https://api.currentsapi.services"
class NewsRequest {
    var titlesList = mutableListOf<String>()
    var descList = mutableListOf<String>()
    var imagesList = mutableListOf<String>()
    var linksList = mutableListOf<String>()

    fun makeAPIRequest(view: RecyclerView, context: Context) {
        val api = Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(GrantAPI::class.java)

        GlobalScope.launch(Dispatchers.IO) {
            try {
                val response = api.getNews()

                for (article in response.news) {
                    Log.d("MainActivity", "Result + $article")
                    addToList(article.title, article.description, article.image, article.url)
                }

                //updates ui when data has been retrieved
                withContext(Dispatchers.Main) {
                    setUpRecyclerView(view, context)
                }
            } catch (e: Exception) {
                Log.d("MainActivity", e.toString())
                withContext(Dispatchers.Main) {
                    // attemptRequestAgain(seconds)
                }
            }
        }
    }
    //adds the items to our recyclerview
    fun addToList(title: String, description: String, image: String, link: String) {
        linksList.add(link)
        titlesList.add(title)
        descList.add(description)
        imagesList.add(image)
    }
    fun setUpRecyclerView(view: RecyclerView, context: Context) {
        view.layoutManager = LinearLayoutManager(context)
        view.adapter = NewsAdapter(titlesList, descList, imagesList, linksList)
    }
}