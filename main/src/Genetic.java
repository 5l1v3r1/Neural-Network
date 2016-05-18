import java.util.ArrayList;
import java.util.Collections;

public class Genetic {

    // Parameters
    Params params = new Params();

    // list of entire population
    public ArrayList<Genome> population = new ArrayList<>();

    // population size
    int popSize;

    // amount of weights per chromosome
    int chromoLength;

    // total fitness of population
    double totalFitness;

    // best fitness of this population
    double bestFitness;

    // average fitness
    double averageFitness;

    // worst fitness
    double worstFitness;

    // keep track of fittest genome
    int fittestGenome;

    // probability that a chromosone will mutate
    // figures should be between 0.05 to 0.3
    double mutationRate;

    // probability of chromosomes crossing
    // 0.7 is 'pretty good'?
    double crossoverRate;

    // generation counter
    int generation;

    public Genetic(int popSize, double mutationRate, double crossoverRate, int numWeights)
    {
        // init params

        params.loadProperties("param.properties");

        this.popSize = popSize;
        this.mutationRate = mutationRate;
        this.crossoverRate = crossoverRate;
        this.chromoLength = numWeights;
        this.totalFitness = 0;
        this.generation = 0;
        this.fittestGenome = 0;
        this.bestFitness = 0;
        this.worstFitness = 99999999;
        this.averageFitness = 0;

        // initialize population with chromosomes with random weights and all fitnesses = 0
        for (int i = 0; i < popSize; i++)
        {
            population.add(new Genome());

            for (int j = 0; j < chromoLength; j++)
            {
                population.get(i).weightList.add(Utils.randomClamped());
            }
        }
    }

    // this runs the GA for one generation
    // returns a new population of chromosomes
    ArrayList<Genome> epoch(ArrayList<Genome> oldPop)
    {
        // assign the given population to the class's population
        population = oldPop;

        // reset the appropriate variables
        reset();

        // sort the population for elitism
        Collections.sort(population);

        // calculate best, worst, average and total fitness
        calculateBestWorstAvgTotal();

        // create temp list to store new chromosomes
        ArrayList<Genome> newPop = new ArrayList<>();

        // add in copies of the fittest genomes. must be even number or roulette sampling will fail
        if (params.numCopiesElite * params.numElite % 2 == 0)
        {
            grabNBest(params.numElite, params.numCopiesElite, newPop);
        }

        // Main GA loop

        // repeat until a new population is generated
        while (newPop.size() < popSize)
        {
            // get two chromosones
            Genome parent1 = getChromoRoulette();
            Genome parent2 = getChromoRoulette();

            // create child by crossover
            ArrayList<Double> child1 = new ArrayList<>();
            ArrayList<Double> child2 = new ArrayList<>();

            crossover(parent1.weightList, parent2.weightList, child1, child2);

            // mutate
            mutate(child1);
            mutate(child2);

            // copy into new population
            newPop.add(new Genome(child1, 0));
            newPop.add(new Genome(child2, 0));
        }

        // assign new population back into population list
        Utils.copyList(newPop, population);

        return population;
    }

    // Accessor methods
    ArrayList<Genome> getPopulation()
    {
        return this.population;
    }

    double getAverageFitness()
    {
        return totalFitness / popSize;
    }

    double getBestFitness()
    {
        return bestFitness;
    }

    // given parents and storage for the children this method performs crossover according to crossover rate
    void crossover(ArrayList<Double> parent1, ArrayList<Double> parent2, ArrayList<Double> child1, ArrayList<Double> child2)
    {
        // just return parents as offspring depenent on the rate
        // or if parents are the same
        if (Utils.randDouble() > crossoverRate || parent1.equals(parent2))
        {
            Utils.copyList(parent1, child1);
            Utils.copyList(parent2, child2);

            return;
        }

        // determine a crossover point
        int crossoverPoint = Utils.randInt(0, chromoLength - 1);

        // create the children
        for (int i = 0; i < crossoverPoint; i++)
        {
            child1.add(parent1.get(i));
            child2.add(parent2.get(i));
        }

        for (int i = crossoverPoint; i < parent1.size(); i++)
        {
            child1.add(parent2.get(i));
            child2.add(parent1.get(i));
        }
    }

    // mutates a chromosome by perturbing its weights by an amount less than maxPerturbation
    void mutate(ArrayList<Double> chromo)
    {
        // goes through the chromosome and mutate each weight dependent on the mutation rate
        for (int i = 0; i < chromo.size(); i++)
        {
            // perturb?
            if (Utils.randDouble() < mutationRate)
            {
                // add or subtract a small value to the weight
                chromo.set(i, chromo.get(i) + (Utils.randomClamped() * params.maxPerturbation));
            }
        }
    }

    // returns a chromo based on roulette wheel sampling
    Genome getChromoRoulette()
    {
        // generate a random number between 0 and total fitness count
        double slice = Utils.randDouble() * totalFitness;

        // this will be the chosen chromosome
        Genome chosen = new Genome();

        // go through the chromosome adding up the fitness so far
        double fitnessSoFar = 0;

        for (int i = 0; i < popSize; i++)
        {
            fitnessSoFar += population.get(i).fitness;

            // if the fitness so far > random number return the chromosome at this point
            if (fitnessSoFar > slice)
            {
                chosen = population.get(i);
                break;
            }
        }

        return chosen;
    }

    // used to introduce elitism (eugenics??)
    // inserts numCopies copies of the nBest most fittest genomes into a population
    void grabNBest(int nBest, int numCopies, ArrayList<Genome> popList)
    {
        // add required amount of copies of the n most fittest to the population
        while (nBest-- > 0)
        {
            for (int i = 0; i < numCopies; i++)
            {
                popList.add(population.get(popSize - 1 - nBest));
            }
        }
    }

    // Calculates the fittest and weakest genome and the average/total fitness scores
    void calculateBestWorstAvgTotal()
    {
        totalFitness = 0;

        double highestSoFar = 0;
        double lowestSoFar = 9999999;

        for (int i = 0; i < popSize; i++)
        {
            // update fitness if necessary
            if (population.get(i).fitness > highestSoFar)
            {
                highestSoFar = population.get(i).fitness;
                fittestGenome = i;
                bestFitness = highestSoFar;
            }

            // update worst if necessary
            if (population.get(i).fitness < lowestSoFar)
            {
                lowestSoFar = population.get(i).fitness;
                worstFitness = lowestSoFar;
            }

            totalFitness += population.get(i).fitness;
        }

        averageFitness = totalFitness / popSize;
    }

    // resets relevant variables to prepare for new gen
    void reset()
    {
        totalFitness = 0;
        bestFitness = 0;
        worstFitness = 0;
        averageFitness = 0;
    }
}
