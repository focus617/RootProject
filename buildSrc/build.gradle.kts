
//第三方插件
plugins {

    `kotlin-dsl`

    //kotlin("jvm") version "1.3.72"
    id("org.jetbrains.kotlin.jvm") version "1.3.72"

    // Apply the java-library plugin to add support for Java Library
    `java-library`

    // Using the Plugin Development plugin for writing plugins
    id("java-gradle-plugin")

}



kotlinDslPluginOptions {
    experimentalWarning.set(false)
}


//设置项目的依赖库
dependencies{
    implementation(gradleApi())
    implementation(kotlin("stdlib-jdk8"))

    testImplementation(kotlin("stdlib-jdk8"))
    testImplementation(gradleApi())
    testImplementation(gradleKotlinDsl())
    testImplementation(gradleTestKit())
    // Use JUnit test framework
    testImplementation("junit:junit:4.13")
    testImplementation("org.jetbrains.kotlin:kotlin-test:1.3.72")
    testImplementation("org.jetbrains.kotlin:kotlin-test-junit:1.3.72")

}

//指定Jar中央仓库
repositories {
    // Use jcenter for resolving dependencies.
    // You can declare any Maven/Ivy/file repository here.
    jcenter()
}

apply(from = "gradle/integration-test.gradle.kts")
