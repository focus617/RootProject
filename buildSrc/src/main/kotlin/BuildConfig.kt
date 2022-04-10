import org.gradle.api.JavaVersion

object BuildConfig {
    const val versionName = "1.0.0"
    const val versionCodeMobile = 1

    const val BUILD_TOOLS = "32.0.0"
    const val COMPILE_SDK = 31
    const val TARGET_SDK = 31
    const val MIN_SDK = 23

    val javaVersion = JavaVersion.VERSION_1_8
}

object ClassPath {
    const val ANDROIDX_NAVIGATION = "androidx.navigation:navigation-safe-args-gradle-plugin:2.4.1"
    const val ANDROID_HILT = "com.google.dagger:hilt-android-gradle-plugin:2.41"
}

object Jetpack {
    private const val lifecycle_version = "2.4.1"
    private const val room_version = "2.4.1"

    const val MATERIAL = "com.google.android.material:material:1.4.0"

    const val ANDROIDX_CORE = "androidx.core:core-ktx:1.7.0"
    const val ACTIVITY_COMPOSE = "androidx.activity:activity-compose"
    const val ACTIVITY_KTX = "androidx.activity:activity-ktx"
    const val ANNOTATION = "androidx.annotation:annotation"
    const val APPCOMPAT = "androidx.appcompat:appcompat:1.4.0"
    const val CONSTRAINTLAYOUT = "androidx.constraintlayout:constraintlayout:2.1.1"
    const val LEGACY_SUPPORT = "androidx.legacy:legacy-support-v4:1.0.0"
    const val PREFERENCE = "androidx.preference:preference:1.1.1"

    const val COMPOSE_ANIMATION = "androidx.compose.animation:animation"
    const val COMPOSE_MATERIAL = "androidx.compose.material:material"
    const val COMPOSE_RUNTIME = "androidx.compose.runtime:runtime"
    const val COMPOSE_THEME_ADAPTER = "com.google.android.material:compose-theme-adapter"
    const val COMPOSE_TOOLING = "androidx.compose.ui:ui-tooling"
    const val COMPOSE_UI = "androidx.compose.ui:ui"
    const val DATA_STORE_PREFERENCES = "androidx.datastore:datastore-preferences"

    const val LIFECYCLE_LIVEDATA_KTX = "androidx.lifecycle:lifecycle-livedata-ktx:$lifecycle_version"
    // Lifecycles only (without ViewModel or LiveData)
    const val LIFECYCLE_RUNTIME_KTX = "androidx.lifecycle:lifecycle-runtime-ktx"
    // ViewModel utilities for Compose
    const val LIFECYCLE_VIEWMODEL_COMPOSE = "androidx.lifecycle:lifecycle-viewmodel-compose:$lifecycle_version"
    const val LIFECYCLE_VIEWMODEL_KTX = "androidx.lifecycle:lifecycle-viewmodel-ktx:$lifecycle_version"
    // Saved state module for ViewModel
    const val LIFECYCLE_VIEWMODEL_SAVEDSTATE =
        "androidx.lifecycle:lifecycle-viewmodel-savedstate:$lifecycle_version"
    // Annotation processor - if using Java8, use the following instead of lifecycle-compiler
    const val LIFECYCLE_ANNOTATION = "androidx.lifecycle:lifecycle-common-java8:$lifecycle_version"

    const val NAVIGATION_FRAGMENT_KTX = "androidx.navigation:navigation-fragment-ktx:2.3.5"
    const val NAVIGATION_UI_KTX = "androidx.navigation:navigation-ui-ktx:2.3.5"

    const val ROOM_RUNTIME = "androidx.room:room-runtime:$room_version"
    const val ROOM_COMPILER = "androidx.room:room-compiler:$room_version"
    const val ROOM_KTX = "androidx.room:room-ktx:$room_version"
    const val ROOM_TESTING = "androidx.room:room-testing:$room_version"

    const val STARTUP = "androidx.startup:startup-runtime"
    const val WORK_RUNTIME_KTX = "androidx.work:work-runtime-ktx:2.7.1"
}

