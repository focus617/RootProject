plugins {
    `kotlin-dsl`
    kotlin("jvm") version "1.5.21"

    // Using the Plugin Development plugin for writing plugins
    id("java-gradle-plugin")
}

repositories {
    gradlePluginPortal()
    google()
    maven("https://mirrors.tencent.com/nexus/repository/maven-public/")
}

dependencies {
    implementation(gradleApi())
    implementation(kotlin("stdlib-jdk8"))
    implementation(kotlin("reflect"))
    implementation(gradleKotlinDsl())

    // Use JUnit test framework
    testImplementation("junit:junit:4.13.2")
    testImplementation("org.jetbrains.kotlin:kotlin-test:1.3.72")
    testImplementation("org.jetbrains.kotlin", "kotlin-test-junit", "1.3.72")
    testImplementation(gradleTestKit())
}

sourceSets {
    main{
        java.srcDirs("src/main/kotlin")
        }
    test{
        java.srcDirs("src/test/kotlin")
        }
}



group = "com.focus617"
version = "1.0.0"

gradlePlugin {

    plugins {
        register("BuildSrcPlugin") {
            id = "BuildSrcPlugin"
            implementationClass = ".buildSrcPlugin.BuildSrcPlugin"
        }
    }
}


