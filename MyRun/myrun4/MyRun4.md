# CS 65 MyRun4

This is a readme file for MyRun4.

### Configuration 
This project is developed under Android Studio 3.3.2 with Android API level 23 and compileSdkVersion is 28.


### Overview
In this project, it continues the work from MyRun3 and Google Map service. I use FusedLocationProvider instead of LocationManager for location updates since I found that the latter has some problems on update the current location for the function getLastLocation. 



### Some warnings?
In ListAdapter.java, there are 3 warnings on super(), as I described in Myrun2. 



<!-- ### Question?
For the current version, if a new user registers, then he can see the entry activities of an old user. To solve this problem, we can create the table and name it with username+table_name, but it needs to make sure that there are not two same username, so in register, it needs to query for the username. This bug can be fixed in the next version. 
 -->

### Reference
[https://www.cs.dartmouth.edu/~campbell/cs65/lecture14/lecture14.html](https://www.cs.dartmouth.edu/~campbell/cs65/lecture14/lecture14.html)

[https://developers.google.com/location-context/fused-location-provider/](https://developers.google.com/location-context/fused-location-provider/)