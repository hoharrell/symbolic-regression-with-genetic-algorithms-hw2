import java.util.*;

public class Tree {
    public Node root;
    private int depth;
    //changed here
    ArrayList<String> operators;
    ArrayList<String> constants;

    Tree(Node root) {
        this.root = new Node(root.symbol);
    }

    class Node {

        String type;
        String operator;
        String val;
        int value;
        int descendants;
        int leafDescendants;
        String symbol;

        Node parent;
        Node leftChild;
        Node rightChild;

        Node(String symbol) {
            this.val = symbol;
            try {
                int i = Integer.parseInt(symbol);
                // value is an Integer
                this.type = "constant";
                this.value = i;
                this.operator = null;

            } catch (NumberFormatException e) {
                this.type = "operator";
                this.operator = symbol;
            }
            this.descendants = 0;
        }

        public String toString() {
            return "node has a value " + value;
        }
    }

    /*
     * Creates a random tree of depth "depth"
     * Operators include: e^x, sin(x), cos(x), log(x), *, +, -,/
     */
    public Tree(int depth, boolean bonusOperators)
    {
        operators = new ArrayList<String>();
        constants = new ArrayList<String>();
        for(int i=1; i<10; i++)
        {
            constants.add(""+i);
        }
        for(int i=0;i<6;i++)
        {
            constants.add("x");
        }
        if(bonusOperators){
            operators.add("e^");
            operators.add("sin");
            operators.add("cos");
            operators.add("log");
        }
        operators.add("*");
        operators.add("+");
        operators.add("-");
        operators.add("/");
        this.depth = 0;
        root = new Node(operators.get((int)(Math.random()*operators.size())));
        populateToDepth(operators, constants, depth, root);
    }

    /*
     * Creates a tree of depth between one and the given depth
     */
    public void populateToDepth(ArrayList<String> operators, ArrayList<String> constants, int depth, Node node) {
        Node leftChild;
        Node rightChild;
        if (depth <= 0)
            return;
        if (depth == 1) {
            leftChild = new Node(constants.get((int) (Math.random() * constants.size())));
            rightChild = new Node(constants.get((int) (Math.random() * constants.size())));
        } else {
            if ((int) Math.random() <= 5)
                leftChild = new Node(operators.get((int) (Math.random() * operators.size())));
            else {

                leftChild = new Node(constants.get((int) (Math.random() * constants.size())));
            }

            if ((int) Math.random() <= 5)
                rightChild = new Node(operators.get((int) (Math.random() * operators.size())));
            else {
                rightChild = new Node(constants.get((int) (Math.random() * constants.size())));
            }
        }
        node.leftChild = leftChild;
        leftChild.parent = node;
        node.rightChild = rightChild;
        rightChild.parent = node;
        populateToDepth(operators, constants, depth - 1, node.leftChild);
        populateToDepth(operators, constants, depth - 1, node.rightChild);

    }

    /*
     * Creates a tree of depth between one and the given depth
     * need to also account for generating something that becomes zero
     * and expressions that only take one parameter
     * need to also account for multiple x values
     */
    public void populateToDepth(ArrayList<String> operators, ArrayList<String> constants, int depth, Node node)
    {
        Node leftChild;
        Node rightChild;
        if(depth <= 0)
            return;
        if(depth == 1)
        {
            leftChild = new Node(constants.get((int)(Math.random()*constants.size())));
            rightChild = new Node(constants.get((int)(Math.random()*constants.size())));
        }
        else{ 
            if((int)Math.random() <= 5)
                leftChild = new Node(operators.get((int)(Math.random()*operators.size())));
            else{
                
                leftChild = new Node(constants.get((int)(Math.random()*constants.size())));
            }

            if((int)Math.random() <= 5)
                rightChild = new Node(operators.get((int)(Math.random()*operators.size())));
            else{
                rightChild = new Node(constants.get((int)(Math.random()*constants.size())));
            }
        }
        node.leftChild = leftChild;
        leftChild.parent = node;
        node.rightChild = rightChild;
        rightChild.parent = node;
        increment(node);
        populateToDepth(operators, constants, depth-1, node.leftChild);
        populateToDepth(operators, constants, depth-1, node.rightChild);

        
    }

    
    public int expressionResult(int x)
    {
        return expressionResult(x, root);
    }

