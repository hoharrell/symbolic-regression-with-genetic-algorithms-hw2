import java.util.*;

//Should be a set of static methods
//
public class Genetic{


    public static void geneticAlgorithm(ArrayList<ArrayList<String>> data, int populationSize)
    {

        //create some initial population of trees, size populationSize

    }

    /*
     * Calculates the mean squared error
     */
    public static double evaluate(ArrayList<ArrayList<String>> data, Tree tree, int cap)
    {
        if(cap >= data.size())
            throw new IndexOutOfBoundsException("Choose a cap within the data set!");

        double actual = 0;
        double guess = 0;
        double error = 0;
        for(int i=1; i<=cap; i++)
        {
            actual = Integer.parseInt(data.get(i).get(1));
            guess = tree.expressionResult(Integer.parseInt(data.get(i).get(0)));
            error+=((actual-guess)*(actual-guess));

        }
        return error/cap;

    }
}
