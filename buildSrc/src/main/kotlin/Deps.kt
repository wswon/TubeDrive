object Deps {
    object Android {
        object X {
            const val core = "androidx.core:core-ktx:${Versions.coreVersion}"
            const val activity = "androidx.activity:activity-ktx:${Versions.activityVersion}"
            const val fragment = "androidx.fragment:fragment-ktx:${Versions.fragmentVersion}"
            const val appCompat = "androidx.appcompat:appcompat:${Versions.appCompatVersion}"
            const val constraintLayout = "androidx.constraintlayout:constraintlayout:${Versions.constraintLayoutVersion}"
            const val recyclerView = "androidx.recyclerview:recyclerview:${Versions.recyclerViewVersion}"
        }
        const val material = "com.google.android.material:material:${Versions.materialVersion}"
    }

    object Firebase {
        const val crashlytics = "com.google.firebase:firebase-crashlytics:${Versions.Firebase.crashlyticsVersion}"
    }

    object Kakao {
    }

    object Rx {
        const val rxAndroid = "io.reactivex.rxjava3:rxandroid:${Versions.rxAndroidVersion}"
        const val rxJava = "io.reactivex.rxjava3:rxkotlin:${Versions.rxJavaVersion}"
    }

    object Network {
        const val retrofit = "com.squareup.retrofit2:retrofit:${Versions.retrofitVersion}"
        const val gson = "com.squareup.retrofit2:converter-gson:${Versions.retrofitVersion}"
        const val rxAdapter = "com.squareup.retrofit2:adapter-rxjava3:${Versions.rxAdapterVersion}"
    }

    const val glide = "com.github.bumptech.glide:glide:${Versions.glideVersion}"

    object Test {
        const val archCore = "androidx.arch.core:core-testing:${Versions.Test.coreTestingVersion}"
        const val mockk = "io.mockk:mockk:${Versions.Test.mockkVersion}"
    }

    object Hilt {
        const val hilt = "com.google.dagger:hilt-android:${Versions.hiltVersion}"
        const val hiltCompiler = "com.google.dagger:hilt-android-compiler:${Versions.hiltVersion}"
    }
}