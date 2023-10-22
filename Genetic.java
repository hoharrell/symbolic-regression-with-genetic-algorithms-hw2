import java.util.*;

//Should be a set of static methods
//
public class Genetic{

    public static final int DEPTH = 4;
    public static final int GENERATIONS = 5;

    public static Tree geneticAlgorithm(ArrayList<ArrayList<String>> data, int populationSize)
    {

        ArrayList<Tree> parents = new ArrayList<Tree>();
        Tree temp;
        for(int i=0; i<populationSize; i++)
        {
            temp = new Tree(DEPTH, false);
            parents.add(temp);
            temp.fitness =  evaluate(data, temp, data.size()/4);
        }
        ArrayList<Tree> children;
        temp = new Tree(DEPTH, false);
        for(int i=0; i<GENERATIONS; i++ )
        {
            children = new ArrayList<Tree>();

            while(children.size() < parents.size()){
                System.out.println("Children: " + children.size() + " and parents: " + parents.size());
                Tree[] arr = tournamentSelect(parents, (int)(.2*populationSize), data);
                arr = Tree.crossover(arr[0],arr[1]);
                Tree child = arr[(int)(2*Math.random())];
                if(Math.random() > .95)
                    child.mutate();
                
                if(child.fitness > temp.fitness)
                    temp = child;
                
                children.add(child);
            }
            System.out.println(i);
            parents = children;
            
        }
        return temp;

    }


    public static Tree[] tournamentSelect(ArrayList<Tree> trees, int tournamentSize, ArrayList<ArrayList<String>> data)
    {
        Tree[] arr = new Tree[2];
        Set<Tree> players = new HashSet<Tree>();
        Tree fittest = trees.get(0);
        Tree temp;
        int fitness = 0;
        double upset = .98;

        while(players.size()<tournamentSize)
        {
            temp = trees.get((int)(Math.random()*trees.size()));
            players.add(temp);
            if(temp.fitness > fitness){
                fittest = temp;
            }
            if(Math.random() > upset)
            {
                fittest = temp;
                break;
            }
        }
        trees.remove(fittest);
        arr[0] = fittest;
        players = new HashSet<Tree>();
        fittest = trees.get(0);
        fitness = 0;

        while(players.size()<tournamentSize)
        {
            temp = trees.get((int)(Math.random()*trees.size()));
            players.add(temp);
            if(temp.fitness > fitness){
                fittest = temp;
            }
            if(Math.random() > upset)
            {
                fittest = temp;
                break;
            }
        }
        arr[1] = fittest;
        trees.add(arr[0]);

        return arr;
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
            actual = Double.parseDouble(data.get(i).get(1));
            guess = tree.expressionResult(Double.parseDouble(data.get(i).get(0)));
            error+=((actual-guess)*(actual-guess));

        }
        return error/cap;

    }
}
