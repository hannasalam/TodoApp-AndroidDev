// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    alias(libs.plugins.androidApplication) apply false
    alias(libs.plugins.jetbrainsKotlinAndroid) apply false
    //id("com.google.devtools.ksp") version "1.8.10-1.0.9" apply false
    //kotlin("jvm") version "1.9.21" apply false
}

/*
buildscript {
    dependencies {
        classpath(kotlin("gradle-plugin", version = "1.9.21"))
    }
}*/
