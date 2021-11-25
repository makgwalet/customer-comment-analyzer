package com.ikhokha.techcheck;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CommentAnalyzer {
	
	private File file;

	public CommentAnalyzer(File file) {
		this.file = file;
	}
	
	public Map<String, Integer> analyze(List<Metric> metricList) {

		Map<String, Integer> resultsMap = new HashMap<>();

		try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
			
			String line = null;
			while ((line = reader.readLine()) != null) {
				for(Metric mt: metricList){
					if (line.toLowerCase().contains(mt.getMetricContains().toLowerCase())){
						incOccurrence(resultsMap, mt.getMetricName());
					}
				}

				if (line.length() < 15) {
					incOccurrence(resultsMap, "SHORTER_THAN_15");
				}

			}

		} catch (FileNotFoundException e) {
			System.out.println("File not found: " + file.getAbsolutePath());
			e.printStackTrace();
		} catch (IOException e) {
			System.out.println("IO Error processing file: " + file.getAbsolutePath());
			e.printStackTrace();
		}
		
		return resultsMap;
		
	}
	
	/**
	 * This method increments a counter by 1 for a match type on the countMap. Uninitialized keys will be set to 1
	 * @param countMap the map that keeps track of counts
	 * @param key the key for the value to increment
	 */
	private void incOccurrence(Map<String, Integer> countMap, String key) {
		
		countMap.putIfAbsent(key, 0);
		countMap.put(key, countMap.get(key) + 1);
	}


}
