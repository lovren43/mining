import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Scanner;
import java.util.Set;

import javax.xml.crypto.Data;

public class App {
    public static void main(String[] args) throws Exception {
        ArrayList<String>data=new ArrayList<>();
        ArrayList<ArrayList>cleanedData=new ArrayList<>();
        readFile(data);
        cleanedData=cleanData(data);
        Set<String> country = new HashSet<String>();
        Set<String> roomType = new HashSet<String>();   
        Set<String> reservationType = new HashSet<String>();
        HashMap<String,Integer> countryCount=new HashMap<String,Integer>();//Creating HashMap    
        HashMap<String,Integer> roomTypeCount=new HashMap<String,Integer>();//Creating HashMap    
        HashMap<String,Integer> reservationTypeCount=new HashMap<String,Integer>();
        createSets(country, roomType, reservationType, cleanedData);//Creating HashMap    
        System.out.println(country);
        System.out.println(roomType);
        System.out.println(reservationType);

        Iterator itr=reservationType.iterator();
        while(itr.hasNext()){
          System.out.println(itr.next());
        }
        // for (int i=0; i<cleanedData.size(); i++){
        //   System.out.println(cleanedData.get(i).get(2));
        // }
    }


    public static void readFile(ArrayList<String> data) throws FileNotFoundException{
        Scanner sc = new Scanner(new File("C:/Users/lovre/Downloads/Assignment1/Assignment1/Book1.csv"));
    sc.useDelimiter("\n");
    ArrayList<String> t=new ArrayList<>();
    while (sc.hasNext()) {
      data.add(sc.next());
    }
    sc.close();
  }


  public static ArrayList<ArrayList> cleanData(ArrayList<String> data){
    ArrayList<ArrayList>cleanedData=new ArrayList<>();
    String[] transaction;
    for (int i=1; i<data.size(); i++){
      transaction=data.get(i).split(",");
      ArrayList<String> cleanData=new ArrayList<>();
      cleanData.add(transaction[1]);
      cleanData.add(transaction[2]);
      cleanData.add(transaction[3]);
      cleanedData.add(cleanData);
    }
    return cleanedData;
  }
  public static void createSets(Set<String> country,Set<String> roomType,Set<String> reservationType,ArrayList<ArrayList>cleanedData){
    for (int i=0; i<cleanedData.size(); i++){
      country.add((String) cleanedData.get(i).get(0));
      roomType.add((String) cleanedData.get(i).get(1)); 
      reservationType.add( (String)cleanedData.get(i).get(2)); 
    }
  }
  public static void createCount(HashMap<String,Integer> countryCount, HashMap<String,Integer> roomTypeCount, HashMap<String,Integer> reservationTypeCount, ArrayList<ArrayList>cleanedData){
    for (int i=0; i<cleanedData.size(); i++){
      
    }
  }
}