object Kotlin {
    const val COROUTINES_CORE = "org.jetbrains.kotlinx:kotlinx-coroutines-core:1.5.0"
    const val COROUTINES_ANDROID = "org.jetbrains.kotlinx:kotlinx-coroutines-android:1.6.0"
    const val COROUTINES_TEST = "org.jetbrains.kotlinx:kotlinx-coroutines-test:1.5.1"
    const val COROUTINES_PLAY_SERVICE = "org.jetbrains.kotlinx:kotlinx-coroutines-play-services"

    const val HILT_CORE = "com.google.dagger:hilt-core:2.40"
    const val HILT_ANDROID = "com.google.dagger:hilt-android:2.40"
    const val HILT_ANDROID_TESTING = "com.google.dagger:hilt-android-testing:2.41"
    const val HILT_ANDROID_COMPILER = "com.google.dagger:hilt-android-compiler:2.40"
    const val HILT_COMPILER = "com.google.dagger:hilt-compiler:2.40"
    const val HILT_ANDROIDX_COMPILER = "androidx.hilt:hilt-compiler:1.0.0-alpha03"
    const val HILT_ANDROIDX_LIFECYCLE_VIEWMODEL =
        "androidx.hilt:hilt-lifecycle-viewmodel:1.0.0-alpha03"

    const val HILT_ANDROIDX_WORK = "androidx.hilt:hilt-work"
    const val HILT_NAVIGATION_COMPOSE = "androidx.hilt:hilt-navigation-compose"

    const val KOTLIN_STDLIB = "org.jetbrains.kotlin:kotlin-stdlib-jdk8:1.6.10"
    const val KOTLIN_REFLECT = "org.jetbrains.kotlin:kotlin-reflect:1.6.10"
    const val KOTLIN_LOGGING = "io.github.microutils:kotlin-logging:1.8.3"
    const val KOTLINX_SERIALIZATION = "org.jetbrains.kotlinx:kotlinx-serialization-json"
}

object Libs {
    const val COMMONS_LANG3 = "org.apache.commons:commons-lang3:3.11"
    const val DESUGAR_JDK = "com.android.tools:desugar_jdk_libs:1.1.5"
    const val SLF4J = "org.slf4j:slf4j-simple:1.7.30"
    const val TIMBER = "com.jakewharton.timber:timber:5.0.1"
    const val CGLIB = "cglib:cglib:3.3.0"

    //Image
    const val GLIDE = "com.github.bumptech.glide:glide:4.13.1"

    //Web
    const val MOSHI = "com.squareup.moshi:moshi:1.9.3"
    const val MOSHI_KOTLIN = "com.squareup.moshi:moshi-kotlin:1.9.3"
    const val RETROFIT2_MOSHI_CONVERTER = "com.squareup.retrofit2:converter-moshi:2.9.0"
    const val RETROFIT2_KOTLIN_COROUTINES_ADAPTER =
        "com.jakewharton.retrofit:retrofit2-kotlin-coroutines-adapter:0.9.2"

    //Network
    const val NETTY = "io.netty:netty-all:4.1.75.Final"
}

object Testing {
    private const val arch_version = "2.1.0"

    const val JUNIT = "junit:junit:4.13.2"
    const val HAMCREST = "org.hamcrest:hamcrest-all:1.3"
    const val MOCKITO = "com.nhaarman:mockito-kotlin-kt1.1:1.5.0"
    const val MOCKK = "io.mockk:mockk:1.10.2"
    const val ROBOLECTRIC = "org.robolectric:robolectric:4.7.3"
    const val GOOGLE_TRUTH = "com.google.truth:truth:1.1.3"

    // Test helpers for LiveData
    const val ANDROIDX_CORE_TEST = "androidx.arch.core:core-testing:$arch_version"
    const val ANDROIDX_TEST_CORE = "androidx.test:core-ktx:1.4.0"
    const val ANDROIDX_TEST_MONITOR = "androidx.test:monitor:1.5.0"
    const val ANDROIDX_TEST_RUNNER = "androidx.test:runner:1.4.0"
    const val ANDROIDX_TEST_RULE = "androidx.test:rules:1.4.0"
    const val ANDROIDX_TEST_JUNIT = "androidx.test.ext:junit-ktx:1.1.3"
    const val ANDROIDX_TEST_TRUTH = "androidx.test.ext:truth:1.4.0"
    const val ANDROIDX_TEST_ESPRESSO = "androidx.test.espresso:espresso-core:3.4.0"
}