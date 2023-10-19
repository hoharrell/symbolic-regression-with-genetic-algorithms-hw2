import java.util.*;

public class Tree {
    public Node root;

    class Node {

        String type;
        String operator;
        String val;
        int value;
        int descendants;

        Node parent;
        Node leftChild;
        Node rightChild;

        Node(String symbol) {
            this.val= symbol;
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
        ArrayList<String> operators = new ArrayList<String>();
        ArrayList<String> constants = new ArrayList<String>();
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
        Node temp;
        root = new Node(operators.get((int)(Math.random()*operators.size())));
        //I would actually change the way random trees are generated to be to keep randomly adding
        //operators or to eventually add a constant, but sort of stay on one path the whole time.
        populateToDepth(operators, constants, depth, root);
    }
    /*
     * Creates a tree of depth between one and the given depth
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
        populateToDepth(operators, constants, depth-1, node.leftChild);
        populateToDepth(operators, constants, depth-1, node.rightChild);
        
    }

    public String postOrderTraverse(Node focusNode) { // LeftRightVisit

        if (focusNode == null) {
            return "";
        }

        String left = postOrderTraverse(focusNode.leftChild);
        String right = postOrderTraverse(focusNode.rightChild);
        
        return left+right+focusNode.value;

    }


    //Visits furthest left (value), then parent node (operator), then furthest right
    public String inOrderTraverse(Node focusNode) {

        if (focusNode == null) {
            return "";
        }

        String left = inOrderTraverse(focusNode.leftChild);
        String right = inOrderTraverse(focusNode.rightChild);
        return left+focusNode.val+right;

    }

    public Node getRandomNode(Node root) {
        if (root.descendants == 0) {
            return root;
        }
        double flip = Math.random();
        if (1 / (root.descendants + 1) > flip) {
            return root;
        }
        flip = Math.random();
        if ((root.leftChild.descendants + 1) / root.descendants > flip) {
            return getRandomNode(root.leftChild);
        }
        return getRandomNode(root.rightChild);
    }

    public void increment(Node root) {
        if (root == this.root) {
            this.root.descendants++;
        } else {
            root.descendants++;
            increment(root.parent);
        }
    }
}
