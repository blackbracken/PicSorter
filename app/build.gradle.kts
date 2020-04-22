plugins {
    id("com.android.application")
    kotlin("android")
    kotlin("android.extensions")
    kotlin("kapt")
    id("kotlin-android")
    id("kotlin-android-extensions")
    id("kotlin-kapt")
}

val versionKotlin: String by project

android {
    compileSdkVersion(28)
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    defaultConfig {
        applicationId = "black.bracken.picsorter"
        minSdkVersion(26)
        targetSdkVersion(28)
        versionCode = 5
        versionName = "1.2.1"
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
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
    dataBinding {
        isEnabled = true
    }
}

tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile> {
    kotlinOptions {
        jvmTarget = "1.8"
    }
}

dependencies {
    implementation(fileTree(mapOf("dir" to "libs", "include" to listOf("*.jar"))))

    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8:$versionKotlin")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.3.3")
    implementation("androidx.appcompat:appcompat:1.2.0-beta01")
    implementation("androidx.constraintlayout:constraintlayout:2.0.0-beta4")
    implementation("androidx.legacy:legacy-support-v4:1.0.0")
    implementation("androidx.lifecycle:lifecycle-extensions:2.2.0")
    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:2.2.0")
    implementation("androidx.lifecycle:lifecycle-livedata-ktx:2.2.0")
    implementation("androidx.lifecycle:lifecycle-viewmodel-savedstate:2.2.0")
    implementation("androidx.lifecycle:lifecycle-common-java8:2.2.0")

    testImplementation("junit:junit:4.12")
    androidTestImplementation("androidx.test:runner:1.2.0")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.2.0")
    implementation("androidx.recyclerview:recyclerview:1.2.0-alpha02")

    implementation("androidx.navigation:navigation-fragment-ktx:2.3.0-alpha05")
    implementation("androidx.navigation:navigation-ui:2.3.0-alpha05")
    implementation("androidx.navigation:navigation-dynamic-features-fragment:2.3.0-alpha05")

    implementation("androidx.room:room-runtime:2.2.5")
    kapt("androidx.room:room-compiler:2.2.5")
    implementation("androidx.room:room-ktx:2.2.5")

    implementation("org.koin:koin-core:2.1.5")
    implementation("org.koin:koin-core-ext:2.1.5")
    testImplementation("org.koin:koin-test:2.1.5")
    implementation("org.koin:koin-android:2.1.5")
    implementation("org.koin:koin-android-scope:2.1.5")
    implementation("org.koin:koin-android-viewmodel:2.1.5")
    implementation("org.koin:koin-android-ext:2.1.5")
    implementation("org.koin:koin-androidx-scope:2.1.5")
    implementation("org.koin:koin-androidx-viewmodel:2.1.5")
    implementation("org.koin:koin-androidx-fragment:2.1.5")
    implementation("org.koin:koin-androidx-ext:2.1.5")

    implementation("io.coil-kt:coil:0.9.5")

    implementation("com.xwray:groupie:2.7.0")
    implementation("com.xwray:groupie-kotlin-android-extensions:2.7.0")
    implementation("com.xwray:groupie-databinding:2.7.0")

    implementation("com.afollestad.material-dialogs:core:3.2.1")
    implementation("com.afollestad.material-dialogs:files:3.2.1")
}
