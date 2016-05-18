/**
 * Created by jshan on 3/9/2016.
 */
public class Vector2D {

    public double x, y;

    public Vector2D(double a, double b)
    {
        x = a;
        y = b;
    }

    public Vector2D(Vector2D vector)
    {
        x = vector.x;
        y = vector.y;
    }

    public Vector2D add(Vector2D rhs)
    {
        x += rhs.x;
        y += rhs.y;
        return this;
    }

    public Vector2D subtract(Vector2D rhs)
    {
        Vector2D newVec = new Vector2D(x, y);
        newVec.x -= rhs.x;
        newVec.y -= rhs.y;
        return newVec;
    }

    public Vector2D multiply(double rhs)
    {
        Vector2D newVec = new Vector2D(x, y);
        newVec.x /= rhs;
        newVec.y /= rhs;
        return newVec;
    }

    public Vector2D divide(double rhs)
    {
        x /= rhs;
        y /= rhs;
        return this;
    }

    public static Vector2D multiply(Vector2D lhs, double rhs)
    {
        Vector2D result = new Vector2D(lhs);
        result.multiply(rhs);
        return result;
    }

    public static Vector2D multiply(double lhs, Vector2D rhs)
    {
        Vector2D result = new Vector2D(rhs);
        result.multiply(lhs);
        return result;
    }

    public static Vector2D subtract(Vector2D lhs, Vector2D rhs)
    {
        Vector2D result = new Vector2D(lhs);
        result.x -= rhs.x;
        result.y -= rhs.y;
        return result;
    }

    /**
     * Returns the length of a 2d vector
     */
    public double length()
    {
        return Math.sqrt(x * x + y * y);
    }

    /**
     * Normalizes a 2d vector
     */
    public void normalize()
    {
        double length = length();

        x = x / length;
        y = y / length;
    }

    /**
     * Calculates the dot product
     */
    public static double dotProduct(Vector2D lhs, Vector2D rhs)
    {
        return lhs.x * rhs.x + lhs.y * rhs.y;
    }

    /**
     * Returns +ve if v2 is clockwise of v1, minus if counterclockwise
     */
    public static int direction(Vector2D v1, Vector2D v2)
    {
        if (v1.y * v2.x > v1.x * v2.y) {
            return 1;
        }
        else {
            return -1;
        }
    }
}
