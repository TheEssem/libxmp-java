# Libxmp Java API

This is a fork of the official JNI bindings for the [libxmp](https://github.com/libxmp/libxmp) tracker module player library, designed for newer Java projects and workflows that require accurate, efficient playback.

## Changes
- Converted to a Gradle project
- Restructured the source tree
- Automatically build libxmp from source
- Exposed bindings for some functions/fields that have been added to libxmp since 2014

## Usage
Usage as a library with Gradle is possible like so:
```groovy
repositories {
    maven { url 'https://projectlounge.pw/maven/releases' }
}

dependencies {
    implementation "org.helllabs:libxmp-java:1.0.4"
}
```

## Build

Due to the repo being converted to a Gradle project, building is much simpler than the original project, requiring only a working JDK (Java 17 or higher is preferred) and CMake 3.9 or higher:
```sh
git clone --recursive https://github.com/TheEssem/libxmp-java && cd libxmp-java
./gradlew build
```

This will compile the Java project and native code together into a single jar in `build/libs`. When using as a library, it will attempt to load the native library from the jar automatically.