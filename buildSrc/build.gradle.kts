//第三方插件
plugins {
    kotlin("jvm") version "1.4.0"
    `kotlin-dsl`
    application
}

application.mainClassName = "PluginTest"

//设置项目的依赖库
dependencies{
    implementation(kotlin("stdlib-jdk8"))
}

//指定Jar中央仓库
repositories {
    jcenter()
}
