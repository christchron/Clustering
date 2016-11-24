package clustering;

import java.util.ArrayList;
import java.util.Collections;
import weka.clusterers.AbstractClusterer;
import weka.core.*;
import weka.filters.Filter;
import weka.filters.unsupervised.attribute.ReplaceMissingValues;

/**
 *
 * @author Randi
 */

public class myKMeans extends AbstractClusterer{
    ArrayList<Instance> centroidInstanceList;
    ArrayList<ArrayList<Instance>> clusterInstanceList;
    int totalEpoch;
    int totalCluster;
    
    public void setTotalCluster(int _totalCluster){
        totalCluster = _totalCluster;
    }
    
    public Instance countNewCentroids(ArrayList<Instance> cluster){
        Instance newCentroids = new Instance(cluster.get(0).numAttributes());
        for (int i = 0 ; i < cluster.get(0).numAttributes() ; i++){
            Double sum = 0.0;
            for (int j = 0 ; j < cluster.size() ; j++){
                sum += (Double) cluster.get(j).value(i);
            }
            newCentroids.setValue(i, sum/cluster.size());
        }
        
        return newCentroids;
    }
    
    public void showClusterList(){
        System.out.println("Total Epoch to Converge: " + totalEpoch);
        for(int i=0; i<clusterInstanceList.size(); i++){
            System.out.println("CLUSTER-"+ (i+1) + ": ");
            System.out.println("  Centroids: " + centroidInstanceList.get(i)); 
            System.out.print("  Members: ");
            for(int j=0; j<clusterInstanceList.get(i).size(); j++){
                System.out.print("["+clusterInstanceList.get(i).get(j)+"] ");
            }
            System.out.println("");
        }
    }
    
    @Override
    public void buildClusterer(Instances data) throws Exception {
        centroidInstanceList = new ArrayList();
        totalEpoch = 0;
        
        ArrayList<ArrayList<Double>> dataset = new ArrayList();
        
        //menginisiasi centroid secara random
        ArrayList<Integer> randomCentroid = new ArrayList<Integer>();
        for (int i=0; i<data.numInstances(); i++) {
            Instance instance = data.instance(i);
            double[] darray = instance.toDoubleArray();
            ArrayList<Double> tempAttr = new ArrayList();
            for (double d : darray){
                tempAttr.add(d);
            }
            dataset.add(tempAttr);
            randomCentroid.add(new Integer(i));
        }
        
        Collections.shuffle(randomCentroid);
        
        System.out.println("Initiate Centroid");
        for (int i=0; i<totalCluster; i++) {
            System.out.println("Centroid Cluster-"+(i+1)+": "+data.instance(randomCentroid.get(i)));
            centroidInstanceList.add(data.instance(randomCentroid.get(i)));
        }
        
        ArrayList<ArrayList<Double>> prevClusterList = new ArrayList();
        ArrayList<ArrayList<Instance>> prevInstanceClusterList = new ArrayList();
        int minDistanceClusterIndex = 0;
        Double minDistanceToCentroidInstance = 0.0;
        DistanceFunction disFunc = new EuclideanDistance();
        
        ReplaceMissingValues replaceMissingFilter = new ReplaceMissingValues();
        Instances instances = new Instances(data);
        instances.setClassIndex(-1);
        replaceMissingFilter.setInputFormat(instances);
        instances = Filter.useFilter(instances, replaceMissingFilter);
        disFunc.setInstances(instances);
        
        
        while (true){
            clusterInstanceList = new ArrayList();
            for (int i = 0;i<totalCluster;i++){
                clusterInstanceList.add(new ArrayList());
            }
            
            for (int i = 0;i<dataset.size();i++){
                minDistanceClusterIndex = 0;
                minDistanceToCentroidInstance = disFunc.distance(instances.instance(i), centroidInstanceList.get(0));
                for (int j = 0;j<centroidInstanceList.size();j++){
                    Double newInsDistance = disFunc.distance(data.instance(i),centroidInstanceList.get(j));
                    if (newInsDistance < minDistanceToCentroidInstance){
                        minDistanceClusterIndex = j;
                        minDistanceToCentroidInstance = newInsDistance;
                    }
                }
                clusterInstanceList.get(minDistanceClusterIndex).add(data.instance(i));
            }
            
            if(!clusterInstanceList.equals(prevClusterList)){
                totalEpoch++;
                prevClusterList = new ArrayList(clusterInstanceList);
            }else{
                break;
            }
            
            for (int i = 0;i<centroidInstanceList.size();i++){
                if(clusterInstanceList.get(i).size() > 0){
                    centroidInstanceList.set(i, countNewCentroids(clusterInstanceList.get(i)));
                }
            }
        }
    }

    @Override
    public int numberOfClusters() throws Exception {
        return totalCluster;
    }
}
