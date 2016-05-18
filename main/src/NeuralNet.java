import java.util.ArrayList;

public class NeuralNet {

    int numInputs;
    int numOutputs;
    int numHiddenLayers;
    int neuronsPerHiddenLayer;

    ArrayList<NeuronLayer> layerList = new ArrayList<>();

    Params params = new Params();

    public NeuralNet()
    {
        params.loadProperties("param.properties");

        this.numInputs = params.numInputs;
        this.numOutputs = params.numOutputs;
        this.numHiddenLayers = params.numHiddenLayers;
        this.neuronsPerHiddenLayer = params.neuronsPerHiddenLayer;

        createNet();
    }


    // Builds the neural net. Weights are all initialized to random values between -1 and 1
    public void createNet()
    {
        // Create layers of network
        if (numHiddenLayers > 0)
        {
            // create first hidden layer
            layerList.add(new NeuronLayer(neuronsPerHiddenLayer, numInputs));

            for (int i = 0; i < numHiddenLayers - 1; i++)
            {
                layerList.add(new NeuronLayer(neuronsPerHiddenLayer, neuronsPerHiddenLayer));
            }

            // create output layer
            layerList.add(new NeuronLayer(numOutputs, neuronsPerHiddenLayer));
        }
        else
        {
            // create output layer
            layerList.add(new NeuronLayer(numOutputs, numInputs));
        }

    }

    // returns a list of all weights
    public ArrayList<Double> getWeights()
    {
        ArrayList<Double> weights = new ArrayList<>();

        // for each layer
        for (int i = 0; i < numHiddenLayers; i++)
        {
            // for each neuron
            for (int j = 0; j < layerList.get(i).numNeurons; j++)
            {
                // for each weight
                for (int k = 0; k < layerList.get(i).neuronList.get(j).numInputs; k++)
                {
                    weights.add(layerList.get(i).neuronList.get(j).weightList.get(k));
                }
            }
        }

        return weights;
    }

    // given a list of weights, puts weights in the NN
    public void putWeights(ArrayList<Double> weights)
    {
        int counter = 0;

        //for each layer
        for (int i=0; i<numHiddenLayers + 1; ++i)
        {
            //for each neuron
            for (int j=0; j<layerList.get(i).numNeurons; ++j)
            {
                // no need to clear because weightlists are initialized with random weights
                //for each weight
                for (int k=0; k<layerList.get(i).neuronList.get(j).numInputs; ++k)
                {
                    //System.out.println("i " + i + "j " + j + "k " + k);
                    layerList.get(i).neuronList.get(j).weightList.set(k, weights.get(counter++));
                }
            }
        }
    }

    // total number of weights needed for the net
    public int getNumWeights(){
        int weights = 0;

        for (int i = 0; i < numHiddenLayers + 1; i++)
        {
            for (int j = 0; j < layerList.get(i).numNeurons; j++)
            {
                for (int k = 0; k < layerList.get(i).neuronList.get(j).numInputs; k++)
                {
                    weights++;
                }
            }
        }

        return weights;
    }

    // Given an input list, calculates the output list
    public ArrayList<Double> update (ArrayList<Double> inputs)
    {
        ArrayList<Double> outputs = new ArrayList<>();

        int counter;

        // check correct number of outputs
        if (inputs.size() != numInputs)
        {
            // return empty list
            return outputs;
        }


        // for each layer
        for (int i = 0 ; i < numHiddenLayers + 1; i++)
        {
            if (i > 0)
            {
                setArrayList(outputs, inputs);
            }

            outputs.clear();
            counter = 0;

            // for each neuron sum the (inputs * weight). input into sigmoid function
            for (int j = 0; j < layerList.get(i).numNeurons; j++)
            {
                double netInput = 0;

                int iNumInputs = layerList.get(i).neuronList.get(j).numInputs;

                // for each weight
                for (int k = 0; k < iNumInputs - 1; k++)
                {
                    // sum the weights x inputs
                    netInput += layerList.get(i).neuronList.get(j).weightList.get(k) * inputs.get(counter++);
                }

                // add in bias
                netInput += layerList.get(i).neuronList.get(j).weightList.get(iNumInputs - 1) * params.bias;

                // store the outputs from each layer as we generate them
                // The combined activation is first filtered through the sigmoid function
                outputs.add(Sigmoid(netInput, params.activationResponse));

                counter = 0;
            }
        }

        return outputs;
    }

    public Double Sigmoid(double netInput, double response)
    {
        return (1 / (1 + Math.exp(-netInput / response)));
    }

    public void setArrayList(ArrayList<Double> source, ArrayList<Double> dest)
    {
        dest.clear();

        for (int i = 0; i < source.size(); i++)
        {
            dest.add(source.get(i));
        }
    }

}
