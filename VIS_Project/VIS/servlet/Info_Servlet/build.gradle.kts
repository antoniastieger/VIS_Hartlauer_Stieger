plugins {
    war
}

repositories {
    mavenCentral()
}

dependencies {
// https://mvnrepository.com/artifact/jakarta.servlet/jakarta.servlet-api
    implementation("jakarta.servlet:jakarta.servlet-api:6.0.0")
}

// Specify the subprojects and their settings
subprojects {
    apply(plugin = "war")

    tasks.war {
        destinationDirectory.set(file("$buildDir/webapp"))
        archiveBaseName.set(name)
    }

    tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile> {
        kotlinOptions.jvmTarget = "1.8"
    }
}

// Configuration for the specific subproject
project(":VIS:servlet:Info_Servlet") {
    apply(plugin = "java")
    apply(plugin = "war")

    dependencies {
        // Add any specific dependencies for Info_Servlet subproject if needed
    }

    tasks.war {
        destinationDirectory.set(file("$buildDir/webapp"))
        archiveBaseName.set("Info_Servlet")
        from("webapp/index.html")
    }
}

tasks.clean {
    val fN_a: String = "${project.projectDir.absolutePath}\\webapp\\$nameWar.war"
    val fN_b: String = "${rootProject.projectDir.absolutePath}\\webapps\\$nameWar.war"
    delete(files(fN_a))
    delete(files(fN_b))
}

// Exclude duplicates while integrating libraries through dependencies
tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile> {
    kotlinOptions.jvmTarget = "1.8"
}

tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile> {
    kotlinOptions.jvmTarget = "1.8"
}