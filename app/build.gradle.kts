plugins {
    alias(libs.plugins.android.application)
}

android {
    namespace = "com.example.librarymangaement"
    compileSdk {
        version = release(37)
    }

    defaultConfig {
        applicationId = "com.example.librarymangaement"
        minSdk = 24
        targetSdk = 37
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            optimization {
                enable = false
            }
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
}

dependencies {
    implementation(libs.activity.ktx)
    implementation(libs.appcompat)
    implementation(libs.constraintlayout)
    implementation(libs.material)
    implementation(libs.recyclerview)
    implementation(libs.room.common)
    testImplementation(libs.junit)
    androidTestImplementation(libs.espresso.core)
    androidTestImplementation(libs.ext.junit)

    implementation("com.github.bumptech.glide:glide:4.16.0")


    annotationProcessor("com.github.bumptech.glide:compiler:4.16.0")


    implementation("com.squareup.retrofit2:retrofit:2.11.0")

    // Converter Factory (Choose based on your API response format)
    // Option A: GSON (Most popular for JSON)
    implementation("com.squareup.retrofit2:converter-gson:2.11.0")

    val roomVersion = "2.7.2"

    // Core Room runtime
    implementation("androidx.room:room-runtime:$roomVersion")

    // Annotation processor for Java
    annotationProcessor("androidx.room:room-compiler:$roomVersion")

    // Optional - Test helpers
    testImplementation("androidx.room:room-testing:$roomVersion")
}