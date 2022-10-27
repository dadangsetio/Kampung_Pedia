plugins {
    kotlin("multiplatform")
    kotlin("native.cocoapods")
    id("com.android.library")
    kotlin("plugin.serialization") version "1.7.10"
    id("com.squareup.sqldelight")
}

sqldelight {
    database("KampungDB"){
        packageName = "com.kampungpedia.data"
    }
    linkSqlite = true
}

version = "1.0"

kotlin {
    android()
    iosX64()
    iosArm64()
    iosSimulatorArm64()

    cocoapods {
        summary = "Some description for the Shared Module"
        homepage = "Link to the Shared Module homepage"
        ios.deploymentTarget = "14.1"
        podfile = project.file("../iosApp/Podfile")
        framework {
            baseName = "shared"
        }
    }
    
    sourceSets {
        val ktorVersion = "2.0.3"
        val napierVersion = "2.6.1"
        val settingsVersion = "0.9"
        val koinVersion = "3.2.0"
        val sqldelightVersion = "1.5.3"
        val commonMain by getting {
            dependencies {
                // Ktor
                implementation("io.ktor:ktor-client-core:$ktorVersion")
                implementation( "io.ktor:ktor-client-cio:$ktorVersion")
                implementation ("io.ktor:ktor-client-logging:$ktorVersion")
                implementation("io.ktor:ktor-client-content-negotiation:$ktorVersion")
                implementation("io.ktor:ktor-serialization-kotlinx-json:$ktorVersion")
                implementation("io.ktor:ktor-client-auth:$ktorVersion")

                implementation ("ch.qos.logback:logback-classic:1.1.0")

                implementation("io.github.aakira:napier:$napierVersion")
                implementation("com.russhwolf:multiplatform-settings:$settingsVersion")
                implementation("com.russhwolf:multiplatform-settings-coroutines:$settingsVersion")
                // Koin for Kotlin apps
                api ("io.insert-koin:koin-core:$koinVersion")
                implementation("com.squareup.sqldelight:runtime:$sqldelightVersion")
            }
        }
        val commonTest by getting {
            dependencies {
                implementation(kotlin("test"))
            }
        }
        val androidMain by getting {
            dependencies {
                implementation("io.ktor:ktor-client-okhttp:$ktorVersion")
                implementation ("com.squareup.sqldelight:android-driver:$sqldelightVersion")
            }
        }
        val androidTest by getting
        val iosX64Main by getting
        val iosArm64Main by getting
        val iosSimulatorArm64Main by getting
        val iosMain by creating {
            dependsOn(commonMain)
            iosX64Main.dependsOn(this)
            iosArm64Main.dependsOn(this)
            iosSimulatorArm64Main.dependsOn(this)
            dependencies {
                implementation("io.ktor:ktor-client-darwin:$ktorVersion")
                implementation ("com.squareup.sqldelight:native-driver:$sqldelightVersion")
            }
        }
        val iosX64Test by getting
        val iosArm64Test by getting
        val iosSimulatorArm64Test by getting
        val iosTest by creating {
            dependsOn(commonTest)
            iosX64Test.dependsOn(this)
            iosArm64Test.dependsOn(this)
            iosSimulatorArm64Test.dependsOn(this)
        }
    }
}

android {
    compileSdk = 32
    sourceSets["main"].manifest.srcFile("src/androidMain/AndroidManifest.xml")
    defaultConfig {
        minSdk = 21
        targetSdk = 32
    }
}
dependencies {
    implementation("androidx.annotation:annotation:1.2.0")
}
