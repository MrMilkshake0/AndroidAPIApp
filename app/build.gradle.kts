// app/build.gradle.kts
plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("org.jetbrains.kotlin.plugin.compose")
    id("com.google.dagger.hilt.android")
    kotlin("kapt")
}

android {
    namespace = "com.example.nit3213"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.nit3213"
        minSdk = 26
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildFeatures { compose = true }

    // ✅ Make Java compile use 17 (matches Kotlin)
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
}

dependencies {
    // Compose
    implementation("androidx.activity:activity-compose:1.9.2")
    implementation("androidx.compose.ui:ui:1.7.2")
    implementation("androidx.compose.material3:material3:1.3.0")
    implementation("com.google.android.material:material:1.12.0")
    implementation("androidx.compose.ui:ui-tooling-preview:1.7.2")
    debugImplementation("androidx.compose.ui:ui-tooling:1.7.2")

    // Lifecycle
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.8.6")
    implementation("androidx.lifecycle:lifecycle-runtime-compose:2.8.6")

    // Navigation Compose
    implementation("androidx.navigation:navigation-compose:2.8.0")

    // Coroutines
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.9.0")

    // Retrofit + OkHttp + Gson
    implementation("com.squareup.retrofit2:retrofit:2.11.0")
    implementation("com.squareup.retrofit2:converter-gson:2.11.0")
    implementation("com.squareup.okhttp3:okhttp:4.12.0")
    implementation("com.squareup.okhttp3:logging-interceptor:4.12.0")

    // Hilt
    implementation("androidx.hilt:hilt-navigation-compose:1.2.0")
    implementation("com.google.dagger:hilt-android:2.52")
    kapt("com.google.dagger:hilt-android-compiler:2.52")

    // Tests
    testImplementation("junit:junit:4.13.2")
    testImplementation("io.mockk:mockk:1.13.12")
    testImplementation("org.jetbrains.kotlinx:kotlinx-coroutines-test:1.9.0")

    // Material icons
    implementation("androidx.compose.material:material-icons-extended:1.7.2")

    // Optional: control status/navigation bar colors
    implementation("com.google.accompanist:accompanist-systemuicontroller:0.34.0")
}

// ✅ Ensure Kotlin (incl. KAPT) uses JDK 17 toolchain
kotlin {
    jvmToolchain(17)
}

// (Optional) still fine to keep; matches toolchain
kapt { correctErrorTypes = true }

tasks.register("testClasses") {
    dependsOn("compileDebugUnitTestSources") // or "testDebugUnitTest" to actually run tests
}