import java.util.*;

public class Tree {
    public Node root;

    class Node {

        String type;
        String operator;
        int value;
        int descendants;

        Node parent;
        Node leftChild;
        Node rightChild;

        Node(String symbol) {
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

    public void addOperator(Node operator, Node c1)
    {
        
    }
    public void addOperator(Node operator, Node c1, Node c2)
    {
        
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
        
        return left+Integer.toString(focusNode.value)+right;

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
