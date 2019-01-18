package marytts;

// Java base
import java.io.FileInputStream;
import java.io.File;
import java.io.PrintWriter;
import java.nio.file.*;

// Configuration
import marytts.config.MaryConfiguration;
import marytts.config.JSONMaryConfigLoader;

// Runtime / Request
import marytts.runutils.Mary;
import marytts.runutils.Request;

/**
 *
 *
 * @author <a href="mailto:lemagues@tcd.ie">Sébastien Le Maguer</a>
 */
public class Synthesize
{
    public static void main(String[] args) throws Exception {
        File input_dir = new File(args[0]);
        File output_dir = new File(args[1]);
        File config_file = new File(args[2]);


        // Starting up mary (should be global but I put it here for example purpose)
        Mary.startup();

        // Read configuration
        MaryConfiguration configuration = (new JSONMaryConfigLoader()).loadConfiguration(new FileInputStream(config_file));

        // Do something
        for (final File text_file : input_dir.listFiles()) {
            // Get the content text
            String input_text =  new String(Files.readAllBytes(text_file.toPath()));

            // Achieve the request
            Request request = new Request(configuration, input_text);
            request.process();

            // Dump the output
            output_dir.mkdirs();
            File output_file = new File(output_dir, text_file.getName() + ".xml");
            PrintWriter writer = new PrintWriter(output_file);
            writer.println(request.serializeFinaleUtterance().toString());
            writer.close();
        }

        // Shutting down mary (should be global but I put it here for example purpose)
        Mary.shutdown();
    }
}
