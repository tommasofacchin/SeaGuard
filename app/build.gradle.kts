plugins {
    alias(libs.plugins.android.application)
    // Tommaso : Firebase SDK
    //id("com.android.application")
    id("com.google.gms.google-services")
}

android {
    namespace = "com.seaguard"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.seaguard"
        minSdk = 24
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
    buildFeatures {
        viewBinding = true
    }
}

dependencies {

            implementation(libs.appcompat)
            implementation(libs.material)
            implementation(libs.constraintlayout)
            implementation(libs.lifecycle.livedata.ktx)
            implementation(libs.lifecycle.viewmodel.ktx)
            implementation(libs.navigation.fragment)
            implementation(libs.navigation.ui)
            implementation(libs.firebase.firestore)
            implementation(libs.annotation)
    implementation(libs.play.services.location)
    testImplementation(libs.junit)
            androidTestImplementation(libs.ext.junit)
            androidTestImplementation(libs.espresso.core)
            // Tommaso : Firebase SDK
            implementation(platform("com.google.firebase:firebase-bom:33.6.0"))
            implementation("com.google.firebase:firebase-analytics")
            implementation("com.google.firebase:firebase-firestore")
            // Add the dependency for the Firebase Authentication library
            // When using the BoM, you don't specify versions in Firebase library dependencies
            implementation("com.google.firebase:firebase-auth")
            // Material 3
            implementation ("com.google.android.material:material:1.7.5")
            // OpenStreetMap
            implementation ("org.osmdroid:osmdroid-android:6.1.20")
}