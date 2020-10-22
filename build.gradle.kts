group = "nl.bartvwezel"
repositories {
    mavenCentral()
}

plugins {
    kotlin("jvm") version "1.4.0"
    id("com.apollographql.apollo").version("2.4.0")
}

dependencies {
    implementation(kotlin("stdlib"))
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.3.9")
    // The core runtime dependencies
    implementation("com.apollographql.apollo:apollo-runtime:2.4.0")
    // Coroutines extensions for easier asynchronicity handling
    implementation("com.apollographql.apollo:apollo-coroutines-support:2.4.0")
}
apollo {
    // instruct the compiler to generate Kotlin models
    generateKotlinModels.set(true)
}
java.sourceCompatibility = JavaVersion.VERSION_1_8
tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile> {
    kotlinOptions {
        jvmTarget = "1.8"
    }
}

