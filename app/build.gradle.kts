plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    /*    id("com.android.application")*/
    id("com.google.gms.google-services")
}

android {
    namespace = "com.example.personaltaskmanager"
    compileSdk = 36

    defaultConfig {
        applicationId = "com.example.personaltaskmanager"
        minSdk = 24
        targetSdk = 36
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    kotlinOptions {
        jvmTarget = "11"
    }
    buildFeatures {
        compose = true
    }
}
dependencies {
    val nav_version = "2.7.4"
    implementation("com.google.firebase:firebase-firestore-ktx:24.4.0")
    implementation("androidx.security:security-crypto:1.1.0-alpha06")
// Jetpack Compose

    implementation("androidx.constraintlayout:constraintlayout:2.0.0-alpha4")/////
    implementation("androidx.compose.ui:ui:1.5.0")
    implementation("androidx.compose.material:material:1.5.0")
    implementation("androidx.compose.ui:ui-tooling:1.5.0")
// Kotlin Coroutines
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.6.4")
    implementation("androidx.navigation:navigation-compose:${nav_version}")
    implementation(platform("com.google.firebase:firebase-bom:34.2.0"))
    implementation("com.google.firebase:firebase-analytics")
    implementation("androidx.compose.material:material-icons-extended:1.5.0")
    implementation("io.coil-kt:coil-compose:2.4.0")
    implementation("androidx.compose.material:material-icons-extended:1.5.0")

    implementation("com.squareup.retrofit2:retrofit:2.9.0")
    //Json to Kotlin object mapping
    implementation("com.squareup.retrofit2:converter-gson:2.9.0")
    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:2.6.1")

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)
    implementation(platform("androidx.compose:compose-bom:2025.09.01")) // Compose BOM
    implementation("com.auth0.android:auth0:2.0.0") // Example: replace with the actual Auth0 SDK
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)

}