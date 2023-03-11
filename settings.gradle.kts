@file:Suppress("UnstableApiUsage")

include(":harlequin")

dependencyResolutionManagement {
    versionCatalogs { create("libs") { from(files("libs.versions.toml")) } }
}
