# D.O.R.I.S. IOT Recycling App

# Inspiration
We want to create an app that encourages and educates people to recycle and be mindful of their waste disposal habits.

# What it does
The app has two features: a reward points system, and an image content detector. The reward points system gives points to the user whenever they recycle, and takes off points to the user whenever they throw garbage. This is detected by an Arduino 101 board attached to a garbage can lid, which is connected to the app using bluetooth.

The image content detector determines if an object in an image is recyclable or not.

# How we built it
We built the app using Android Studio, Arduino 101 board, Google Cloud Vision API, and Bluetooth.

# Challenges we ran into
Connecting the Android phone with the Arduino 101 board via bluetooth, and understanding how data gets transferred over bluetooth


# Accomplishments that we’re proud of
* Integrating Google Cloud Vision to determine recyclable objects by through a mobile camera.
* Using bluetooth to connect the Android phone with the  Arduino 101 board
* Implementing bluetooth connectivity in the android app

# What we learned
* How to use a Google Cloud API to identify objects from photos
* How to use an Arduino 101 board and have it connected to a Android phone using BLE (Bluetooth Low Energy)
* How to implement bluetooth connectivity in an Android app

# What’s next for Disposal Organic Revolutionary Innovative System
We would like to bring greater incentive by having company sponsors and expand with having separate Arduino setups for different bins. (e.g. garbage, compost) and tracking the activity between them.

https://devpost.com/software/disposal-organic-revolutionary-innovative-system
