import org.jetbrains.kotlin.gradle.plugin.KotlinDependencyHandler

plugins {
    kotlin("multiplatform")
    kotlin("plugin.serialization") version ("1.4-M1")
    id("com.android.library")
    id("maven-publish")
}

fun KotlinDependencyHandler.asoftLibs(platform: String) {
    api(asoftBasics("networking", platform))
    api(asoftBasics("sms-gateway", platform))
}

android {
    configureAndroid()
}

kotlin {
    android {
        compilations.all {
            kotlinOptions { jvmTarget = "1.8" }
        }
        publishLibraryVariants("release")
    }

    jvm {
        compilations.all {
            kotlinOptions { jvmTarget = "1.8" }
        }
    }

    js {
        compilations.all {
            kotlinOptions {
                metaInfo = true
                sourceMap = true
                moduleKind = "commonjs"
            }
        }
    }

    sourceSets {
        val commonMain by getting {
            dependencies {
                implementation(kotlin("stdlib-common"))
                asoftLibs("metadata")
            }
        }

        val commonTest by getting {
            dependencies {
                implementation(asoftTest("metadata"))
            }
        }

        val androidMain by getting {
            dependencies {
                implementation(kotlin("stdlib"))
                asoftLibs("android")
            }
        }

        val androidTest by getting {
            dependencies {
                implementation(asoftTest("android"))
            }
        }

        val jvmMain by getting {
            dependencies {
                implementation(kotlin("stdlib"))
                asoftLibs("jvm")
            }
        }

        val jvmTest by getting {
            dependencies {
                implementation(asoftTest("jvm"))
            }
        }
        val jsMain by getting {
            dependencies {
                implementation(kotlin("stdlib-js"))
                asoftLibs("js")
            }
        }
        val jsTest by getting {
            dependencies {
                implementation(asoftTest("js"))
            }
        }
    }
}