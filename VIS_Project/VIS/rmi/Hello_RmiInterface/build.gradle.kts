plugins {
    // Apply the application plugin to add support for building a CLI application in Java.
    application
}

repositories {
    // Use Maven Central for resolving dependencies.
    mavenCentral()
}

dependencies {
    // This dependency is used by the application.
    implementation("com.google.guava:guava:31.0.1-jre")
}

application {
}

val nameJar: String = "Hello_RmiInterface"
tasks.jar {
    archiveBaseName.set(jarName)
    doLast {
        copy {
            println("*** copying jar to libs folder ... ")
            val bDir: String = layout.buildDirectory.get().toString()
            val fromS: String = "${bDir}\\libs\\$jarName.jar"
            val intoS: String = "${rootProject.projectDir.absolutePath}\\libs"
            from(fromS)
            into(intoS)
        }
    }
}
tasks.clean {
    val fN: String = "${rootProject.projectDir.absolutePath}\\libs\\$jarName.jar"
    delete(files(fN))
}
