@file:Suppress("UnstableApiUsage")

include(":harlequin")
include(":harlequin-ashley")

dependencyResolutionManagement {
    versionCatalogs { create("libs") { from(files("libs.versions.toml")) } }
}
