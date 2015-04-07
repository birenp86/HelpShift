package com.helpshift.takehome.psil;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
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
		ListIterator<String> iterList = inputList.listIterator();
		calculateResult(inputList, iterList);
	}

	/**
	 * Gets String List and does the arithmetic calculation
	 * @param inputList
	 * @param iterList2 
	 */
	private void calculateResult(List<String> inputList, ListIterator<String> iterList) {
		try {
			boolean cont = countPr(inputList);
			if(cont == false){
				System.out.println("Invalid Parenthesis");
				throw new NumberFormatException();
			}
			while(iterList.hasNext()){ 
				String s = (String) iterList.next();
				if (s.equals("("))    vals.push(0.0);
				else if (s.equals("+"))    ops.push(s);
				else if (s.equals("-"))    ops.push(s);
				else if (s.equals("*"))    ops.push(s);
				else if (s.equals("/"))    ops.push(s);
				else if (s.equals(")")) {
					String op = ops.pop();
					if (op.equals("-")){
						List<Double> subList = new ArrayList<Double>();
						boolean loopStack = true;
						while(loopStack){
							double v1 = vals.pop();
							if(v1 == 0.0)
								loopStack = false;
							else{
								subList.add(v1);
							}
						}
						Collections.reverse(subList);
						Double dVal = subList.get(0);
						subList.remove(0);
						for(Double subDouble : subList){
							dVal = dVal - subDouble;
						}
						vals.push(dVal);
						ops.push(op);
					}
					else{
						boolean loop = true;
						while(loop == true){							
							double v1 = vals.pop();
							double v2 = vals.pop();
							if(v2 == 0.0){
								vals.push(v1);
								loop = false;
							}
							else{
								if (op.equals("+"))    v2 = v2 + v1;
								else if (op.equals("*"))    v2 = v2 * v1;
								else if (op.equals("/"))    v2 = v2 / v1;
								vals.push(v2);
								ops.push(op);
							}
						}
					}
				}
				else if(s.equalsIgnoreCase("bind")){
					if(addBindValue.containsKey(s)){
						vals.push(addBindValue.get(s));
					}
					else{
						boolean continueRun = false;
						Double bindValueDouble = 0.0;
						String bindString = (String) iterList.next();
						continueRun = checkBindString(bindString);
						if(continueRun == true){
							String bindValueString = (String) iterList.next();
							if(addBindValue.containsKey(bindValueString))
								bindValueDouble = addBindValue.get(bindValueString);
							else
								bindValueDouble = Double.parseDouble(bindValueString);
							String bindRP = (String) iterList.next();
							if(bindRP.equalsIgnoreCase("(")){						
								calculateResult(inputList, iterList);
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
					}
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

	private boolean checkBindString(String bindString) {
		try{
			Double checkDouble = Double.parseDouble(bindString);
			System.out.println("Invalid bind value");
			return false;
		}
		catch (java.lang.NumberFormatException e) {
			return true;
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

	private boolean countPr(List<String> inputList) {
		int rightP = 0;
		int leftP = 0;

		for(String temp : inputList){
			if(temp.equals("("))
				leftP = leftP+1;
			else if(temp.equals(")"))
				rightP = rightP+1;
		}
		if(rightP == leftP)
			return true;
		else
			return false;		
	}
}
