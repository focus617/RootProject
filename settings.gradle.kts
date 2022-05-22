pluginManagement {
    repositories {
        gradlePluginPortal()
        google()
        mavenCentral()
    }
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
    }
}
rootProject.name = "XApp"
include(":app_tankwar")
include(":app_bookreader")
include(":login")
include(":core")
include(":myAndroidLibrary")
include(":myLib")
include(":app_demo")
include(":nativelib")

//include(":Fleks")         无法作为Submodule，直接拷贝到Core
