plugins {
    alias(libs.plugins.androidApplication)
    alias(libs.plugins.jetbrainsKotlinAndroid)
   // id("com.android.application")
    id("com.google.gms.google-services")
   id("com.google.android.libraries.mapsplatform.secrets-gradle-plugin") //ovo  me zezalo nesto, ne znam zasto

}

buildscript {
    dependencies {
        classpath("com.google.android.libraries.mapsplatform.secrets-gradle-plugin:secrets-gradle-plugin:2.0.1")
    }
}

android {
    namespace = "com.example.routeexplorer2"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.routeexplorer2"
        minSdk = 24
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }
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
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
    buildFeatures {
        compose = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.1"
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
//    implementation(libs.androidx.material3) //ovo se koristilo dok nisam ubacio ispod, ako nesto zeza, onda mozda koristiti ovaj
    implementation("androidx.compose.material3:material3-android:1.2.0-rc01")//ovo da bi radio circularprogress indicator
    implementation(libs.androidx.constraintlayout.compose)

    implementation("androidx.compose.material:material-icons-extended:1.5.0")
//    implementation (androidx.compose.material:material-icons-extended:$compose_ui_version) nece kod mene 26:00

    // Import the Firebase BoM
//    implementation(platform("com.google.firebase:firebase-bom:33.2.0"))
    implementation(platform("com.google.firebase:firebase-bom:33.2.0"))

    // When using the BoM, you don't specify versions in Firebase library dependencies

    // Add the dependency for the Firebase SDK for Google Analytics
    implementation("com.google.firebase:firebase-auth-ktx")
    implementation(libs.firebase.firestore)
//    implementation(libs.firebase.auth.ktx)
    implementation("com.google.firebase:firebase-storage-ktx")
    implementation(libs.androidx.navigation.compose)
    implementation(libs.firebase.storage)
    implementation(libs.play.services.cast.framework)//naknadno sam dodao
//    implementation(libs.firebase.storage.ktx)

    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)

    implementation("androidx.lifecycle:lifecycle-viewmodel-compose:2.7.0")
    implementation("com.google.maps.android:maps-compose:4.4.1")
    implementation ("com.google.android.gms:play-services-maps:18.2.0")
    implementation ("com.google.android.gms:play-services-location:21.2.0")

    //implementation (libs.coil.compose)
    implementation("io.coil-kt:coil-compose:2.4.0")

}