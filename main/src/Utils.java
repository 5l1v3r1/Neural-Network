import java.util.ArrayList;

public class Utils {

    // Returns a random int between x and y
    public static int randInt(int x, int y)
    {
        return (int)(Math.random()*(y - x + 1) + x);
    }

    // Returns a random double between 0 and 1
    public static double randDouble()
    {
        return Math.random();
    }

    // Returns random true or false
    public static boolean randBool()
    {
        return randInt(0, 1) == 1;
    }

    public static double randomClamped()
    {
        return randDouble() - randDouble();
    }

    public static String itos(int i)
    {
        return Integer.toString(i);
    }

    public static String ftos(double d)
    {
        return Double.toString(d);
    }

    public static int stoi(String s)
    {
        return Integer.parseInt(s);
    }

    public static double stof(String s)
    {
        return Double.parseDouble(s);
    }

    public static void copyList(ArrayList copyFrom, ArrayList copyTo)
    {
        copyTo.clear();
        for (int i = 0; i < copyFrom.size(); i++)
        {
            copyTo.add(copyFrom.get(i));
        }
    }

    public static double clamp(double d, double min, double max)
    {
        if (d < min)
            return min;
        if (d > max)
            return max;

        return d;
    }
}
