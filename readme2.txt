CS102 Project - Group1C - Coming Soon 

Description of the Project 
We propose an Android application that will provide the user with relevant information about minibuses to 
ultimately make it easier for them to schedule their travel and/or find suitable minibus lines within Ankara. 
To this end, the application will include a number of features, including the routes of the minibuses in the database, 
the live location of the chosen minibus, and the information about the driver. 
This application will be of interest to virtually anyone who uses public transport, and especially minibuses, in Ankara. 
Eventually, the app will provide users with facilities that contain relevant and accurate live information and save their 
time without them having to wait for long durations for their minibus of choice. On the other hand, since minibus drivers 
move slowly along the roads in order to collect passengers, sharing their live locations would enable the users to find them faster. 
This, in turn, would also solve traffic jam problems caused by minibus drivers searching for passengers.

Project Current Status: We implemented the majority of the features that we planned. The project is functioning as intended.

Contributors
Mehmet Bahadır Erkan - Retrieving and uploading data to online database by using Google Firebase. Organising the location markers
according to user's realtime data by using Google Firebase. Drawing different routes by using Google Maps. By using json files of
the routes, helping to store routes encrypted.

Mert Şen - Implementing sign-in, sign-up logic by using Google Firebase, getting duration between the user and the bus by using 
Google Directions API, and putting it to route adapter.

Ege Demirkırkan - Recieving autocompleted places suggestions in search view by using Google Places API. 

Şebnem Türkoğlu - Designing the general UI of the app. Allowing driver to see passengers on their route.
Allowing passengers to find route that goes through both the user's own location and the chosen destination.
Sending notification to the user when the chosen minibus is in a certain range (according to expected arrival time of the bus).

Arda Atahan İbiş - Implementing the map activity for both the driver and the passenger. Encrypting and storing the 
routes in a csv file using Google Directions API. Writing a method to decrypt the encrypted routes and show them on the 
map as a polyline with relevant styling. Implementing anonymous login for the passengers.

Used Softwares/Libraries
Firebase Database 19.3.0
Firebase Auth 19.3.1
Google Play Services Maps 17.0.0
Google Play Services Location 17.0.0
Google Places Libraries 2.2.0
Google Directions Libraries 2.2.0

***************************************************************************
The virtual device to run the project in android studio must have api > 26. 
***************************************************************************
