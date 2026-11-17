# Kisumu Market

The app for the DevFest Kisumu 2026 workshop: make an Android app smaller and faster with the
Android CLI, Google's Android skills and an AI agent, then prove it with numbers.

## Before you start

You need a JDK (17 or later), the [Android CLI](https://developer.android.com/tools/agents/android-cli)
and an agent CLI. Then, in this folder:

```
android sdk install platforms/android-37.0 build-tools/36.0.0 platform-tools
./gradlew assembleRelease
android analyze app/build/outputs/apk/release/app-release.apk
```

On Windows use `gradlew.bat` in place of `./gradlew`.

Write down the numbers `android analyze` prints. That is your "before".
