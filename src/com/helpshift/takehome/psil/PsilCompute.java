package com.helpshift.takehome.psil;

import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;
import java.util.Scanner;
import java.util.Stack;


public class PsilCompute {

	private static PsilCompute psilCompute = null;
	List<String> inputList = new ArrayList<String>();

	public static PsilCompute getPage() {
		if (psilCompute == null) {
			psilCompute = new PsilCompute();
		}
		return psilCompute;
	}

	public void readInput(Scanner stdin) {
		while (stdin.hasNext()) {
			String line = stdin.nextLine();

			line = line.replaceAll("[\\n\\t]", " ");
			line = line.replaceAll("[(]", "( ");
			line = line.replaceAll("[)]", " )");
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
		displayInput();
		calculateResult(inputList);

	}

	private void calculateResult(List<String> inputList) {
		try {
			Stack<String> ops  = new Stack<String>();
			Stack<Double> vals = new Stack<Double>();
			ListIterator<String> iterList = inputList.listIterator();

			while(iterList.hasNext()){ 
				String s = (String) iterList.next();
				if (s.equals(" "));
				else if (s.equals("("))    vals.push(-1.0);
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
							vals.push(v2);
							ops.push(op);
						}
					}
				}
				else vals.push(Double.parseDouble(s));
			}
		} catch (java.util.EmptyStackException e) {
			System.out.println("Invalid Program");
			//e.printStackTrace();
		}
		
		catch (java.lang.NumberFormatException e) {
			System.out.println("Invalid Program");
			//e.printStackTrace();
		}
	}

	public void displayInput(){
		System.out.print("User Input is: ");
		for(String str : inputList){
			System.out.print(str+" ");
		}
		System.out.println("\n");
	}
}
