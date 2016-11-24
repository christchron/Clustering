/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package clustering;

import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import weka.clusterers.ClusterEvaluation;
import weka.clusterers.Clusterer;
import weka.core.Instances;
import weka.core.converters.ConverterUtils;

/**
 *
 * @author Natan
 */
public class MeansAgnes {
    private Instances data;
    private Clusterer model;
    private int clusterer;
    
    public MeansAgnes(){
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
                model = new myAgnes(2, "COMPLETE_LINK");
                break;
            case 2  :
                break;
            default:
                break;
        }
        try {
            model.buildClusterer(train);
//            System.out.println(model.toString());
            //return model;
        } catch (Exception ex) {
            Logger.getLogger(MeansAgnes.class.getName()).log(Level.SEVERE, null, ex);
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
   
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        String file = "";
        int clusterer;
        String testfile = "";
        MeansAgnes w = new MeansAgnes();
        Scanner scan = new Scanner(System.in);
        Clusterer model = null;
        boolean stat = true;
        while(stat){
            System.out.println("\n\nProgram Eksplorasi Weka");
            System.out.println("1. Load data set");
            System.out.println("2. Build Clusterer");
            System.out.println("3. Percentage Split");
            System.out.println("4. Exit");
            System.out.print("Pilih Menu : "); 
            int option = scan.nextInt();
            if(option == 1) {
                System.out.println("====LOAD DATA====");
                System.out.println("Pilih data yang akan digunakan:");
                System.out.println("1. Weather - Nominal");
                System.out.println("2. Weather - Kontinu");
                System.out.println("3. Iris");
                System.out.print("Nomor data : ");
                int idData = scan.nextInt();
                if(idData == 1) {
                    file = "data/weather.nominal.arff";
                    testfile = "data/weather.nominal.test.arff";
                }
                else if(idData == 2) {
                    file = "data/weather.numeric.arff";
                    testfile = "data/weather.numeric.test.arff";
                }
                else if(idData == 3){
                    file = "data/iris.arff";    
                    testfile = "data/iris.test.arff";
                }
                w.loadFile(file); 
            } else if(option == 2) {
                System.out.println("====Build Clusterer====");
                //create model
                System.out.println("Clusterer yang akan digunakan:");
                System.out.println("1. Agnes Single Link");
                System.out.println("2. Agnes Complete Link");
                System.out.println("3. K-Means");
                System.out.print("Masukan pilihan : ");
                clusterer = scan.nextInt();
                w.setClusterer(clusterer - 1);
            }else if(option == 3) {
                System.out.print("Masukan nilai percentage split : ");
                double p = scan.nextDouble();
                w.percentageSplit(p);
                //w.percentageSplit(model, p);
            }else {
                stat = false;
                System.out.println("====TERIMAKASIH :D====");
            }
        }
    }
}
