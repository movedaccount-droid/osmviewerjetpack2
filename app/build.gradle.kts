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
    implementation(libs.okhttp) // also required
    implementation(libs.kotlinx.serialization.json) // generic serde
    implementation(libs.retrofit2.kotlinx.serialization.converter) // json deserialization
    implementation(libs.converter.gson) // json deserialization again - do we need both?
    implementation(libs.logging.interceptor) // okhttp logging, for debugging retrofit calls
    implementation(libs.coil) // asynchronous image loading
    implementation(libs.coil.compose) // coil jetpack support
    implementation(libs.androidx.paging.runtime)
    implementation(libs.androidx.paging.compose)
    implementation(libs.hilt.android) // hilt, and kapt annotation compiler
    kapt(libs.hilt.compiler)
    androidTestImplementation(libs.hilt.android.testing)
    kaptAndroidTest(libs.hilt.compiler)
    testImplementation(libs.hilt.android.testing)
    kaptTest(libs.hilt.compiler)
    implementation(libs.androidx.hilt.navigation.compose) // navhost support for hilt
    implementation(libs.androidx.room.runtime) // room sql database
    annotationProcessor(libs.androidx.room.compiler)
    kapt(libs.androidx.room.compiler)
    implementation(libs.androidx.room.ktx)
    implementation(libs.androidx.room.rxjava3)
    implementation(libs.androidx.work.runtime.ktx) // workmanager
    androidTestImplementation(libs.androidx.work.testing) // workmanager testing
    implementation(libs.androidx.hilt.work) // workmanager hilt dependency injection
    kapt(libs.androidx.hilt.compiler) // compiler for above
    testImplementation(libs.mockito.core) // mock objects for testing
    testImplementation(libs.kotlinx.coroutines.test) // blocking coroutines for testing
}

kapt {
    correctErrorTypes = true
}