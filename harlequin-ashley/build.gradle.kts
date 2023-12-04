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
    implementation(project(":harlequin"))
    api(libs.ashley)
    api(libs.gdx.core)
}

publishing {
    publications {
        create<MavenPublication>("mavenJava") {
            artifactId = "harlequin-ashley"
            from(components["java"])
            pom {
                name.set("Harlequin-Ashley")
                description.set("Ashley-specific extension for Harlequin")
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
