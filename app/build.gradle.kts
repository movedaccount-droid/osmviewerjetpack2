plugins {
    alias(libs.plugins.androidApplication)
    alias(libs.plugins.jetbrainsKotlinAndroid)
    id("kotlin-parcelize") // https://developer.android.com/kotlin/parcelize#kts
    id("org.jetbrains.kotlin.plugin.serialization") version "1.8.10" // to enable serialization
    kotlin("kapt") // hilt dependency, annotation compiler
    id("com.google.dagger.hilt.android") // hilt preprocessing
}

android {
    namespace = "ac.uk.hope.osmviewerjetpack"
    compileSdk = 34

    defaultConfig {
        applicationId = "ac.uk.hope.osmviewerjetpack"
        minSdk = 24
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }
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
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
    buildFeatures {
        compose = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.1"
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}

dependencies {
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)
    implementation(libs.androidx.preference) // non-deprecated getDefaultSharedProperties
    implementation(libs.androidx.navigation.compose) // jetpack navigation
    implementation(libs.retrofit) // type-safe http requests
    implementation(libs.kotlinx.serialization.json) // generic serde
    implementation(libs.retrofit2.kotlinx.serialization.converter) // json deserialization
    implementation(libs.okhttp) // json deserialization
    implementation(libs.hilt.android) // hilt, and kapt annotation compiler
    kapt(libs.hilt.compiler)
    androidTestImplementation (libs.hilt.android.testing)
    kaptAndroidTest(libs.hilt.compiler)
    testImplementation(libs.hilt.android.testing)
    kaptTest(libs.hilt.compiler)
}

kapt {
    correctErrorTypes = true
}