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
    api(project(":myLib"))
    implementation(Kotlin.KOTLIN_STDLIB)

    // Coroutines
    implementation(Kotlin.COROUTINES_CORE)

    // Hilt
    implementation(Kotlin.HILT_CORE)
    kapt(Kotlin.HILT_COMPILER)

    // Log
    implementation(Kotlin.KOTLIN_LOGGING)
    implementation(Libs.SLF4J)

    // Test
    testImplementation(Testing.JUNIT)
    testImplementation(Testing.MOCKITO)
    testImplementation(Testing.MOCKK)
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