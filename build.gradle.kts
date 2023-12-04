plugins {
    alias(libs.plugins.nexus.publish)
}

buildscript {
    repositories {
        mavenLocal()
        mavenCentral()
        gradlePluginPortal()
    }
}

val libVersion: String by project
val publishGroup = "io.github.fourlastor.gdx"

allprojects {
    repositories {
        mavenLocal()
        mavenCentral()
        gradlePluginPortal()
    }
    version = libVersion
    group = publishGroup
}

group = publishGroup
version = libVersion

nexusPublishing {
    repositories {
        sonatype()
    }
}
