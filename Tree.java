import java.util.*;

public class Tree implements Comparable<Tree>{
    public double fitness;//fitness score which can be assigned by evaluate
    public Node root;//root node of tree
    public ArrayList<Node> nodes;//all nodes currently in tree
    boolean bonusOperators;//if true, operators will include logx, sinx, cosx, e^x
    // changed here
    ArrayList<String> operators; //operators this tree can choose from
    ArrayList<String> constants;//constants this tree can choose from
    boolean bonusFields; //if true, then constants will include x_1, x_2, x_3

    /*
     * Helper class to store values of nodes
     * and pointers to parents/children.
     */
    public static class Node {

        String type;//operator, constant, or variable
        String val;//whatever value is stored
        double value;//if constant, this will be the constant's value
        int descendants;//number of node descendants
        boolean bonusOperator;//if this node is a bonus operator which can only have one child

        Node parent;
        Node leftChild;
        Node rightChild;

        //Node constructor, given the symbol assigns fields properly
        Node(String symbol) {
            this.val = symbol;
            this.descendants = 0;
            try {
                this.bonusOperator = false;
                double i = Double.parseDouble(symbol);
                // value is an Integer
                this.type = "constant";
                this.value = i;

            } catch (NumberFormatException e) {
                if (symbol.charAt(0) == 'x') {
                    this.type = "variable";
                } else {
                    this.type = "operator";
                    if(this.val.equals("e^") || this.val.equals("log") || this.val.equals("cos") || this.val.equals("sin"))
                        this.bonusOperator = true;
                }
            }
        }

        public String toString() {
            return "node has a value " + val;
        }
    }

    //Adds a node to the tree
    public Node addNode(String val, Node parent) {
        Node newNode = new Node(val);
        newNode.parent = parent;
        if (parent.leftChild == null) {
            parent.leftChild = newNode;
        } else {
            parent.rightChild = newNode;
        }
        nodes.add(newNode);
        return newNode;
    }


    /*
     * Tree constructor which, given a root node, builds a new tree with
     * just that root node, populating all fields according to
     * Contains operators logx, sinx, cosx, and e^x if bonus operators is true
     * Contains variables x_1,x_2,x_3 is bonus fields is true, otherwise just x
     */
    public Tree(Node root, boolean bonusOperators, boolean bonusFields) {
        this.bonusOperators = bonusOperators;
        this.bonusFields = bonusFields;
        this.root = root;
        operators = new ArrayList<String>();
        constants = new ArrayList<String>();
        nodes = new ArrayList<Node>();
        for (int i = 1; i < 10; i++) {
            constants.add("" + i);
        }
        if(!bonusFields){
            for (int i = 0; i < 6; i++) {
                constants.add("x");
            }
        }
        else{
            for(int i=0; i<3; i++)
            {
                constants.add("x1");
                constants.add("x2");
                constants.add("x3");
            }
        }
        if (bonusOperators) {
            operators.add("e^");
            operators.add("sin");
            operators.add("cos");
            operators.add("log");
        }
        operators.add("*");
        operators.add("+");
        operators.add("-");
        operators.add("/");
    }
    /*
     * Creates a random tree of depth "depth"
     * Contains operators logx, sinx, cosx, and e^x if bonus operators is true
     * Contains variables x_1,x_2,x_3 is bonus fields is true, otherwise just x
     */
    public Tree(int depth, boolean bonusOperators, boolean bonusFields) {
        operators = new ArrayList<String>();
        constants = new ArrayList<String>();
        nodes = new ArrayList<Node>();
        this.bonusFields = bonusFields;
        this.bonusOperators = bonusOperators;
        for (int i = 1; i < 10; i++) {
            constants.add("" + i);
        }
        if(!bonusFields){
            for (int i = 0; i < 6; i++) {
                constants.add("x");
            }
        }
        else{
            for(int i=0; i<3; i++)
            {
                constants.add("x1");
                constants.add("x2");
                constants.add("x3");
            }
        }
        if (bonusOperators) {
            operators.add("e^");
            operators.add("sin");
            operators.add("cos");
            operators.add("log");
        }
        operators.add("*");
        operators.add("+");
        operators.add("-");
        operators.add("/");
        this.root = new Node(operators.get((int) (Math.random() * operators.size())));
        nodes.add(this.root);
        populateToDepth(operators, constants, depth, root);
    }

