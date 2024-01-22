plugins {
    application
}

repositories {
    mavenCentral()
}

dependencies {
// JAXB (XML)
// https://mvnrepository.com/artifact/org.glassfish.jaxb/jaxb-runtime
    implementation("jakarta.xml.bind:jakarta.xml.bind-api:3.0.1")
    implementation("org.glassfish.jaxb:jaxb-runtime:4.0.4")
}

application {
// Define the main class for the application.
    mainClass.set("at.fhooe.sail.vis.Pet")
}