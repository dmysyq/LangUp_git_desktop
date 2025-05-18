plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.google.gms.google.services)
    id("com.google.firebase.crashlytics")
}

android {
    namespace = "com.example.langup"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.langup"
        minSdk = 26
        targetSdk = 34
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
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
        isCoreLibraryDesugaringEnabled = true
    }
}

dependencies {
    coreLibraryDesugaring("com.android.tools:desugar_jdk_libs:2.1.5")
    
    implementation(libs.appcompat)
    implementation(libs.material)
    implementation(libs.activity)
    implementation(libs.constraintlayout)
    implementation(libs.annotation)
    implementation(libs.gson)
    implementation(libs.flexbox)
    implementation(libs.jsoup)
    implementation(libs.glide)
    annotationProcessor(libs.compiler)

    // Firebase
    implementation(platform("com.google.firebase:firebase-bom:33.12.0"))
    implementation(libs.com.google.firebase.firebase.auth)
    implementation(libs.google.firebase.database)
    implementation(libs.google.firebase.firestore)
    implementation(libs.firebase.crashlytics)
    
    // Google Play Services
    implementation("com.google.android.gms:play-services-auth:21.3.0")
    implementation(libs.play.services.base)
    implementation(libs.play.services.tasks.v1810)
    
    // Stripe
    implementation("com.stripe:stripe-android:20.38.0")
    
    testImplementation(libs.junit)
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)
}
