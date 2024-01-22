plugins {
    application
}
repositories {
    mavenCentral()
}
dependencies {
    // Moxy-JAXB (JSON) DON’T mix with standard XML dependency
    implementation("org.glassfish.jersey.media:jersey-media-moxy:3.1.5")
}
application {
    // Define the main class for the application.
    mainClass.set("at.fhooe.sail.vis.Pet")
}