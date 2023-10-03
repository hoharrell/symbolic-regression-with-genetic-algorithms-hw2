import java.util.*;

public class Tree {
    class Node {

        String type;
        String operator;
        int value;

        Node leftChild;
        Node rightChild;

        Node(String symbol) {
            try {
                int i = Integer.parseInt(symbol);
                // value is an Integer
                this.type = "constant";
                this.value = i;

            } catch (NumberFormatException e) {
                this.type = "operator";
                this.operator = symbol;
            }
        }

        public String toString() {
            return "node has a value " + value;
        }
    }
}