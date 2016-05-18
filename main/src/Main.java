import java.awt.*;
import java.util.ArrayList;
import java.util.Vector;

/**
 * Main class to run the program. Drawing and stuff will happen here
 */
public class Main {

    // main parameters
    public static Params params = new Params().loadProperties("param.properties");

    // console variables
    public static PCPC c;
    public static Graphics g;
    public static Graphics2D g2;
    public static int cW = params.windowWidth;
    public static int cH = params.windowHeight;

    // image variables
    public static Image antImg, mineImg;

    /** Control Variables **/

    // enables generations to speed up
    public static boolean fastMode;

    // cycles per generation
    public static int numTicks;

    // generation counter
    public static int generation;

    /************************************/
    /** NEURAL NETWORK VARIABLES BEGIN **/
    /************************************/

    // Storage for the population
    public static ArrayList<Genome> population = new ArrayList<>();

    // Storage for the mine ants
    public static ArrayList<Ant> ants = new ArrayList<>();

    // Storage for the mines
    public static ArrayList<Vector2D> mines = new ArrayList<>();

    // The Genetic Algorithm
    public static Genetic GA;

    public static int numants;
    public static int numMines;
    public static int numWeights;

    // the ant's shape's vertices
    public static ArrayList<Point> antVertices = new ArrayList<>();

    // the mine shape's vertices
    public static ArrayList<Point> mineVertices = new ArrayList<>();

    // Storage for the average fitness per generation to be used for graphing
    public static ArrayList<Double> avgFitnesses = new ArrayList<>();

    // Storage for the best fitness per generation to be used for graphing
    public static ArrayList<Double> bestFitnesses = new ArrayList<>();

    /**********************************/
    /** NEURAL NETWORK VARIABLES END **/
    /**********************************/


    /** ant and Mine Geometry Variables **/

    public static int numantVertices = 16;

    public static Point ant []= {new Point (-1,-1),       new Point (-1,1),
                                     new Point (-0.5,1),      new Point (-0.5,-1),
                                     new Point (0.5,-1),      new Point (1,-1),
                                     new Point (1, 1),        new Point (0.5,1),
                                     new Point (-0.5, -0.5),  new Point (0.5,-0.5),
                                     new Point (-0.5, 0.5),   new Point (-0.25, 0.5),
                                     new Point (-0.25, 1.75), new Point (0.25, 1.75),
                                     new Point (0.25, 0.5),   new Point (0.5, 0.5)};

    public static int numMineVertices = 4;

    public static Point mine [] = {new Point (-1,-1), new Point (-1,1),
                                   new Point (1,1),   new Point (1,-1)};
    /** End geometry **/

    public static void init() throws Exception
    {
        // Initialize the ants, neural nets and GA
        numants = params.numAnts;
        GA = null;
        fastMode = false;
        numTicks = 0;
        numMines = params.numMines;
        generation = 0;

        // create Ants
        for (int i = 0; i < numants; i++)
        {
            Ant newAnt = new Ant();
            ants.add(newAnt);
        }

        // get number of weights
        numWeights = ants.get(0).getNumWeights();
        //System.out.println("numweights" + numWeights);

        // initialize GA class
        GA = new Genetic(numants, params.mutationRate, params.crossoverRate, numWeights);

        // get weights from GA and insert into ants
        population = GA.getPopulation();

        for (int i = 0; i < numants; i++)
        {
            ants.get(i).setWeights(population.get(i).weightList);
        }

        // init mines to random locations
        for (int i = 0; i < numMines; i++)
        {
            mines.add(new  Vector2D(Utils.randDouble() * cW, Utils.randDouble() * cH));
        }

        // TODO: load image files here
    }

    public static String antPos(Ant ant)
    {
        return ": " + ant.position.x + ", " + ant.position.y;
    }

    // Main logic method
    // controls the simulation
    public static void update() throws Exception
    {
        // runs the ant for numTick cycles. Here the network is updated with
        // information from the environment. output form network is obtained and the
        // ants are moved. if it hits a mine the fitness is updated
        if (numTicks++ < params.numTicks)
        {
            //System.out.println(numTicks);
            for (int i = 0; i < numants; i++)
            {
                // update the NN and positions
                if (!ants.get(i).update(mines))
                {
                    // error in processing the neural net
                    throw new Exception("Wrong amount of inputs");
                }

                // see if it hit a mine
                int grabHit = ants.get(i).checkForMine(mines, params.mineScale);

                if (grabHit > 0)
                {
                    // hit a mine so increase fitness
                    //System.out.println("ant # " + i + " HIT A MINE");
                    ants.get(i).incrementFitness();

                    // mine found so replace the mine with another at a random position
                    mines.set(grabHit, new Vector2D(Utils.randDouble() * cW, Utils.randDouble() * cH));
                }

                // update the chromos fitness score
                population.get(i).fitness = ants.get(i).fitness;
            }
        }

        // finish generation
        // run the GA and update ants with their new NNs
        else
        {
            // update the stats to be used in stat window
            avgFitnesses.add(GA.getAverageFitness());
            bestFitnesses.add(GA.getBestFitness());

            System.out.println("generation is " + generation);
            System.out.println("fitness is " + avgFitnesses.get(avgFitnesses.size()-1));

            // inc the gen counter
            generation++;

            // reset cycles
            numTicks = 0;

            // run the GA to create new population
            population = GA.epoch(population);

            // insert the new NN into ants and reset their positions
            for (int i = 0; i < numants; i++)
            {
                ants.get(i).setWeights(population.get(i).weightList);
                ants.get(i).reset();
            }
        }

    }

    public static void draw()
    {
        if (!fastMode)
        {
            // do normal stuff here
        }
        else
        {
            drawGraph();
        }
    }

    public static void drawGraph()
    {

    }

    public static void start() throws Exception // Main run method
    {
        long time = System.currentTimeMillis();
        long now;

        // 1/fps seconds per frame, used to track when to refresh
        float fps = 1000/params.fps;

        while (true)
        {
            if (c.isFastMode) {
                now = System.currentTimeMillis();
                if (now - time > fps) {
                    time = now;

                    c.cls();
                    //TODO: main logic here
                    update();
                    for (int i = 0; i < numMines; i++) {
                        g.setColor(Color.BLACK);
                        g.fillRect((int) mines.get(i).x, (int) mines.get(i).y, 5, 5);
                    }
                    for (int i = 0; i < numants; i++) {
                        g.setColor(Color.red);
                        if (i < 4)
                            g.setColor(Color.BLUE);
                        g.fillRect((int) ants.get(i).getPosition().x, (int) ants.get(i).getPosition().y, 5, 5);
                        //g.drawString("a" + ants.get(i).getClosestMine(mines).length(), (int)ants.get(i).getPosition().x, (int)ants.get(i).getPosition().y);
                    }
                    c.ViewUpdate();
                }

                Thread.sleep(2);
            }
            else
            {
                update();
            }
        }
    }

    public static void main(String args[]) throws Exception
    {
        init();
        c = new PCPC();
        g = c.getGraphics();
        g2 = (Graphics2D) g;
        params.loadProperties("param.properties");
        start();
    }
}
