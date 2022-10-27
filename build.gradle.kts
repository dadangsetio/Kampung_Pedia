import java.net.URI

buildscript {
    repositories {
        gradlePluginPortal()
        google()
        mavenCentral()
    }
    dependencies {
        classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:1.7.0")
        classpath("com.android.tools.build:gradle:7.2.2")
        classpath("com.google.gms:google-services:4.3.13")
        classpath ("com.squareup.sqldelight:gradle-plugin:1.5.3")
    }
}

allprojects {
    repositories {
        google()
        mavenCentral()
        maven { url = URI("https://jitpack.io") }
    }
}


tasks.register("clean", Delete::class) {
    delete(rootProject.buildDir)
}