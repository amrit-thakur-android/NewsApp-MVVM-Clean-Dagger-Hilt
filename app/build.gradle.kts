// NewsApp - Dagger Edition
// Clean Architecture + MVVM + Dagger 2 for Dependency Injection
// For Hilt version, see: NewsApp-MVVM-Clean-Hilt repository

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    id("com.google.devtools.ksp")
    id ("com.google.dagger.hilt.android")
}

android {
    namespace = "com.amritthakur.newsapp"
    compileSdk = 36

    defaultConfig {
        applicationId = "com.amritthakur.newsapp.dagger"
        minSdk = 24
        targetSdk = 36
        versionCode = 1
        versionName = "1.0.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"

        // Keep only English resources for smaller APK size
        androidResources.localeFilters += listOf("en")

        // Add API key to BuildConfig for security
        buildConfigField("String", "NEWS_API_KEY", "\"f26da49a11a6415593a21e293ade2072\"")
    }

    signingConfigs {
        create("release") {
            // Release keystore for Play Store submission
            storeFile = file("release.keystore")
            storePassword = "newsapp123"
            keyAlias = "release"
            keyPassword = "newsapp123"
        }
    }

    buildTypes {
        debug {
            // No minification/proguard for debug builds
            isDebuggable = true
            applicationIdSuffix = ".debug"
            versionNameSuffix = "-debug"
        }
        release {
            isMinifyEnabled = true
            isShrinkResources = true
            isDebuggable = false
            signingConfig = signingConfigs.getByName("release")
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
    kotlin {
        compilerOptions {
            jvmTarget.set(org.jetbrains.kotlin.gradle.dsl.JvmTarget.JVM_11)
        }
    }
    buildFeatures {
        compose = true
        buildConfig = true
    }
}

dependencies {
    // Module dependencies
    implementation(project(":domain"))
    implementation(project(":data"))
    implementation(project(":presentation"))

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)

    // Navigation
    implementation(libs.navigation.compose)
    implementation(libs.compose.hilt.navigation)

    // Dependency Injection
    implementation(libs.dagger.hilt)
    ksp(libs.dagger.hilt.compiler)

    // Networking (for NetworkModule)
    implementation(libs.retrofit)
    implementation(libs.retrofit.moshi)
    implementation(libs.okhttp)
    implementation(libs.okhttp.logging)
    implementation(libs.moshi)
    implementation(libs.moshi.kotlin)

    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)
}