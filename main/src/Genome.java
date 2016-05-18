import java.util.ArrayList;

/**
 * Created by jshan on 4/14/2016.
 */
public class Genome implements Comparable<Genome> {

    public ArrayList<Double> weightList;

    public double fitness;

    public Genome()
    {
        fitness = 0;
        weightList = new ArrayList<>();
    }

    public Genome(ArrayList<Double> weightList, double fitness)
    {
        this.weightList = weightList;
        this.fitness = fitness;
    }

    public boolean isLessThan(Genome other)
    {
        return this.fitness < other.fitness;
    }

    // Interface method
    public int compareTo(Genome o)
    {
        if (this.fitness < o.fitness)
        {
            return -1;
        }
        else if (this.fitness > o.fitness)
        {
            return 1;
        }
        else return 0;
    }
}
