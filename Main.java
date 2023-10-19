import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

public class Main {
    double MUTATION_PROB = 0.1;
    double CROSSOVER_PROB = 0.6;

    public static void main(String[] args) {
        String newLine = "";
        String comma = ",";
        ArrayList<ArrayList<String>> arr = new ArrayList<ArrayList<String>>();
        try {
            BufferedReader br = new BufferedReader(new FileReader("dataset1.csv"));
            while ((newLine = br.readLine()) != null) // returns a Boolean value
            {
                ArrayList<String> subArr = new ArrayList<String>();
                arr.add(subArr);
                String[] vals = newLine.split(comma); // use comma as separator
                for (String s : vals) {
                    subArr.add(s);
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

}
