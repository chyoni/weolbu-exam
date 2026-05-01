import com.diffplug.gradle.spotless.SpotlessExtension
import org.gradle.kotlin.dsl.libs

plugins {
    java
    alias(libs.plugins.spring.boot)
    alias(libs.plugins.spring.dependency)
    alias(libs.plugins.spotless)
}

allprojects {
    group = "cwchoiit"
    version = "1.0.0"
    description = "weolbu-exam"

    repositories {
        mavenCentral()
    }

    var libs = rootProject.the<VersionCatalogsExtension>().named("libs")

    configure<JavaPluginExtension> {
        sourceCompatibility = JavaVersion.VERSION_21
        targetCompatibility = JavaVersion.VERSION_21
    }

    tasks.withType<JavaCompile> {
        options.encoding = "UTF-8"
    }

    configurations {
        compileOnly {
            extendsFrom(configurations.annotationProcessor.get())
        }

        register("querydsl") {
            extendsFrom(configurations.compileClasspath.get())
        }

        register("mockitoAgent")
    }

    // Global Dependency
    dependencies {
        implementation(libs.findLibrary("spring-boot-starter-web").orElseThrow())
        implementation(libs.findLibrary("spring-boot-data-jpa").orElseThrow())
        implementation(libs.findLibrary("spring-boot-validation").orElseThrow())
        implementation(libs.findLibrary("spring-security-core").orElseThrow())

        // Querydsl
        implementation(libs.findLibrary("querydsl-jpa").orElseThrow())
        annotationProcessor(variantOf(libs.findLibrary("querydsl-apt").orElseThrow()) {
            classifier(libs.findVersion("querydsl-jpa").orElseThrow().requiredVersion)
        })
        annotationProcessor(libs.findLibrary("jakarta-annotation").orElseThrow())
        annotationProcessor(libs.findLibrary("jakarta-persistence").orElseThrow())

        // DB
        runtimeOnly(libs.findLibrary("h2").orElseThrow())

        // Utils
        implementation(libs.findLibrary("springdoc").orElseThrow())
        compileOnly(libs.findLibrary("lombok").orElseThrow())
        annotationProcessor(libs.findLibrary("lombok").orElseThrow())

        // Testing
        "mockitoAgent"(libs.findLibrary("mockito-core").orElseThrow()) { isTransitive = false }
        testImplementation(libs.findLibrary("junit-jupiter").orElseThrow())
        testImplementation(libs.findLibrary("spring-boot-starter-test").orElseThrow())
        testImplementation(libs.findLibrary("lombok").orElseThrow())
        testAnnotationProcessor(libs.findLibrary("lombok").orElseThrow())
    }

    // Spotless lint automatically applied in compile time (Intellij)
    if (System.getProperty("idea.active") == "true") {
        tasks.named("processResources") {
            dependsOn("spotlessApply")
        }
    }

    configure<SpotlessExtension> {
        java {
            target("**/*.java")
            targetExclude("**/generated/**/*.java")
            googleJavaFormat().aosp()
            importOrder()
            endWithNewline()
            trimTrailingWhitespace()
        }
    }

    tasks.test {
        testLogging {
            showExceptions = true
            showStackTraces = true
            showStandardStreams = true
        }
        useJUnitPlatform()
        jvmArgs("-javaagent:${configurations["mockitoAgent"].asPath}")
    }
}