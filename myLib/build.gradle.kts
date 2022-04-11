plugins {
    `java-library`
    kotlin("jvm")
    kotlin("kapt")
}

kapt {
    correctErrorTypes = true
}

java {
    sourceCompatibility = BuildConfig.javaVersion
    targetCompatibility = BuildConfig.javaVersion
}

dependencies {
    implementation(Kotlin.KOTLIN_STDLIB)
    implementation(Kotlin.KOTLIN_REFLECT)

    // Log
    implementation(Kotlin.KOTLIN_LOGGING)
    implementation(Libs.SLF4J)

    // Coroutines
    implementation(Kotlin.COROUTINES_CORE)

    // Hilt
    implementation(Kotlin.HILT_CORE)
    kapt(Kotlin.HILT_COMPILER)

    // TestApp: CharUtils
    implementation(Libs.COMMONS_LANG3)

    // Code Generator for DynamicProxy
    implementation(Libs.CGLIB)

    // Network
    implementation(Libs.MOSHI)
    implementation(Libs.MOSHI_KOTLIN)
    // Retrofit with Moshi Converter
    implementation(Libs.RETROFIT2_MOSHI_CONVERTER)
    // Retrofit library that integrates coroutines.
    implementation(Libs.RETROFIT2_KOTLIN_COROUTINES_ADAPTER)
    // Netty
    implementation(Libs.NETTY)  // TODO:需要裁剪，留下有用的jar

    // Test
    testImplementation(Testing.JUNIT)
    testImplementation(Testing.MOCKITO)
    testImplementation(Testing.ROBOLECTRIC)

    // AndroidX Test - JVM testing
    testImplementation(Testing.ANDROIDX_TEST_CORE)

    // AndroidJUnitRunner and JUnit Rules
    testImplementation(Testing.ANDROIDX_TEST_RUNNER)
    testImplementation(Testing.ANDROIDX_TEST_RULE)

    // Coroutines
    testImplementation(Kotlin.COROUTINES_TEST)

    // Assertions
    testImplementation(Testing.ANDROIDX_TEST_JUNIT)
    testImplementation(Testing.ANDROIDX_TEST_TRUTH)
    testImplementation(Testing.HAMCREST)
    testImplementation(Testing.GOOGLE_TRUTH)
}