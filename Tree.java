import java.util.*;

public class Tree{
    public double fitness;
    public Node root;
    public ArrayList<Node> nodes;
    boolean bonusOperators;
    // changed here
    ArrayList<String> operators;
    ArrayList<String> constants;
    boolean bonusFields;


    public static class Node {

        String type;
        String val;
        double value;
        int descendants;

        Node parent;
        Node leftChild;
        Node rightChild;

        Node(String symbol) {
            this.val = symbol;
            this.descendants = 0;
            try {
                double i = Double.parseDouble(symbol);
                // value is an Integer
                this.type = "constant";
                this.value = i;

            } catch (NumberFormatException e) {
                if (symbol.charAt(0) == 'x') {
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
        nodes.add(newNode);
        return newNode;
    }

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
            for(int i=0; i<2; i++)
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
     * Operators include: e^x, sin(x), cos(x), log(x), *, +, -,/
     */
    public Tree(int depth, boolean bonusOperators, boolean bonusFields) {
        operators = new ArrayList<String>();
        constants = new ArrayList<String>();
        nodes = new ArrayList<Node>();
        this.bonusFields = bonusFields;
        for (int i = 1; i < 10; i++) {
            constants.add("" + i);
        }
        if(!bonusFields){
            for (int i = 0; i < 6; i++) {
                constants.add("x");
            }
        }
        else{
            for(int i=0; i<2; i++)
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

        populateToDepth(operators, constants, depth - 1, node.leftChild);
        populateToDepth(operators, constants, depth - 1, node.rightChild);

    }

    public double expressionResult(double x) {
        return expressionResult(x, root);
    }

    public double expressionResult(double[] vars)
    {
        return expressionResult(vars, root);
    }

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

        }

        if (n.val.equals("x"))
            return vars[0];
        
        if (n.val.charAt(0) == 'x')
        {
            return vars[((int)n.val.charAt(1))-1];
        }
        
        return n.value;
    }

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

    
    public Tree cloneTree() {
        Node cloneRoot = new Node(this.root.val);
        Tree newTree = new Tree(cloneRoot, this.bonusOperators, this.bonusFields);
        newTree.nodes.add(cloneRoot);
        cloneNodes(newTree.root, this.root, newTree);
        return newTree;
    }


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

    public static void updateNodes(Node node, Tree tree)
    {
        if(node == null)
            return;
        
        tree.nodes.add(node);
        updateNodes(node.leftChild, tree);
        updateNodes(node.rightChild, tree);
    }

    public void simplify()
    {
        simplify(this.root);
    }

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
                node.value = Double.parseDouble(node.val);

        }
        clone.simplify();
        return clone;
    }

}
