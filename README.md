# Tap_News
Project done for Mobile App Development course at Georgia State University, built in Android Studio with Java and Kotlin. News, weather, and traffic information all in one app.

## What's supported
* News
  * 📰 Receives latest headlines
* Weather (from The Weather Company's API)
  * ⛅ Provides current conditions
  * 🕐 A hourly summary
  * 📆 7-day forecast narratives (API key needed)
* Traffic (uses Google Cloud)
  * 📝 Uses autocompletion for address input
  * 🗺️ Map that centers based on physical GPS location
  * 📍 Calculates distance between two points

All three facets of the app require API keys for the respective service. Please refer to the "How do I build?" section below.

## Screenshots of Tap News
### (News, Weather, Traffic)
![image](https://user-images.githubusercontent.com/34446470/117905974-0c8dd580-b2a2-11eb-9200-90b678a58ad4.png) ![image](https://user-images.githubusercontent.com/34446470/117905983-10b9f300-b2a2-11eb-8ae5-4328b9f8aaa3.png) ![image](https://user-images.githubusercontent.com/34446470/117905989-144d7a00-b2a2-11eb-9e82-13c14e60cf34.png)

## Cool! How do I build?
Open the project in Android Studio after cloning the repo using `git clone`. This app was tested in Android Studio 4.1.2 with the Pixel 3A emulator and API Level 30.

Before you build the app, be sure to provide API keys of your own:

* app/manifests/AndroidManifest.xml: Add com.google.android.geo.API_key.
* app/java/weather/WeatherDataManager.java: TODO item to add key for api.weather.com.
* app/res/values/strings.xml
  - places_api_key: Replace string with API key for Places.
  - news_api_key: Replace string with API key for https://api.currentsapi.services

## Thanks to the following people for making this project possible:
* Ethan Bovard (full-stack, weather page)
* Matthew Johnson (traffic page)
* Solumtochukwu Orji (news page)
* Alexandra Ochoa (frontend designer)

## Known issues with the current iteration
* Weather and Settings are unusable due to improper permissions checking for the first run

## Other stuff
Some of us have used external resources (i.e. YouTube, Stack Overflow, etc.) to build this app. If any of this content was derivative somewhere where credit should be due -- please let me know so I can give credit. You can reach me at everblackx@gmail.com for such inquiries or otherwise.
