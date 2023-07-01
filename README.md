# MyWeather

<br></br>

## App Preview

MyWeather is a weather app that will display weather information in your local area, it also provides weather information in several countries. 

<p align="center">
  <img width=360 height=720 src="https://github.com/rahmadanu/MyWeather/assets/96525733/31ac0770-e151-48ca-84d1-b35dd332e5af">
</p>

<br></br>

### Features

- Get latest weather information by current location and/or city
<br></br>
Weather information is retrieved by sending latitude and longtitude from user's device, user has to accept the location permission and turning on GPS in order to retrieve weather information in the app.
<br></br>
- Support offline-first functionality
<br></br>
Offline-first is needed to provide some weather information while user is offline. This is done by inserting weather response from api to local database in the app and the weather information retrieved to the UI is only from local database in order to follow best practice Single Source of Truth.
<br></br>
- Sync network call in background task
<br></br>
Network call is called to update the latest data to local database. It is called in background task with one time periodic task, meaning when user has just launched the app. User could also request the network sync by clicking "Click to Refresh" button
