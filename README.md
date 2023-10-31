[![Quality Gate Status](https://sonar.etendo.cloud/api/project_badges/measure?project=etendosoftware_etendo_sonar_java_plugin_AYr6uYUVCucK2F9Bx-rk&metric=alert_status&token=sqb_865515ecfdb83773df450a229b23a4966c7577af)](https://sonar.etendo.cloud/dashboard?id=etendosoftware_etendo_sonar_java_plugin_AYr6uYUVCucK2F9Bx-rk)

# Sonar Java Plugin for Etendo

### Setting up plugin (before publishing)
1. Copy the `gradle.properties.template` file and paste it in project root as `gradle.properties`
2. Fill the `githubUser` and `githubToken` fields with your GitHub credentials

### Creating jar file (which will be added in the sonar server)

`./gradlew shadowJar`

### Publishing jar to maven repository

1. Change version number on `build.gradle`
2. run `./gradlew publish`

### Using jar in sonarqube server
1. Download the desired plugin version on your SonarQube server
2. Move the downloaded jar to extensions/plugins
3. Restart SonarQube

# Release Notes

### 1.0.0
- Created SonarQube plugin for Etendo Java rules