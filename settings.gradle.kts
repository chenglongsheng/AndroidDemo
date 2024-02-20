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
rootProject.name = "android-note"
include(":app")
include(":widget")
include(":multiprocess")
include(":message")
include(":wanandroid")
include(":wanandroid:app")
include(":shimmer")
include(":shimmer:shimmer")
include(":shimmer:sample")
