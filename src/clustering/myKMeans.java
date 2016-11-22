/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package clustering;

import java.util.ArrayList;
import java.util.Collections;

/**
 *
 * @author Randi
 */

public class myKMeans {
    ArrayList<ArrayList<Double>> centroidList;
    ArrayList<ArrayList<ArrayList<Double>>> clusterList;
    int totalEpoch;
    
    public Double countEuDist(ArrayList<Double> first, ArrayList<Double> second){
        Double distance = 0.0;
        for (int i = 0 ; i<first.size() ; i++){
            distance += Math.pow((second.get(i) - first.get(i)),2);
        }
        return Math.sqrt(distance);
    }
    
    public ArrayList<Double> countNewCentroids(ArrayList<ArrayList<Double>> dataset){
        ArrayList<Double> newCentroids = new ArrayList();
        for (int i = 0 ; i < dataset.get(0).size() ; i++){
            Double sum = 0.0;
            for (int j = 0 ; j < dataset.size() ; j++){
                sum += dataset.get(j).get(i);
            }
            newCentroids.add(sum/dataset.size());
        }
        
        return newCentroids;
    }
    
    public void buildCluster(ArrayList<ArrayList<Double>> dataset, int totalCluster){
        centroidList = new ArrayList();
        totalEpoch = 0;
        
        //menginisiasi centroid secara random
        ArrayList<Integer> randomCentroid = new ArrayList<Integer>();
        for (int i=0; i<dataset.size(); i++) {
            randomCentroid.add(new Integer(i));
        }
        Collections.shuffle(randomCentroid);
        System.out.println("Initiate Centroid");
        for (int i=0; i<totalCluster; i++) {
            System.out.println("Centroid Cluster-"+(i+1)+": "+dataset.get(randomCentroid.get(i)));
            centroidList.add(dataset.get(randomCentroid.get(i)));
        }
        
        ArrayList<ArrayList<Double>> prevClusterList = new ArrayList();
        int minDistanceClusterIndex = 0;
        Double minDistanceToCentroid = 0.0;
        
        while (true){
            clusterList = new ArrayList();
            for (int i = 0;i<totalCluster;i++){
                clusterList.add(new ArrayList());
            }
            
            for (int i = 0;i<dataset.size();i++){
                minDistanceClusterIndex = 0;
                minDistanceToCentroid = countEuDist(dataset.get(i), centroidList.get(0));
                for (int j = 0;j<centroidList.size();j++){
                    Double newDistance = countEuDist(dataset.get(i), centroidList.get(j));
                    if (newDistance < minDistanceToCentroid){
                        minDistanceClusterIndex = j;
                        minDistanceToCentroid = newDistance;
                    }
                }
                clusterList.get(minDistanceClusterIndex).add(dataset.get(i));
            }
            
            if(!clusterList.equals(prevClusterList)){
                totalEpoch++;
                prevClusterList = new ArrayList(clusterList);
            }else{
                break;
            }
            
            for (int i = 0;i<centroidList.size();i++){
                if(clusterList.get(i).size() > 0){
                    centroidList.set(i, countNewCentroids(clusterList.get(i)));
                }
            }
        }
    }
    
    public void showClusterList(){
        System.out.println("Total Epoch to Converge: " + totalEpoch);
        for(int i=0; i<clusterList.size(); i++){
            System.out.println("CLUSTER-"+ (i+1) + ": ");
            System.out.println("  Centroids: " + centroidList.get(i)); 
            System.out.print("  Members: ");
            for(int j=0; j<clusterList.get(i).size(); j++){
                System.out.print(clusterList.get(i).get(j)+" ");
            }
            System.out.println("");
        }
    }
}
