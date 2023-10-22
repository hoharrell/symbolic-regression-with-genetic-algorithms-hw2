import java.io.BufferedReader;  
import java.io.FileReader;  
import java.io.IOException;  
import java.util.*;
public class Main
{  
public static void main(String[] args)   
{  
    String newLine = "";  
    String comma = ",";  
    ArrayList<ArrayList<String>> arr = new ArrayList<ArrayList<String>>();  
    try   
    {  
    BufferedReader br = new BufferedReader(new FileReader("dataset1.csv"));
    while ((newLine = br.readLine()) != null)   //returns a Boolean value  
    {
        ArrayList<String> subArr = new ArrayList<String>();
        arr.add(subArr);
    String[]  vals = newLine.split(comma);    // use comma as separator
    for(String s : vals){
        subArr.add(s);
    }
    }
    Tree regression = Genetic.geneticAlgorithm(arr);  
    System.out.println(regression.inOrderTraverse());

    }   
    catch (IOException e)   
    {  
    e.printStackTrace();  
    }  

    // Tree tree1 = new Tree(4, false);
    // Tree tree2 = new Tree(4, false);
    // System.out.println("Tree 1:" + tree1.inOrderTraverse());
    // System.out.println("Tree 2: " + tree2.inOrderTraverse());
    // tree1.simplify();
    // tree2.simplify();
    // Tree mutatedTree = tree1.mutate();
    // System.out.println(mutatedTree.inOrderTraverse());
    // Tree[] arr = Tree.crossover(tree1, tree2);
    // System.out.println("Simplified Tree 1:" + tree1.inOrderTraverse());
    // System.out.println("Simplified Tree 2: " + tree2.inOrderTraverse());
    // System.out.println(arr[0].inOrderTraverse());
    // System.out.println(arr[1].inOrderTraverse());


 }


} 
