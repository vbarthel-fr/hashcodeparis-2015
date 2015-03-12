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
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public final class Launcher {


    private static final String INPUT_RESOURCE = "/dc.in";

    private static final String TEST_RESOURCE = "/test.in";

    private static final String OUTPUT = "results.out";

    public static void main(String[] args) {
        System.out.println("#### Qualification Round ####");

        final Problem problem = parseInput(INPUT_RESOURCE);

        // Populate groups
        populateGroups(problem);

        // Insert servers
        insertServersIntoRacks(problem);

        // Sort servers by ids
        Collections.sort(problem.servers, new Comparator<Server>() {
            @Override
            public int compare(Server a, Server b) {
                if (a.id == b.id) {
                    return 0;
                } else if (a.id < b.id) {
                    return -1;
                } else {
                    return 1;
                }
            }
        });

        writeFile(OUTPUT, problem.servers);
        return;
    }

    private static void populateGroups(Problem problem) {
        Collections.sort(problem.servers, new Comparator<Server>() {
            public int compare(Server a, Server b) {
                if (a.capacity == b.capacity) {
                    return 0;
                } else if (a.capacity < b.capacity) {
                    return -1;
                } else {
                    return 1;
                }
            }
        });

        ArrayList<Server> panier = new ArrayList<Server>();
        int panierCount = 0;

        for (Server server : problem.servers) {
            panierCount++;
            panier.add(server);
            if (panierCount == problem.p) {
                processPanier(panier, problem.groups);
                panier.clear();
                panierCount = 0;
            }
        }

        if (panierCount != 0) {
            processPanier(panier, problem.groups);
        }
    }

    private static void processPanier(ArrayList<Server> panier, List<Group> groups) {
        Collections.sort(panier, new Comparator<Server>() {
            public int compare(Server a, Server b) {
                if (a.size == b.size) {
                    return 0;
                } else if (a.size < b.size) {
                    return -1;
                } else {
                    return 1;
                }
            }
        });


        Collections.sort(groups, new Comparator<Group>() {
            public int compare(Group a, Group b) {
                if (a.sizeTotal == b.sizeTotal) {
                    return 0;
                } else if (a.sizeTotal > b.sizeTotal) {
                    return -1;
                } else {
                    return 1;
                }
            }
        });

        for (int i = 0; i < panier.size(); i++) {
            groups.get(i).addServer(panier.get(i));
        }

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


    private static void insertServersIntoRacks(Problem problem) {
        final List<Group> groups = new ArrayList<Group>(problem.groups);

        for (Group group : groups) {
            final List<Rack> racks = new ArrayList<Rack>(problem.racks);

            // Sort the servers by increasing size
            final List<Server> servers = new ArrayList<Server>(group.servers);
            Collections.sort(servers, new Comparator<Server>() {
                @Override
                public int compare(Server a, Server b) {
                    if (a.size == b.size) {
                        return 0;
                    } else if (a.size > b.size) {
                        return -1;
                    } else {
                        return 1;
                    }
                }
            });

            // Sort the racks by decreasing free size
            Collections.sort(racks, new Comparator<Rack>() {
                @Override
                public int compare(Rack a, Rack b) {
                    if (a.freeSize == b.freeSize) {
                        return 0;
                    } else if (a.freeSize > b.freeSize) {
                        return -1;
                    } else {
                        return 1;
                    }
                }
            });

            // For each server
            for (Server server : servers) {
                // Search for a best position in each rack.
                for (Rack rack : racks) {
                    final int bestPosition = rack.bestPosition(server);
                    if (bestPosition != -1) {
                        rack.addServer(server, bestPosition);
                        racks.remove(rack);
                        break;
                    }
                }
            }
        }
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
        public int rackPosition = -1;
        public int rackNumber = -1;
        public int groupId = -1;

        public Server(int id, int capacity, int size) {
            this.id = id;
            this.capacity = capacity;
            this.size = size;
        }

        @Override
        public String toString() {
            // TODO use that for result
            if (rackNumber == -1) return "x";
            return rackNumber + " " + rackPosition + " " + groupId;
            //return "Server id: " + id + "  capacity: " + capacity + "  size: " + size + "  rackNumber: " + rackNumber + "  rackPosition:" + rackPosition + "  groupId:" + groupId;
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
            availability = new boolean[size];
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
            for (int i = position; i < position + server.size; i++) {
                availability[i] = false;
            }
        }


        // TODO improve create holes in the racks.
        public int bestPosition(Server server) {
            final int size = server.size;
            final int maxSize = availability.length;

            for (int i = 0; i < maxSize; i++) {
                boolean ok = true;
                int j;

                for (j = i; j < maxSize && j < i + size; j++) {
                    if (!availability[j]) ok = false;
                }

                if (ok && ((j - i) == server.size)) {
                    return i;
                }
            }

            return -1;
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
