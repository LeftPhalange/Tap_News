# Tap_News
Project done for Mobile App Development course at Georgia State University, built in Android Studio with Java and Kotlin. News, weather, and traffic information all in one app.

## What's supported
* News
* Weather (from The Weather Company's API)
* Traffic (Places, Map and Autocomplete Support Fragments)

All three facets of the app require API keys for the respective service. Please refer to the "How do I build?" section below.

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
