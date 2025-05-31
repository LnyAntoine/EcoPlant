plugins {
    alias(libs.plugins.android.application)
    id("com.google.gms.google-services")
}

android {
    namespace = "com.launay.ecoplant"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.launay.ecoplant"
        minSdk = 24
        targetSdk = 35
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
    buildFeatures {
        viewBinding = true
    }
}

dependencies {
    implementation("com.google.android.gms:play-services-location:21.0.1")
    implementation("com.google.firebase:firebase-storage:20.3.0")
    implementation("com.fasterxml.jackson.core:jackson-databind:2.15.2")
    implementation(libs.glide)
    implementation("com.google.firebase:firebase-auth:22.3.1")
    implementation("com.google.firebase:firebase-database:20.3.0") // si tu utilises Realtime Database
    implementation("com.google.firebase:firebase-firestore:24.11.0") // pour Firestore
    implementation("com.google.firebase:firebase-config:21.0.1")  // Dépendance Firebase Remote Config
    implementation("com.google.firebase:firebase-analytics:21.1.0")  // Optionnel mais recommandé pour Firebase Analytics
    implementation(libs.appcompat)
    implementation(libs.material)
    implementation(libs.constraintlayout)
    implementation(libs.navigation.fragment)
    implementation(libs.navigation.ui)
    implementation(libs.activity)
    testImplementation(libs.junit)
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)
    implementation(platform(libs.firebase.bom))
    implementation(libs.firebase.analytics)
}

apply(plugin = "com.google.gms.google-services")