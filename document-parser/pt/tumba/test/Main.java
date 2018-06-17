package pt.tumba.test;

import java.net.MalformedURLException;
import java.net.URL;

import pt.tumba.parser.HTMLParser;
import pt.tumba.parser.HyperLinks;

public class Main {

	public static void main(String[] args) {
		
		try {
			HTMLParser p = new HTMLParser("sd");
			p.initTokenizer(new URL("http://cima4u.tv/"), "UTF8", 1);
			p.processData();
			HyperLinks g = p.getHyperLinks();
			while( g.getLinks().hasNext() ) {
				System.out.println("element " + g.getLinks().next().toString());
			}
			p.getContent()
			
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		

	}

}
