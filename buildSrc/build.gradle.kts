
plugins {
    kotlin("jvm") version "1.4.0"
    `kotlin-dsl`
    application
}

application.mainClassName = "PluginTest"


dependencies{
    implementation(kotlin("stdlib-jdk8"))
}

repositories {
    jcenter()
}
