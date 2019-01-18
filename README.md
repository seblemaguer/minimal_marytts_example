# Minimal example of how to integrate MaryTTS into its java application

## How to use

In order to run, just do the following commands

```sh
./gradlew synthesize [-Dlog4.level=<level>]
```

- `<level>` defines the level of the logger (by default it is warning for MaryTTS), you can put WARN, INFO or DEBUG.

## The interesting part

Everything is architecture to embed the call of mary. It is done in the file
[buildSrc/src/main/java/marytts/Synthesize.java](https://github.com/seblemaguer/minimal_marytts_example/blob/master/buildSrc/src/main/java/marytts/Synthesize.java)

In order to use Mary the following logic is **imposed**:

```java
// Starting up mary
Mary.startup();

// Dealing with requests
for (...) {
    // Retrieve the input (input_date, configuration) from somewhere
    ...

    // Generate the input
    String input =  prepareInput(...);

    // Generate the configuration object
    MaryConfiguration configuration = getConfiguration(...);

    // Generate the request, process it and retrieve the results
    Request request = new Request(configuration, input);
    request.process();
    Object output = request.serializeFinaleUtterance();

    // Do something with the output
    ...
}

// Shutting down mary
Mary.shutdown();
```

## The request

The request is composed by a input (in the example a text) and a configuration like the on presented in the [src/config.json](https://github.com/seblemaguer/minimal_marytts_example/blob/master/src/config.json)

This minimal example contains this:

```json
{
    "marytts.runutils.Request": {
        "input_serializer": "marytts.io.serializer.TextSerializer",
        "output_serializer": "marytts.io.serializer.XMLSerializer",
        "module_sequence": "REF:en_US"
    }
}
```

Here, only the module `marytts.runutils.Request` is adapted and 3 properties are sets:
  * The input and output serializers which are the classes which are dealing with the import and the
    export of the utterance (the core object that Mary uses to store the information).
  * The sequence of modules via `module_sequence`. The value of this field is normally more complex
    but here, we are simply making a reference to a preloaded configuration labeled `en_US`. That
    means that, for this property only, we are using the value of the configuration `en_US`.
