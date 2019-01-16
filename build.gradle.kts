buildscript {
    repositories {
        jcenter()
        mavenLocal()
        maven { setUrl("https://oss.jfrog.org/artifactory/oss-snapshot-local") }
        maven { setUrl("https://oss.jfrog.org/artifactory/oss-release-local") }
    }

    dependencies {
        classpath("de.dfki.mary:marytts-common:6.0.1-SNAPSHOT")
        classpath("de.dfki.mary:marytts-lang-en:6.0.1-SNAPSHOT")
    }
}


// Configuration
import marytts.config.MaryConfiguration
import marytts.config.JSONMaryConfigLoader

// Runtime / Request
import marytts.runutils.Mary
import marytts.runutils.Request

open class Synthesize @javax.inject.Inject constructor(objects: ObjectFactory): DefaultTask() {
    @get: InputDirectory
    val textDirectory: DirectoryProperty = objects.directoryProperty()

    @get: OutputDirectory
    val outputDirectory: DirectoryProperty = objects.directoryProperty()

     @InputFile
    val configFile: RegularFileProperty = objects.fileProperty()

    @TaskAction
    fun synthesize() {
        // Starting up mary (should be global but I put it here for example purpose)
        Mary.startup()

        // Read configuration
        val configuration: MaryConfiguration = JSONMaryConfigLoader().loadConfiguration(configFile.get().asFile.inputStream())

        // Do something
        val inputFileTree = project.fileTree(textDirectory)
        inputFileTree.forEach{ textFile: File ->
            // Get the content text
            val input_text: String = textFile.readText(Charsets.UTF_8)

            // Achieve the request
            val request: Request =  Request(configuration, input_text);
            request.process();

            // Dump the output
            File(outputDirectory.get().asFile, textFile.getName() + ".xml").writeText(request.serializeFinaleUtterance().toString())
        }



        // Shutting down mary (should be global but I put it here for example purpose)
        Mary.shutdown()

    }
}

task(name="synthesize", type=Synthesize::class) {
    description = "Synthesize default files"

    textDirectory.set(project.file("src/text"))
    outputDirectory.set(project.file("${buildDir}/output"))
    configFile.set(project.file("src/config.json"))
}
