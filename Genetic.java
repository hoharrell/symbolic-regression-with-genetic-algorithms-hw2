import java.util.*;

//Should be a set of static methods
//
public class Genetic{

    public static final int DEPTH = 5; //maximum depth of randomly generated trees. Best for Rank sort: 5
    public static final int GENERATIONS = 400; //number of generations run for
    public static final double PERCENT_SELECTION = .75; //this portion of the children will be selected using tournament selection and the rest are randomly generated to increase diversity
    public static final double ERROR_PORTION = .25; //portion of dataset that's being checked to avoid overfitting hasn't been implemented yet, overfitting hasn't yet been an issue
    public static final double MUTATION_RATE = .95; //what Math.Random has to beat for a mutation
    public static final double UPSET_RATE = .95; //what Math.Random has to beat to allow an upset in tournament selection
    public static final double TOURNAMENT_PERCENT = .2; //percent of population which participates in tournament selection
    public static final int POPULATION_SIZE = 5000; //size of population
    public static Tree geneticAlgorithm(ArrayList<ArrayList<String>> data, boolean bonusFields, boolean bonusOperators)
    {

        ArrayList<Tree> parents = new ArrayList<Tree>();
        Tree temp;
        Tree best;
        while(parents.size() < POPULATION_SIZE)
        {
            temp = new Tree(DEPTH, bonusOperators, bonusFields);
            temp.fitness =  evaluate(data, temp, (int)(ERROR_PORTION*data.size()));
            if(Double.isFinite(temp.fitness))
                parents.add(temp);
        }
        ArrayList<Tree> children;
        best = parents.get(0);
        for(int i=0; i<GENERATIONS; i++ )
        {
            children = new ArrayList<Tree>();

            parents.sort(Comparator.naturalOrder());
            //experiment with portion which is composed by selection and change
            while(children.size() < PERCENT_SELECTION*parents.size()){
            //dial in portion for tournament select

                Tree[] arr = rankSelection(parents, data);
                //Tree[] arr = tournamentSelect(parents, data);
                //Tree[] arr = rouletteSelect(parents, data);
                arr = Tree.crossover(arr[0],arr[1]);
                arr[0].fitness = evaluate(data, arr[0], (int)(data.size()*ERROR_PORTION));
                arr[1].fitness = evaluate(data, arr[1], (int)(data.size()*ERROR_PORTION));
                Tree child;
                if(arr[0].fitness < arr[1].fitness && Double.isFinite(arr[0].fitness))
                {
                    child = arr[0];
                }
                else
                    child = arr[1];

                //possibly tinker with mutation rate.
                if(Math.random() > MUTATION_RATE){
                    child = child.mutate();
                }
                
                    //evaluating by 1/4 the data to avoid overfitting
                child.fitness = evaluate(data, child, (int)(data.size()*ERROR_PORTION));

    
                if(Double.isFinite(child.fitness) && child.fitness < best.fitness)
                    best = child;
                
                if(Double.isFinite(child.fitness))
                    children.add(child);
            }
            while(children.size()<parents.size())
            {
                temp = new Tree(DEPTH, bonusOperators, bonusFields);
                temp.fitness =  evaluate(data, temp, (int)(data.size()*ERROR_PORTION));
                if(Double.isFinite(temp.fitness)){
                    children.add(temp);
                    if(temp.fitness < best.fitness)
                        best = temp;

                }
            }
            System.out.println("Current Generation: " + i + " with fitness " + best.fitness + "from function" + best.inOrderTraverse() + " and size " + children.size());
            parents = children;
            if(best.fitness == 0)
                break;
        }
        return best;

    }

    public static Tree rankSelection(ArrayList<Tree> trees, ArrayList<ArrayList<String>> data, boolean override)
    {

        int sum = 0;
        for(int i=1; i<=trees.size(); i++)
        {
            sum+=i;
        }
        int spin = (int)(sum*Math.random());
        sum = 0;
        for(int i=0; i<trees.size(); i++)
        {
            sum+=i+1;
            if(sum >= spin)
                return trees.get(i);
        }

        return null;

    }

    public static Tree[] rankSelection(ArrayList<Tree> trees, ArrayList<ArrayList<String>> data)
    {
        Tree[] arr = new Tree[2];
        arr[0] = rankSelection(trees, data, true);
        arr[1] = rankSelection(trees,data, true);
        return arr;
    }

    public static Tree rouletteSelect(ArrayList<Tree> trees, ArrayList<ArrayList<String>> data, boolean override)
    {
        double sum = 0;
        for(Tree t : trees)
        {
            if(!Double.isFinite(t.fitness))
                System.out.println("not finite");
            sum+=t.fitness;
        }
        double spin = sum*Math.random();
        sum = 0;
        for(Tree t : trees)
        {
            sum+=t.fitness;
            if(sum >= spin)
                return t;
        }
        System.out.println("hereeee" + trees.size());
        return null;
    }

    public static Tree[] rouletteSelect(ArrayList<Tree> trees, ArrayList<ArrayList<String>> data)
    {
        Tree[] arr = new Tree[2];
        arr[0] = rouletteSelect(trees, data, true);
        arr[1] = rouletteSelect(trees,data, true);
        return arr;
    }


    public static Tree[] tournamentSelect(ArrayList<Tree> trees, ArrayList<ArrayList<String>> data)
    {
        Tree[] arr = new Tree[2];
        int tournamentSize = (int)(TOURNAMENT_PERCENT*POPULATION_SIZE);
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

        //This if statement is in place to punish cases where expression tree
        //is composed of a constant
        if(tree.bonusFields)
        {
            double[] arr1 = {0,0,0};
            double[] arr2 = {1000*Math.random(),1000*Math.random(),1000*Math.random()};
            if(tree.expressionResult(arr1) == tree.expressionResult(arr2))
            {
                return Double.MAX_VALUE;
            }
        }

        double actual = 0;
        double guess = 0;
        double error = 0;
        double[] vars;
        ArrayList<String> current;
        for(int i=1; i<cap; i++)
        {
            if(!tree.bonusFields){
            actual = Double.parseDouble(data.get(i).get(1));
            guess = tree.expressionResult(Double.parseDouble(data.get(i).get(0)));
            error+=((actual-guess)*(actual-guess));
            }
            else
            {
                current = data.get(i);
                vars = new double[current.size()-1];
                for(int j=0; j<current.size()-1; j++)
                {
                    vars[j] = Double.parseDouble(current.get(j));
                }
                guess = tree.expressionResult(vars);
                actual = Double.parseDouble(current.get(current.size()-1));
                error+=((actual-guess)*(actual-guess));
            }

        }
        return error/cap;

    }
}
