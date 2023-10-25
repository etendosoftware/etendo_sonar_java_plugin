# Sonar Java Plugin for Etendo

### Setting up plugin (before publishing)
1. Copy `gradle.properties.template` file and paste it in project root as `gradle.properties`
2. Fill the `githubUser` and `githubToken` fields with your GitHub credentials

### Creating jar file (which will be added in the sonar server)

`./gradlew shadowJar`

### Publishing jar to maven repository

1. Change version number on `build.gradle`
2. run `./gradlew publish`

### Using jar in sonarqube server
1. Download the desired plugin version on your sonarqube server
2. Move the downloaded jar to extensions/plugins
3. Restart Sonarqube