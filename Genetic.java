import java.util.*;

//Should be a set of static methods
//
public class Genetic{

    public static final int DEPTH = 4; //maximum depth of randomly generated trees
    public static final int GENERATIONS = 30; //number of generations run for
    public static final double PERCENT_SELECTION = .7; //this portion of the children will be selected using tournament selection and the rest are randomly generated to increase diversity
    public static final double ERROR_PORTION = .25; //portion of dataset that's being checked to avoid overfitting hasn't been implemented yet, overfitting hasn't yet been an issue
    public static final double MUTATION_RATE = .95; //what Math.Random has to beat for a mutation
    public static final double UPSET_RATE = .95; //what Math.Random has to beat to allow an upset in tournament selection
    public static final double TOURNAMENT_PERCENT = .2; //percent of population which participates in tournament selection
    public static final int POPULATION_SIZE = 5000; //size of population
    public static Tree geneticAlgorithm(ArrayList<ArrayList<String>> data)
    {

        ArrayList<Tree> parents = new ArrayList<Tree>();
        Tree temp;
        Tree best;
        while(parents.size() < POPULATION_SIZE)
        {
            temp = new Tree(DEPTH, false);
            temp.fitness =  evaluate(data, temp, (int)(ERROR_PORTION*data.size()));
            if(Double.isFinite(temp.fitness))
                parents.add(temp);
        }
        ArrayList<Tree> children;
        best = parents.get(0);
        for(int i=0; i<GENERATIONS; i++ )
        {
            children = new ArrayList<Tree>();

            //best range between 5-7/10
            //experiment with portion which is composed by selection and change
            while(children.size() < PERCENT_SELECTION*parents.size()){
            //dial in portion for tournament select
                Tree[] arr = tournamentSelect(parents, (int)(TOURNAMENT_PERCENT*POPULATION_SIZE), data);
                arr = Tree.crossover(arr[0],arr[1]);
                Tree child = arr[(int)(2*Math.random())];
                //possibly tinker with mutation rate.
                if(Math.random() > MUTATION_RATE)
                    child = child.mutate();
                
                    //evaluating by 1/4 the data to avoid overfitting
                child.fitness = evaluate(data, child, (int)(data.size()*ERROR_PORTION));

                //
                if(Double.isFinite(child.fitness) && child.fitness < best.fitness)
                    best = child;
                
                if(Double.isFinite(best.fitness))
                    children.add(child);
            }
            while(children.size()<parents.size())
            {
                temp = new Tree(DEPTH, false);
                temp.fitness =  evaluate(data, temp, (int)(data.size()*ERROR_PORTION));
                if(Double.isFinite(temp.fitness)){
                    children.add(temp);
                    if(temp.fitness < best.fitness)
                        best = temp;

                }
            }
            System.out.println("Current Generation: " + i + " with fitness " + best.fitness + "from function" + best.inOrderTraverse() + " and size " + children.size());
            parents = children;
            
        }
        return best;

    }


    public static Tree[] tournamentSelect(ArrayList<Tree> trees, int tournamentSize, ArrayList<ArrayList<String>> data)
    {
        Tree[] arr = new Tree[2];
        Set<Tree> players = new HashSet<Tree>();
        Tree fittest = trees.get(0);
        Tree temp;
        double bestFitness = 0;
        double upset = UPSET_RATE;

        while(players.size()<tournamentSize)
        {
            temp = trees.get((int)(Math.random()*trees.size()));
            players.add(temp);
            if(temp.fitness < bestFitness){
                bestFitness = temp.fitness;
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
        bestFitness = 0;

        while(players.size()<tournamentSize)
        {
            temp = trees.get((int)(Math.random()*trees.size()));
            players.add(temp);
            if(temp.fitness < bestFitness){
                bestFitness = temp.fitness;
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
        if(cap > data.size())
            throw new IndexOutOfBoundsException("Choose a cap within the data set!");

        double actual = 0;
        double guess = 0;
        double error = 0;
        for(int i=1; i<cap; i++)
        {
            actual = Double.parseDouble(data.get(i).get(1));
            if(Double.parseDouble(data.get(i).get(1)) == 0.0)
                System.out.println("Fuck off!!!");
            guess = tree.expressionResult(Double.parseDouble(data.get(i).get(0)));
            error+=((actual-guess)*(actual-guess));

        }
        return error/cap;

    }
}
