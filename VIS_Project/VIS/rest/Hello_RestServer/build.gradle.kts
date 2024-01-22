plugins {
    war
}

repositories {
    mavenCentral()
}

dependencies {
    // JAX-RS
    // https://mvnrepository.com/artifact/org.glassfish.jersey.bundles/jaxrs-ri
    implementation("org.glassfish.jersey.bundles:jaxrs-ri:3.1.5")

    // Servlet related stuff
    // https://mvnrepository.com/artifact/jakarta.servlet/jakarta.servlet-api
    implementation("jakarta.servlet:jakarta.servlet-api:6.1.0-M1")
    // Moxy-JAXB (JSON)
    // https://mvnrepository.com/artifact/org.glassfish.jersey.media/jersey-media-moxy
    implementation("org.glassfish.jersey.media:jersey-media-moxy:3.1.5")
}

val nameWar: String = "Hello_RestServer"

tasks.war {
    destinationDirectory.set(file("./webapp"))
    archiveBaseName.set(nameWar)
    // from("webapp/index.html") // remove for "landing" page
    doLast {
        copy {
            println("*** copying war to root webapp folder ... ")
            val fromS: String = "${project.projectDir.absolutePath}\\webapp\\$nameWar.war"
            val intoS: String = "${rootProject.projectDir.absolutePath}\\webapps"
            from(fromS)
            into(intoS)
        }
    }
}

tasks.clean {
    val fN_a: String = "${project.projectDir.absolutePath}\\webapp\\$nameWar.war"
    val fN_b: String = "${rootProject.projectDir.absolutePath}\\webapps\\$nameWar.war"
    delete(files(fN_a))
    delete(files(fN_b))
}
