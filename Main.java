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
String[] employee = newLine.split(comma);    // use comma as separator
for(String s : employee){
    subArr.add(s);
}
}  

}   
catch (IOException e)   
{  
e.printStackTrace();  
}  
}

}  
