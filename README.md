![Pink version of the Mastodon for Android launcher icon](mastodon/src/main/res/mipmap-xhdpi/ic_launcher_round.png)

# Mastodon for Android Fork

## Changes

* [Enable "Unlisted" as a visibility option](https://github.com/sk22/mastodon-android-fork/tree/feature/enable-unlisted)
  ([Pull request](https://github.com/mastodon/mastodon-android/pull/103)) and
  [set as default](https://github.com/sk22/mastodon-android-fork/tree/feature/enable-unlisted-as-default)
* [Add "Federation" tab and change Discover tab order](https://github.com/sk22/mastodon-android-fork/tree/feature/add-federated-timeline) ([Fixes issue](https://github.com/mastodon/mastodon-android/issues/8))
* [Add image description button and viewer](https://github.com/sk22/mastodon-android-fork/tree/feature/display-alt-text) ([Pull request](https://github.com/mastodon/mastodon-android/pull/129))
* [Implement pinning posts and displaying pinned posts](https://github.com/sk22/mastodon-android-fork/tree/feature/pin-posts) ([Pull request](https://github.com/mastodon/mastodon-android/pull/140))
* [Display full image when adding image description](https://github.com/sk22/mastodon-android-fork/tree/feature/compose-image-description-full-image) ([Pull request](https://github.com/mastodon/mastodon-android/pull/182))
* [Always preserve content warnings when replying](https://github.com/sk22/mastodon-android-fork/tree/feature/always-preserve-cw) ([Fixes issue](https://github.com/mastodon/mastodon-android/issues/113))
* [Make back button return to the home tab before exiting the app](https://github.com/sk22/mastodon-android-fork/tree/feature/back-returns-home) ([Fixes issue](https://github.com/mastodon/mastodon-android/issues/118))
* [Implement a bookmark button and list](https://github.com/sk22/mastodon-android-fork/tree/feature/bookmarks) ([Fixes issue](https://github.com/mastodon/mastodon-android/issues/22))
* [Implement deleting and re-drafting](https://github.com/sk22/mastodon-android-fork/tree/feature/delete-redraft) ([Fixes issue](https://github.com/mastodon/mastodon-android/issues/21))

## Fork-specific changes

* Custom app name
* Custom icon: Modulate upstream icon's hue by `161%` using ImageMagick

  ```bash
  mogrify -modulate 100,100,161 mastodon/src/main/res/mipmap-*/ic_launcher*.png
  ```

* Custom primary color: Hue of all `primary` colors in `colors.xml` is rotated
  by `109.8°` (equivalent of `161%`, done by hand using
  [PineTools](https://pinetools.com/shift-hue-color))

## Building

As this app is using Java 17 features, you need JDK 17 or newer to build it. Other than that, everything is pretty standard. You can either import the project into Android Studio and build it from there, or run the following command in the project directory:

```
./gradlew assembleRelease
```

## License

This project is released under the [GPL-3 License](./LICENSE).
