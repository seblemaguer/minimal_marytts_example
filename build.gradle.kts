plugins {
    `java-library`
}

repositories {
    jcenter()
    mavenLocal()
    maven { setUrl("https://oss.jfrog.org/artifactory/oss-snapshot-local") }
    maven { setUrl("https://oss.jfrog.org/artifactory/oss-release-local") }
}

configurations {
    create("javaex")
}

dependencies {
    "javaex"("de.dfki.mary:marytts-common:6.0.1-SNAPSHOT")
    "javaex"("de.dfki.mary:marytts-lang-en:6.0.1-SNAPSHOT")
}


task(name="synthesize", type=JavaExec::class) {
    description = "Synthesize default files"

    // Add mary dependencies
    classpath(configurations["javaex"])

    // Add buildSrc to the class path
    classpath(file("${rootDir}/buildSrc/build/libs/buildSrc.jar"))

    // Define main class
    main = "marytts.Synthesize"

    // Define log configuration file
    systemProperty("log4j.configurationFile", "src/log4j2.xml")
    if (System.getProperties().containsKey("log4j.level")) {
        systemProperty("log4j.level", System.getProperty("log4j.level"))
    }

    // arguments to pass to the application
    args(project.file("src/text"), project.file("${buildDir}/output"), project.file("src/config.json"))
}
