
//第三方插件
plugins {
    kotlin("jvm") version "1.3.72"
    `kotlin-dsl`

}


//设置项目的依赖库
dependencies{
    implementation(kotlin("stdlib-jdk8"))

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
