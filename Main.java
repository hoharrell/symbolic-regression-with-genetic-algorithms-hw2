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
    arr.remove(0);
   // arr = preProcess(arr);

    Tree regression = Genetic.geneticAlgorithm(arr, false,false);  
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

 public static class Data implements Comparable<Data>{
    ArrayList<String> arr;

    Data(ArrayList<String> arr)
    {
        this.arr = arr;
    }

    public int compareTo(Data other)
    {
        double one = Double.parseDouble(arr.get(3));
        double two = Double.parseDouble(other.arr.get(3));
        if(two-one < 0)
            return -1;
        if(two-one == 0)
            return 0;

        return 1;
    }
 }
 

 public static ArrayList<ArrayList<String>> preProcess(ArrayList<ArrayList<String>> data)
 {

    Double i;
    Double j;
    for(ArrayList<String> arr : data)
    {
        i = Double.parseDouble(arr.get(0));
        j = Double.parseDouble(arr.get(1));
        i/=Math.pow(10,6);
        j/=Math.pow(10,6);
        arr.remove(0);
        arr.remove(1);
        arr.add(0,j.toString());
        arr.add(0,i.toString());
    }


    PriorityQueue<Data> arr = new PriorityQueue<Data>();
    for(ArrayList<String> arr2 : data)
    {
        arr.add(new Data(arr2));
    }

    ArrayList<ArrayList<String>> processed = new ArrayList<ArrayList<String>>();
    while(!arr.isEmpty())
    {
        processed.add(arr.remove().arr);
    }
    ArrayList<ArrayList<String>> result = new ArrayList<ArrayList<String>>();
    
    for(int x=0; x<processed.size(); x++)
    {
        
        if((x > processed.size()/4) && (x < 3*processed.size()/4)){
            result.add(processed.get(x));

        }
    }

    System.out.println(result.size());
    return result;



 }


} 
