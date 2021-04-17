package com.ethanbovard.tapnews.models.newsApi;

// For endpoint https://newsapi.org/v2/everything
public class EverythingModel {
    String status;
    Integer totalResults;
    EverythingArticle[] articles;
}