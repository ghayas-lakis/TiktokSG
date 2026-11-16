# Series Grabber (Android)

Downloads a TikTok series for personal offline viewing, skips ads/unrelated videos,
and merges everything into one MP4 saved in `Movies/SeriesGrabber`.

## Get the APK
1. Push this folder to GitHub (branch `main`).
2. Open the **Actions** tab -> *Build APK* -> latest run -> download the `SeriesGrabber-debug-apk` artifact.
3. Unzip, copy `app-debug.apk` to your phone, install (allow "install unknown apps").

## Use
Paste the link of the **first video** of a series (the app finds and downloads the rest in order, skipping ads and unrelated posts), or a playlist / creator profile link, or use TikTok's **Share -> Series Grabber**.
Options: *Only videos with Part / Ep number* (best for profiles), *reverse order* (profiles list newest first).

## Build locally
Open in Android Studio (Ladybug or newer), or run `gradle assembleDebug` with JDK 17.

Personal use only. Respect TikTok's terms and creators' copyright.
