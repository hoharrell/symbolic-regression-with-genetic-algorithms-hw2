import java.util.*;

public class Tree {
    public Node root;

    Tree(Node root) {
        this.root = new Node(root.symbol);
    }

    class Node {

        String type;
        String operator;
        String symbol;
        int value;
        int descendants;

        Node parent;
        Node leftChild;
        Node rightChild;

        Node(String symbol) {
            this.symbol = symbol;
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

    public void addOperator(Node operator, Node c1, Node c2) {

    }

    public String postOrderTraverse(Node focusNode) { // LeftRightVisit

        if (focusNode == null) {
            return "";
        }

        String left = postOrderTraverse(focusNode.leftChild);
        String right = postOrderTraverse(focusNode.rightChild);

        return left + right + focusNode.value;

    }

    // Visits furthest left (value), then parent node (operator), then furthest
    // right
    public String inOrderTraverse(Node focusNode) {

        if (focusNode == null) {
            return "";
        }

        String left = inOrderTraverse(focusNode.leftChild);
        String right = inOrderTraverse(focusNode.rightChild);

        return left + Integer.toString(focusNode.value) + right;

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
        Node selfFocusNode = getRandomNode(selfClone.root);
        Node otherFocusNode = getRandomNode(otherClone.root);
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
        return self;
    }
}
