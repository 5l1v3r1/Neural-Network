import java.util.ArrayList;

/**
 * the neuron layer class
 */

public class NeuronLayer {

    public int numNeurons;
    public ArrayList<Neuron> neuronList = new ArrayList<>();

    public NeuronLayer(int numNeurons, int numInputsPerNeuron)
    {
        this.numNeurons = numNeurons;

        for (int i = 0; i < numNeurons; i++)
        {
            neuronList.add(new Neuron(numInputsPerNeuron));
        }
    }
}