    /*
     * Creates a tree of depth between one and the given depth
     * need to also account for generating something that becomes zero
     * and expressions that only take one parameter
     * need to also account for multiple x values
     */
    public void populateToDepth(ArrayList<String> operators, ArrayList<String> constants, int depth, Node node) {
        if (node.type != "operator") {
            return;
        }
        Node leftChild;
        Node rightChild;
        if (depth <= 0)
            return;
        if (depth == 1) {
            leftChild = addNode(constants.get((int) (Math.random() * constants.size())), node);
            if(!node.bonusOperator)
                rightChild = addNode(constants.get((int) (Math.random() * constants.size())), node);
        } else {
            if (Math.random() <= 0.5)
                leftChild = addNode(operators.get((int) (Math.random() * operators.size())), node);
            else {
                leftChild = addNode(constants.get((int) (Math.random() * constants.size())), node);
            }
            if(!node.bonusOperator){
            if (Math.random() <= 0.5)
                rightChild = addNode(operators.get((int) (Math.random() * operators.size())), node);
            else {
                rightChild = addNode(constants.get((int) (Math.random() * constants.size())), node);
            }
            }
        }

        populateToDepth(operators, constants, depth - 1, node.leftChild);
        if(!node.bonusOperator){
        populateToDepth(operators, constants, depth - 1, node.rightChild);
        }

    }

    //Passes value x for dataset 1 and 3 into expressionResult
    //to calculate value when x is this value
    public double expressionResult(double x) {
        return expressionResult(x, root);

    }

    //For dataset 2, where position 0,1, and 2 of vars
    //are x_1,x_2,x_3 respectively.
    public double expressionResult(double[] vars)
    {
        return expressionResult(vars, root);
    }

    //Calculates recursively the value the expression tree dictates
    //For dataset 2, where position 0,1, and 2 of vars
    //are x_1,x_2,x_3 respectively.
    public static double expressionResult(double[] vars, Node n)
    {
        if (n.type.equals("operator")) {
            String op = n.val;
            if (op.equals("*"))
                return expressionResult(vars, n.leftChild) * expressionResult(vars, n.rightChild);
            if (op.equals("+"))
                return expressionResult(vars, n.leftChild) + expressionResult(vars, n.rightChild);
            if (op.equals("/"))
                return expressionResult(vars, n.leftChild) / expressionResult(vars, n.rightChild);
            if (op.equals("-"))
                return expressionResult(vars, n.leftChild) - expressionResult(vars, n.rightChild);
            if(op.equals("e^"))
                return Math.exp(expressionResult(vars, n.leftChild));
            if(op.equals("sin"))
                return Math.sin(expressionResult(vars, n.leftChild));
            if(op.equals("cos"))
                return Math.cos(expressionResult(vars, n.leftChild));
            if(op.equals("log"))
                return Math.log10(expressionResult(vars, n.leftChild));
        }

        if (n.val.equals("x"))
            return vars[0];
        
        if (n.val.charAt(0) == 'x')
        {
            return vars[((int)n.val.charAt(1))-'1'];
        }
        
        return n.value;
    }

    public int compareTo(Tree other)
    {
        if(other.fitness-this.fitness < 0)
        return -1;

        if(other.fitness-this.fitness > 0)
        return 1;

        return 0;
    }
    //Calculates recursively the value the expression tree dictates
    //For dataset 1 and 2, where variable value is double x.
    //Starts from Node n.
    public static double expressionResult(double x, Node n) {

        if (n.type.equals("operator")) {
            String op = n.val;
            if (op.equals("*"))
                return expressionResult(x, n.leftChild) * expressionResult(x, n.rightChild);
            if (op.equals("+"))
                return expressionResult(x, n.leftChild) + expressionResult(x, n.rightChild);
            if (op.equals("/"))
                return expressionResult(x, n.leftChild) / expressionResult(x, n.rightChild);
            if (op.equals("-"))
                return expressionResult(x, n.leftChild) - expressionResult(x, n.rightChild);
            if(op.equals("e^"))
                return Math.exp(expressionResult(x, n.leftChild));
            if(op.equals("sin"))
                return Math.sin(expressionResult(x, n.leftChild));
            if(op.equals("cos"))
                return Math.cos(expressionResult(x, n.leftChild));
            if(op.equals("log"))
                return Math.log10(expressionResult(x, n.leftChild));

        }

        if (n.val.equals("x"))
            return x;
        
        return n.value;

    }

