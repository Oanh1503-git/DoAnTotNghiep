plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.kotlin.serialization)
}

android {
    namespace = "com.example.laptopstore"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.example.laptopstore"
        minSdk = 28
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
    buildFeatures {
        compose = true
    }
}

dependencies {

    implementation("androidx.compose.material3:material3:1.1.2")
    implementation ("com.google.accompanist:accompanist-pager:0.31.3-beta")
    implementation ("com.google.accompanist:accompanist-pager-indicators:0.31.3-beta")
    implementation("androidx.compose.material:material-icons-extended-android:1.7.5")
    implementation("androidx.navigation:navigation-compose:2.8.4")
    implementation("com.google.code.gson:gson:2.11.0")
    implementation("io.coil-kt:coil-compose:2.7.0")
    implementation ("com.google.accompanist:accompanist-pager:0.31.3-beta")
    implementation ("com.google.accompanist:accompanist-pager-indicators:0.31.3-beta")
    implementation ("androidx.compose.foundation:foundation:1.5.0")
    implementation ("io.coil-kt:coil-compose:2.4.0")
    implementation ("androidx.compose.animation:animation:1.5.0")
    implementation ("org.jetbrains.kotlinx:kotlinx-serialization-json:1.5.1")
    implementation(libs.kotlinx.serialization.json)
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)
    implementation(libs.androidx.navigation.runtime.android)
    implementation(libs.androidx.ui.test.android)
    implementation(libs.engage.core)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)
}