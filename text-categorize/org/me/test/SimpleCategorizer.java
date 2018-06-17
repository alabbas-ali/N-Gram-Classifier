package org.me.test;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.knallgrau.utils.textcat.TextCategorizer;

public class SimpleCategorizer {

	public static void main(String[] args) throws IOException {
		String category = "No Categorised";
		TextCategorizer guesser = new TextCategorizer();

		InputStream is = new FileInputStream("C:/test/test.txt");
		BufferedReader buf = new BufferedReader(new InputStreamReader(is));
		String line = buf.readLine();
		StringBuilder sb = new StringBuilder();
		while (line != null) {
			sb.append(line).append("\n");
			line = buf.readLine();
		}
		String fileAsString = sb.toString();
		System.out.println("Contents : " + fileAsString);

		category = guesser.categorize(fileAsString);

		System.out.println(category);
		buf.close();
	}
}