# Notice
Manual firmware downloads and downloading from the firmware history feature have been disabled for now. Samsung changed something on the backend and always serves the latest available firmware, no matter which is requested.

If you know a workaround, please follow up on [this issue](https://github.com/zacharee/SamloaderKotlin/issues/10).

# Bifrost - Samsung Firmware Downloader
This is yet another firmware downloader for Samsung devices, but it has some special features.

For one, it's cross-platform. Bifrost runs on Windows, Linux, macOS, and even Android! 

Bifrost is also a graphical program, with a shared UI across all supported platforms.

Most of the functionality in Bifrost is based on [Samloader](https://github.com/nlscc/samloader). The Python code has been converted to Kotlin and tweaked to take advantage of some of Kotlin's features.

Bifrost uses Jetpack Compose, JetBrains Compose for Desktop, and Kotlin Multiplatform to create a shared codebase for all supported platforms.

# Download
Binaries are available for 64-bit versions Windows, Linux, macOS, and Android. JetBrains Compose can't currently build for 32-bit operating systems.

Check out the [Releases](https://github.com/zacharee/SamloaderKotlin/releases) page for the downloads.

# Troubleshooting

## Bifrost is returning error 400/401 when downloading
These errors are on Samsung's end. If you can, try using a different region/CSC.

## Bifrost is returning error 403 when checking for updates
These errors are on Samsung's end. Samsung may no longer be serving firmware for your device or may not have started serving firmware yet. Try a different region/CSC if possible and check to make sure your model number is correct.

## Bifrost opens to a blank screen on Windows
On certain GPUs, Jetpack Compose/Skia has trouble rendering. Try running the program as an administrator.

If you have switchable graphics, try using a different GPU.

## Download speeds are slow
Samsung's servers sometimes throttle downloads to about 3MiB/s. For older devices, you may see even slower speeds. Different regions/CSCs may have faster downloads.

# Building
Building this project should be fairly easy.

## Prep:
1. Make sure you have the latest [Android Studio Canary](https://developer.android.com/studio/preview) installed.
2. Clone this project into Android Studio and let it import.
   
## Desktop:
Run the `package` Gradle task.

### Command Line:
1. Open the Terminal view in Android Studio (bottom-left).
2. Enter `gradlew createDistributable` on Windows, `./gradlew createDistributable` on Linux, or `./gradlew packageDmg` on macOS.
3. Once it finishes building, check the output log to see where the executable was saved.

### GUI:
1. Open the Gradle view in Android Studio (top-right).
2. Expand the project, then expand "desktop".
3. Expand "Tasks," then "build," and double-click "createDistributable" on Windows and Linux, or "packageDmg" on macOS.
4. Once it finishes building, check the output log to see where the executable was saved.

## Android:

### Command Line:
1. Open the Terminal view in Android Studio (bottom-left).
2. Enter `gradlew :android:build` on Windows or `./gradlew :android:build` on macOS and Linux.
3. Once it finishes building, go to `android/build/outputs/apk/debug` and install `android-debug.apk`.

### GUI:
1. Open the Gradle view in Android Studio (top-right).
2. Expand the project, then expand "android".
3. Expand "Tasks," then "build," and double-click "build".
4. Once it finishes building, go to `android/build/outputs/apk/debug` and install `android-debug.apk`.

# Running

## Windows

1. Extract the release ZIP for Windows and go through the folders until you find "Bifrost.exe".
2. Launch the EXE. If it fails, launch as Administrator.

## Linux

1. Extract the release ZIP for Linux and go through the folders until you find "Bifrost".
2. Open a terminal in this location.
3. Enter `chmod +x Bifrost`.
4. Enter `./Bifrost`.

## macOS

1. Extract the release ZIP and open the DMG.
2. Move "Bifrost.app" to the Applications folder.
3. Launch the app.

There may be a security error when launching the app. If there is, follow the steps outlined [here](https://github.com/hashicorp/terraform/issues/23033#issuecomment-542302933).

Alternatively, if the above doesn't work, you can try running the following in a Terminal (requires root permissions):

`sudo xattr -cr /Applications/Bifrost.app`.

Once that command is executed, the app should run.

It's also possible that the DMG itself will refuse to open. If that happens, the same `xattr` command, but run on the DMG, should work:

`sudo xattr -cr ~/Downloads/Bifrost-<VERSION>.dmg`.

## Android

1. Download the release APK to your phone.
2. Install and run it.

# Translating

Bifrost supports basic text localization. To translate Bifrost into another language:

1. Fork the repository.
2. Go to [common/src/commonMain/i18n/tk/zwander/samloaderkotlin](/common/src/commonMain/i18n/tk/zwander/samloaderkotlin).
3. Create a file following following the template `strings_COUNTRY_CODE.properties` (e.g., `strings_fr.properties`).
4. Copy the contents of `strings_en.properties` into your new file.
5. Translate the values (everything to the right of the `=` signs).
6. Create a PR with your translations.

Note: Pay special attention to formatting arguments. Numbers inside curly brackets (e.g., `{0}`, `{1}`) should be kept as-is as they will be replaced with text during the application's runtime.

Note: Make sure to keep any other formatting characters as-is (e.g., `\n` should stay as `\n` and `%%` should stay as `%%`).

# Screenshots

## Desktop:

![Blank Desktop Downloader](/screenshots/DesktopDownloadViewBlank.png)
![Blank Desktop Decrypter](/screenshots/DesktopDecrypterViewBlank.png)
![Blank Desktop History](/screenshots/DesktopHistoryViewBlank.png)
![Desktop Download Progress](/screenshots/DesktopDownloadViewProgress.png)
![Desktop Decrypter Progress](/screenshots/DesktopDecrypterViewProgress.png)
![Desktop History Populated](/screenshots/DesktopHistoryViewPopulated.png)

## Mobile:
![Blank Android Downloader](/screenshots/AndroidDownloaderBlank.png)
![Blank Android Decrypter](/screenshots/AndroidDecrypterBlank.png)
![Blank Android History](/screenshots/AndroidHistoryBlank.png)
![Android Download Progress](/screenshots/AndroidDownloaderProgress.png)
![Android Decrypter Progress](/screenshots/AndroidDecrypterProgress.png)
![Android History Populated](/screenshots/AndroidHistoryPopulated.png)
