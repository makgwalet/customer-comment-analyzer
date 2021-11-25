package com.ikhokha.techcheck;

import java.io.File;
import java.util.*;

public class Main {

	public static void main(String[] args) {
		
		Map<String, Integer> totalResults = new HashMap<>();

		List<Metric> metricList = new ArrayList<>();
				
		File docPath = new File("docs");
		File[] commentFiles = docPath.listFiles((d, n) -> n.endsWith(".txt"));

		matchMetric(metricList);

		Thread[] threads = new Thread[commentFiles.length];
		for(int t = 0; t< commentFiles.length;t++){
			final int thread =t;
			threads[t] = new Thread(){
				@Override public void run(){
					runThread(commentFiles,thread,totalResults,metricList);
				}
			};
		}
		for (Thread t1: threads)
			t1.start();
		for (Thread t2: threads)
			try {
				t2.join();
			}catch (InterruptedException e){}

		System.out.println("RESULTS\n=======");
		totalResults.forEach((k,v) -> System.out.println(k + " : " + v));


	}
	
	/**
	 * This method adds the result counts from a source map to the target map 
	 * @param source the source map
	 * @param target the target map
	 */
	private static void addReportResults(Map<String, Integer> source, Map<String, Integer> target) {
		int count = 1;
		for (Map.Entry<String, Integer> entry : source.entrySet()) {
			target.putIfAbsent(entry.getKey(), 0);
			target.put(entry.getKey(), target.get(entry.getKey()) + entry.getValue());
			count++;
		}

	}

	//multithreading method
	public static void runThread(File[] commentFiles, int thread, Map<String, Integer> totalResults, List<Metric> metricList){
		//assign 1 file per thread
		List<File> inFiles = new ArrayList<>();
		for(int i = thread; i < (thread+1);i++){
			inFiles.add(commentFiles[i]);
		}

		//looping through the files and calling the methods
		for(File file: inFiles){
			CommentAnalyzer commentAnalyzer = new CommentAnalyzer(file);
			Map<String, Integer> fileResults = commentAnalyzer.analyze(metricList);
			addReportResults(fileResults, totalResults);
			System.out.println("processing " + file.getName() + " in thread " + Thread.currentThread().getName());
		}
	}

	//method to invoke promp to add more metric and populate an Array list
	public static List matchMetric(List<Metric> metricList){
		Metric metric = new Metric();
		int howMany;
		String isAdding;
		Scanner scan = new Scanner(System.in);

		System.out.println("Do you wanna add new Metric(Y/N)");

		while (!scan.hasNext("[ynYN]")){
			System.out.println("invalid input. Enter Y/N");
			scan.next();
		}

		isAdding = scan.next();


		//pre-poluting an array with already existing metrics
		metricList.add(new Metric("QUESTION","?"));
		metricList.add(new Metric("SHAKER_MENTIONS","shaker"));
		metricList.add(new Metric("MOVER_MENTIONS","mover"));
		metricList.add(new Metric("SPAM","http"));


		if (isAdding.equalsIgnoreCase("y")) {
			System.out.println("===================================");
			System.out.println("How many metric do you want to add(excluding :SHORTER_THAN_15,SHAKER_MENTIONS,MOVER_MENTIONS,SPAM,QUESTION)");
			System.out.println("EXAMPLE: Metric name = CLIENT_GREETING, Metric contains = hello ");
			System.out.println("Type 0 if you don't add any metric");

			while (!scan.hasNextInt()){
				System.out.println("Input is not numeric. Enter a number");
				scan.next();
			}
			howMany = scan.nextInt();

			for (int i = 1; i < howMany + 1; i++) {
				System.out.println("name of the Metric " + i + ":");
				metric.setMetricName(scan.next());

				System.out.println("Metric Contains " + i + ":");
				metric.setMetricContains(scan.next());

				metricList.add(metric);
				metric = new Metric();
			}
		}

		return metricList;
	}

}
