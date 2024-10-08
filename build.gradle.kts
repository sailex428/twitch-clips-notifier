plugins {
    java
    id("org.springframework.boot") version "3.3.2"
    id("io.spring.dependency-management") version "1.1.6"
    id("com.diffplug.spotless") version "7.0.0.BETA2"
}

group = "io.sailex"
version = "1.0.1"

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(21)
    }
}

repositories {
    mavenCentral()
}

spotless {
    java {
        googleJavaFormat("1.23.0")
        indentWithTabs(2)
        indentWithSpaces(4)
    }
}


dependencies {
    implementation("org.springframework.boot:spring-boot-starter-web")
    developmentOnly("org.springframework.boot:spring-boot-devtools")
    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
    implementation("com.github.twitch4j:twitch4j:1.21.0")
    implementation("com.github.philippheuer.events4j:events4j-handler-spring:0.12.2")
    implementation("org.telegram:telegrambots-longpolling:7.9.0")
    implementation("org.telegram:telegrambots-client:7.9.0")
}

tasks.withType<Test> {
    useJUnitPlatform()
}
