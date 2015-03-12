package fr.mashbarth.hashcode.qualificationround;


import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public final class Launcher {

    public static void main(String[] args) {
        System.out.println("#### Qualification Round ####");

        // Example of reading a file.
        readResource("/test-resources.txt");

        // Example of writing a list of object to a file.
        final List<String> strings = new ArrayList<String>();
        strings.add("Here");
        strings.add("is");
        strings.add("a");
        strings.add("small");
        strings.add("example !");
        writeFile("test-result.txt", strings);
    }


    /**
     * Read a resource line by line.
     * A simple example of reading a resource line by line.
     *
     * @param resourceName the name of the resource.
     */
    public static void readResource(String resourceName) {
        try {

            final InputStream inputStream = resourceName.getClass().getResourceAsStream(resourceName);
            final InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
            final BufferedReader br = new BufferedReader(inputStreamReader);
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


    private class Server {
        public int capacity = 0;
        public int size = 0;
        public int id = -1;
        public int rackPosition;
        public int rackNumber;
        public int groupId;

        public Server(int id, int capacity, int size) {
            this.id = id;
            this.capacity = capacity;
            this.size = size;
        }
    }

    private class Rack {
        public int id = -1;
        public boolean[] availability;
        public ArrayList<Server> servers = new ArrayList<Server>();
        public int capacityTotal;
        public int freeSize;

        public Rack(int id, int size) {
            freeSize = size;
            availability = new boolean[freeSize];
            Arrays.fill(availability, true);
            this.id = id;
        }

        public void markAsUnavailable(int position) {
            availability[position] = false;
            freeSize--;
        }

        public void addServer(Server server, int position) {
            capacityTotal += server.capacity;
            freeSize -= server.size;
            server.rackNumber = id;
            server.rackPosition = position;
            servers.add(server);
        }
    }

    private class Group {
        public int id = -1;
        ArrayList<Server> servers = new ArrayList<Server>();
        public int sizeTotal;
        public int capacityTotal;

        public Group(int id) {
            this.id = id;
        }

        public void addServer(Server server) {
            sizeTotal += server.size;
            capacityTotal += server.capacity;
            server.groupId = id;
            servers.add(server);
        }
    }
}
