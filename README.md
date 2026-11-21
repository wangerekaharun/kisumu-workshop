# Kisumu Market

The app for the DevFest Kisumu 2026 workshop, "Engineering memory-performant Android apps": make an
Android app smaller and faster with the Android CLI, Google's Android skills and an AI agent, then
prove it with numbers.

## Why this matters

Tuning R8 keep rules, baseline profiles, hunting memory leaks and tracing slow starts used to need
specialists, weeks of work, and often a platform team that most teams don't have. Now you can point
your agent at the app: the Android CLI gives it the tools, Google's Android skills give it the
know-how, and you check the numbers. It works with the agent you already use, such as Antigravity,
Claude Code or Codex. A small team can now do what only big teams could.

## Follow along

You can follow it on your own. Each step says what to run and what you should see. The answers are in
the folded "What we found" sections, so try each step before you open them.

## What you need

- A laptop with a JDK, version 17 or later.
- The [Android CLI](https://developer.android.com/tools/agents/android-cli).
- An agent CLI. The workshop uses [Antigravity CLI](https://antigravity.google/docs/cli) (`agy`), but
  any agent the Android CLI supports works.
- For Lab 2 and the extras: an Android phone with USB debugging on. Lab 1 needs no phone.

## Set up

1. Clone this repo and open a terminal in it.

   ```
   git clone https://github.com/wangerekaharun/kisumu-workshop
   cd kisumu-workshop
   ```

2. Set up the Android CLI and start the first build. The build is slow the first time, so leave it
   running and carry on.

   ```
   android init
   android sdk install platforms/android-37.0 build-tools/36.0.0 platform-tools
   ./gradlew assembleRelease
   ```

   On Windows use `gradlew.bat` in place of `./gradlew`, here and below.

3. Install `agy`, sign in with your Google account, and add all of Google's Android skills to this
   project. They go in `.agents/skills`, where the agent finds them.

   ```
   curl -fsSL https://antigravity.google/cli/install.sh | bash
   android skills add --all --agent=antigravity-cli --project=.
   ```

   On Windows, use the install line on the [Antigravity CLI page](https://antigravity.google/docs/cli).
   Check it worked with `agy --version`.

Start `agy` in this folder for the labs. It reads [`AGENTS.md`](AGENTS.md), which tells it how to
build, run and measure this app. Keep its terminal in sight: it stops and waits when it wants your
approval.

## Lab 1: make it smaller

R8 shrinks, renames and optimizes your code when you build a release. It is switched on in this app,
but the app's keep rules stop it doing much.

1. **Measure.** Run `android analyze` on the release APK and write the numbers down. That is your
   "before".

   ```
   android analyze app/build/outputs/apk/release/app-release.apk
   ```

   You should see an APK of about 7.8 MB and "R8 Enabled=true".

2. **Ask for the report.** In `agy`:

   > Use the r8-analyzer skill to report on this app's release build and keep rules.

   The skill asks Gradle for R8's configuration report, scores each keep rule and points at the
   costliest. It only reports: its own rules forbid it from editing your files.

3. **Ask for the fix, then measure.**

   > Fix what the report found. Then build the release APK and measure it with android analyze.

4. **Check it still works.** Open the market list, a product and the Tips screen. A smaller app that
   crashes doesn't count.

<details>
<summary>What we found</summary>

- The report said R8 could optimize only 18% of the app, and one rule caused 69% of what R8 couldn't
  touch.
- The rules holding R8 back are in [`app/proguard-rules.pro`](app/proguard-rules.pro):
  `-dontobfuscate`, the package-wide `-keep class androidx.** { *; }`, `kotlin.**` and `kotlinx.**`
  rules, and keeps for every Activity and Application, which the build already keeps from the
  manifest. Libraries ship their own keep rules, so an app rarely needs to keep theirs.
- The trap: the Tips screen loads its class by a name read from `assets/features.txt`. Delete every
  keep rule and the build still passes, but the Tips screen crashes. The narrow
  `-keep class dev.kisumu.market.tips.MarketTips { <init>(); }` rule is the one to keep.
- A good fix brought the APK from about 7.8 MB to about 1.55 MB, with 1.76 MB of dex: about 80%
  smaller, with every screen still opening.

Size comes out the same on every build, so one measurement is enough here. That isn't true of speed.

</details>

## Lab 2: faster start, smoother list

This lab needs a phone. Plug it in, unlock it, and check `adb devices` lists it.

1. **Ask for the before numbers.**

   > Install a release build on the phone. Measure its start time (middle of 10 launches) and janky
   > frames while scrolling the list.

2. **Ask for the fix.**

   > Use the jetpack-compose-performance and anr-audit-and-fix skills to make the list scroll smoothly
   > and the app start faster.

3. **Ask for the after numbers.**

   > Measure start time and scrolling again the same way, and compare.

Start time is how long the app takes to open from nothing. Janky frames are frames that arrive late
and make the list stutter. `AGENTS.md` gives the agent the exact `adb` commands for both, so you can
run them yourself and check its numbers.

<details>
<summary>What we found</summary>

What slows the app down:

- **At start-up**, `MarketApp` loads and indexes the whole catalogue on the main thread before the
  first screen can draw.
- **In the list**, every row decodes a full-size photo on the main thread and then scales it down,
  the list is sorted again every time the screen redraws, and the rows have no keys.

On a Huawei nova 10 Pro, release builds with the Lab 1 fix in both:

| | Before | After |
|---|---|---|
| Start time (median of 15) | 820 ms | 237 ms |
| Janky frames while scrolling | 24% | 4% |

An agent's own fix on the same phone reached 240 ms and 4.1%.

Speed numbers are noisy. One build gave 465, 366 and 229 ms in the same afternoon. A freshly
installed app runs uncompiled code until the phone compiles it in the background, so open it once,
wait, then time it, and compare medians of many runs, never one.

</details>

## Extras: three more things the CLI does

### See the phone on your laptop

```
android device hub
```

This opens a window that mirrors the plugged-in phone, and you can control the phone from it. The
first run downloads the tool.

### See your UI without a phone

The app has one Compose preview, the product row in
[`MarketScreen.kt`](app/src/main/kotlin/dev/kisumu/market/ui/market/MarketScreen.kt).

```
android preview list app
android preview render app --fqn=dev.kisumu.market.ui.market.MarketScreenKt.ProductRowPreview -o row.png
```

Open `row.png` to see the row drawn from the code. Without `-o` the image goes to a temporary folder
and the CLI doesn't print where. The first render takes about 40 seconds while it downloads the
renderer.

### Find a memory leak

A heap dump is a snapshot of the app's memory. It needs a debug build, because a release build can't
be heap-dumped. In `agy`:

> Install a debug build on the phone and open the app. Rotate the screen five times, then take a heap
> dump with android profiler. Use the android-profiler skill to find how many MainActivity instances
> are in memory and what keeps the old ones alive.

Rotating the phone rebuilds the main screen each time. Backing out and reopening doesn't, because on
Android 12 and later the back button keeps the app's first screen alive.

Heap dumps hold user data such as tokens and images, so treat them as personal data.

<details>
<summary>What we found</summary>

`MainActivity` adds a listener to `PriceTicker`, which lives for as long as the app does, and never
removes it. The listener holds on to the activity, so every rotation leaves the old `MainActivity` in
memory. The fix is to remove the listener in `onDestroy`.

</details>

## About the agent's free tier

`agy` works with a free personal Google account, but the free quota is small and refreshes weekly. If
you run out halfway through, carry on with a teammate signed in on their own account. Short,
specific prompts like the ones above use less of it than open-ended ones.

## Learn more

- The Android CLI: [developer.android.com/tools/agents/android-cli](https://developer.android.com/tools/agents/android-cli)
- All of Google's Android skills: [github.com/android/skills](https://github.com/android/skills), or
  run `android skills list`
- Antigravity CLI: [antigravity.google/docs/cli](https://antigravity.google/docs/cli)

The agent proposes. Measurement decides.
