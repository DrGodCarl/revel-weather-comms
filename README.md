# Weather-based Communication Decision Engine #

## High Level ##
User research indicates the following:
 - If it's raining, users would prefer to get a phone call
 - If it's warm out, they'd prefer a text.
 - If it's cool out, they'd prefer a phone call.
 - If it's in between, they'd prefer an email.

This tool projects out five days from today how the user should be communicated with.
It's built in such a way that there are fairly clear cut- and refactor-points that would
allow anyone to extend it for additional weather conditions, or even other conditions.

## Some Details ##
This project makes use of:
 - [Retrofit](https://github.com/square/retrofit) to make API calls
 - [GSON](https://github.com/google/gson) to deserialize the results into concrete types
 - [Result4k](https://github.com/npryce/result4k) to handle errors using the result monad pattern

For testing:
 - [Kotest](https://github.com/kotest/kotest) for generative testing and better assertions
 - [MockK](https://github.com/mockk/mockk) for mocking
 - [Retrofit Mock](https://github.com/square/retrofit/tree/master/retrofit-mock) for alleviating difficulties with mocking API calls
 
## Running ##
### Requirements: ###
 - Java 11 (this could probably be tweaked in the build.gradle.kts file if another version is better)

### Commands (all at the root of the project): ###
 - To run: `./gradlew run`
 - To build: `./gradlew jar`
 - To test: `./gradlew test`
 