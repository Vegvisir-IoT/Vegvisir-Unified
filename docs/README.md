# Documentation
This repository contains APIs, ABIs specification and other documentation about
Vegvisir. The ```.gitignore``` file has been updated to ensure that local
changes to Android Studio Configurations do not adversely impact team members.
On your machine ensure that Gradle 5.2 or greater is installed. Additional
dependencies should be noted by the README in the applicable directory.

## Checking out a directory
Generally, a library (e.g. Vegvisir-Core) can be accessed by opening Android
Studio (3.4.1 or greater).
* Click to File -> Open Project
* Navigate to repository location and then continue on towards the containing
  directory. In the case of our example,
  ```Vegvisir-Unified/src/core/java/Vegvisir-Core``` and click open
* The build script will attempt to load all the applicable dependencies. If the
  file does not load successfully, click ```File -> Sync Project with Gradle
  Files```

## Setting up Test Environment
All tests should be created using JUnit 5 going forward for the Java
Deployment. This does involve modifying the the build.gradle script. Be careful
to modify the build script associated with the Module and NOT the project. The
dependency section of the build.gradle file should be modified to resemble the
following:
```

test{
    useJUnitPlatform()
}


dependencies {
    implementation fileTree(include: ['*.jar'], dir: 'libs')
    implementation 'com.google.protobuf:protobuf-java:3.6.1'
    testImplementation 'org.junit.jupiter:junit-jupiter-api:5.4.2'
    testRuntime 'org.junit.jupiter:junit-jupiter-engine:5.4.2'
    testImplementation 'org.junit.jupiter:junit-jupiter-params:5.4.2'
}
```
In regards to import for the Test Class, the import statement for general test
should be as follows: ```import org.junit.jupiter.api.Test;```, rather than the
```import org.junit.Test``` which is JUnit 4.

## Importing Libraries into a Project
In order to build upon another library in the project, one again has to modify
the build.gradle sript that is associated with the module (NOT the project).
Update the dependency section with implemntation project (":<module name>").
This will allow for one to import classes from that module. More concretely, in
the case of Vegvisir Pub Sub layer found:
```Vegvisir-Unified/src/pub_sub/java/Vegvisir-PubSub/vegvisirPubSub/``` can be
added to the project by adding the following annotation to the dependencies
section in the build.gradle file:
```
implementation project (":vegvisirPubSub")
```
