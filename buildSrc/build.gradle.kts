repositories {
    jcenter()
    mavenLocal()
    maven { setUrl("https://oss.jfrog.org/artifactory/oss-snapshot-local") }
    maven { setUrl("https://oss.jfrog.org/artifactory/oss-release-local") }
}

dependencies {
    compile("de.dfki.mary:marytts-common:6.0.1-SNAPSHOT")
    compile("de.dfki.mary:marytts-lang-en:6.0.1-SNAPSHOT")
}
