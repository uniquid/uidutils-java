Uniquid java utils
======================================

The Uniquid java utils project contains many utilities classes shared between many Uniquid applications.

### Technologies

* Java 7 for the core modules, Java 8 for everything else
* [Gradle 3.4+](https://gradle.org/) - to building the project

### Getting started

To get started, it is best to have the latest JDK and Maven installed. The HEAD of the `master` branch contains the latest development code and various production releases are provided on feature branches.

#### Building from the command line

To perform a full build use
```
gradle clean build
```
The outputs are under the `build` directory.

To move jar package in your /m2/repository
```
gradle install
```
