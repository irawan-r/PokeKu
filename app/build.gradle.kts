plugins {
	id("com.android.application")
	id("org.jetbrains.kotlin.android")
	id("kotlin-kapt")
	id ("kotlin-parcelize")
	id("dagger.hilt.android.plugin")
}

android {
	namespace = "com.amora.pokeku"
	compileSdk = 34

	defaultConfig {
		applicationId = "com.amora.pokeku"
		minSdk = 24
		targetSdk = 34
		versionCode = 1
		versionName = "1.0"

		testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
		vectorDrawables {
			useSupportLibrary = true
		}
	}

	val baseUrl = "https://pokeapi.co/api/v2/"
	val baseImgUrl = "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/other/official-artwork/"

	defaultConfig {
		buildConfigField("String", "BASE_URL", "\"$baseUrl\"")
		buildConfigField("String", "BASE_IMG_URL", "\"$baseImgUrl\"")
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
		buildConfig = true
	}
	composeOptions {
		kotlinCompilerExtensionVersion = "1.4.3"
	}
	packaging {
		resources {
			excludes += "/META-INF/{AL2.0,LGPL2.1}"
		}
	}
}

dependencies {

	implementation("androidx.core:core-ktx:1.12.0")
	implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.6.2")
	implementation("androidx.activity:activity-compose:1.8.1")
	implementation(platform("androidx.compose:compose-bom:2023.03.00"))
	implementation("androidx.compose.ui:ui")
	implementation("androidx.compose.ui:ui-graphics")
	implementation("androidx.compose.ui:ui-tooling-preview")
	implementation("androidx.compose.material3:material3")
	testImplementation("junit:junit:4.13.2")
	androidTestImplementation("androidx.test.ext:junit:1.1.5")
	androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
	androidTestImplementation(platform("androidx.compose:compose-bom:2023.03.00"))
	androidTestImplementation("androidx.compose.ui:ui-test-junit4")
	debugImplementation("androidx.compose.ui:ui-tooling")
	debugImplementation("androidx.compose.ui:ui-test-manifest")

	// compose
	implementation("androidx.compose.ui:ui:1.5.4")
	implementation("androidx.activity:activity-compose:1.8.1")
	implementation("androidx.compose.material:material:1.5.4")
	implementation("androidx.compose.material:material-icons-extended:1.5.4")
	implementation("androidx.compose.foundation:foundation:1.5.4")
	implementation("androidx.compose.foundation:foundation-layout:1.5.4")
	implementation("androidx.compose.animation:animation:1.5.4")
	implementation("androidx.compose.runtime:runtime:1.5.4")
	implementation("androidx.navigation:navigation-compose:2.7.5")
	implementation("androidx.compose.ui:ui-tooling:1.5.4")
	implementation("androidx.constraintlayout:constraintlayout-compose:1.0.1")

	// material
	implementation("com.google.android.material:material:1.10.0")

	// accompanist
	implementation("com.google.accompanist:accompanist-systemuicontroller:0.28.0")

	// Hilt dagger
	implementation("com.google.dagger:hilt-android:2.48")
	implementation("androidx.hilt:hilt-navigation-compose:1.1.0")
	kapt("com.google.dagger:hilt-compiler:2.47")

	// architecture components
	implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.6.2")
	implementation("androidx.room:room-ktx:2.6.0")
	implementation("androidx.room:room-common:2.6.0")
	kapt("androidx.room:room-compiler:2.6.0")

	// compose image loading
	implementation("com.github.skydoves:landscapist-coil:2.1.2")
	implementation("com.github.skydoves:landscapist-animation:2.1.2")
	implementation("com.github.skydoves:landscapist-placeholder:2.1.2")
	implementation("com.github.skydoves:landscapist-palette:2.1.2")

	// landscapist
	implementation("com.github.skydoves:landscapist-palette:2.1.2")

	// ballooon
	implementation("com.github.skydoves:orchestra-balloon:1.2.0")

	// network
	implementation("com.github.skydoves:sandwich:1.3.5")
	implementation("com.squareup.retrofit2:retrofit:2.9.0")
	implementation("com.squareup.retrofit2:converter-moshi:2.9.0")
	implementation("com.squareup.okhttp3:logging-interceptor:5.0.0-alpha.6")
	implementation("com.squareup.moshi:moshi-kotlin:1.14.0")
	kapt("com.squareup.moshi:moshi-kotlin-codegen:1.14.0")

	// pagination
	implementation("androidx.paging:paging-runtime-ktx:3.2.1")
	implementation("androidx.room:room-paging:2.6.0")
	implementation ("androidx.paging:paging-compose:3.3.0-alpha02")


	// Timber
	implementation("com.jakewharton.timber:timber:5.0.1")

	// pallete
	implementation("androidx.palette:palette-ktx:1.0.0")
}