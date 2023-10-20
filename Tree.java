import java.util.*;

public class Tree {
    public Node root;
    private int depth;
    // changed here
    ArrayList<String> operators;
    ArrayList<String> constants;

    Tree(Node root) {
        this.root = root;
    }

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

    public static Node addNode(String val, Node parent) {
        Node newNode = new Node(val);
        newNode.parent = parent;
        if (parent.leftChild == null) {
            parent.leftChild = newNode;
        } else {
            parent.rightChild = newNode;
        }
        increment(newNode.parent);
        incrementLeaves(newNode.parent);
        return newNode;
    }

    /*
     * Creates a random tree of depth "depth"
     * Operators include: e^x, sin(x), cos(x), log(x), *, +, -,/
     */
    public Tree(int depth, boolean bonusOperators) {
        operators = new ArrayList<String>();
        constants = new ArrayList<String>();
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
            System.out.println(Math.random());
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
            System.out.println(op);
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
        System.out.println(n.value);
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

    public static Node getRandomNode(Node root, boolean includeNonLeaves) {
        if (root.descendants == 0) {
            return root;
        }
        double flip = Math.random();
        if (includeNonLeaves) {
            System.out.println(1.0 / (root.descendants + 1));
            if (1.0 / (root.descendants + 1) > flip) {
                return root;
            }
            flip = Math.random();
            System.out.println((root.leftChild.descendants + 1.0) / root.descendants);
            if ((root.leftChild.descendants + 1.0) / root.descendants > flip) {
                return getRandomNode(root.leftChild, true);
            }
            return getRandomNode(root.rightChild, true);
        } else {
            flip = Math.random();
            double leftChildLeaves = 0;
            if (root.leftChild != null) {
                leftChildLeaves = (root.leftChild.descendants == 0) ? 1.0 : (double) root.leftChild.descendants;
            }
            if (leftChildLeaves / root.leafDescendants > flip) {
                return getRandomNode(root.leftChild, false);
            } else {
                return getRandomNode(root.rightChild, false);
            }
        }

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

    public static Tree cloneTree(Tree self) {
        Node cloneRoot = new Node(self.root.val);
        Tree newTree = new Tree(cloneRoot);
        cloneNodes(newTree.root, self.root);
        return newTree;
    }

    public static void cloneNodes(Node newRoot, Node oldRoot) {
        if (oldRoot.descendants > 0) {
            if (oldRoot.leftChild != null) {
                addNode(oldRoot.leftChild.val, newRoot);
            }
            if (oldRoot.rightChild != null) {
                addNode(oldRoot.rightChild.val, newRoot);
            }
            cloneNodes(newRoot.leftChild, oldRoot.leftChild);
            cloneNodes(newRoot.rightChild, oldRoot.rightChild);
        }
    }

    public static Tree[] crossover(Tree self, Tree other) {
        Tree selfClone = cloneTree(self);
        Tree otherClone = cloneTree(other);
        System.out.println(selfClone.inOrderTraverse());
        System.out.println(otherClone.inOrderTraverse());
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

    public static Tree mutate(Tree self) {
        Tree selfClone = cloneTree(self);
        Node leafNode = getRandomNode(selfClone.root, false);
        double flip = Math.random();
        if (flip > 0.5) {
            // change to new constant or variable
            if (leafNode == leafNode.parent.leftChild) {
                int randomIndex = (int) (Math.random() * self.constants.size());
                leafNode.parent.leftChild = new Node(self.constants.get(randomIndex));
            }
        } else {
            // change to operator and give children
            int randomIndex = (int) (Math.random() * self.operators.size());
            leafNode = new Node(self.operators.get(randomIndex));
            randomIndex = (int) (Math.random() * self.constants.size());
            leafNode.leftChild = new Node(self.constants.get(randomIndex));
            leafNode.rightChild = new Node(self.constants.get(randomIndex));
        }
        return selfClone;
    }
}
