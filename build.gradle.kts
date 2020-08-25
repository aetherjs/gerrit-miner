plugins {
    kotlin("jvm") version "1.3.72"
    application
}

group = "org.jetbrains.research.gerritminer"
version = "1.0-SNAPSHOT"

application {
    mainClassName = "org.jetbrains.research.gerritminer.main.MainKt"
}

repositories {
    mavenCentral()
    maven ("https://jitpack.io")
}

dependencies {
    implementation(kotlin("stdlib"))
    implementation("com.github.jkcclemens:khttp:0.1.0")
    implementation("com.google.code.gson:gson:2.8.6")
}




