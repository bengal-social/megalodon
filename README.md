![Pink version of the Mastodon for Android launcher icon](mastodon/src/main/res/mipmap-xhdpi/ic_launcher_round.png)

# Mastodos

> A fork of the [official Mastodon Android app](https://github.com/mastodon/mastodon-android) adding important features that are missing in the official app and possibly won’t ever be implemented, such as the federated timeline, unlisted posting, bookmarks and an image description viewer.

[![Download latest release](https://img.shields.io/badge/dynamic/json?color=d92aad&label=download%20apk&query=%24.tag_name&url=https%3A%2F%2Fapi.github.com%2Frepos%2Fsk22%2Fmastodon-android-fork%2Freleases%2Flatest&style=for-the-badge)](https://github.com/sk22/mastodon-android-fork/releases/latest/download/mastodos.apk)

## Changes

### Features

* [Add “Unlisted” as a post visibility option](https://github.com/sk22/mastodon-android-fork/commits/feature/enable-unlisted)
  ([Pull request](https://github.com/mastodon/mastodon-android/pull/103))
* [Add “Federation” tab and change Discover tab order](https://github.com/sk22/mastodon-android-fork/commits/feature/add-federated-timeline) ([Closes issue](https://github.com/mastodon/mastodon-android/issues/8))
* [Add image description button and viewer](https://github.com/sk22/mastodon-android-fork/commits/feature/display-alt-text) ([Pull request](https://github.com/mastodon/mastodon-android/pull/129))
* [Implement pinning posts and displaying pinned posts](https://github.com/sk22/mastodon-android-fork/commits/feature/pin-posts) ([Pull request](https://github.com/mastodon/mastodon-android/pull/140))
* [Implement deleting and re-drafting](https://github.com/sk22/mastodon-android-fork/commits/feature/delete-redraft) ([Closes issue](https://github.com/mastodon/mastodon-android/issues/21))
* [Implement a bookmark button and list](https://github.com/sk22/mastodon-android-fork/commits/feature/bookmarks) ([Closes issue](https://github.com/mastodon/mastodon-android/issues/22))
* [Add “Check for update” button in addition to integrated update checker](https://github.com/sk22/mastodon-android-fork/commits/feature/check-for-update-button)
* [Add “Mark media as sensitive” option](https://github.com/sk22/mastodon-android-fork/commits/feature/mark-media-as-sensitive)
* [Add settings to hide replies and reposts from the timeline](https://github.com/sk22/mastodon-android-fork/commits/feature/filter-home-timeline) ([Pull request](https://github.com/mastodon/mastodon-android/pull/317))
* [Follow and unfollow hashtags](https://github.com/sk22/mastodon-android-fork/commit/7d38f031f197aa6cefaf53e39d929538689c1e4e) ([Closes issue](https://github.com/mastodon/mastodon-android/issues/233))
* [Notification bell for posts](https://github.com/sk22/mastodon-android-fork/commit/b166ca705eb9169025ef32bbe6315b42491b57ea) ([Closes issue](https://github.com/mastodon/mastodon-android/issues/81))
* [Lists view (viewing only, for now)](https://github.com/sk22/mastodon-android-fork/commits/list-timeline-views) based on [@obstsalatschuessel](https://github.com/obstsalatschuessel)'s [Pull request](https://github.com/mastodon/mastodon-android/pull/286)

### Behavior

* [Make back button return to the home tab before exiting the app](https://github.com/sk22/mastodon-android-fork/commits/feature/back-returns-home) ([Closes issue](https://github.com/mastodon/mastodon-android/issues/118))
* [Always preserve content warnings when replying](https://github.com/sk22/mastodon-android-fork/commits/feature/always-preserve-cw) ([Closes issue](https://github.com/mastodon/mastodon-android/issues/113))
* [Display full image when adding image description](https://github.com/sk22/mastodon-android-fork/commits/feature/compose-image-description-full-image) ([Pull request](https://github.com/mastodon/mastodon-android/pull/182))
* [Set spoiler height independently to content height](https://github.com/sk22/mastodon-android-fork/commits/spoiler-height-independent) ([Closes issue](https://github.com/mastodon/mastodon-android/issues/166))
* [Custom extended footer redesign](https://github.com/sk22/mastodon-android-fork/commits/compact-extended-footer)

### Installation

To install this app on your Android device, download the [latest release from GitHub](https://github.com/sk22/mastodon-android-fork/releases/latest/download/mastodos.apk) and open it. You might have to accept installing APK files from your browser when trying to install it. You can also take a look at all releases on the [Releases](https://github.com/sk22/mastodon-android-fork/releases) page.

Mastodos makes use of [Mastodon for Android](https://github.com/mastodon/mastodon-android)’s automatic update checker. Mastodos will check for new updates available on GitHub and offer to download and install them. You can also manually press “Check for updates” at the bottom of the settings page!

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
