import java.util.Properties

plugins {
    // android
    alias(libs.plugins.android.library)
    // jetbrains
    alias(libs.plugins.jetbrains.kotlin.compose)
    // maven
    alias(libs.plugins.maven.publish)
    // security
    id("signing")
}

android {

    namespace = "ir.farsroidx.overscroll"

    compileSdk { version = release(37) }

    defaultConfig {
        minSdk = 23
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        consumerProguardFiles("consumer-rules.pro")
    }

    buildTypes { release { isMinifyEnabled = false } }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }

    publishing {

        singleVariant("release") {
            withSourcesJar()
            withJavadocJar()
        }
    }
}

kotlin {

    jvmToolchain(17)

    compilerOptions {
        freeCompilerArgs.addAll("-Xannotation-default-target=param-property")
    }
}

dependencies {

    // Compose
    implementation( platform( libs.compose.bom ) )
    implementation(libs.bundles.compose)

    // JUnit and Test
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
}

afterEvaluate {

    publishing {

        publications {

            register<MavenPublication>("release") {

                groupId    = "ir.farsroidx"
                artifactId = "compose-overscroll"
                version    = "1.0.0"

                from( components["release"] )

                pom {

                    this.name.set("Compose Overscroll")

                    this.description.set("A smooth elastic overscroll effect for Jetpack Compose lists, scroll and lazy layouts.")

                    this.url.set("https://github.com/farsroidx/ComposeElasticOverscroll")

                    this.licenses {

                        this.license {

                            this.name.set("The Apache Software License, Version 2.0")

                            this.url.set("http://www.apache.org/licenses/LICENSE-2.0.txt")

                        }
                    }

                    this.developers {

                        this.developer {

                            this.id.set("farsroidx")

                            this.name.set("Mohammad ali Riazati")

                            this.email.set("farsroidx@gmail.com")

                        }
                    }

                    this.scm {
                        this.url.set("https://github.com/farsroidx/ComposeElasticOverscroll")
                        this.connection.set("scm:git:git://github.com/farsroidx/ComposeElasticOverscroll.git")
                        this.developerConnection.set("scm:git:ssh://github.com/farsroidx/ComposeElasticOverscroll.git")
                    }
                }
            }
        }
    }

    signing {

        isRequired = gradle.taskGraph.hasTask("publish")

        val filePath = findLocalProperty(key = "signing.filePath")
        val password = findLocalProperty(key = "signing.password")

        if (!filePath.isNullOrBlank() && !password.isNullOrBlank()) {

            val signingFile = project.file(filePath)

            if (signingFile.exists()) {

                useInMemoryPgpKeys(signingFile.readText(), password)

                sign(publishing.publications)
            }

        } else logger.lifecycle("Signing skipped (no signing config found)")
    }
}

private var localProperties: Properties? = null

fun Project.findLocalProperty(key: String, file: String = "local.properties"): String? {

    if (localProperties == null) {

        localProperties = this.rootDir
            .resolve(file)
            .takeIf { it.exists() }
            ?.inputStream()
            ?.use { Properties().apply { load(it) } }
    }

    return localProperties?.getProperty(key) ?: this.findProperty(key) as? String
}