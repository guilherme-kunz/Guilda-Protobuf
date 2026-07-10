import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.hilt)
    alias(libs.plugins.ksp)
    alias(libs.plugins.protobuf)
}

android {
    namespace  = "com.guilda.protobuf"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.guilda.protobuf"
        minSdk        = 26
        targetSdk     = 35
        versionCode   = 1
        versionName   = "1.0"
    }

    buildFeatures {
        compose = true
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
}

// Substitui o kotlinOptions deprecated — API estável no Kotlin 2.x
kotlin {
    compilerOptions {
        jvmTarget.set(JvmTarget.JVM_17)
    }
}

protobuf {
    protoc {
        // Versão hardcoded: o plugin protobuf registra seu próprio `libs` na
        // ExtensionContainer, eclipsando o accessor do version catalog neste escopo.
        artifact = "com.google.protobuf:protoc:4.28.3"
    }
    generateProtoTasks {
        all().forEach { task ->
            task.builtins {
                // Gera classes Java com suporte lite (menor footprint para Android)
                create("java") {
                    option("lite")
                }
                // Gera extensões Kotlin (DSL builders, propriedades)
                create("kotlin")
            }
        }
    }
}

dependencies {
    val composeBom = platform(libs.compose.bom)
    implementation(composeBom)
    implementation(libs.compose.ui)
    implementation(libs.compose.ui.tooling.preview)
    implementation(libs.compose.material3)
    implementation(libs.compose.icons.core)
    implementation(libs.compose.activity)
    debugImplementation(libs.compose.ui.tooling)

    implementation(libs.hilt.android)
    ksp(libs.hilt.compiler)
    implementation(libs.hilt.navigation.compose)
    implementation(libs.navigation.compose)

    implementation(libs.lifecycle.viewmodel.compose)
    implementation(libs.lifecycle.runtime.compose)

    implementation(libs.retrofit)
    // converter-protobuf tem protobuf-java como dependência transitiva.
    // Excluímos para evitar conflito com protobuf-javalite no classpath.
    implementation(libs.retrofit.protobuf) {
        exclude(group = "com.google.protobuf", module = "protobuf-java")
    }
    implementation(libs.okhttp.logging)

    implementation(libs.protobuf.javalite)
    implementation(libs.protobuf.kotlin.lite)

    implementation(libs.material)
}
