plugins {
    id("com.android.application")
    id("kotlin-android")
    id("kotlin-kapt")
    id("kotlin-parcelize")
    id("com.google.firebase.crashlytics")
    id("dagger.hilt.android.plugin")
}

android {
    compileSdkVersion(Versions.Android.compileSdkVersion)
    buildToolsVersion("29.0.3")

    defaultConfig {
        applicationId = "com.tube.driver"
        minSdkVersion(Versions.Android.minSdkVersion)
        targetSdkVersion(Versions.Android.targetSdkVersion)
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildFeatures {
        viewBinding = true
    }

    buildTypes {
        getByName("release") {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
}

dependencies {
    implementation(fileTree(mapOf("include" to listOf("*.jar"), "dir" to "libs")))
    implementation(files("libs/libDaumMapAndroid.jar"))

    implementation("org.jetbrains.kotlin:kotlin-stdlib:${Versions.kotlinVersion}")
    implementation(Deps.Firebase.crashlytics)

    implementation(Deps.Android.X.core)
    implementation(Deps.Android.X.activity)
    implementation(Deps.Android.X.fragment)
    implementation(Deps.Android.X.appCompat)
    implementation(Deps.Android.X.constraintLayout)
    implementation(Deps.Android.X.recyclerView)

    implementation(Deps.Android.material)

    implementation(Deps.glide)
    implementation(Deps.Rx.rxAndroid)
    implementation(Deps.Rx.rxJava)

    implementation(Deps.Network.retrofit)
    implementation(Deps.Network.gson)
    implementation(Deps.Network.rxAdapter)

    implementation(Deps.Hilt.hilt)
    kapt(Deps.Hilt.hiltCompiler)

    testImplementation(Deps.Test.archCore)
    testImplementation(Deps.Test.mockk)

    implementation("com.squareup.okhttp3:logging-interceptor:4.9.0")
}