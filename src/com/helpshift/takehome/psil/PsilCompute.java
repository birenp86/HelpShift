package com.helpshift.takehome.psil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.Scanner;
import java.util.Stack;


public class PsilCompute {

	private static PsilCompute psilCompute = null;
	List<String> inputList = new ArrayList<String>();
	Map<String, Double> addBindValue = new HashMap<String, Double>();
	//stack to store "(" and numerics
	Stack<String> ops  = new Stack<String>();
	//stack to store operands
	Stack<Double> vals = new Stack<Double>();
	

	/**
	 * returns object of Class PsilCompute
	 * @return object psilCompute
	 */
	public static PsilCompute getPage() {
		if (psilCompute == null) {
			psilCompute = new PsilCompute();
		}
		return psilCompute;
	}

	/**
	 * Gets Scanner object and reads user input until "Exit" is entered
	 * @param stdin
	 */
	public void readInput(Scanner stdin) {
		
		//reads user input
		while (stdin.hasNext()) {
			String line = stdin.nextLine();
			//replaces new line and tab with ""
			line = line.replaceAll("[\\n\\t]", "");
			// adds spaces for splitting user input
			line = line.replaceAll("[(]", "( ");
			line = line.replaceAll("[)]", " )");
			// split string into string[]
			String[] splitInput = line.split("\\s+");
			for(String str : splitInput){
				if(str.equalsIgnoreCase("exit"))
					continue;
				else
					inputList.add(str);
			}
			if(line.contains("exit"))
				break; 
		}
		// Display user input
		displayInput();
		//calculate input
		calculateResult(inputList);

	}

	/**
	 * Gets String List and does the arithmetic calculation
	 * @param inputList
	 */
	private void calculateResult(List<String> inputList) {
		try {
			ListIterator<String> iterList = inputList.listIterator();
			while(iterList.hasNext()){ 
				String s = (String) iterList.next();
				if (s.equals("("))    vals.push(-1.0);
				else if (s.equals("+"))    ops.push(s);
				else if (s.equals("-"))    ops.push(s);
				else if (s.equals("*"))    ops.push(s);
				else if (s.equals("/"))    ops.push(s);
				else if (s.equals(")")) {
					boolean loop = true;
					while(loop == true){
						String op = ops.pop();
						double v1 = vals.pop();
						double v2 = vals.pop();
						if(v2 == -1.0){
							vals.push(v1);
							loop = false;
						}
						else{
							if (op.equals("+"))    v2 = v2 + v1;
							else if (op.equals("*"))    v2 = v2 * v1;
							else if (op.equals("-"))    v2 = v2 - v1;
							else if (op.equals("/"))    v2 = v2 / v1;
							vals.push(v2);
							ops.push(op);
						}
					}
				}
				else if(s.equalsIgnoreCase("bind")){
					Double bindValueDouble = 0.0;
					String bindString = (String) iterList.next();
					String bindValueString = (String) iterList.next();
					if(addBindValue.containsKey(bindValueString))
						bindValueDouble = addBindValue.get(bindValueString);
					else
						bindValueDouble = Double.parseDouble(bindValueString);
					String bindRP = (String) iterList.next();
					if(bindRP.equalsIgnoreCase("(")){
						calculateResult(inputList);
					}
					else if(bindRP.equalsIgnoreCase(")")){
						//vals.pop();
						addBindValue.put(bindString, bindValueDouble);
						if(inputList.size() == 5){
							vals.push(bindValueDouble);
						}
					}
					System.out.println(bindString+" --> "+bindValueDouble);
				}
				else {
					if(addBindValue.containsKey(s))
						vals.push(addBindValue.get(s));
					else
						vals.push(Double.parseDouble(s));
				}
			}
			Double val = vals.pop();
			System.out.println("Result: " +val);
		} catch (java.util.EmptyStackException e) {
			System.out.println("Invalid Program");
			//e.printStackTrace();
		}

		catch (java.lang.NumberFormatException e) {
			System.out.println("Invalid Program");
			//e.printStackTrace();
		}
	}

	/**
	 * Display User Input
	 */
	public void displayInput(){
		System.out.print("User Input is: ");
		for(String str : inputList){
			System.out.print(str+" ");
		}
		System.out.println("\n");
	}
}
