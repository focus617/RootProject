plugins {
    id("com.android.library")
    id("org.jetbrains.kotlin.android")
    kotlin("kapt")
}

android {
    compileSdk = BuildConfig.COMPILE_SDK

    defaultConfig {
        minSdk = BuildConfig.MIN_SDK
        targetSdk = BuildConfig.TARGET_SDK

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        consumerProguardFiles("consumer-rules.pro")
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
        sourceCompatibility = BuildConfig.javaVersion
        targetCompatibility = BuildConfig.javaVersion
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }

}

dependencies {

    implementation(Jetpack.ANDROIDX_CORE)
    implementation(Jetpack.APPCOMPAT)
    implementation(Jetpack.MATERIAL)

    // Timber log
    implementation(Libs.TIMBER)

    // Moshi
    implementation(Libs.MOSHI)
    implementation(Libs.MOSHI_KOTLIN)
    // Retrofit with Moshi Converter
    implementation(Libs.RETROFIT2_MOSHI_CONVERTER)
    // Retrofit library that integrates coroutines.
    implementation(Libs.RETROFIT2_KOTLIN_COROUTINES_ADAPTER)

    // Test
    testImplementation(Testing.JUNIT)
    testImplementation(Testing.MOCKITO)
    testImplementation(Testing.ROBOLECTRIC)

    // AndroidX Test - JVM testing
    testImplementation(Testing.ANDROIDX_TEST_CORE)

    // AndroidJUnitRunner and JUnit Rules
    testImplementation(Testing.ANDROIDX_TEST_RUNNER)
    testImplementation(Testing.ANDROIDX_TEST_RULE)

    // Assertions
    testImplementation(Testing.ANDROIDX_TEST_JUNIT)
    testImplementation(Testing.ANDROIDX_TEST_TRUTH)
    testImplementation(Testing.HAMCREST)
    testImplementation(Testing.GOOGLE_TRUTH)

    // Core library
    androidTestImplementation(Testing.ANDROIDX_TEST_CORE)

    // AndroidJUnitRunner and JUnit Rules
    androidTestImplementation(Testing.ANDROIDX_TEST_RULE)

    // Assertions
    androidTestImplementation(Testing.ANDROIDX_TEST_JUNIT)
    androidTestImplementation(Testing.ANDROIDX_TEST_TRUTH)

    // Espresso dependencies
    androidTestImplementation(Testing.ANDROIDX_TEST_ESPRESSO)}