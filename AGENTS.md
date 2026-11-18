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
- See a UI change without a phone: `android preview list app`, then
  `android preview render app --fqn=<a name from the list>`, and look at the PNG it writes.
- Start time, on a phone: open the app once and wait, then run
  `adb shell am start -W -S -n dev.kisumu.market/.MainActivity` 10 times and report the median
  `TotalTime`.
- Scrolling, on a phone: open the market list, run `adb shell dumpsys gfxinfo dev.kisumu.market reset`,
  fling the list for 20 seconds (`adb shell input swipe 540 1800 540 400 150`, repeated), then run
  `adb shell dumpsys gfxinfo dev.kisumu.market` and report "Janky frames".
- Traces and heap dumps: `android profiler systemtrace` and `android profiler heapdump`.

## Rules

- Measure the release build, never the debug build.
- Don't change `minSdk`, `targetSdk` or the app's dependencies.
- After every change, build the release APK and run `android analyze`. Report the numbers before and
  after.
- Before saying a change works, clear the crash log (`adb logcat -b crash -c`), open each screen
  once with the `--es screen` command above (market, detail and tips), then check
  `adb logcat -d -b crash` shows no crash. Don't tap through the app by hand.
