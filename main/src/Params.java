import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class Params {

    //------------------------------------general parameters
    public double pi = 3.14159265358979;
    public double halfPi = pi / 2;
    public double twoPi = pi * 2;

    public int    windowWidth = 400;
    public int    windowHeight = 400;

    public int    fps = 0;


    //-------------------------------------used for the neural network
    public int    numInputs = 0;
    public int    numHiddenLayers = 0;
    public int    neuronsPerHiddenLayer = 0;
    public int    numOutputs = 0;

    //for tweaking the sigmoid function
    public double activationResponse = 0;
    //bias value
    public double bias = 0;

    //--------------------------------------used to define the ants

    //limits how fast the ants can turn
    public double maxTurnRate = 0;

    public double maxSpeed = 0;

    //for controlling the size
    public int   antScale = 0;


    //--------------------------------------controller parameters
    public int    numAnts = 0;

    public int    numMines = 0;

    //number of time steps we allow for each generation to live
    public int    numTicks = 0;

    //scaling factor for mines
    public double mineScale = 0;

    //---------------------------------------GA parameters
    public double crossoverRate = 0;
    public double mutationRate = 0;

    //the maximum amount the ga may mutate each weight by
    public double maxPerturbation = 0;

    //used for elitism
    public int    numElite = 0;
    public int    numCopiesElite = 0;

    public Params loadProperties(String propFile)
    {
        Properties prop = new Properties();
        try (InputStream input = new FileInputStream(propFile)) {
            prop.load(input);

            fps                   = Utils.stoi(prop.getProperty("fps"));
            numInputs             = Utils.stoi(prop.getProperty("numInputs"));
            numHiddenLayers       = Utils.stoi(prop.getProperty("numHiddenLayers"));
            neuronsPerHiddenLayer = Utils.stoi(prop.getProperty("neuronsPerHiddenLayer"));
            numOutputs            = Utils.stoi(prop.getProperty("numOutputs"));
            antScale          = Utils.stoi(prop.getProperty("antScale"));
            numAnts           = Utils.stoi(prop.getProperty("numAnts"));
            numMines              = Utils.stoi(prop.getProperty("numMines"));
            numTicks              = Utils.stoi(prop.getProperty("numTicks"));
            numElite              = Utils.stoi(prop.getProperty("numElite"));
            numCopiesElite        = Utils.stoi(prop.getProperty("numCopiesElite"));

            activationResponse    = Utils.stof(prop.getProperty("activationResponse"));
            bias                  = Utils.stof(prop.getProperty("bias"));
            maxTurnRate           = Utils.stof(prop.getProperty("maxTurnRate"));
            maxSpeed              = Utils.stof(prop.getProperty("maxSpeed"));
            mineScale             = Utils.stof(prop.getProperty("mineScale"));
            crossoverRate         = Utils.stof(prop.getProperty("crossoverRate"));
            mutationRate          = Utils.stof(prop.getProperty("mutationRate"));
            maxPerturbation       = Utils.stof(prop.getProperty("maxPertubation"));

        }  catch (IOException ex) {
            System.out.println("couldn't load file\n "+ ex);
        }

        return this;
    }
}
