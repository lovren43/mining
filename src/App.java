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
    Scanner sc= new Scanner(System.in);  
    System.out.println("please enter minimum support");
    double minimumSupport=sc.nextDouble();
    System.out.println("please enter minimum confidence");
    double minimumConfidence=sc.nextDouble();
    ArrayList<String> data = new ArrayList<>();
    ArrayList<ArrayList> cleanedData = new ArrayList<>();
    readFile(data);
    cleanedData = cleanData(data);
    int numberOfTransactions = cleanedData.size();
    Set<String> country = new HashSet<String>();
    Set<String> roomType = new HashSet<String>();
    Set<String> reservationType = new HashSet<String>();
    HashMap<String, Integer> countryCount = new HashMap<String, Integer>();// Creating HashMap
    HashMap<String, Integer> roomTypeCount = new HashMap<String, Integer>();// Creating HashMap
    HashMap<String, Integer> reservationTypeCount = new HashMap<String, Integer>();
    createSets(country, roomType, reservationType, cleanedData);
    createCount(countryCount, roomTypeCount, reservationTypeCount, cleanedData);
    applyMinimumSupport(roomTypeCount, minimumSupport,numberOfTransactions);
    Set<String> supportedCountry = new HashSet<String>();
    supportedCountry=applyMinimumSupport(countryCount, minimumSupport,numberOfTransactions);
    Set<String> supportedRoomType = new HashSet<String>();
    supportedRoomType=applyMinimumSupport(roomTypeCount, minimumSupport,numberOfTransactions);
    Set<String> supportedReservationType = new HashSet<String>();
    supportedReservationType=applyMinimumSupport(reservationTypeCount, minimumSupport,numberOfTransactions);
    Set<Set>combinedSet= new HashSet<Set>();
    combinedSet=combineSets(supportedCountry, supportedRoomType);
    combinedSet.addAll(combineSets(supportedRoomType, supportedReservationType));
    combinedSet.addAll(combineSets(supportedCountry, supportedReservationType));
    HashMap<Set, Integer> compinedSetCount=new HashMap<Set,Integer>();
    compinedSetCount=createCountOfSets(cleanedData, combinedSet);
    ArrayList<Set> supportedCompinedSet=new ArrayList<Set>();
    supportedCompinedSet=applyMinimumSupportOnCompination(minimumSupport, compinedSetCount, numberOfTransactions);
    HashMap<ArrayList,Integer> transcationCount=new HashMap<ArrayList,Integer>();
    transcationCount=createTransactionCount(cleanedData);
    Set<ArrayList> frequenSet=new HashSet<ArrayList>();
    frequenSet=createFrequentSets(cleanedData, supportedCompinedSet);
    System.out.println(frequenSet);
    printAssociationRules(cleanedData,frequenSet, transcationCount, compinedSetCount, countryCount, roomTypeCount, reservationTypeCount, minimumConfidence, numberOfTransactions);
  }

  public static void readFile(ArrayList<String> data) throws FileNotFoundException {
    Scanner sc = new Scanner(new File("C:/Users/lovre/Downloads/Assignment1/Assignment1/hotel_bookings.csv"));
    sc.useDelimiter("\n");
    ArrayList<String> t = new ArrayList<>();
    while (sc.hasNext()) {
      data.add(sc.next());
    }
    sc.close();
  }

  public static ArrayList<ArrayList> cleanData(ArrayList<String> data) {
    ArrayList<ArrayList> cleanedData = new ArrayList<>();
    String[] transaction;
    for (int i = 1; i < data.size(); i++) {
      transaction = data.get(i).split(",");
      ArrayList<String> cleanData = new ArrayList<>();
      cleanData.add(transaction[1]);
      cleanData.add(transaction[2]);
      cleanData.add(transaction[3]);
      cleanedData.add(cleanData);
    }
    return cleanedData;
  }

  public static void createSets(Set<String> country, Set<String> roomType, Set<String> reservationType,
      ArrayList<ArrayList> cleanedData) {
    for (int i = 0; i < cleanedData.size(); i++) {
      country.add((String) cleanedData.get(i).get(0));
      roomType.add((String) cleanedData.get(i).get(1));
      reservationType.add((String) cleanedData.get(i).get(2));
    }
  }

  public static void createCount(HashMap<String, Integer> countryCount, HashMap<String, Integer> roomTypeCount,
      HashMap<String, Integer> reservationTypeCount, ArrayList<ArrayList> cleanedData) {
    for (int i = 0; i < cleanedData.size(); i++) {
      if (countryCount.containsKey(cleanedData.get(i).get(0).toString())) {
        countryCount.put(cleanedData.get(i).get(0).toString(),
            countryCount.get(cleanedData.get(i).get(0).toString()) + 1);
      } else {
        countryCount.put(cleanedData.get(i).get(0).toString(), 1);
      }
      if (roomTypeCount.containsKey(cleanedData.get(i).get(1).toString())) {
        roomTypeCount.put(cleanedData.get(i).get(1).toString(),
            roomTypeCount.get(cleanedData.get(i).get(1).toString()) + 1);
      } else {
        roomTypeCount.put(cleanedData.get(i).get(1).toString(), 1);
      }
      if (reservationTypeCount.containsKey(cleanedData.get(i).get(2).toString())) {
        reservationTypeCount.put(cleanedData.get(i).get(2).toString(),
            reservationTypeCount.get(cleanedData.get(i).get(2).toString()) + 1);
      } else {
        reservationTypeCount.put(cleanedData.get(i).get(2).toString(), 1);
      }
    }
  }

  public static Set<String> applyMinimumSupport(HashMap<String, Integer> count, Double minimumSupport, int numberOfTransactions ) {
    Set<String> supportedSet=new HashSet<String>();
    for (HashMap.Entry<String, Integer> set : count.entrySet()) {
      if((set.getValue()/(double)numberOfTransactions)<minimumSupport){
        continue;
      }
      else{
        supportedSet.add(set.getKey());
      }
    }
    return supportedSet;
  }
  public static Set<Set> combineSets(Set<String> s1, Set<String>s2){
    Set<Set>combinedSet= new HashSet<Set>();
    for(String iteString:s1){
      for(String iteString2:s2){
        Set<String> subSet=new HashSet<String>();
        subSet.add(iteString);
        subSet.add(iteString2);
        combinedSet.add(subSet);
      }
    }
    return combinedSet;
  }
  public static HashMap<Set, Integer> createCountOfSets(ArrayList<ArrayList> cleanedData, Set<Set>combinedSet){
    HashMap<Set,Integer>combinedSetCount=new HashMap<Set,Integer>();
    for (Set iterSet: combinedSet){
      for (int i = 0; i < cleanedData.size(); i++){
        if(cleanedData.get(i).containsAll(iterSet)){
          if (combinedSetCount.containsKey(iterSet)){
            combinedSetCount.put(iterSet, combinedSetCount.get(iterSet)+1);
          }
          else{
            combinedSetCount.put(iterSet, 1);
          }
        }
      } 
    }
    return combinedSetCount;
  }
  public static ArrayList<Set> applyMinimumSupportOnCompination(double minimumSupport,HashMap<Set, Integer> combinedSetCount, int numberOfTransactions){
    ArrayList<Set> supportedCompinedSet=new ArrayList<Set>();
    for (HashMap.Entry<Set, Integer> set : combinedSetCount.entrySet()){
      if((set.getValue()/(double)numberOfTransactions)<minimumSupport){
        continue;
      }
      else{
        supportedCompinedSet.add(set.getKey());
      }
    }
    return supportedCompinedSet;
  }
  public static HashMap<ArrayList, Integer> createTransactionCount(ArrayList<ArrayList> cleanedData){
    HashMap<ArrayList,Integer> numberOfTranscation=new HashMap<ArrayList,Integer>();
    for (int i = 0; i < cleanedData.size(); i++){
      if (numberOfTranscation.containsKey(cleanedData.get(i))){
        numberOfTranscation.put(cleanedData.get(i), numberOfTranscation.get(cleanedData.get(i))+1);
      }
      else{
        numberOfTranscation.put(cleanedData.get(i), 1);
      }
    }
    return numberOfTranscation;
  }

  public static Set<ArrayList> createFrequentSets(ArrayList<ArrayList> cleanedData,ArrayList<Set> supportedCompinedSet){
    Set<ArrayList> frequenSet=new HashSet<ArrayList>();
    for (int i = 0; i < cleanedData.size(); i++){
      Set<String> s12=new HashSet<>();
      Set<String> s13=new HashSet<>();
      Set<String> s23=new HashSet<>();
      s12.add(cleanedData.get(i).get(0).toString());
      s12.add(cleanedData.get(i).get(1).toString());
      s13.add(cleanedData.get(i).get(0).toString());
      s13.add(cleanedData.get(i).get(2).toString());
      s23.add(cleanedData.get(i).get(1).toString());
      s23.add(cleanedData.get(i).get(2).toString());
      if (supportedCompinedSet.contains(s12)&&supportedCompinedSet.contains(s13)&&supportedCompinedSet.contains(s23) ){
        frequenSet.add(cleanedData.get(i));
      }
    }
    return frequenSet;
  }
  public static int countTranscation(ArrayList<String> transcation,ArrayList<ArrayList> cleanedData){
    int counter=0;
    for (int i = 0; i < cleanedData.size(); i++){
      if (cleanedData.get(i).equals(transcation)){
        counter++;
      }
    }
    return counter;
  }
  public static int counTwoStrings(String s1, String s2,ArrayList<ArrayList> cleanedData){
    int counter=0;
    for (int i = 0; i < cleanedData.size(); i++){
      if (cleanedData.get(i).contains(s1)&&cleanedData.get(i).contains(s2)){
        counter++;
      }
    }
    return counter;
  }

  public static void printAssociationRules(ArrayList<ArrayList> cleanedData,Set<ArrayList> frequenSet, HashMap<ArrayList,Integer> transcationCount, HashMap<Set,Integer>combinedSetCount,
  HashMap<String, Integer> countryCount,HashMap<String, Integer> roomTypeCount,HashMap<String, Integer> reservationTypeCount, double minimumConfidence, int numberOfTransactions ){
    for (ArrayList iterSet: frequenSet){
      double transactionSupport=countTranscation(iterSet, cleanedData)/(double)numberOfTransactions;
      double support1=countryCount.get(iterSet.get(0))/(double)numberOfTransactions;
      double support2=roomTypeCount.get(iterSet.get(1))/(double)numberOfTransactions;
      double support3=reservationTypeCount.get(iterSet.get(2))/(double)numberOfTransactions;
      double support12=counTwoStrings(iterSet.get(0).toString(), iterSet.get(1).toString(), cleanedData)/(double)numberOfTransactions;
      double support13=counTwoStrings(iterSet.get(0).toString(), iterSet.get(2).toString(), cleanedData)/(double)numberOfTransactions;
      double support23=counTwoStrings(iterSet.get(1).toString(), iterSet.get(2).toString(), cleanedData)/(double)numberOfTransactions;
      if (transactionSupport/support1>minimumConfidence){
        System.out.println(iterSet.get(0)+"=>"+iterSet.get(1)+","+iterSet.get(2)+" Confidence= "+(transactionSupport/support1));
      }
      if (transactionSupport/support2>minimumConfidence){
        System.out.println(iterSet.get(1)+"=>"+iterSet.get(0)+","+iterSet.get(2)+" Confidence= "+(transactionSupport/support2));
      }
      if (transactionSupport/support3>minimumConfidence){
        System.out.println(iterSet.get(2)+"=>"+iterSet.get(1)+","+iterSet.get(0)+" Confidence= "+(transactionSupport/support3));
      }
      if (transactionSupport/support12>minimumConfidence){
        System.out.println(iterSet.get(0).toString()+","+iterSet.get(1).toString()+"=>"+","+iterSet.get(2).toString()+" Confidence= "+(transactionSupport/support12));
      }
      if (transactionSupport/support13>minimumConfidence){
        System.out.println(iterSet.get(0).toString()+","+iterSet.get(2).toString()+"=>"+","+iterSet.get(1).toString()+" Confidence= "+(transactionSupport/support13));
      } 
      if (transactionSupport/support23>minimumConfidence){
        System.out.println(iterSet.get(1).toString()+","+iterSet.get(2).toString()+"=>"+","+iterSet.get(0).toString()+" Confidence= "+(transactionSupport/support23));
      } 
    }
  }
}
