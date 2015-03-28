package com.helpshift.takehome.analyzewords;

import java.io.File;
import java.io.IOException;

public class AnalyzeWords {

	//main function
	public static void main(String[] args) throws IOException {
		//File to Read and Analyze
		String path = ("/Users/birenp/Desktop/Android_Practise/takeHome/");
		File fileName = new File(path+"100.txt");
		//StopWords Object: 
		StopWords stopWords = StopWords.getPage();
		//CommonWords Object
		CommonWords commonWords = CommonWords.getPage();
		
		//Load Stop Words
		stopWords.stopWords();
		//Display Stop Words
		stopWords.display();

		//Load and Read File
		commonWords.readFile(fileName);
		//find 5 max occurrences
		int max = 15;
		commonWords.findMax(max);
		//find 5 min occurrences
		int min = 15;
		commonWords.findMin(min);
		//Task Finished
		System.out.println("--**--Task Done--**--");
	}

}
