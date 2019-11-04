[![License](https://img.shields.io/github/license/ezralazuardy/muvi)](https://github.com/ezralazuardy/muvi/blob/master/LICENSE) 
[![Author](https://img.shields.io/badge/author-ezra%20lazuardy-blue.svg)](https://github.com/ezralazuardy) 

## Muvi

A movie catalogue Android app using TMDB API. Built with MVVM architecture pattern, and Repository + Singleton design pattern. Written in Kotlin.

### Library used:
  - [**Anko Commons**](https://github.com/Kotlin/anko) (intent, dialog, and snackbar DSL)
  - [**Android Architecture Components**](https://developer.android.com/topic/libraries/architecture) (UI component lifecycle and handling data persistence)
  - [**ElasticView**](https://github.com/armcha/ElasticView) (flexable view)
  - [**Glide**](https://github.com/bumptech/glide) (fetching image from url)
  - [**Kotlin Coroutines**](https://github.com/Kotlin/kotlinx.coroutines) (asynchronous operation)
  - [**Lottie**](https://github.com/airbnb/lottie-android) (loading animation)
  - [**Retrofit**](https://square.github.io/retrofit/) (fetching API data)
  - [**Room**](https://developer.android.com/topic/libraries/architecture/room) (local database)
  - [**RoundedImageView**](https://github.com/vinc3m1/RoundedImageView) (rounded imageview)
  - [**JUnit 4**](https://junit.org/junit4/) (unit testing)
  - [**Androidx Test**](https://github.com/android/android-test) (test utilities for androidx)
  - [**Kotlinx Coroutines Test**](https://kotlin.github.io/kotlinx.coroutines/kotlinx-coroutines-test/) (test utilities for kotlinx.coroutines)
  - [**Mockito**](https://site.mockito.org/) (mocking framework)
  - [**Espresso**](https://developer.android.com/training/testing/espresso) (instrumental testing)

Please add your TMDB API key in **gradle.properties**. If you don't have the key, create it at [TMDB API](https://developers.themoviedb.org/3/getting-started/introduction). Example:

**TMDB_API_KEY="your-api-key-here"**
