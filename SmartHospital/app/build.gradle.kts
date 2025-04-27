plugins {
    alias(libs.plugins.android.application)
}

android {
    namespace = "com.example.smarthospital"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.example.smarthospital"
        minSdk = 24
        targetSdk = 35
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
}

dependencies {
    // Core AndroidX libraries
    implementation(libs.appcompat)
    implementation(libs.material)
    implementation(libs.activity)
    implementation(libs.constraintlayout)
    implementation(libs.recyclerview)

    // Room for SQLite
    implementation(libs.room.runtime)
    implementation(libs.room.ktx)
    annotationProcessor(libs.room.compiler)

    // Material Design (giữ phiên bản mới nhất)
//    implementation("com.google.android.material:material:1.11.0")
    implementation("com.google.android.material:material:1.12.0")
    // RecyclerView
    implementation("androidx.recyclerview:recyclerview:1.3.2")

    // ViewModel
    implementation("androidx.lifecycle:lifecycle-viewmodel:2.7.0")

    // JetBrains annotations
    implementation("org.jetbrains:annotations:23.0.0") {
        exclude(group = "com.intellij", module = "annotations")
    }

    // Testing libraries
    testImplementation(libs.junit)
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)
}
//plugins {
//    alias(libs.plugins.android.application)
//}
//
//android {
//    namespace = "com.example.smarthospital"
//    compileSdk = 35
//
//    defaultConfig {
//        applicationId = "com.example.smarthospital"
//        minSdk = 24
//        targetSdk = 35
//        versionCode = 1
//        versionName = "1.0"
//
//        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
//    }
//
//    buildTypes {
//        release {
//            isMinifyEnabled = false
//            proguardFiles(
//                getDefaultProguardFile("proguard-android-optimize.txt"),
//                "proguard-rules.pro"
//            )
//        }
//    }
//
//    compileOptions {
//        sourceCompatibility = JavaVersion.VERSION_11
//        targetCompatibility = JavaVersion.VERSION_11
//    }
//}
//
//dependencies {
//    // Core AndroidX libraries
//    implementation(libs.appcompat)
//    implementation(libs.material)
//    implementation(libs.activity)
//    implementation(libs.constraintlayout)
//    implementation(libs.recyclerview)
//
//    // Room for SQLite
//    implementation(libs.room.runtime)
//    implementation(libs.room.ktx) // Thêm room-ktx
//    annotationProcessor(libs.room.compiler)
//
//    //giao_diện
//
//    // Material Design
//    implementation ("com.google.android.material:material:1.11.0")
//
//    // RecyclerView
//    implementation("androidx.recyclerview:recyclerview:1.3.2")
//
//    // Nếu có ViewModel / LiveData (tuỳ kiến trúc)
//    implementation("androidx.lifecycle:lifecycle-viewmodel:2.7.0")
//
//    // JetBrains annotations with exclusion to resolve duplicate class issue
//    implementation("org.jetbrains:annotations:23.0.0") {
//        exclude(group = "com.intellij", module = "annotations")
//
//
//    }
//
//    // Testing libraries
//    testImplementation(libs.junit)
//    androidTestImplementation(libs.ext.junit)
//    androidTestImplementation(libs.espresso.core)
//}