    public static String postOrderTraverse(Node focusNode) { // LeftRightVisit

        if (focusNode == null) {
            return "";
        }

        String left = postOrderTraverse(focusNode.leftChild);
        String right = postOrderTraverse(focusNode.rightChild);

        return left + right + focusNode.value;

    }

    //To make the function more legible
    public String inOrderTraverse() {
        return inOrderTraverse(root);
    }

    // Visits furthest left (value), then parent node (operator), then furthest
    // right
    public static String inOrderTraverse(Node focusNode) {

        if (focusNode == null) {
            return "";
        }

        String left = inOrderTraverse(focusNode.leftChild);
        String right = inOrderTraverse(focusNode.rightChild);
        return "(" + left + focusNode.val + right + ")";

    }

    //Returns random node from Nodes
    public Node getRandomNode() {
        int randIndex = (int)((double)nodes.size()*Math.random());
        return nodes.get(randIndex); 

    }

    //Returns a new tree with exactly the same values as another.
    public Tree cloneTree() {
        Node cloneRoot = new Node(this.root.val);
        Tree newTree = new Tree(cloneRoot, this.bonusOperators, this.bonusFields);
        newTree.nodes.add(cloneRoot);
        cloneNodes(newTree.root, this.root, newTree);
        return newTree;
    }


    //Clones all the nodes from a given stree starting at root newroot.
    public void cloneNodes(Node newRoot, Node oldRoot, Tree newTree) {
        if (oldRoot != null) {
            if (oldRoot.leftChild != null) {
                newTree.addNode(oldRoot.leftChild.val, newRoot);
            }
            if (oldRoot.rightChild != null) {
                newTree.addNode(oldRoot.rightChild.val, newRoot);
            }
            cloneNodes(newRoot.leftChild, oldRoot.leftChild, newTree);
            cloneNodes(newRoot.rightChild, oldRoot.rightChild, newTree);
        }
    }

    //Performs the crossing over of the genetic algorithm
    //by swapping subtrees from two random nodes.
    public static Tree[] crossover(Tree self, Tree other) {
        Tree selfClone = self.cloneTree();
        Tree otherClone = other.cloneTree();
        Node selfFocusNode = selfClone.getRandomNode();
        Node otherFocusNode = otherClone.getRandomNode();
        while(selfFocusNode.parent == null)
        {
            if(selfClone.nodes.size() == 1)
            {
                Tree[] arr = {selfClone, otherClone};
                return arr;
            }

            selfFocusNode = selfClone.getRandomNode();
            
        }
        while(otherFocusNode.parent == null)
        {
            if(otherClone.nodes.size() == 1)
            {
                Tree[] arr = {selfClone, otherClone};
                return arr;
            }
            otherFocusNode = otherClone.getRandomNode();
        }
        Node selfParent = selfFocusNode.parent;
        Node otherParent = otherFocusNode.parent;

        if (selfFocusNode == selfParent.leftChild) {
            selfParent.leftChild = otherFocusNode;
        } else {
            selfParent.rightChild = otherFocusNode;
        }
        if (otherFocusNode == otherParent.leftChild) {
            otherParent.leftChild = selfFocusNode;
        } else {
            otherParent.rightChild = selfFocusNode;
        }
        selfClone.nodes = new ArrayList<Node>();
        otherClone.nodes = new ArrayList<Node>();
        updateNodes(selfClone.root,selfClone);
        updateNodes(otherClone.root, otherClone);
        selfClone.simplify();
        otherClone.simplify();
        Tree[] returnArray = { selfClone, otherClone };
        return returnArray;
    }

