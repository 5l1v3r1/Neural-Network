import java.util.ArrayList;

// The ant class
// each instance will be a separate ant
public class Ant {
    
    // Parameters
    public Params params = new Params();

    // Ant's neural net
    public NeuralNet brain = new NeuralNet();

    // Position on the map
    public Vector2D position;

    // direction its going
    public Vector2D lookat = new Vector2D(0,0);

    // its rotation
    public double rotation;
    public double speed;

    // stored output from the ANN
    public double lTrack, rTrack;

    // the sweeper's fitness score
    public double fitness;

    // the scale of the sweeper when drawn
    public double scale;

    // index position of closest mine
    public int closestMine;

    public Ant()
    {
        // init params
        params.loadProperties("param.properties");
        
        // constants
        lTrack = 0.16;
        rTrack = 0.16;
        scale = params.antScale;
        closestMine = 0;

        // creates random starting position
        // creates random rotation
        // sets fitness to 0
        reset();
    }

    // updates the ANN with information from the environment
    //
    //	take readings and input into the network
    //
    //	inputs:
    //
    //	A vector to the closest mine (x, y)
    //	where the sweeper is looking at (x, y)
    //
    //	two outputs from the network: lTrack and rTrack
    //	given these outputs, we can compute the rotation
    //	and acceleration and apply to current velocity vector.
    //
    public boolean update(ArrayList<Vector2D> mines)
    {
        // stores all inputs from the network
        ArrayList<Double> inputs = new ArrayList<>();

        // get a vector to the closest mine
        int temp = closestMine;
        Vector2D closestMineVec = getClosestMine(mines);

        if (closestMine != temp)
        {
            //System.out.println("new closest mine discovered");
        }

        // normalize vector
        closestMineVec.normalize();

        // add in vector to closest mine
        inputs.add(closestMineVec.x);
        inputs.add(closestMineVec.y);


        // add in sweepers direction vector
        inputs.add(lookat.x);
        inputs.add(lookat.y);

        ArrayList<Double> output = brain.update(inputs);

        // check validity of output
        if (output.size() < params.numOutputs)
        {
            return false;
        }

        // assign output to sweepers lTrack and rTrack
        lTrack = output.get(0);
        rTrack = output.get(1);

        // calculate rotation
        double netRot = lTrack - rTrack;

        // clamp rotation
        Utils.clamp(netRot, -params.maxTurnRate, params.maxTurnRate);

        rotation += netRot;

        speed = lTrack + rTrack;
        Utils.clamp(speed, 0, params.maxSpeed);
        //System.out.println(speed);

        //System.out.println("unupdated position is " + position.x + ", " + position.y);

        // update direction
        lookat.x = -Math.sin(rotation);
        lookat.y = Math.cos(rotation);

        // update position
        position.add(lookat.multiply(0.5));

        // wrap around screen
        if (position.x > params.windowWidth)
            position.x = 0;
        if (position.x < 0)
            position.x = params.windowWidth;
        if (position.y > params.windowHeight)
            position.y = 0;
        if (position.y < 0)
            position.y = params.windowHeight;

        //System.out.println("updated position is " + position.x + ", " + position.y);
        return true;
    }


    // returns a vector to the closest mine
    public Vector2D getClosestMine(ArrayList<Vector2D> mines)
    {
        double closestSoFar = 99999;

        Vector2D closestObjectVec = new Vector2D(0,0);

        // cycle through mines to find closest one
        for (int i = 0; i < mines.size(); i++)
        {
            // redundant brackets? who knows
            double distToObject = (mines.get(i).subtract(position)).length();

            if (distToObject < closestSoFar)
            {
                closestSoFar = distToObject;
                closestObjectVec = position.subtract(mines.get(i));
                closestMine = i;
            }

        }
        return closestObjectVec;
    }

    // Ant-mine collision detection
    public int checkForMine(ArrayList<Vector2D> mines, double size)
    {
        Vector2D distToObject = position.subtract(mines.get(closestMine));

        if (distToObject.length() < (size + 5))
        {
            return closestMine;
        }

        return -1;
    }

    // reset function
    public void reset()
    {
        // reset the sweepers positions
        double randPosX = Utils.randDouble() * params.windowWidth;
        double randPosY = Utils.randDouble() * params.windowHeight;
        position = new Vector2D(randPosX, randPosY);

        // reset the fitness
        fitness = 0;

        // reset the rotation
        rotation = Utils.randDouble() * params.twoPi;

        //System.out.println("created Ant with position " + position.x + ", " + position.y);
    }

    // accessor functions
    public Vector2D getPosition()
    {
        return position;
    }

    public void incrementFitness()
    {
        fitness++;
    }

    public double getFitness()
    {
        return fitness;
    }

    public void setWeights(ArrayList<Double> weights)
    {
        brain.putWeights(weights);
    }

    public int getNumWeights()
    {
        return brain.getNumWeights();
    }
}
