plugins {
    id("com.android.library")
    id("org.jetbrains.kotlin.android")
}

android {
    compileSdk = 32

    defaultConfig {
        minSdk = 24
        targetSdk = 32

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        consumerProguardFiles("consumer-rules.pro")

        externalNativeBuild {
            cmake {
                cppFlags("-std=c++11 -frtti -fexceptions")
                arguments("-DANDROID_ABI=arm64-v8a")
                arguments("-DANDROID_STL=c++_shared")
            }
        }

        ndk {
            // Specifies the ABI configurations of your native
            // libraries Gradle should build and package with your app.
            abiFilters += listOf("arm64-v8a")
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

    ndkVersion = "24.0.8215888"
    externalNativeBuild {
        cmake {
            path("src/main/cpp/CMakeLists.txt")
            version = "3.18.1"
        }
    }
    packagingOptions {
        jniLibs.pickFirsts.add("lib/arm64-v8a/libassimp.so")
    }

}

dependencies {
    implementation(Jetpack.ANDROIDX_CORE)
    implementation(Jetpack.APPCOMPAT)
    implementation(Jetpack.MATERIAL)

    testImplementation(Testing.JUNIT)
    androidTestImplementation(Testing.ANDROIDX_TEST_JUNIT)
    androidTestImplementation(Testing.ANDROIDX_TEST_ESPRESSO)
}