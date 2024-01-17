plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("com.google.devtools.ksp")
    id("dagger.hilt.android.plugin")
}

android {
    namespace = "com.abhicoding.pdfconverter"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.abhicoding.pdfconverter"
        minSdk = 29
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
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlinOptions {
        jvmTarget = "17"
    }
    buildFeatures {
        compose = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.8"
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}

dependencies {

    implementation("androidx.core:core-ktx:1.12.0")
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.7.0")
    implementation("androidx.activity:activity-compose:1.8.2")
    implementation(platform("androidx.compose:compose-bom:2023.08.00"))
    implementation("androidx.compose.ui:ui")
    implementation("androidx.compose.ui:ui-graphics")
    implementation("androidx.compose.ui:ui-tooling-preview")
    implementation("androidx.compose.material3:material3")
    implementation(platform("androidx.compose:compose-bom:2023.08.00"))
    implementation(platform("androidx.compose:compose-bom:2023.08.00"))
    implementation(platform("androidx.compose:compose-bom:2023.08.00"))
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
    androidTestImplementation(platform("androidx.compose:compose-bom:2023.08.00"))
    androidTestImplementation("androidx.compose.ui:ui-test-junit4")
    androidTestImplementation(platform("androidx.compose:compose-bom:2023.08.00"))
    androidTestImplementation(platform("androidx.compose:compose-bom:2023.08.00"))
    androidTestImplementation(platform("androidx.compose:compose-bom:2023.08.00"))
    debugImplementation("androidx.compose.ui:ui-tooling")
    debugImplementation("androidx.compose.ui:ui-test-manifest")

    // room
    ksp("androidx.room:room-compiler:2.6.1")
    ksp("androidx.hilt:hilt-navigation-compose:1.1.0")
    ksp("androidx.navigation:navigation-compose:2.7.6")
    ksp("androidx.lifecycle:lifecycle-viewmodel-compose:2.7.0")

    // material ripple, morphing button, material dialog, animations
    implementation ("com.airbnb.android:lottie-compose:6.3.0")
    implementation ("com.squareup.picasso:picasso:2.8")

    // https://mvnrepository.com/artifact/com.itextpdf/itext7-core
    implementation("com.itextpdf:itext7-core:8.0.2")
    implementation("com.madgag.spongycastle:core:1.58.0.0")
    implementation ("com.burhanrashid52:photoeditor:3.0.1")
    implementation("com.vanniktech:android-image-cropper:4.5.0")
    implementation ("com.journeyapps:zxing-android-embedded:4.3.0")
    implementation("io.coil-kt:coil-compose:2.5.0")


    // https://mvnrepository.com/artifact/org.apache.poi/poi
    implementation("org.apache.poi:poi:5.2.5")
    implementation ("com.github.bumptech.glide:glide:4.16.0")

    //scanning qr code
    implementation ("com.github.yuriy-budiyev:code-scanner:2.3.0")

    //android-pdf-viewer
    implementation ("com.github.barteksc:android-pdf-viewer:2.8.2")
    // color-picker
    implementation("com.github.skydoves:colorpickerview:2.3.0")

    implementation ("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.7.3")
    // Dagger Hilt
    implementation("com.google.dagger:hilt-android:2.50")
    ksp("androidx.hilt:hilt-compiler:1.1.0")
    ksp("com.google.dagger:hilt-android-compiler:2.50")
    implementation("androidx.work:work-runtime:2.9.0")
    implementation("androidx.hilt:hilt-navigation-compose:1.1.0")

}
