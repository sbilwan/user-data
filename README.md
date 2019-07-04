This is A REST based API that consumes API hosted at https://bpdts-test-app.herokuapp.com and using the response of the API returns users that have either listed London as their 
city or their current coordinates are within 50 miles of London 
 
 # Softwares/Frameworks
API is built using Java 8,  Spring Boot and  Lombok.
NOTE :- If using the API in IDE like Eclipse, Intellij or NetBeans, might be needed to Enable Annotation Processors for Lombok.

Spring Boot is the framework to provide REST, Tomcat
Lombok is to avoid writing boilerplate code.

#Implementation 
1. To get the users listed in London it is simple to get all the users in London by using endpoint `https://bpdts-test-app.herokuapp.com/city/London/users`

2. To get the users whose coordinates are within 50 miles range, first fetch all the users using endpoint `https://bpdts-test-app.herokuapp.com/users` then calculate their coordinates distance from London coordinates by 
using Haversine formula and choose only those users that are closer within the 50 miles range.

3. CompletableFuture is being used that calls the above two endpoints in parallel and then combine the results from both the operations and present the final list to the caller.

#Run the Application 

The source code of the application can be directly pulled into an IDE (IntelliJ, Eclipse) and run from inside them or a jar can be created.

To create a Jar file, after downloading the source code, navigate to project directory and create the jar file by running the command 
`gradlew clean build`. After the build is successful, navigate to build/libs directory and run the below command 
`java -jar uesr-data-0.0.1-SNAPSHOT.jar`

#Endpoints 

Instructions endpoint

>  GET  http://localhost:8080/api.userdata/v1/

This will return all the endpoints exposed by the API.

> GET http://localhost:8080/api.userdata/v1/users/city/{cityname}/{distance}

The endpoint takes the cityname and distance as the path variable and return the results.

#Testing 
UserData.postman_collection.json is the exported collection containing REST requests that has both the GET requests.
