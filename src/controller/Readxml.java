package controller;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;

import com.sun.xml.internal.messaging.saaj.packaging.mime.internet.ParseException;

import org.w3c.dom.Node;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;

import java.io.File;
import java.net.URL;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import model.Currency;

public class Readxml {

	public static void main(String argv[]) {
		
		//doStuff();
		
		List<Currency> swagCurrencies = getCurrencies();
		for(Currency c : swagCurrencies){
			System.out.println("CURRENCY: " + c.toString());
		}
	}
	
	public static List<Currency> getCurrencies(){
		List<Currency> returnCurrencies = new ArrayList<Currency>();
		try {			
			//File xmlFile = new File("/Users/example.xml");
			URL url = new URL("http://statistika.eestipank.ee/Reports?type=curd&format=xml&date1=2010-12-30&lng=est&print=off");
			//URL url = new URL("http://webservices.lb.lt/ExchangeRates/ExchangeRates.asmx/getExchangeRatesByDate?Date=2010-12-30");
			DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
			DocumentBuilder db = dbf.newDocumentBuilder();
			//Document doc = db.parse(xmlFile);
			Document doc = db.parse(url.openStream());

			//optional, but recommended
			//read this - http://stackoverflow.com/questions/13786607/normalization-in-dom-parsing-with-java-how-does-it-work
			doc.getDocumentElement().normalize();

			System.out.println("Root element :" + doc.getDocumentElement().getNodeName());

			NodeList nList = doc.getElementsByTagName("Currency"); // row
			System.out.println(nList.getLength() + " nodes found");
			
			System.out.println("----------------------------");
			System.out.println("nList length: " + nList.getLength());
			String dateString = doc.getElementsByTagName("Date").item(0).getTextContent(); //.getTextContent(); //07.03.16        "January 2, 2010";
			System.out.println("dateString: " + dateString);
			SimpleDateFormat format = new SimpleDateFormat("dd.MM.yy"); // new SimpleDateFormat("MMMM d, yyyy", Locale.ENGLISH); // DateFormat
			// java.text.ParseException: Unparseable date: ""
			Date date = format.parse(dateString);
			System.out.println("DATE IS: " + date);
			
			// CURRENCY ELEMENTS:
			for (int temp = 0; temp < nList.getLength(); temp++) {
				Node nNode = nList.item(temp);
				//System.out.println("\nCurrent Element :" + nNode.getNodeName()); // Current Element :Currency
				if (nNode.getNodeType() == Node.ELEMENT_NODE) {
					Element eElement = (Element) nNode;
					//System.out.println("I HAVE NODE!");
					
					String name = eElement.getAttribute("name"); // shortName
					String text = eElement.getAttribute("text"); // fullName
					Float rate;
					NumberFormat nf = new DecimalFormat ("#,#");
					try{
						rate = nf.parse(eElement.getAttribute("rate")).floatValue();
						//rate = Float.parseFloat(eElement.getAttribute("rate"));
					}catch(NumberFormatException e){
						e.printStackTrace();
						rate = null;
					}
					
					
//					String dateString = eElement.getTextContent(); //07.03.16        "January 2, 2010";
//					System.out.println("dateString: " + dateString);
//					SimpleDateFormat format = new SimpleDateFormat("dd.MM.yy"); // new SimpleDateFormat("MMMM d, yyyy", Locale.ENGLISH); // DateFormat
//					// java.text.ParseException: Unparseable date: ""
//					Date date = format.parse(dateString);
//					System.out.println("DATE IS: " + date);
							
					System.out.println("name: " + eElement.getAttribute("name") + " text: " + eElement.getAttribute("text") + " rate: " + eElement.getAttribute("rate"));
					returnCurrencies.add(new Currency(name, text, rate, date));
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return null;
	}

	private static void doStuff(){
		try {			
			//File xmlFile = new File("/Users/example.xml");
			URL url = new URL("http://statistika.eestipank.ee/Reports?type=curd&format=xml&date1=2010-12-30&lng=est&print=off");
			//URL url = new URL("http://webservices.lb.lt/ExchangeRates/ExchangeRates.asmx/getExchangeRatesByDate?Date=2010-12-30");
			DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
			DocumentBuilder db = dbf.newDocumentBuilder();
			//Document doc = db.parse(xmlFile);
			Document doc = db.parse(url.openStream());
			NodeList nodes = doc.getElementsByTagName("Currency"); // row
			System.out.println(nodes.getLength() + " nodes found");

			//optional, but recommended
			//read this - http://stackoverflow.com/questions/13786607/normalization-in-dom-parsing-with-java-how-does-it-work
			doc.getDocumentElement().normalize();

			System.out.println("Root element :" + doc.getDocumentElement().getNodeName());

			NodeList nList = doc.getElementsByTagName("Currency");

			System.out.println("----------------------------");
			System.out.println("nList length: " + nList.getLength());
			for (int temp = 0; temp < nList.getLength(); temp++) {

				Node nNode = nList.item(temp);

				//System.out.println("\nCurrent Element :" + nNode.getNodeName()); // Current Element :Currency

				if (nNode.getNodeType() == Node.ELEMENT_NODE) {

					Element eElement = (Element) nNode;

					//System.out.println("I HAVE NODE!");
					System.out.println("name: " + eElement.getAttribute("name") + " text: " + eElement.getAttribute("text") + " rate: " + eElement.getAttribute("rate"));

					//					System.out.println("Staff id : " + eElement.getAttribute("id"));
					//					System.out.println("First Name : " + eElement.getElementsByTagName("firstname").item(0).getTextContent());
					//					System.out.println("Last Name : " + eElement.getElementsByTagName("lastname").item(0).getTextContent());
					//					System.out.println("Nick Name : " + eElement.getElementsByTagName("nickname").item(0).getTextContent());
					//					System.out.println("Salary : " + eElement.getElementsByTagName("salary").item(0).getTextContent());

				}
			}

			// RECURSIVELY PRINT NODES:
			//			if (doc.hasChildNodes()) {
			//				System.out.println("\nPRINTING NODES:");
			//				printNote(doc.getChildNodes());
			//			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private static void printNote(NodeList nodeList) {
		for (int count = 0; count < nodeList.getLength(); count++) {
			Node tempNode = nodeList.item(count);
			// make sure it's element node.
			if (tempNode.getNodeType() == Node.ELEMENT_NODE) {
				// get node name and value
				System.out.println("\nNode Name =" + tempNode.getNodeName() + " [OPEN]");
				System.out.println("Node Value =" + tempNode.getTextContent());
				if (tempNode.hasAttributes()) {
					// get attributes names and values
					NamedNodeMap nodeMap = tempNode.getAttributes();
					for (int i = 0; i < nodeMap.getLength(); i++) {
						Node node = nodeMap.item(i);
						System.out.println("attr name : " + node.getNodeName());
						System.out.println("attr value : " + node.getNodeValue());
					}
				}
				if (tempNode.hasChildNodes()) {
					// loop again if has child nodes
					printNote(tempNode.getChildNodes());
				}
				System.out.println("Node Name =" + tempNode.getNodeName() + " [CLOSE]");
			}
		}
	}


}
