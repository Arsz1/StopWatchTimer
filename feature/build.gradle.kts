@file:OptIn(ExperimentalWasmDsl::class)

import org.jetbrains.kotlin.gradle.ExperimentalWasmDsl

plugins {
    kotlin("multiplatform")
    id("com.android.kotlin.multiplatform.library")
    id("org.jetbrains.compose")
    id("org.jetbrains.kotlin.plugin.compose")
}

kotlin {
    jvm()
    js(IR) {
        browser()
    }
    wasmJs {
        browser()
    }
    iosX64()
    iosArm64()
    iosSimulatorArm64()

    androidLibrary {
        namespace = "com.example.timer.feature"
        compileSdk = 34
        minSdk = 21
    }

    sourceSets {
        val androidMain by getting {
            dependencies {
                implementation(project(":model"))
                implementation(project(":shared"))
                implementation(libs.androidx.activity.compose) // for ComponentActivity & setContent
                implementation(libs.androidx.ui)           // Compose core
                implementation(libs.androidx.material3) // Material3
                implementation(libs.androidx.ui.tooling.preview)
                // Koin for Android
                implementation(libs.koin.android)

                // Koin for Compose
                implementation(libs.insert.koin.koin.androidx.compose)
            }
        }
        val commonMain by getting {
            dependencies {
                implementation(project(":model"))
                implementation(project(":shared"))
                implementation(libs.compose.runtime)
                implementation(libs.compose.foundation)
                implementation(libs.compose.material3)
            }
        }
        val commonTest by getting {
            dependencies {
                implementation(libs.mockk.common) // Mocking library
                implementation(libs.kotlinx.coroutines.test)
                implementation(libs.kotlinx.coroutines.core.v164)
                implementation(kotlin("test"))
            }
        }
    }
}
