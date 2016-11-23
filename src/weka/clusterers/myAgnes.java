/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package weka.clusterers;

import java.util.ArrayList;
import weka.core.*;

/**
 *
 * @author user
 */
public class myAgnes extends AbstractClusterer{
    private int numOfClusters = 2;
    private String type;
    private Instances instances;
    private ArrayList<ArrayList<Instance>> clusters;
    private ArrayList<ArrayList<Double>> clusterDistances;
    
    protected DistanceFunction distanceFunction = new EuclideanDistance();
    
    public DistanceFunction getDistanceFunction(){
        return this.distanceFunction;
    };
    
    public void setDistanceFunction(DistanceFunction distanceFunction) {
        this.distanceFunction = distanceFunction;
    }
    
    public myAgnes(int numOfClusters, String type) {
        super();
        this.numOfClusters = numOfClusters;
        this.type = type;
        clusters = new ArrayList<ArrayList<Instance>>();
        clusterDistances = new ArrayList<ArrayList<Double>>();
    }
    
    @Override
    public void buildClusterer(Instances data) throws Exception {
        for (int i = 0; i < data.numInstances(); i++) {
            ArrayList<Instance> instance = new ArrayList<Instance>();
            instance.add(data.instance(i));
            clusters.add(instance);
        }
        System.out.println("Clusters  : " + clusters.size());
        
        distanceFunction.setInstances(data);
        for (int i = 0; i < clusters.size(); i++) {
            ArrayList<Double> distances = new ArrayList<Double>();
            for (int j = 0; j < clusters.size(); j++) {
                Double distance = distanceFunction.distance(data.instance(i), data.instance(j));
                distances.add(distance);
            }
            clusterDistances.add(distances);
        }
        System.out.println("Cluster Distance : " + clusterDistances.size());
        
        while (clusters.size() > numberOfClusters()) {
            int c1 = -1;
            int c2 = -1;
            double min = Double.MAX_VALUE;
            for (int i = 0; i < clusters.size(); i++) {
                for (int j = i+1; j < clusters.size(); j++) {
                    if (clusterDistances.get(i).get(j) <= min) {
                        min = clusterDistances.get(i).get(j);
                        c1 = i;
                        c2 = j;
                    }
                }
            }
            merge(c1,c2);
        }
    }
    
    public void merge(int c1, int c2) {
        clusters.get(c1).addAll(clusters.get(c2));
        clusters.remove(c2);        
//        System.out.println("C1 : " + c1);
//        System.out.println("C2 : " + c2);
//        System.out.println("Clusters : " + clusters.size());
//        System.out.println("Cluster Distance : " + clusterDistances.size());
//        System.out.println("Type : " + this.type);
//        //System.out.println("Size : " + clusters.get(c1).size());
        
        for (int i = 0; i < clusterDistances.get(c1).size(); i++) {
            if (this.type.equals("SINGLE_LINK")) {
                if (clusterDistances.get(c1).get(i) > clusterDistances.get(c2).get(i)) {
                   clusterDistances.get(c1).set(i, clusterDistances.get(c2).get(i));
                }
            } else if (this.type.equals("COMPLETE_LINK")) {
               if (clusterDistances.get(c1).get(i) < clusterDistances.get(c2).get(i)) {
                   clusterDistances.get(c1).set(i, clusterDistances.get(c2).get(i));
               }
            }
        }
        
        for (int i = 0; i < clusterDistances.size(); i++) {
            clusterDistances.get(i).remove(c2);
        }
        
        clusterDistances.remove(c2);
    }
    
    @Override
    public String toString() {
        String temp = "";
        for (int i = 0; i < clusters.size(); i++) {
            temp += "Cluster " + i + " : " + clusters.get(i).size() + "\n";
            /*temp += "Instances : ";
            for (int j = 0; j < clusters.get(i).size(); j++) {
                temp += clusters.get(i).get(j);
            }*/
            temp += "\n";
        }
        return temp;
    }

    @Override
    public int numberOfClusters() throws Exception {
        return numOfClusters;
    }
    
    @Override
    public int clusterInstance(Instance instance) {
        ArrayList<Double> instanceDistances = new ArrayList<Double>();
        for (int i = 0; i < clusters.size(); i++) {
            double temp = Double.MAX_VALUE;
            for (int j = 0; j < clusters.get(i).size(); j++) {
                Double distance = distanceFunction.distance(clusters.get(i).get(j), instance);
                if (distance < temp) {
                    temp = distance;
                }
            }
            instanceDistances.add(temp);
        }
        
        Double min = instanceDistances.get(0);
        int cluster = 0;
        for (int i = 0; i < instanceDistances.size(); i++) {
            if (instanceDistances.get(i) < min) {
                min = instanceDistances.get(i);
                cluster = i;
            }
        }
        
        return cluster;
    }
}
