import org.jetbrains.kotlin.gradle.plugin.*

plugins {
    kotlin("jvm") version "1.3.31"
}

repositories {
    jcenter()
}

val coroutinesVersion = "1.2.1"
val retrofitVersion = "2.5.0"
val jacksonModuleKotlinVersion = "2.9.8"
val logbackVersion = "1.1.3"

dependencies {
    implementation(kotlin("stdlib-jdk8"))
    implementation(kotlin("reflect"))
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:$coroutinesVersion")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-swing:$coroutinesVersion")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-jdk8:$coroutinesVersion")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-slf4j:$coroutinesVersion")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-debug:$coroutinesVersion")
    implementation("com.squareup.retrofit2:retrofit:$retrofitVersion")
    implementation("com.squareup.retrofit2:converter-jackson:$retrofitVersion")
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin:$jacksonModuleKotlinVersion")
    implementation("ch.qos.logback:logback-classic:$logbackVersion")
}

sourceSets["main"].apply {
    resources.srcDir("resources")
    withConvention(KotlinSourceSet::class) {
        kotlin.srcDir("src")
    }
}
