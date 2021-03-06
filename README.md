# Iris Msg App

This repo is the app for [irismsg.io](https://irismsg.io), an sms donation platform.
It lets your sign up, create organisations, send messages and processes donations.
It's a native Android app, written in [Kotlin](https://kotlinlang.org/) using
[Android Jetpack](https://developer.android.com/jetpack/),
[Retrofit](https://square.github.io/retrofit/) &
[Dagger](https://google.github.io/dagger/)

Iris Msg is developed by [Open Lab](https://openlab.ncl.ac.uk) at Newcastle University as an Open source project.

## Features

There are two users of this app.
A **coordinator** manages an organisation and sends messages to it's subscribers.
A **donor** uses their sms to forward those messages through the app.

A coordinator can also be a donor for their organisation, but can remove themselves when more donors are added.

#### Coordinators

* Sign up in the app and verify their phone number
* Accept app permissions to send SMS
* Create an organisation and add subscribers
* Invite donors to the app who're sent an SMS
* Compose & send messages to the organisation's subscribers

#### Donors

* Receive an donation invite sms
* Accept app permissions to send SMS
* Get notifications when a message is sent, asking for donations
* Have the control to send as many / few messages as they want
* Can opt-out whenever they want

## Code Structure

Folder | Contents
------ | --------
activity  | The app's Activities
api       | Classes for accessing the Iris Msg api
common    | Common utilities, types & functions
di        | The Dagger dependancy injection modules & components
jwt       | Logic related to json web tokens
model     | The models returned from the api
receiver  | Broadcast receivers to handle app messages i.e. sms sent
repo      | Repositories for ViewModel's to get their data from, uses the api
service   | External services, i.e. for handling firebase messages
ui        | Fragments rendered by activities
viewmodel | ViewModels for accessing api data

## Libraries used

* [Kotlin](https://kotlinlang.org/)
* [Retrofit](https://square.github.io/retrofit/)
* [Android Jetpack](https://developer.android.com/jetpack/)
  * [LiveData](https://developer.android.com/topic/libraries/architecture/livedata)
  * [ViewModel](https://developer.android.com/topic/libraries/architecture/viewmodel)
  * [AppCompat](https://developer.android.com/topic/libraries/support-library/packages#v7-appcompat)
* [Dagger](https://google.github.io/dagger/)
* [Firebase](https://firebase.google.com/)

## Conventions

#### Mock api

The api can be swapped for a fake implementation for development, see [AppModule](/app/src/main/java/uk/ac/ncl/openlab/irismsg/di/AppModule.kt)

#### View Transitions

A small Finite State Machine (fsm) is used to handle multiple views in a single activity.
The activity will have an `enterState()` method which transitions the state
and `ActivityName.State` associated enum to define what states the views can be in.

#### Api Logic

Api-related logic should not be in the fragments, instead it bubbles up events or messages for the activity to handle it.

#### Event bus

An event bus is used to communicate between some classes, i.e. Activities and BroadcastReceivers.

## Releasing

Follow these steps to create and deploy a new release.

### Generating an APK

> Make sure there are no unstaged changes

1. Edit [app/build.gradle](/app/build.gradle) to update the `versionName` 
   and increment the `versionCode`
2. Perform a gradle sync
3. Build a signed APK: `Build > Generate Signed APK / Bundle`
   1. Select APK
   2. Select the signing certificate, the credentials are in the Open Lab vault at `devops/iris/app-signing-key`
4. Wait for the build to complete
5. Add changes to git `git add app/build.gradle app/release/output.log`
6. Commit the changes with the version number and a preceeding v, e.g. `git commit -m 'v1.2'`
7. Tag the commit with the version, e.g. `git tag 1.2`

### Uploading an APK

```bash
# cd /to/the/repo/root
# where $VERSION is the desired version

# Upload the archive
scp app/release/app-release.apk safeuser@irismsg.io:/srv/files/iris-msg-$VERSION.apk

# Update the symlink
ssh safeuser@irismsg.io
cd /srv/files/
rm latest.apk
ln -s iris-msg-$VERSION.apk latest.apk
```

### Updating the Api

You'll need to update the server [stack](https://openlab.ncl.ac.uk/gitlab/iris/stack)
to tell it the new version