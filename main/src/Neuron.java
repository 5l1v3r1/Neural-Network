import java.util.ArrayList;

/**
 * the base neuron class
 */
public class Neuron {

    public int numInputs;

    public ArrayList<Double> weightList = new ArrayList<>();

    public Neuron(int numInputs){
        this.numInputs = numInputs;

        // need additional weight for bias so +1
        for (int i = 0; i < numInputs + 1; i++)
        {
            // init weights with random values
            weightList.add(Utils.randomClamped());
        }
    }
}
