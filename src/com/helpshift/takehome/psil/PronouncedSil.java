package com.helpshift.takehome.psil;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.util.Scanner;

public class PronouncedSil {

	private static Scanner stdin;
	//main function
	public static void main(String[] args) throws IOException {
		//PsilCompute Object:
		PsilCompute psilCompute = PsilCompute.getPage();
		//Scanner Object for User MultiLine Input
		stdin = new Scanner(new BufferedInputStream(System.in));
		System.out.println("Enter Input -- \n\nTo Exit Type \"exit\"\n");
		//Read User Input
		psilCompute.readInput(stdin);
	}
}
