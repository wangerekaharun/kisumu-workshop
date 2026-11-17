# Kisumu Market

A small Jetpack Compose app: a price list for Kisumu's markets, a product page and a tips page.
There is no Android Studio here. Use the `android` CLI and the Gradle wrapper.

## Build, run and measure

- Release build: `./gradlew assembleRelease` (Windows: `gradlew.bat assembleRelease`).
  The APK is `app/build/outputs/apk/release/app-release.apk`. It is signed with the debug key so it
  installs on any phone.
- Size: `android analyze app/build/outputs/apk/release/app-release.apk`.
- Install and open on a phone: `android run --apks=app/build/outputs/apk/release/app-release.apk`.
- Open one screen directly: `adb shell am start -n dev.kisumu.market/.MainActivity --es screen <market|detail|tips>`.

## Rules

- Measure the release build, never the debug build.
- Don't change `minSdk`, `targetSdk` or the app's dependencies.
- After every change, build the release APK and run `android analyze`. Report the numbers before and
  after.
- Before saying a change works, open every screen on a phone: the market list (scroll it), a
  product, and Tips.
