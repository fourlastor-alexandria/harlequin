@Suppress(
    // known false positive: https://youtrack.jetbrains.com/issue/KTIJ-19369
    "DSL_SCOPE_VIOLATION"
)
plugins {
    `java-library`
    `maven-publish`
    signing
    alias(libs.plugins.spotless)
}

java.sourceCompatibility = JavaVersion.VERSION_1_8
java.targetCompatibility = JavaVersion.VERSION_1_8

spotless {
    isEnforceCheck = false
    java {
        palantirJavaFormat()
    }
}

java {
    withJavadocJar()
    withSourcesJar()
}

dependencies {
    api(libs.gdx.core)
}

publishing {
    publications {
        create<MavenPublication>("mavenJava") {
            artifactId = "harlequin"
            from(components["java"])
            pom {
                name.set("Harlequin")
                description.set("A set of extensions for libGDX's scene2d")
                url.set("https://www.github.com/fourlastor/harlequin")
                licenses {
                    license {
                        name.set("MIT License")
                        url.set("https://github.com/fourlastor/harlequin/blob/main/LICENSE")
                    }
                }
                developers {
                    developer {
                        id.set("fourlastor")
                        name.set("Daniele Conti")
                    }
                }
                scm {
                    connection.set("scm:git:https://www.github.com/fourlastor/harlequin.git")
                    developerConnection.set("scm:git:https://www.github.com/fourlastor/harlequin.git")
                    url.set("https://www.github.com/fourlastor/harlequin")
                }
            }
        }
    }
}

signing {
    val signingKey: String? by project
    val signingPassword: String? by project
    useInMemoryPgpKeys(signingKey, signingPassword)
    sign(publishing.publications["mavenJava"])
}
