plugins {
    application
}

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.json:json:20210307")
}

application {
    mainClass.set("at.fhooe.sail.vis.HighscoreParser")
}