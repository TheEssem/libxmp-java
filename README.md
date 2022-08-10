# Libxmp Java API

This is a fork of the official JNI bindings for the [libxmp](https://github.com/libxmp/libxmp) tracker module player library, designed for newer Java projects and workflows that require accurate, efficient playback.

## Changes
- Converted to a Gradle project
- Restructured the source tree
- Automatically build libxmp from source
- Added binding for `xmp_load_module_from_memory`

## Build

Due to the repo being converted to a Gradle project, building is much simpler than the original project, requiring only a working JDK (Java 11 or higher is preferred):
```sh
git clone --recursive https://github.com/TheEssem
./gradlew build
```

This will compile the Java project and native code together into a single jar in `build/libs`. When using as a library, it will attempt to load the native library from the jar automatically.