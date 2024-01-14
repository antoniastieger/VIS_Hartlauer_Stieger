plugins {
    id("org.jetbrains.kotlin.jvm") version "1.8.20"
    war
}

repositories {
    mavenCentral()
}

dependencies {
    // https://mvnrepository.com/artifact/jakarta.servlet/jakarta.servlet-api
    implementation("jakarta.servlet:jakarta.servlet-api:6.0.0")
    implementation(project(mapOf("path" to ":general:EnvironmentI")))
    implementation(project(mapOf("path" to ":rmi:Environment_RmiClient")))
    implementation(project(mapOf("path" to ":socket:java:Environment_SocketClient")))
}

val nameWar: String = "EnvironmentServiceServlet"

// Configuration for the specific subproject
project(":servlet:EnvironmentService_Servlet") {
    apply(plugin = "java")
    apply(plugin = "war")

    dependencies {
        // Add any specific dependencies for Info_Servlet subproject if needed
    }

    tasks.war {
        destinationDirectory.set(file("./webapp"))
        archiveBaseName.set(nameWar)
        //from("webapp/index.html") // only integrate if index.html should be provided/used
        doLast {
            copy {
                println("*** copying war to root webapps folder ... ")
                val fromS: String = "${project.projectDir.absolutePath}\\webapp\\$nameWar.war"
                val intoS: String = "${rootProject.projectDir.absolutePath}\\webapps"
                from(fromS)
                into(intoS)
            }
        }
    }
}

tasks.clean {
    val fN_a: String = "${project.projectDir.absolutePath}/webapp/${project.name}.war"
    val fN_b: String = "${rootProject.projectDir.absolutePath}/webapps/${project.name}.war"
    delete(files(fN_a))
    delete(files(fN_b))
}
