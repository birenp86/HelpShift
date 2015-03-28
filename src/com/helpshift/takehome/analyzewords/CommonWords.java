package com.helpshift.takehome.analyzewords;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;

public class CommonWords {

	StopWords stopWords = StopWords.getPage();
	File propFile = new File("/Users/birenp/Desktop/Android_Practise/takeHome/count.properties");
	List<String> wordList = null;
	Map<String, Integer> wordsToAdd = null;
	Map<String, Integer> wordsToProp = null;
	Properties props = new Properties();

	private static CommonWords commonWords = null;

	/**
	 * returns object of Class CommonWords
	 * @return object commonWords
	 */
	public static CommonWords getPage() {
		if (commonWords == null) {
			commonWords = new CommonWords();
		}
		return commonWords;
	}

	/**
	 * Read File one line at a time, unit EOF is reached
	 * @param filePath - location of file to analyze words
	 * @throws IOException - if File Not Found
	 */
	public void readFile(File filePath) throws IOException {
		BufferedReader brRead = null;
		String readLine = null;
		int i = 0;
		// open file to read
		brRead = openToRead(filePath);
		//read one line at a time
		while ((readLine = brRead.readLine()) != null) {
			System.out.println("Eval Line: "+i);
			if(readLine.equals("")){
				continue;
			}
			//split line into individual words and store in ArrayList
			wordList = splitLine(readLine);
			//add word-count to map
			addToMap(wordList);
			//check for existing word-count and new word-count in file
			checkInProp(wordsToAdd);
			//write updated word-count and new word-count to file
			writeToProp(wordsToAdd, wordsToProp);
			i++;
		}
	}

	/**
	 * Adds individual word-count pair to Map
	 * @param wordList
	 * @throws IOException
	 */
	private void addToMap(List<String> wordList) throws IOException {
		wordsToAdd = new HashMap<String, Integer>();
		ListIterator<String> iterList = wordList.listIterator();
		//add words to map with their occurrence count
		while(iterList.hasNext()){
			String word= (String) iterList.next();
			//increase count of a word
			if(wordsToAdd.containsKey(word)){
				Integer occurence = wordsToAdd.get(word);
				wordsToAdd.put(word, occurence + 1);
			}
			else
				//no occurrence: add to map
				wordsToAdd.put(word, 1);
		}
	}

	/**
	 * Check for existing Word-Count pair in the .properties file
	 * If found adds latest count to existing count value, else add to .properties file
	 * @param wordsToAdd
	 * @throws IOException
	 */
	private void checkInProp(Map<String, Integer> wordsToAdd) throws IOException {
		wordsToProp = new HashMap<String, Integer>();
		String getPropWord = null;
		//Load .properties file where word-count is stored
		InputStream ips = new FileInputStream(propFile);
		props.load(ips);
		//Iterate for each word in Map from latest readLine
		Iterator<Map.Entry<String, Integer>> entries = wordsToAdd.entrySet().iterator();
		while (entries.hasNext()) {
			Entry<String, Integer> entry = entries.next();
			getPropWord = props.getProperty(entry.getKey());
			//if word not found, add to wordsToProp Map and remove from existing Map
			// not required for task purpose, but for re-factoring in future
			if(getPropWord == null){
				wordsToProp.put(entry.getKey(), entry.getValue());
				entries.remove();
			}
			//if word is found, read count from .properties and increment the word count value
			else{
				Integer count = Integer.valueOf(getPropWord);
				count = (count+entry.getValue());
				wordsToAdd.replace(entry.getKey(), entry.getValue(), count);
			}
		}
		ips.close();		
	}

	/**
	 * Adds new word-count pair and updates existing word-count with new values in .properties file
	 * @param wordsToAdd
	 * @param wordsToProp
	 * @throws IOException
	 */
	private void writeToProp(Map<String, Integer> wordsToAdd,
			Map<String, Integer> wordsToProp) throws IOException {		
		OutputStream ops = new FileOutputStream(propFile);
		Iterator<Map.Entry<String, Integer>> addEntries = wordsToProp.entrySet().iterator();
		//add new entries (first time occurrence) to .properties file
		while (addEntries.hasNext()) {
			Entry<String, Integer> addEntry = addEntries.next();
			String getWord = addEntry.getKey();
			Integer getCount = addEntry.getValue();
			props.setProperty(getWord, getCount.toString());
		}

		Iterator<Map.Entry<String, Integer>> updateEntries = wordsToAdd.entrySet().iterator();
		// update existing word-count pairs in .properties file based on latest count
		while (updateEntries.hasNext()) {
			Entry<String, Integer> addEntry = updateEntries.next();
			String getWord = addEntry.getKey();
			Integer getCount = addEntry.getValue();
			props.setProperty(getWord, getCount.toString());
		}
		props.store(ops, "");
		ops.close();		
	}

