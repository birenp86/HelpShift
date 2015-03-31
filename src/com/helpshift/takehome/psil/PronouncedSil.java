package com.helpshift.takehome.psil;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.util.Scanner;

public class PronouncedSil {

	private static Scanner stdin;

	public static void main(String[] args) throws IOException {
		PsilCompute psilCompute = PsilCompute.getPage();
		stdin = new Scanner(new BufferedInputStream(System.in));
		System.out.println("Enter Input -- \n\nTo Exit Type \"exit\"\n");
		psilCompute.readInput(stdin);
	}
}
