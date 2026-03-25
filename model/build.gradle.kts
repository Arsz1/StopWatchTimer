@file:OptIn(ExperimentalWasmDsl::class)

import org.jetbrains.kotlin.gradle.ExperimentalWasmDsl

plugins {
    kotlin("multiplatform")
    id("com.android.kotlin.multiplatform.library")
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
        namespace = "com.example.timer.model"
        //noinspection GradleDependency
        compileSdk = 34
        minSdk = 21
    }

    sourceSets {
        val commonMain by getting {
            dependencies {
                implementation(kotlin("stdlib"))
                implementation(libs.kotlinx.coroutines.core)
            }
        }
        val commonTest by getting {
            dependencies {
                implementation(kotlin("test"))
                implementation(libs.kotlinx.coroutines.test)
                implementation(libs.kotlinx.coroutines.core)
            }
        }
    }
}
