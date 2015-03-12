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


    private static final String INPUT_RESOURCE = "/dc.in";

    private static final String TEST_RESOURCE = "/test.in";

    public static void main(String[] args) {
        System.out.println("#### Qualification Round ####");

        final Problem problem = parseInput(INPUT_RESOURCE);

        System.out.println("#### Racks ####");
        System.out.println(problem.racks.toString());

        System.out.println("#### Servers ####");
        System.out.println(problem.servers.toString());

        System.out.println("#### Groups ####");
        System.out.println(problem.groups.toString());
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

    private static Problem parseInput(String inputName) {
        Problem problem = new Problem();
        try {
            final InputStream inputStream = Launcher.class.getClass().getResourceAsStream(inputName);
            final InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
            final BufferedReader br = new BufferedReader(inputStreamReader);
            String line;

            // First line
            line = br.readLine();
            String[] args = line.split(" ");

            problem.r = Integer.valueOf(args[0]);
            problem.racks = new ArrayList<Rack>(problem.r);
            problem.s = Integer.valueOf(args[1]);
            problem.u = Integer.valueOf(args[2]);
            problem.p = Integer.valueOf(args[3]);
            problem.groups = new ArrayList<Group>(problem.p);
            problem.m = Integer.valueOf(args[4]);
            problem.servers = new ArrayList<Server>(problem.m);

            // Create Racks
            for (int i = 0; i < problem.r; i++) {
                final Rack rack = new Rack(i, problem.s);
                problem.racks.add(rack);
            }

            // Create Groups
            for (int i = 0; i < problem.p; i++) {
                final Group group = new Group(i);
                problem.groups.add(group);
            }

            // Read unavailability
            for (int i = 0; i < problem.u; i++) {
                line = br.readLine();
                args = line.split(" ");
                final int rackNumber = Integer.valueOf(args[0]);
                final int unavailbilityPosition = Integer.valueOf(args[1]);

                final Rack rack = problem.racks.get(rackNumber);
                rack.markAsUnavailable(unavailbilityPosition);
            }

            // Read servers
            for (int i = 0; i < problem.m; i++) {
                line = br.readLine();
                args = line.split(" ");
                final int size = Integer.valueOf(args[0]);
                final int capacity = Integer.valueOf(args[1]);

                final Server server = new Server(i, capacity, size);
                problem.servers.add(server);
            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return problem;
    }

    private static class Problem {
        public List<Server> servers;
        public List<Rack> racks;
        public List<Group> groups;
        public int r, s, u, p, m;

    }


    private static class Server {
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

        @Override
        public String toString() {
            // TODO use that for result
            // return id + " " + rackNumber + " " + rackPosition + " " + groupId;
            return "Server id: " + id + "  capacity: " + capacity + "  size: " + size + "  rackNumber: " + rackNumber + "  rackPosition:" + rackPosition + "  groupId:" + groupId;

        }
    }

    private static class Rack {
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

        @Override
        public String toString() {
            return "Rack id: " + id + "  capacityTotal: " + capacityTotal + "  freeSize: " + freeSize + "  availability: " + Arrays.toString(availability);
        }
    }

    private static class Group {
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

        @Override
        public String toString() {
            return "Group id: " + id + "  sizeTotal: " + sizeTotal + "  capacityTotal: " + capacityTotal;
        }
    }
}
