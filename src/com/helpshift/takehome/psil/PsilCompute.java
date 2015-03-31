package com.helpshift.takehome.psil;

import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;
import java.util.Scanner;
import java.util.Stack;


public class PsilCompute {

	private static PsilCompute psilCompute = null;
	List<String> inputList = new ArrayList<String>();

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
			//replaces new line and tab with " "
			line = line.replaceAll("[\\n\\t]", " ");
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
			//stack to store "(" and numerics
			Stack<String> ops  = new Stack<String>();
			//stack to store operands
			Stack<Double> vals = new Stack<Double>();
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
							if      (op.equals("+"))    v2 = v2 + v1;
							else if (op.equals("*"))    v2 = v2 * v1;
							else if (op.equals("-"))    v2 = v2 - v1;
							else if (op.equals("/"))    v2 = v2 / v1;
							vals.push(v2);
							ops.push(op);
						}
					}
				}
				else vals.push(Double.parseDouble(s));
			}
			System.out.println("Result: " +vals.pop());
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
