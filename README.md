# photogallery
A simple app that connects to Unsplash.com free API to fetch random photos from their collections; displayed them as feed on to seamless endless scroll user experience.

# Architecture
App built using clean architecture with 3 main layer : presentation, domain, and data. The presentation and data layer are platform dependent layers while domain layer is pure Kotlin classes.
The design pattern used is MVVM with LiveData. A repository used as a single source of truth.

# Stacks
Android SDK, Retrofit as REST API Client, Kotlin flow as streamer, Kotlin Coroutines as async processor, Android Live Data, Mockito for testing
