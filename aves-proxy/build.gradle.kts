plugins {
   id("java")
   id("com.github.johnrengelman.shadow") version "8.1.1"
}

repositories {
   mavenCentral()
   maven {
      name = "velocity"
      setUrl("https://nexus.velocitypowered.com/repository/maven-public/")
   }
}

dependencies {
   implementation(project(":aves-api"))

   // https://mvnrepository.com/artifact/com.google.guava/guava
   implementation("com.google.guava:guava:31.1-jre")

   // https://mvnrepository.com/artifact/com.google.code.gson/gson
   implementation("com.google.code.gson:gson:2.10.1")

   // https://mvnrepository.com/artifact/io.netty/netty-all
   implementation("io.netty:netty-all:4.1.90.Final")

   // https://mvnrepository.com/artifact/com.mojang/authlib
   implementation("com.mojang:authlib:3.11.50")
}

java {
   sourceCompatibility = JavaVersion.VERSION_17
   sourceCompatibility = JavaVersion.VERSION_17
}

tasks.shadowJar {
   minimize()
   manifest {
      attributes["Main-Class"] = "one.aves.proxy.Launcher"
   }
}