    public int expressionResult(int x, Node n)
    {

        if(n.type.equals("operator"))
        {
            String op = n.val;
            System.out.println(op);
            if(op.equals("*"))
                return expressionResult(x,n.leftChild)*expressionResult(x,n.rightChild);
            if(op.equals("+"))
                return expressionResult(x,n.leftChild)+expressionResult(x,n.rightChild);
            if(op.equals("/"))
                return expressionResult(x,n.leftChild)/expressionResult(x,n.rightChild);
            if(op.equals("-"))
                return expressionResult(x,n.leftChild)-expressionResult(x,n.rightChild);
            
        }

            if(n.val.equals("x"))
                return x;
            System.out.println(n.value);
            return n.value;
            
    }
    
    public String postOrderTraverse(Node focusNode) { // LeftRightVisit

        if (focusNode == null) {
            return "";
        }

        String left = postOrderTraverse(focusNode.leftChild);
        String right = postOrderTraverse(focusNode.rightChild);

        return left + right + focusNode.value;

    }

    public String inOrderTraverse()
    {
        return inOrderTraverse(root);
    }

    //Visits furthest left (value), then parent node (operator), then furthest right
    public String inOrderTraverse(Node focusNode) {

        if (focusNode == null) {
            return "";
        }

        String left = inOrderTraverse(focusNode.leftChild);
        String right = inOrderTraverse(focusNode.rightChild);
        return "("+left+focusNode.val+right+")";

    }

    public Node getRandomNode(Node root, boolean includeNonLeaves) {
        if (root.descendants == 0) {
            return root;
        }
        double flip = Math.random();
        if (includeNonLeaves) {
            if (1 / (root.descendants + 1) > flip) {
                return root;
            }
            flip = Math.random();
            if ((root.leftChild.descendants + 1) / root.descendants > flip) {
                return getRandomNode(root.leftChild, true);
            }
            return getRandomNode(root.rightChild, true);
        } else {
            flip = Math.random();
            int leftChildLeaves = 0;
            if (root.leftChild != null) {
                leftChildLeaves = (root.leftChild.descendants == 0) ? 1 : root.leftChild.descendants;
            }
            if (leftChildLeaves / root.leafDescendants > flip) {
                return getRandomNode(root.leftChild, false);
            } else {
                return getRandomNode(root.rightChild, false);
            }
        }

    }

    public void increment(Node focusNode) {
        if (focusNode == this.root) {
            this.root.descendants++;
        } else {
            focusNode.descendants++;
            increment(focusNode.parent);
        }
    }

    public void incrementLeaves(Node focusNode) {
        focusNode.leafDescendants++;
    }

    public Tree cloneTree(Tree self) {
        Tree newTree = new Tree(self.root);
        cloneNodes(newTree.root, self.root);
        return self;
    }

    public void cloneNodes(Node newRoot, Node oldRoot) {
        if (oldRoot.descendants > 0) {
            if (oldRoot.leftChild != null) {
                newRoot.leftChild = oldRoot.leftChild;
            }
            if (oldRoot.rightChild != null) {
                newRoot.rightChild = oldRoot.rightChild;
            }
            cloneNodes(newRoot.leftChild, oldRoot.leftChild);
            cloneNodes(newRoot.rightChild, oldRoot.rightChild);
        }
    }

    public Tree[] crossover(Tree self, Tree other) {
        Tree selfClone = cloneTree(self);
        Tree otherClone = cloneTree(other);
        Node selfFocusNode = getRandomNode(selfClone.root, true);
        Node otherFocusNode = getRandomNode(otherClone.root, true);
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
        Tree[] returnArray = { selfClone, otherClone };
        return returnArray;
    }

    public Tree mutate(Tree self) {
        Tree selfClone = cloneTree(self);
        Node leafNode = getRandomNode(selfClone.root, false);
        double flip = Math.random();
        if (flip > 0.5) {
            // change to new constant or variable
            if (leafNode == leafNode.parent.leftChild) {
                int rnd = new Random().nextInt(constants.length);
                leafNode.parent.leftChild = new Node(constants[rnd]);
            }
        } else {
            // change to operator and give children
            int rnd = new Random().nextInt(operators.length);
            leafNode = new Node(operators[rnd]);
            rnd = new Random().nextInt(constants.length);
            leafNode.leftChild = new Node(constants[rnd]);
            leafNode.rightChild = new Node(constants[rnd]);
        }
        return selfClone;
    }
}
