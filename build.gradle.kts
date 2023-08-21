plugins {
    id("java")
    id("com.github.johnrengelman.shadow") version "7.0.0"
}

group = "phss.quizbot"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
    maven { url = uri("https://jitpack.io/") }
}

dependencies {
    implementation("com.github.discord-jda:JDA:v5.0.0-beta.13")
    implementation("org.mongodb:mongodb-driver-sync:4.10.2")
    implementation("com.googlecode.json-simple:json-simple:1.1.1")
    testImplementation("org.junit.jupiter:junit-jupiter-api:5.9.2")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:5.9.2")
}

tasks.withType<Jar> {
    manifest {
        attributes["Main-Class"] = "phss.quizbot.QuizBot"
    }
}

tasks.getByName<Test>("test") {
    useJUnitPlatform()
}