    //Updates the nodes arraylist
    public static void updateNodes(Node node, Tree tree)
    {
        if(node == null)
            return;
        
        tree.nodes.add(node);
        updateNodes(node.leftChild, tree);
        updateNodes(node.rightChild, tree);
    }

    //Calls simplify, easier to call from outside tree class
    public void simplify()
    {
        simplify(this.root);
    }

    //Attempts to simplify the expression tree by combining constants and variables
    //that can be reduced.
    public void simplify(Node node)
    {
        if(node == null)
            return;
        
        simplify(node.leftChild);
        simplify(node.rightChild);
        double simplify;
        if(node.type.equals("operator") && node.leftChild != null && node.rightChild != null){
            if(node.leftChild.type.equals("constant") && node.rightChild.type.equals("constant"))
            {
                simplify = expressionResult(0, node);
                node.type = "constant";
                node.val = ""+simplify;
                node.value = simplify;
                nodes.remove(node.leftChild);
                nodes.remove(node.rightChild);
                node.leftChild = null;
                node.rightChild = null;
                return;
            }
            if(node.val.equals("*") && node.leftChild.value == 1.0)
            {
                
                nodes.remove(node);
                nodes.remove(node.leftChild);
                if(node == root)
                    root = node.rightChild;
                else
                    node.rightChild.parent = node.parent;
                node = node.rightChild;
                return;                
            }
            if(node.val.equals("*") && node.rightChild.value == 1.0)
            {
                nodes.remove(node);
                nodes.remove(node.rightChild);
                if(node == root)
                    root = node.leftChild;
                else
                    node.leftChild.parent = node.parent;
                node = node.leftChild;
                
                return;                
            }

            if(node.val.equals("-") && (node.rightChild.type.equals("constant") && node.rightChild.value == 0.0))
            {
                nodes.remove(node);
                nodes.remove(node.rightChild);
                if(node == root)
                    root = node.leftChild;
                else
                    node.leftChild.parent = node.parent;
                node = node.leftChild;
                
                return;                    
            }

            if(node.val.equals("*") && ((node.rightChild.type.equals("constant") && node.rightChild.value == 0.0) || (node.leftChild.type.equals("constant") && node.leftChild.value == 0.0)))
            {
                node.type = "constant";
                node.val = ""+0;
                node.value = 0;
                nodes.remove(node.leftChild);
                nodes.remove(node.rightChild);
                node.leftChild = null;
                node.rightChild = null;
                return;
            }
            if(node.leftChild.val.equals(node.rightChild.val) && (!node.rightChild.type.equals("operator")))
            {
                if(node.val.equals("-"))
                {
                node.type = "constant";
                node.val = ""+0;
                node.value = 0;
                nodes.remove(node.leftChild);
                nodes.remove(node.rightChild);
                node.leftChild = null;
                node.rightChild = null;
                return;
                }
                if(node.val.equals("/"))
                {
                node.type = "constant";
                node.val = ""+1;
                node.value = 1;
                nodes.remove(node.leftChild);
                nodes.remove(node.rightChild);
                node.leftChild = null;
                node.rightChild = null;
                return;
                }

            }
    }


        
    }

    /*
     * Mutates the value of some random node from nodes.
     * Maintains type.
     */
    public Tree mutate() {
        Tree  clone = this.cloneTree();
        Node node = clone.getRandomNode();
        if(node.type.equals("operator"))
        {
            if(bonusOperators)
            {
                if(node.bonusOperator)
                {
                    node.val = operators.get((int)(4*Math.random()));
                }
                else
                {
                    node.val = operators.get(4+(int)(4*Math.random()));
                }
            }
            else{
                node.val = operators.get((int)(operators.size()*Math.random()));
            }
        }
        else
        {
            node.val = constants.get((int)(constants.size()*Math.random()));
            if(!(node.val.charAt(0) == 'x'))
                node.value = Double.parseDouble(node.val);

        }
        clone.simplify();
        return clone;
    }

}
