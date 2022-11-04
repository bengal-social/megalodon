![Pink version of the Mastodon for Android launcher icon](mastodon/src/main/res/mipmap-xhdpi/ic_launcher_round.png)

# Mastodos

> A fork of the [official Mastodon Android app](https://github.com/mastodon/mastodon-android) adding important features that are missing in the official app and possibly won’t ever be implemented, such as the federated timeline, unlisted posting, bookmarks and an image description viewer.

[![Download latest release](https://img.shields.io/badge/dynamic/json?color=ae218a&label=download%20apk&query=%24.tag_name&url=https%3A%2F%2Fapi.github.com%2Frepos%2Fsk22%2Fmastodon-android-fork%2Freleases%2Flatest&style=for-the-badge)](https://github.com/sk22/mastodon-android-fork/releases/latest/download/mastodos.apk)

## Changes

### Features

* [Add “Unlisted” as a post visibility option](https://github.com/sk22/mastodon-android-fork/tree/feature/enable-unlisted)
  ([Pull request](https://github.com/mastodon/mastodon-android/pull/103))
* [Add “Federation” tab and change Discover tab order](https://github.com/sk22/mastodon-android-fork/tree/feature/add-federated-timeline) ([Fixes issue](https://github.com/mastodon/mastodon-android/issues/8))
* [Add image description button and viewer](https://github.com/sk22/mastodon-android-fork/tree/feature/display-alt-text) ([Pull request](https://github.com/mastodon/mastodon-android/pull/129))
* [Implement pinning posts and displaying pinned posts](https://github.com/sk22/mastodon-android-fork/tree/feature/pin-posts) ([Pull request](https://github.com/mastodon/mastodon-android/pull/140))
* [Implement a bookmark button and list](https://github.com/sk22/mastodon-android-fork/tree/feature/bookmarks) ([Fixes issue](https://github.com/mastodon/mastodon-android/issues/22))

### Behavior

* [Make back button return to the home tab before exiting the app](https://github.com/sk22/mastodon-android-fork/tree/feature/back-returns-home) ([Fixes issue](https://github.com/mastodon/mastodon-android/issues/118))
* [Always preserve content warnings when replying](https://github.com/sk22/mastodon-android-fork/tree/feature/always-preserve-cw) ([Fixes issue](https://github.com/mastodon/mastodon-android/issues/113))
* [Display full image when adding image description](https://github.com/sk22/mastodon-android-fork/tree/feature/compose-image-description-full-image) ([Pull request](https://github.com/mastodon/mastodon-android/pull/182))
* [Implement deleting and re-drafting](https://github.com/sk22/mastodon-android-fork/tree/feature/delete-redraft) ([Fixes issue](https://github.com/mastodon/mastodon-android/issues/21))

### Branding

* App name “Mastodos”
* Pink primary color
* Custom icon: Modulate upstream icon using ImageMagick
  ```bash
  mogrify -modulate 90,100,140 mastodon/src/main/res/mipmap-*/ic_launcher*.png
  ```

## Building

As this app is using Java 17 features, you need JDK 17 or newer to build it. Other than that, everything is pretty standard. You can either import the project into Android Studio and build it from there, or run the following command in the project directory:

```
./gradlew assembleRelease
```

## License

This project is released under the [GPL-3 License](./LICENSE).
