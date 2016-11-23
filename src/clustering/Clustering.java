/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package clustering;

import au.com.bytecode.opencsv.CSVReader;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import weka.clusterers.ClusterEvaluation;
import weka.clusterers.Clusterer;
import weka.core.Instances;
import weka.core.converters.ConverterUtils;

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
    
    private Instances data;
    private Clusterer model;
    private int clusterer;
    
    public Clustering(){
        data = null;
        model = null;
        clusterer = 0;
    }
    
    public void setClusterer(int i){
        clusterer = i;
    }
    
    //load data (arrf dan csv)
    public void loadFile(String data_address){
        try {
            data = ConverterUtils.DataSource.read(data_address);
            System.out.println("LOAD DATA BERHASIL\n\n");
            System.out.println(data.toString() + "\n"); 
            if (data.classIndex() == -1)
            data.setClassIndex(data.numAttributes() - 1);
        } catch (Exception ex) {
            System.out.println("File gagal di-load");
        }     
    }
    
    public void buildClusterer(int type, Instances train){
        //Classifier model = null;
        switch (type) {
            case 0:
                model = new myAgnes(2, "SINGLE_LINK");
                break;
            case 1 :
                model = new myAgnes(2, "COMPLETE-LINK");
                break;
            default:
                break;
        }
        try {
            model.buildClusterer(train);
//            System.out.println(model.toString());
            //return model;
        } catch (Exception ex) {
            Logger.getLogger(Clustering.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    
    // percentage split
    public void percentageSplit(double percent){
        try {
            data.randomize(new java.util.Random(0));
            int trainSize = (int) Math.round((double) data.numInstances() * percent/100f);
            int testSize = data.numInstances() - trainSize;
            
            Instances train = new Instances(data, 0, trainSize);
            Instances test = new Instances(data, trainSize, testSize);
            
            buildClusterer(clusterer, train);

            ClusterEvaluation eval = new ClusterEvaluation();
            eval.setClusterer(model);
            eval.evaluateClusterer(test);
            System.out.println(eval.clusterResultsToString());
        } catch (Exception ex) {
            System.out.println(ex);
        }
    }
    
    public static void main(String[] args) throws IOException{
        
        BufferedReader input = new BufferedReader(new InputStreamReader(System.in));
        
        while (true) {
            System.out.println();
            System.out.println("==== Select Clustering Algorithm ====");
            System.out.println("1. KMeans");
            System.out.println("2. Agnes");

            System.out.print("Your choice : ");
            int opt = Integer.parseInt(input.readLine());
            switch(opt) {
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
                String file = "";
                int clusterer;
                String testfile = "";
                Clustering w = new Clustering();
                Scanner scan = new Scanner(System.in);
                Clusterer model = null;
                boolean stat = true;
                
                while(stat){
                    System.out.println("\n\nProgram Eksplorasi Weka");
                    System.out.println("1. Load data set");
                    System.out.println("2. Build Classifier");
                    System.out.println("3. Percentage Split");
                    System.out.println("4. Exit");
                    System.out.print("Pilih Menu : "); 
                    int option = scan.nextInt();
                    switch (option) {
                        case 1:
                            System.out.println("====LOAD DATA====");
                            System.out.println("Pilih data yang akan digunakan:");
                            System.out.println("1. Weather - Nominal");
                            System.out.println("2. Weather - Kontinu");
                            System.out.println("3. Iris");
                            System.out.print("Nomor data : ");
                            int idData = scan.nextInt();
                            switch (idData) {
                                case 1:
                                    file = "dataset/weather.nominal.arff";
                                    testfile = "dataset/weather.nominal.test.arff";
                                    break;
                                case 2:
                                    file = "dataset/weather.numeric.arff";
                                    testfile = "dataset/weather.numeric.test.arff";
                                    break;
                                case 3:
                                    file = "dataset/iris.arff";
                                    testfile = "dataset/iris.test.arff";
                                    break;
                                default:
                                    break;
                            }
                            w.loadFile(file);
                            break;
                        case 2:
                            System.out.println("====Build Classifier====");
                            //create model
                            System.out.println("Classifier yang akan digunakan:");
                            System.out.println("1. Agnes - SIMPLE LINK");
                            System.out.println("2. Agnes - COMPLETE LINK");
                            System.out.print("Masukan pilihan : ");
                            clusterer = scan.nextInt();
                            w.setClusterer(clusterer - 1);
                            break;
                        case 3:
                            System.out.print("Masukan nilai percentage split : ");
                            double p = scan.nextDouble();
                            w.percentageSplit(p);
                            //w.percentageSplit(model, p);
                            break;
                        default:
                            stat = false;
                            System.out.println("====TERIMAKASIH :D====");
                            break;
                    }
                }
                break;
            default :
               System.out.println("Invalid Option");
               break;
            }
        }
        
    }
}
