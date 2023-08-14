plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
}

android {
    namespace = "com.vecentek.decorelinkdemo"
    compileSdk = 33

    defaultConfig {
        applicationId = "com.vecentek.decorelinkdemo"
        minSdk = 24
        targetSdk = 33
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        externalNativeBuild {
            ndk {
                abiFilters += setOf("arm64-v8a")
            }
        }
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.4.0"
    }
    buildFeatures {
        // 启用 Jetpack Compose
        compose = true
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
    sourceSets["main"].jniLibs.srcDirs("libs")

}

dependencies {
    implementation ("androidx.core:core-ktx:1.8.0")
    implementation ("androidx.appcompat:appcompat:1.4.1")
    implementation ("com.google.android.material:material:1.5.0")
    implementation ("androidx.constraintlayout:constraintlayout:2.1.3")
    implementation (files("libs\\DecoreLink_v1.0.5.jar"))
    testImplementation ("junit:junit:4.13.2")
    androidTestImplementation ("androidx.test.ext:junit:1.1.3")
    androidTestImplementation ("androidx.test.espresso:espresso-core:3.4.0")

    // 权限请求框架：https://github.com/getActivity/XXPermissions
    implementation ("com.github.getActivity:XXPermissions:16.6")
    implementation ("com.squareup.okhttp3:okhttp:4.9.1")

    // Integration with activities
    implementation ("androidx.activity:activity-compose:1.3.1")
    // Compose Material Design
    implementation ("androidx.compose.material:material:1.0.1")
    // Animations
    implementation ("androidx.compose.animation:animation:1.0.1")
    // Tooling support (Previews, etc.)
    implementation ("androidx.compose.ui:ui-tooling:1.0.1")
    // Integration with ViewModels
    implementation ("androidx.lifecycle:lifecycle-viewmodel-compose:1.0.0-alpha07")
    // UI Tests
    androidTestImplementation ("androidx.compose.ui:ui-test-junit4:1.0.1")

}