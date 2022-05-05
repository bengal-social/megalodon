# Forked Mastodon for Android
[![Crowdin](https://badges.crowdin.net/mastodon-for-android/localized.svg)](https://crowdin.com/project/mastodon-for-android)

This is the repository for an officially forked Android app for Mastodon.

Learn more about this app in the [blog post](https://blog.joinmastodon.org/2022/02/official-mastodon-for-android-app-is-coming-soon/).

## Changes

* [Enable "Unlisted" as a visibility option](https://github.com/sk22/mastodon-android-fork/tree/feature/enable-unlisted)
  ([Pull request](https://github.com/mastodon/mastodon-android/pull/103)) and
  [set as default](https://github.com/sk22/mastodon-android-fork/tree/feature/enable-unlisted-as-default)
* [Add "Federation" tab and change Discover tab order](https://github.com/sk22/mastodon-android-fork/tree/feature/add-federated-timeline)
* [Add image description button and viewer](https://github.com/sk22/mastodon-android-fork/tree/feature/display-alt-text) ([Pull request](https://github.com/mastodon/mastodon-android/pull/129))

## Building

As this app is using Java 17 features, you need JDK 17 or newer to build it. Other than that, everything is pretty standard. You can either import the project into Android Studio and build it from there, or run the following command in the project directory:

```
./gradlew assembleRelease
```

## License

This project is released under the [GPL-3 License](./LICENSE).