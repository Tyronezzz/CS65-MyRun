# CS 65 MyRun5

This is a readme file for MyRun5.

### Configuration 
This project is developed under Android Studio 3.3.2 with Android API level 23 and compileSdkVersion is 28.


### Overview
In this project, it continues the work from MyRun4. In MR5, it is mainly about Firebase and HTTP GET/POST method. For sign up/in, it uses authentication for multi-users.In history fragment, it can syn between the local SQL and Firebase reatime database. For board fragment, it can upload the user data to the server and view the data on the server. 



### Some warnings?
(1) In ListAdapter.java, there are 3 warnings on super(), as I described in Myrun2. 
(2) Some warnings at ExerciseEntry.java. It seems that there are some getter/setter not used. But they are useful for the line 
ExerciseEntry entry = snapshot.getValue(ExerciseEntry.class); 

getter/setter functions are used for convert the json data to ExerciseEntry here. So it is inevitable to have these warnings. 



### Reference
[https://www.cs.dartmouth.edu/~campbell/cs65/lecture14/lecture14.html](https://www.cs.dartmouth.edu/~campbell/cs65/lecture14/lecture14.html)

[https://developers.google.com/location-context/fused-location-provider/](https://developers.google.com/location-context/fused-location-provider/)