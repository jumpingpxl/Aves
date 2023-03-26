plugins {
    id("java-library")
}

repositories {
    mavenCentral()
}

dependencies {
    // https://mvnrepository.com/artifact/com.google.guava/guava
    implementation("com.google.guava:guava:31.1-jre")

    // https://mvnrepository.com/artifact/com.google.code.gson/gson
    implementation("com.google.code.gson:gson:2.10.1")

    // https://mvnrepository.com/artifact/io.netty/netty-common
    implementation("io.netty:netty-common:4.1.90.Final")

    // https://mvnrepository.com/artifact/io.netty/netty-transport
    implementation("io.netty:netty-transport:4.1.90.Final")
}

java {
    sourceCompatibility = JavaVersion.VERSION_17
    sourceCompatibility = JavaVersion.VERSION_17
}