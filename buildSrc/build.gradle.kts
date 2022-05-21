plugins {
    `kotlin-dsl`
    kotlin("jvm") version "1.5.31"

    // Using the Plugin Development plugin for writing plugins
    id("java-gradle-plugin")

    // used for distribution
    `maven-publish`
}

repositories {
    gradlePluginPortal()
    google()
    maven("https://mirrors.tencent.com/nexus/repository/maven-public/")

    // You can declare any Maven/Ivy/file repository here.
    // mavenLocal()
}

dependencies {
    implementation(gradleApi())
    implementation(kotlin("stdlib-jdk8"))
    implementation(kotlin("reflect"))
    implementation(gradleKotlinDsl())

    // Git
    implementation("org.eclipse.jgit:org.eclipse.jgit:5.6.1.202002131546-r")
    implementation("com.jcraft:jsch.agentproxy.jsch:0.0.9")
    implementation("com.jcraft:jsch.agentproxy.usocket-jna:0.0.9")
    implementation("com.jcraft:jsch.agentproxy.sshagent:0.0.9")

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

publishing {
    publications.create<MavenPublication>("mavenJava") {
        artifactId = project.name
        from(components["java"])
    }

    repositories {
        maven {
            url = uri("$buildDir/repository")
        }

        // 打包上传到本地
        flatDir {
            dirs("../repo/")
        }
    }
}
