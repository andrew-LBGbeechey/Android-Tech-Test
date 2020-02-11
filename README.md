# Studio Ghibli

Android Application employing Model-View-Intent (MVI) to display Studio Ghibli Films, Film Details, and People.
The application attempts to support Dark Theme with the minimum of work, e.g. using Android standard theme attributes.
Studio Ghibli is written in 100% Kotlin and employs Material Theme, Coroutines, Retrofit, Room, Koin, Arrow.kt and Workers.

Coroutines are used in place of RxJava
Koin Dependency Injections has builtin support for Android ViewModels and allows simple DI for Android Workers.
Arrow.kt is a library for Typed Functional Programming in Kotlin. Studio Ghibli employs a small subset of the available features, namely IO.fx{}

Arrow Fx is an Effects Library that makes effectful and polymorphic programming first class in Kotlin.
fx acts as an extension to the Kotlin native suspend system.

## Getting Started

The Studio Ghibli application does not require user login. However it does depend on having access to the Internet.

### Prerequisites

Studio Ghibli will install and run on all Android devices with minSdkVersion 23 and above

### Installing

Normal APK Instal

## Running the tests

Standard Android Unit tests exist for Network and Database modules and App ViewModel and background workers
All Network tests require access to the internet.

### Approach & Improvements

The Application structure consists of a main "app" with separate modules for database, network and side_effects.

An attempt has been made to implement MVI architecture via ViewModels exposing a "perform" function and activities
have a "render" function.

Each Activity has an associated sealed class representing available Actions that can be "performed" and a
related "Re Action" rendered via the Activity

The Studio Ghibli API exposes 5 types of related data e.g. Films, Locations, People, Species, and Vehicles.
As none of these endpoints support paging the application retrieves a maximum of 250 items with a single call.

The network data is persisted in a Room (Sqlite) database.

Currently the application only displays Films, Film detail and People.
The Film detail screen is incomplete as it displays the raw urls for People, Species, Locations and Vehicles.

Improvements for the application would consist of:-

a). Detect/Report when network access is lost/regained.

b). Employ a Repository for each data type.

c). Implement remaining activities to fully display Locations, Species and Vehicles.

d). Complete Film & People Detail activities.

e). Tests should employ mocked data and not rely on access to the Network.

## 3rd Party Libraries

Arrow - https://arrow-kt.io/

Timber - https://github.com/JakeWharton/timber

retrofit2 - https://square.github.io/retrofit/

Koin - https://insert-koin.io/

Gson - https://github.com/google/gson


## Built With

Android Studio 4.0 Canary 9

Build #AI-193.5233.102.40.6137316, built on January 15, 2020

Runtime version: 1.8.0_212-release-1586-b4-5784211 x86_64

VM: OpenJDK 64-Bit Server VM by JetBrains s.r.o


