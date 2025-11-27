plugins {
    id("com.android.application")
    id("com.google.gms.google-services")
}

android {
    namespace = "com.whasupp.app"
    compileSdk = 34 // Usamos 34 para máxima estabilidad

    defaultConfig {
        applicationId = "com.whasupp.app"
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

    // Configuración obligatoria de Java 8
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }

    buildFeatures {
        viewBinding = true
    }
}

dependencies {
    // --- 1. Android Core & UI ---
    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("com.google.android.material:material:1.11.0")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")

    // --- 2. FIREBASE (Plataforma BOM) ---
    implementation(platform("com.google.firebase:firebase-bom:32.7.0"))
    implementation("com.google.firebase:firebase-analytics")
    implementation("com.google.firebase:firebase-auth")
    implementation("com.google.firebase:firebase-database")

    // --- 3. Google Sign-In (Para el Token) ---
    implementation("com.google.android.gms:play-services-auth:20.7.0")

    // --- 4. MQTT (Cliente Paho) ---
    implementation("org.eclipse.paho:org.eclipse.paho.client.mqttv3:1.2.5")
    implementation("org.eclipse.paho:org.eclipse.paho.android.service:1.1.1")

    // --- 5. UI Extras (Imagen Circular) ---
    implementation("de.hdodenhof:circleimageview:3.1.0")

    // --- 6. Arquitectura MVVM (Lifecycle) ---
    implementation("androidx.lifecycle:lifecycle-viewmodel:2.6.2")
    implementation("androidx.lifecycle:lifecycle-livedata:2.6.2")

    // AGREGAR ESTA LÍNEA PARA LAS IMÁGENES:
    implementation("com.github.bumptech.glide:glide:4.16.0")

    // Testing (Necesario mantenerlo para evitar errores de carpetas vacías)
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
}