# CS 65 MyRun3

This is a readme file for MyRun3.

### Configuration 
This project is developed under Android Studio 3.3.2 with Android API level 23 and compileSdkVersion is 28.


### Overview
In this project, it continues the work from MyRun2 and adds database and threads. It records the exercise entries in the database SQLite, uses AsyncTask for writing data to database, uses an AsyncTaskLoader for reading data and a background thread to delete information in the database. 



### Some warnings?
In ListAdapter.java, there are 3 warnings on super(), as I described in Myrun2. 
And there are warnings in the model ExerciseEntry.java. It can be fixed in MyRun4, since there are some parameters not used now, but can be used in the future. 


### Question?
For the current version, if a new user registers, then he can see the entry activities of an old user. To solve this problem, we can create the table and name it with username+table_name, but it needs to make sure that there are not two same username, so in register, it needs to query for the username. This bug can be fixed in the next version. 


### Reference
[https://www.cs.dartmouth.edu/~campbell/cs65/lecture14/lecture14.html](https://www.cs.dartmouth.edu/~campbell/cs65/lecture14/lecture14.html)
