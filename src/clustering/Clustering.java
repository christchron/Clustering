/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package clustering;

import au.com.bytecode.opencsv.CSVReader;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Edwin
 */
public class Clustering {
    
    public static void main(String[] args) throws IOException{
        
        BufferedReader input = new BufferedReader(new InputStreamReader(System.in));
        
        while (true) {
            System.out.println();
            System.out.println("==== Select Clustering Algorithm ====");
            System.out.println("1. KMeans");
            System.out.println("2. Agnes");

            System.out.print("Your choice : ");
            int option = Integer.parseInt(input.readLine());
            switch(option) {
            case 1 :
                ArrayList<ArrayList<Double>> dataset = new ArrayList<>();
                System.out.print("Input dataset: ");
                String filename = input.readLine();
                CSVReader reader = new CSVReader(new FileReader("dataset/"+filename));
                System.out.print("Total Cluster: ");
                int totalCluster = Integer.parseInt(input.readLine());
                System.out.print("Dataset with Class (yes/no)? ");
                String answer = input.readLine();
                String [] nextLine;
                while ((nextLine = reader.readNext()) != null) {
                   ArrayList<Double> instance = new ArrayList<>();
                   for(int i=0; i<nextLine.length; i++){
                        if(answer.equals("no") || answer.equals("No") || (answer.equals("yes") && i<nextLine.length-1)){ //assume class located in last attr
                            instance.add(Double.parseDouble(nextLine[i]));
                        }
                   }
                   dataset.add(instance);
                }

                myKMeans K = new myKMeans();
                K.buildCluster(dataset, totalCluster);
                K.showClusterList();
                break;
            case 2 :
//                data = resample(data);
//                System.out.println("After resample");
//                System.out.println(data.toString());
//                break;
            default :
               System.out.println("Invalid Option");
               break;
            }
        }
        
    }
}
