import java.util.*;

public class Tree {
    public Node root;
    private int depth;
    public ArrayList<Node> nodes;
    boolean bonusOperators;
    // changed here
    ArrayList<String> operators;
    ArrayList<String> constants;


    public static class Node {

        String type;
        String val;
        int value;
        int descendants;
        int leafDescendants;

        Node parent;
        Node leftChild;
        Node rightChild;

        Node(String symbol) {
            this.val = symbol;
            this.descendants = 0;
            try {
                int i = Integer.parseInt(symbol);
                // value is an Integer
                this.type = "constant";
                this.value = i;

            } catch (NumberFormatException e) {
                if (symbol == "x") {
                    this.type = "variable";
                } else {
                    this.type = "operator";
                }
            }
        }

        public String toString() {
            return "node has a value " + val;
        }
    }

    public Node addNode(String val, Node parent) {
        Node newNode = new Node(val);
        newNode.parent = parent;
        if (parent.leftChild == null) {
            parent.leftChild = newNode;
        } else {
            parent.rightChild = newNode;
        }
        increment(newNode.parent);
        incrementLeaves(newNode.parent);
        nodes.add(newNode);
        return newNode;
    }

    public Tree(Node root, boolean bonusOperators) {
        this.bonusOperators = bonusOperators;
        this.root = root;
        operators = new ArrayList<String>();
        constants = new ArrayList<String>();
        nodes = new ArrayList<Node>();
        for (int i = 1; i < 10; i++) {
            constants.add("" + i);
        }
        for (int i = 0; i < 6; i++) {
            constants.add("x");
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
     * Operators include: e^x, sin(x), cos(x), log(x), *, +, -,/
     */
    public Tree(int depth, boolean bonusOperators) {
        operators = new ArrayList<String>();
        constants = new ArrayList<String>();
        nodes = new ArrayList<Node>();
        for (int i = 1; i < 10; i++) {
            constants.add("" + i);
        }
        for (int i = 0; i < 6; i++) {
            constants.add("x");
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
        this.depth = depth;
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
            rightChild = addNode(constants.get((int) (Math.random() * constants.size())), node);
        } else {
            if (Math.random() <= 0.5)
                leftChild = addNode(operators.get((int) (Math.random() * operators.size())), node);
            else {
                leftChild = addNode(constants.get((int) (Math.random() * constants.size())), node);
            }
            if (Math.random() <= 0.5)
                rightChild = addNode(operators.get((int) (Math.random() * operators.size())), node);
            else {
                rightChild = addNode(constants.get((int) (Math.random() * constants.size())), node);
            }
        }
        // node.leftChild = leftChild;
        // leftChild.parent = node;
        // node.rightChild = rightChild;
        // rightChild.parent = node;
        // increment(node);
        // incrementLeaves(node);
        populateToDepth(operators, constants, depth - 1, node.leftChild);
        populateToDepth(operators, constants, depth - 1, node.rightChild);

    }

    public int expressionResult(int x) {
        return expressionResult(x, root);
    }

    public static int expressionResult(int x, Node n) {

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

    public Node getRandomNode() {
        int randIndex = (int)((double)nodes.size()*Math.random());
        return nodes.get(randIndex); 

    }

    public static void increment(Node focusNode) {
        focusNode.descendants++;
        if (focusNode.parent != null) {
            increment(focusNode.parent);
        }
    }

    public static void incrementLeaves(Node focusNode) {
        focusNode.leafDescendants++;
        if (focusNode.rightChild != null && focusNode.parent != null) {
            incrementLeaves(focusNode.parent);
        }
    }

    public Tree cloneTree() {
        Node cloneRoot = new Node(this.root.val);
        Tree newTree = new Tree(cloneRoot, this.bonusOperators);
        newTree.nodes.add(cloneRoot);
        cloneNodes(newTree.root, this.root, newTree);
        return newTree;
    }


    public void cloneNodes(Node newRoot, Node oldRoot, Tree newTree) {
        if (oldRoot.descendants > 0) {
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

    public static Tree[] crossover(Tree self, Tree other) {
        Tree selfClone = self.cloneTree();
        Tree otherClone = other.cloneTree();
        Node selfFocusNode = selfClone.getRandomNode();
        Node otherFocusNode = otherClone.getRandomNode();
        while(selfFocusNode.parent == null)
        {
            selfFocusNode = selfClone.getRandomNode();
        }
        while(otherFocusNode.parent == null)
        {
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
        Tree[] returnArray = { selfClone, otherClone };
        return returnArray;
    }

    public static void updateNodes(Node node, Tree tree)
    {
        if(node == null)
            return;
        
        tree.nodes.add(node);
        updateNodes(node.leftChild, tree);
        updateNodes(node.rightChild, tree);
    }

    public Tree mutate() {
        Tree  clone = this.cloneTree();
        Node node = clone.getRandomNode();
        if(node.type.equals("operator"))
        {
            node.val = operators.get((int)(operators.size()*Math.random()));
        }
        else
        {
            node.val = constants.get((int)(constants.size()*Math.random()));
            if(!(node.val.charAt(0) == 'x'))
                node.value = Integer.parseInt(node.val);

        }
        return clone;
    }
}
