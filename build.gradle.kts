plugins {
    application
    java
}

group = "Shift"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    testImplementation(platform("org.junit:junit-bom:5.10.0"))
    testImplementation("org.junit.jupiter:junit-jupiter")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
}

// Теперь этот блок будет работать, так как плагин application подключен
application {
    mainClass = "shiftreader.Main"
}

tasks.test {
    useJUnitPlatform()
}