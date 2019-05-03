import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import org.openapitools.codegen.DefaultGenerator
import org.openapitools.codegen.config.CodegenConfigurator

group = "fr.behaska.kotlin.examples"
version = "1.0-SNAPSHOT"


buildscript {

    val kotlinVersion: String by extra { "1.3.30" }
    val junitPlatformVersion: String by extra { "1.2.0" }

    repositories {
        jcenter()
    }

    dependencies {
        classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:$kotlinVersion")
        classpath("org.junit.platform:junit-platform-gradle-plugin:$junitPlatformVersion")
        classpath("org.openapitools:openapi-generator:3.3.4")
    }
}

plugins {
    application
    java
    kotlin("jvm") version "1.3.30"
}

application {
    mainClassName = "fr.behaska.kotlin.examples.jacksonmarshalling.Main"
}

dependencies {
    val kotlintestVersion = "5.4.0"
    val logbackVersion = "1.2.3"
    val ktorVersion = "1.1.4"
    val jacksonVersion = "2.9.8"

    fun ktor(module: String) = "io.ktor:ktor-$module:$ktorVersion"
    fun ktor() = "io.ktor:ktor:$ktorVersion"

    //compile(kotlin("stdlib"))
    compile(kotlin("stdlib-jdk8"))

    compile("com.fasterxml.jackson.datatype:jackson-datatype-jdk8:$jacksonVersion")
    compile("com.fasterxml.jackson.datatype:jackson-datatype-jsr310:$jacksonVersion")
    compile("com.fasterxml.jackson.module:jackson-module-kotlin:$jacksonVersion")
    compile("com.fasterxml.jackson.dataformat:jackson-dataformat-yaml:$jacksonVersion")

    compile("ch.qos.logback:logback-classic:$logbackVersion")

    implementation("com.squareup.moshi:moshi-kotlin:1.8.0") // nécessaire pour les classes générées par swagger

    testImplementation("org.junit.jupiter:junit-jupiter:$kotlintestVersion")
}

repositories {
    jcenter()
}

java {
    sourceCompatibility = JavaVersion.VERSION_1_8
    targetCompatibility = JavaVersion.VERSION_1_8
}

val compileKotlin by tasks.getting(KotlinCompile::class) {
    // Customise the “compileKotlin” task.
    kotlinOptions.jvmTarget = "1.8"
    kotlinOptions.freeCompilerArgs = listOf("-Xuse-experimental=kotlin.Experimental")
    dependsOn("generateSwaggerSources")
    doLast { println("Finished compiling Kotlin source code") }
}

kotlin.sourceSets["main"].kotlin.srcDirs("src","build/generated-sources/swagger/src/main/kotlin")
kotlin.sourceSets["test"].kotlin.srcDirs("test")

sourceSets["main"].resources.srcDirs("resources")
sourceSets["test"].resources.srcDirs("testresources")

val generateSwaggerSources = task("generateSwaggerSources") {

    description = "Generates sources from Swagger."

    val output = "$buildDir/generated-sources/swagger"

    outputs.dir(output)

    doLast {
        with(CodegenConfigurator()) {
            inputSpec = "$projectDir/src/main/resources/katalog-api.json"
            outputDir = output
            addSystemProperty("models", "") // pour ne générer que les classes du modèle, pas les stubs
            addSystemProperty("modelDocs", "false") // pour ne générer que les classes du modèle, pas les stubs
            generatorName = "kotlin"
            modelPackage = "com.orange.ccmd.katalog.api.domain.models"
            additionalProperties = mapOf(
                    "dateLibrary" to "java8",
                    "enumPropertyNaming" to "original" // sinon par défaut les enums sont en camelCase
            )
            typeMappings = mapOf(
                    "array" to "kotlin.collections.List"
            )

            DefaultGenerator().opts(toClientOptInput()).generate()
        }
    }
}