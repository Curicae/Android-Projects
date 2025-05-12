import java.util.Properties

plugins {
    alias(libs.plugins.android.application)
}

// Load API key from local.properties
val localProperties = Properties().apply {
    project.rootProject.file("local.properties").inputStream().use { stream ->
        load(stream)
    }
}

android {
    namespace = "com.example.maps"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.example.maps"
        minSdk = 31
        targetSdk = 35
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"

        // Pass the API key to the manifest
        val apiKey = localProperties.getProperty("MAPS_API_KEY", "YOUR_DEFAULT_API_KEY_IF_MISSING")
        manifestPlaceholders["MAPS_API_KEY"] = apiKey
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
    buildFeatures {
        buildConfig = true
    }
}

dependencies {
    implementation(libs.appcompat)
    implementation(libs.material)
    implementation(libs.activity)
    implementation(libs.constraintlayout)
    implementation("com.google.android.gms:play-services-maps:19.2.0")
    implementation("com.google.android.gms:play-services-location:21.3.0")
    testImplementation(libs.junit)
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)
}