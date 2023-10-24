import java.io.BufferedReader;  
import java.io.FileReader;  
import java.io.IOException;  
import java.util.*;
public class Main
{  
public static void main(String[] args)   
{  
    //Beginning of testing part2
    String newLine = "";  
    String comma = ",";  
    ArrayList<ArrayList<String>> arr = new ArrayList<ArrayList<String>>();  
    try   
    {  
    BufferedReader br = new BufferedReader(new FileReader("HW2-main\\dataset3.csv"));
    while ((newLine = br.readLine()) != null)   //returns a Boolean value  
    {
        ArrayList<String> subArr = new ArrayList<String>();
        arr.add(subArr);
    String[]  vals = newLine.split(comma);    // use comma as separator
    for(String s : vals){
        subArr.add(s);
    }
    }
    Tree regression = Genetic.geneticAlgorithm(arr, false,true);  
    System.out.println(regression.inOrderTraverse());

    }   
    catch (IOException e)   
    {  
    e.printStackTrace();  
    }
    
    // arr = new ArrayList<ArrayList<String>>();  
    // try   
    // {  
    // BufferedReader br = new BufferedReader(new FileReader("dataset3.csv"));
    // while ((newLine = br.readLine()) != null)   //returns a Boolean value  
    // {
    //     ArrayList<String> subArr = new ArrayList<String>();
    //     arr.add(subArr);
    // String[]  vals = newLine.split(comma);    // use comma as separator
    // for(String s : vals){
    //     subArr.add(s);
    // }
    // }
    // Tree regression = Genetic.geneticAlgorithm(arr, true);  
    // System.out.println(regression.inOrderTraverse());

    // }   
    // catch (IOException e)   
    // {  
    // e.printStackTrace();  
    // }
    //end of test part 2

    // Tree tree1 = new Tree(4, false, true);
    // Tree tree2 = new Tree(4, false, true);
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

    // Tree tree1 = new Tree(2, true, false);
    // Tree tree2 = new Tree(2, true, false);

    // System.out.println("Tree 1:" + tree1.inOrderTraverse() + " with value " + tree1.expressionResult(5));
    // System.out.println("Tree 2: " + tree2.inOrderTraverse() + " with value " + tree2.expressionResult(5));
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
