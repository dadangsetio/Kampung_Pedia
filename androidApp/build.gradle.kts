plugins {
    id("com.android.application")
    kotlin("android")
    id("com.google.gms.google-services")
}

val composeVersion = "1.2.0"
val koinVersion = "3.2.0"
val navVersion = "2.5.0"
val voyagerVersion = "1.0.0-rc02"
val accompanistVersion = "0.25.0"
val cameraxVersion = "1.1.0"

android {
    compileSdk = 32
    defaultConfig {
        applicationId = "com.kampungpedia.android"
        minSdk = 21
        targetSdk = 32
        versionCode = 1
        versionName = "1.0"
    }
    buildTypes {
        getByName("release") {
            isMinifyEnabled = false
        }
    }
    buildFeatures {
        viewBinding = true
        compose = true
    }

    composeOptions {
        kotlinCompilerExtensionVersion = composeVersion
    }
}

dependencies {
    implementation(project(":shared"))
    implementation("com.google.android.material:material:1.6.1")
    implementation("androidx.appcompat:appcompat:1.4.2")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")

    implementation ("com.google.android.gms:play-services-auth:20.2.0")

    implementation ("com.github.yuriy-budiyev:code-scanner:2.3.2")

    implementation ("androidx.lifecycle:lifecycle-viewmodel-ktx:2.5.1")
    implementation ("androidx.activity:activity-ktx:1.5.1")
    implementation ("androidx.fragment:fragment-ktx:1.5.1")

    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.6.3")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.6.3")
    implementation("com.google.firebase:firebase-auth-ktx:21.0.6")

    implementation ("androidx.compose.runtime:runtime:$composeVersion")
    implementation ("androidx.compose.foundation:foundation-layout:$composeVersion")
    implementation("androidx.compose.ui:ui:$composeVersion")
    // Tooling support (Previews, etc.)
    implementation("androidx.compose.ui:ui-tooling:$composeVersion")
    // Foundation (Border, Background, Box, Image, Scroll, shapes, animations, etc.)
    implementation("androidx.compose.foundation:foundation:$composeVersion")
    // Material design icons
    implementation("androidx.compose.material:material-icons-core:$composeVersion")
    implementation("androidx.compose.material:material-icons-extended:$composeVersion")
    // Integration with observables
    implementation("androidx.compose.runtime:runtime-livedata:$composeVersion")
    implementation("androidx.compose.runtime:runtime-rxjava2:$composeVersion")
    implementation("androidx.compose.material3:material3:1.0.0-alpha15")

    // UI Tests
    androidTestImplementation("androidx.compose.ui:ui-test-junit4:$composeVersion")

    // Material
    implementation ("com.google.android.material:compose-theme-adapter:1.1.15")

    implementation ("com.airbnb.android:lottie-compose:5.2.0")
    implementation ("androidx.activity:activity-compose:1.5.1")

    // Coil Image Loader
    implementation("io.coil-kt:coil-compose:2.2.0")

    // Koin for Android
    implementation ("io.insert-koin:koin-core:$koinVersion")
    implementation ("io.insert-koin:koin-android:$koinVersion")

    // Koin Test
    testImplementation ("io.insert-koin:koin-test:$koinVersion")
    testImplementation ("io.insert-koin:koin-test-junit4:$koinVersion")
    implementation("androidx.navigation:navigation-compose:$navVersion")

    // Voyager
    implementation ("cafe.adriel.voyager:voyager-navigator:$voyagerVersion")
    implementation( "cafe.adriel.voyager:voyager-tab-navigator:$voyagerVersion")
    implementation ("cafe.adriel.voyager:voyager-bottom-sheet-navigator:$voyagerVersion")
    implementation ("cafe.adriel.voyager:voyager-koin:$voyagerVersion")
    implementation( "cafe.adriel.voyager:voyager-androidx:$voyagerVersion")

    // Accompanist
    implementation("com.google.accompanist:accompanist-insets:$accompanistVersion")
    implementation("com.google.accompanist:accompanist-pager:$accompanistVersion")
    implementation ("com.google.accompanist:accompanist-permissions:$accompanistVersion")
    implementation("com.google.accompanist:accompanist-swiperefresh:$accompanistVersion")


    implementation ("androidx.camera:camera-core:$cameraxVersion")
    implementation ("androidx.camera:camera-camera2:$cameraxVersion")
    implementation ("androidx.camera:camera-lifecycle:$cameraxVersion")
    implementation ("androidx.camera:camera-view:$cameraxVersion")

    //Barcode
    implementation ("com.google.mlkit:barcode-scanning:17.0.2")

}