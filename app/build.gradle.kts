plugins {
    // Aplica el complemento de aplicación Android.
    alias(libs.plugins.android.application)
}

// Configuración principal del módulo Android.
android {

    // Identificador del paquete de la aplicación.
    namespace = "com.example.logistics"

    // Versión del SDK utilizada para compilar.
    compileSdk = 37

    // Configuración general de la aplicación.
    defaultConfig {

        // Identificador único de LOGISTICS.
        applicationId = "com.example.logistics"

        // Versión mínima de Android compatible.
        minSdk = 24

        // Versión objetivo de Android.
        targetSdk = 37

        // Número interno de versión.
        versionCode = 1

        // Nombre visible de la versión.
        versionName = "1.0"

        // Configuración para pruebas instrumentadas.
        testInstrumentationRunner =
            "androidx.test.runner.AndroidJUnitRunner"
    }

    // Configuración de compilación para distribución.
    buildTypes {

        // Configuración de versión de producción.
        release {
            // Mantiene desactivada la reducción de código.
            isMinifyEnabled = false
        }

        // Configuración de desarrollo.
        debug {
            // Mantiene desactivada la reducción de código.
            isMinifyEnabled = false
        }
    }

    // Configuración de compatibilidad con Java.
    compileOptions {

        // Versión de Java utilizada para compilar.
        sourceCompatibility = JavaVersion.VERSION_11

        // Versión de Java utilizada para generar el código.
        targetCompatibility = JavaVersion.VERSION_11
    }
}

// Dependencias utilizadas por LOGISTICS.
dependencies {

    // Componentes de compatibilidad de Android.
    implementation(libs.appcompat)

    // Componentes para organizar las interfaces.
    implementation(libs.constraintlayout)

    // Componentes visuales de Material Design.
    implementation(libs.material)

    // Dependencia para pruebas unitarias.
    testImplementation(libs.junit)

    // Dependencias para pruebas instrumentadas.
    androidTestImplementation("androidx.test.ext:junit:1.2.1")

    // Dependencia para pruebas de interfaz.
    androidTestImplementation("androidx.test.espresso:espresso-core:3.6.1")
}