	/**
	 * Separates a single line into words (replaces special characters)
	 * Checks if each word is a stopWord or not
	 * @param readLine
	 * @return ArrayList
	 */
	private List<String> splitLine(String readLine) {
		String cleanWords = readLine;
		boolean check = false;
		//removes special characters
		cleanWords = cleanWords.replaceAll("[~!-,?;()*#\"<>\\t\\]\\[]", "");
		cleanWords = cleanWords.replaceAll("[~/:.-]", " ");
		//changes string to lower case
		cleanWords = cleanWords.toLowerCase();
		//splits line into words separated by " "
		wordList = new ArrayList<String>(Arrays.asList(cleanWords.split("\\s+")));
		ListIterator<String> iter = wordList.listIterator();
		while(iter.hasNext()){
			String checkWord = (String) iter.next();
			//remove 
			if(checkWord.equals(""))
				iter.remove();
			//checks for a stopWord
			check = stopWords.isStop(checkWord);
			if(check == true){
				iter.remove();
			}
		}
		return wordList;
	}

	/**
	 * BufferReader to Read a File
	 * @param filePath
	 * @return BufferedReader
	 */
	private BufferedReader openToRead(File filePath) {
		BufferedReader brIn = null;
		try {
			brIn = new BufferedReader(new FileReader(filePath));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		return brIn;
	}

	/**
	 * Find Maximum Occurrences of a Word
	 * @param max
	 * @throws IOException
	 */
	public void findMax(int max) throws IOException {
		int[] findMaxCount = new int[max];
		String[] findMaxWord = new String[max];

		//Load .properties file where word-count is stored
		InputStream ips = new FileInputStream(propFile);
		props.load(ips);
		// loop through the file
		Enumeration<?> e = props.propertyNames();
		while (e.hasMoreElements()) {
			String key = (String) e.nextElement();
			Integer value = Integer.parseInt(props.getProperty(key));	
			//if element is smaller than value of 5th element: discard
			if(findMaxCount[4] > value)
				continue;
			else
				//swap values
				changeMaxValues(value, findMaxCount, findMaxWord, key);
		}
		ips.close();
		//Display Max Word-Count
		System.out.println("Max Word-----Count Pairs are as follows:");
		for(int i = 0; i < findMaxCount.length; i++){
			System.out.println(findMaxWord[i]+"-----"+findMaxCount[i]);
		}
	}

	/**
	 * swap values
	 * @param value
	 * @param findMaxCount
	 * @param findMaxWord
	 * @param key
	 */
	private void changeMaxValues(Integer value, int[] findMaxCount, String[] findMaxWord, String key) {
		for(int i = 0; i < findMaxCount.length; i++){
			if(findMaxCount[i] < value){
				int intTemp = findMaxCount[i];
				String strTemp = findMaxWord[i];
				findMaxCount[i] = value;
				findMaxWord[i] = key;
				value = intTemp;
				key = strTemp;
			}			
		}
	}
	
	/**
	 * Find Maximum Occurrences of a Word
	 * @param max
	 * @throws IOException
	 */
	public void findMin(int min) throws IOException {
		int[] findMinCount = new int[min];
		String[] findMinWord = new String[min];

		//Load .properties file where word-count is stored
		InputStream ips = new FileInputStream(propFile);
		props.load(ips);
		
		// loop through the file
		Enumeration<?> e = props.propertyNames();
		while (e.hasMoreElements()) {
			String key = (String) e.nextElement();
			Integer value = Integer.parseInt(props.getProperty(key));
			//if element is greater than value of 5th element: discard

			if(findMinCount[4] < value && findMinCount[4] > 0)
				continue;
			else
				//swap values
				changeMinValues(value, findMinCount, findMinWord, key);
		}
		ips.close();
		//Display Min Word-Count
		System.out.println("Min Word-----Count Pairs are as follows:");
		for(int i = 0; i < findMinCount.length; i++){
			System.out.println(findMinWord[i]+"-----"+findMinCount[i]);
		}
	}

	/**
	 * swap values
	 * @param value
	 * @param findMaxCount
	 * @param findMaxWord
	 * @param key
	 */
	private void changeMinValues(Integer value, int[] findMinCount, String[] findMinWord, String key) {
		for(int i = 0; i < findMinCount.length; i++){
			if(findMinCount[i] > value || findMinCount[i] == 0){
				int intTemp = findMinCount[i];
				String strTemp = findMinWord[i];
				findMinCount[i] = value;
				findMinWord[i] = key;
				value = intTemp;
				key = strTemp;
			}			
		}
	}

}

