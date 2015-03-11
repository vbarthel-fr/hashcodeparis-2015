package fr.mashbarth.hascode.qualificationround;


import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public final class Launcher {

    private static final String INPUT_DIRECTORY = "input";

    private static final String OUTPUT_DIRECTORY = "output";


    public static void main(String[] args) {
        System.out.println("#### Qualification Round ####");

        // Example of reading a file.
        readFile(INPUT_DIRECTORY + "/test.txt");

        // Example of writing a list of object to a file.
        final List<String> strings = new ArrayList<String>();
        strings.add("Here");
        strings.add("is");
        strings.add("a");
        strings.add("small");
        strings.add("example !");
        writeFile(OUTPUT_DIRECTORY + "/test.txt", strings);
    }


    /**
     * Read a file line by line.
     * A simple example of reading a file line by line.
     *
     * @param fileName the name of the file.
     */
    public static void readFile(String fileName) {
        try {
            final File inputFile = new File(fileName);
            final FileReader fileReader = new FileReader(inputFile);
            final BufferedReader br = new BufferedReader(fileReader);
            String line;

            while ((line = br.readLine()) != null) {
                // TODO process the line
                System.out.println(line);
            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Write a {@link java.util.List} of {@link java.lang.Object}s to a file.
     * A simple example that appends, for each object, the result of toString.
     *
     * @param fileName the name of the file.
     */
    public static void writeFile(String fileName, List<? extends Object> objects) {
        try {
            final File outputFile = new File(fileName);
            final FileWriter fileReader = new FileWriter(outputFile);

            for (Object object : objects) {
                fileReader.append(object.toString()).append("\n");
            }

            fileReader.flush();
            fileReader.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    // Non instantiability.
    private Launcher() {
    }
}
