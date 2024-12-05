plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.google.gms.google.services)
    id("kotlin-kapt")
    id ("com.google.dagger.hilt.android")
    alias(libs.plugins.compose.compiler)
}

android {
    namespace = "com.capstone.surevenir"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.capstone.surevenir"
        minSdk = 24
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"
        buildConfigField("String", "BASE_URL", "\"http://10.0.2.2:3000/api/\"")
        buildConfigField("String", "MAPS_API_KEY", "\"AIzaSyD85fB5qwbsbMAAMu-7rhm52Ywtdy5WJvQ\"")
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
    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.10"
    }

    buildFeatures {
        compose = true
        buildConfig = true
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
    kapt {
        correctErrorTypes = true
    }
}

dependencies {
    implementation ("com.google.maps.android:maps-compose:2.1.0")
    implementation ("com.google.android.gms:play-services-maps:18.2.0")
    implementation ("com.google.accompanist:accompanist-placeholder-material:0.28.0")
    implementation ("com.squareup.retrofit2:converter-moshi:2.9.0")
    implementation ("com.google.dagger:hilt-android:2.52")
    implementation(libs.play.services.location)
    implementation(libs.androidx.camera.lifecycle)
    implementation(libs.androidx.camera.view)
    implementation (libs.androidx.camera.camera2)
    kapt ("com.google.dagger:hilt-compiler:2.52")
    implementation ("androidx.hilt:hilt-navigation-compose:1.0.0")
    implementation ("com.google.dagger:hilt-android:2.44")
    implementation ("com.squareup.retrofit2:retrofit:2.9.0")
    implementation ("com.squareup.retrofit2:converter-gson:2.9.0")
    implementation ("com.squareup.okhttp3:logging-interceptor:4.9.3")
    implementation ("io.coil-kt:coil-compose:2.4.0")
    implementation (libs.coil)
    implementation ("com.google.accompanist:accompanist-pager:0.30.1")
    implementation ("com.google.accompanist:accompanist-pager-indicators:0.30.1")
    implementation("com.airbnb.android:lottie-compose:6.0.0")
    implementation ("com.google.accompanist:accompanist-systemuicontroller:0.30.1")
    implementation ("androidx.lifecycle:lifecycle-runtime-ktx:2.8.7")
    implementation ("androidx.datastore:datastore-preferences:1.0.0")
    implementation(platform("com.google.firebase:firebase-bom:33.6.0"))
    implementation ("com.google.android.gms:play-services-auth:21.2.0")
    implementation ("com.google.firebase:firebase-auth-ktx")
    implementation(libs.androidx.material.icons.extended)
    implementation (libs.accompanist.pager)
    implementation (libs.androidx.material3)
    implementation (libs.accompanist.pager.indicators)
    implementation(libs.androidx.runtime.livedata)
    debugImplementation(libs.androidx.ui.tooling)
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.navigation.compose)
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.activity)
    implementation ("androidx.compose.foundation:foundation:1.4.0")
    implementation(libs.androidx.constraintlayout)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
}
