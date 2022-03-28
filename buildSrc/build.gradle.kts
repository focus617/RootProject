plugins {
    `kotlin-dsl`
}

repositories {
    gradlePluginPortal()
    google()
    maven("https://mirrors.tencent.com/nexus/repository/maven-public/")
}

dependencies {
    implementation(gradleApi())
    implementation(kotlin("stdlib-jdk8"))
}

sourceSets {
    main{
        java.srcDirs("src/main/kotlin")
        }
    test{
        java.srcDirs("src/test/kotlin")
        }
}



