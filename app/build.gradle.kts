plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
}

android {
    namespace = "com.example.finals_activity3ramos"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.finals_activity3ramos"
        minSdk = 33
        targetSdk = 34
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
}

dependencies {
    // Change from 1.12.2 to 1.8.0
    implementation("androidx.activity:activity-ktx:1.8.0")

    // Change from 1.16.0 to 1.12.0
    implementation("androidx.core:core-ktx:1.12.0")

    // Remove or downgrade this if you have it
    // implementation("androidx.navigationevent:navigationevent-android:1.0.1")

    // Keep your other dependencies
    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("com.google.android.material:material:1.11.0")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")
}