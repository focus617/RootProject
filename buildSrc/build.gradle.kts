//第三方插件
plugins {

    `kotlin-dsl`

    //kotlin("jvm") version "1.3.72"
    id("org.jetbrains.kotlin.jvm") version "1.3.72"

    // Apply the java-library plugin to add support for Java Library
    `java-library`

    // Using the Plugin Development plugin for writing plugins
    id("java-gradle-plugin")

    // used for distribution
    `maven-publish`

}

kotlinDslPluginOptions {
    experimentalWarning.set(false)
}


//设置项目的依赖库
dependencies {
    implementation(gradleApi())
    implementation(kotlin("stdlib-jdk8"))
    implementation(kotlin("reflect"))
    implementation(gradleKotlinDsl())

    testImplementation(gradleTestKit())
    // Use JUnit test framework
    testImplementation("junit:junit:4.13")
    testImplementation("org.jetbrains.kotlin:kotlin-test:1.3.72")
    testImplementation("org.jetbrains.kotlin", "kotlin-test-junit", "1.3.72")
}

//指定Jar中央仓库
repositories {
    // 阿里云的国内镜像仓库
    maven(url="http://maven.aliyun.com/nexus/content/groups/public/")

    // Use jcenter for resolving dependencies.
    jcenter()
    mavenLocal()
    // You can declare any Maven/Ivy/file repository here.

}

apply(from = "gradle/integration-test.gradle.kts")

sourceSets {
    test {
        java {
            srcDirs("src/test/kotlin")
        }
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


