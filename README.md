# Listn   [![CircleCI](https://circleci.com/gh/JesperQv/Listn.svg?style=shield&circle-token=745faaddfe7553d8c5a40452d7d44764e863ca6a)](https://circleci.com/gh/JesperQv/Listn)

App can be downloaded from [here](https://play.google.com/store/apps/details?id=com.jesperqvarfordt.listn). Simple music player app built on top of the Soundcloud API. The app is built with Uncle Bob's Clean Architecture, loosely inspired by [this project](https://github.com/android10/Android-CleanArchitecture). The audio player is inspired by [this project](https://github.com/googlesamples/android-UniversalMusicPlayer).
App requires a Soundcloud API key to use properly.

Design by [Anton Andersson Andrejić](http://www.iamanton.se/)
Code by [me](https://jesperqvarfordt.com/)

<p align="center">
    <img src ="resources/demo.gif" height=500 />
</p>

## General information

Disclaimer: This is a work in progress. Some presenters in the presentation module have missing tests etc. More features may be implemented in the future.

The project is built to display my implementation of Clean Architecture on the Android platform. I use Dagger 2 for dependency injection to achieve code that is easy to test and understand. RxJava 2 and Android Data Bindings are used to create a reactive application where the UI always (hopefully?) accurately represents whats going on under the hood. 

## Used Libraries

* __Dagger2__ 
* __RxJava/RxAndroid__ 
* __Android Data Binding Library__
* __FirebaseCore (for crash reporting)__
* __Picasso__
* __Exoplayer 2__ 

Libraries for testing
* __JUnit__
* __Mockito__

## License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details


