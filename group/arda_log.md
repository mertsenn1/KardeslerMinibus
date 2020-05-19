# CS102 ~ Personal Log page ~

---

## Arda Atahan Ibis

---

On this page I will keep a weekly record of what I have done for the CS102 group project. This page will be submitted together with the rest of the repository, in partial fulfillment of the CS102 course requirements.

### ~ 27.04.2020 ~

This week I learned how to implement a map view for our application on Android Studio. I implemented a general template for the map view both for the passenger and the driver. I also added some basic functionality to the map which tracks the user's, be it the driver or the passenger, real time location and reflect the changes on the map.

### ~ 02.05.2020 ~

This week, upon finalizing the general structure of the passenger UI, I went on to look for some way to add the minibus routes in Ankara to the application. Surprisingly, and fortunately of course, I managed to find a website that visualizes different minibus routes in Ankara in the form of polylines on Google Maps. I searched ways of implementing a similar functionality in Coming Soon and decided to utilize Google Directions API in order to encrypt five different routes I found from the website that shows the minibus routes in Ankara. The reason I encrypted them is that it makes it very convenient to store them in a cvs (comma-separated values) file and saves us from having to make API calls (which costs money and results in a lower performance) whenever the user wants to visualize a certain route on the map. I also implemented a csv decryptor which enables the user to see the encrypted route as a polyline drawn on the map.

### ~ 09.05.2020 date ~

This week I learned how to use Firebase after my fellow teammates implemented the login and logout logic for the drivers. I also experimented with the realtime database in order to be able to find ways to send and receive location data and update the map accordingly. I, with my teammates, looked for ways to display the locations of the passengers on each route on the driver map and vice versa.

### ~ 16.05.2020 date ~

## This week I managed to implement anonymous login for the passengers. This will allow them to easily use the application without us tracking their data. I also made it possible for the passenger to select a certain route and visualizing the minibuses on the selected route. With this final addition, the core functionality of the app is almost done with a little bits and pieces to consider.
