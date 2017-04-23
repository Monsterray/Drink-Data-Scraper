package testing;


import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.jaunt.Element;
import com.jaunt.Elements;
import com.jaunt.JauntException;
import com.jaunt.UserAgent;

/**
 * @author Monsterray
 *
 */
public class TestingDrinkData {


	private int drinkNumber = 0;
	private int processedDrink = 0;
	private int drinksNoPage = 0;
	private String folderBase = "./Data Drinks/";
	private long startTime;
	private long endTime;
	private boolean verbose = false;

	/**
	 * 
	 */
	TestingDrinkData(){
		System.out.println("Running...");
		startTime = System.currentTimeMillis();

		drinkNumber = 4; // Starts from 4 all the way to 12317
		while(drinkNumber <= 12317){
			String site = "http://www.drinksmixer.com/drink" + drinkNumber + ".html";
			writeDataToFile(site);
			drinkNumber++;
		}
//		writeDataToFile("http://www.drinksmixer.com/drink2124.html");
		
		endTime = System.currentTimeMillis();
		float totalTime = (endTime - startTime) / 1000;
		System.out.println("Finished in " + totalTime + "seconds!");

		System.out.println("\nDrinks Proccesed: " + this.processedDrink);
		System.out.println("Drinks withought a page: " + this.drinksNoPage);
		
	}
	
	/**
	 * @param site
	 */
	public void writeDataToFile(String site){ // Need to actually make this write to file after I make a standardized file
		
		List<String> folders = new ArrayList<String>();
		String title = "";
		String glassType = "";
		String alcoholPercent = "";
		String instructions = "";
		String servingSize = "";
		Map<String, String> ingredients;
		HashMap<String, String> nutritionData;
		
		try{
			UserAgent userAgent = new UserAgent();	//create new userAgent (headless browser).
			userAgent.visit(site);//visit a url   
			
			// This is the folder section
			Element drinkTrail = null;
			try {
				drinkTrail = userAgent.doc.findFirst("<div class=\"pm\">");
			} catch(JauntException e){
				System.out.println("[ERROR] No drink " + drinkNumber);
				drinksNoPage++;
				return;
			}
		  	Elements trailElements = drinkTrail.findEvery("<a href>");
		  	for(Element e : trailElements){	// This is where I make and change the folder
//		  		String folder = e.innerText().replace("&reg;", "\u00AE").replaceAll("*", "~").trim();
		  		String folder = e.innerText().replace("&reg;", "\u00AE").trim();
//		  		System.out.println(folder);
		  		if(folder.equals("Tropical / fruity")){
		  			folder = "Tropical or Fruity";
		  		}else if(folder.equals("Creamy / milky")){
		  			folder = "Creamy or Milky";
		  		}
		  		folders.add(folder);
		  	}
		  	
		  	// Title Section For File
			Element drinkName = userAgent.doc.findFirst("<h1 class=\"fn recipe_title\">"); //find first div who's class matches "fn recipe_title"
			String titleLong = drinkName.innerHTML();
			title = titleLong.substring(0, titleLong.length() - 7).trim();
			if(verbose){
				System.out.println("Working on: " + title);
				System.out.println("Drink number: " + this.drinkNumber);
			}
		  	
			// Glass Type and Alcohol Percentage Section
			Element recipeStats = userAgent.doc.findFirst("<div class=\"recipeStats\">"); //find first div who's class matches "recipeStats"
		  	glassType = recipeStats.findFirst("<img>").getAt("title");
		  	try {
				alcoholPercent = recipeStats.findFirst("<b>").innerText().trim();
			} catch (JauntException e){
				if(verbose){
					System.out.println("[WARNING] Couldn't find alcohol Percentage");
				}
				alcoholPercent = "0";
			}
		  	
		  	// Ingredients Section
			Element recipe_data = userAgent.doc.findFirst("<div class=\"recipe_data\">"); //find first div who's class matches recipe_data"
			Elements seperateIngredients = recipe_data.findEvery("<span class=\"ingredient\">");
			ingredients = new HashMap<String, String>();
			for(Element section : seperateIngredients){
				ingredients.put(section.findFirst("<span class=\"name\">").innerText().trim(), section.findFirst("<span class=\"amount\">").innerText().trim());
			}
		  	
			// Instructions Section
			Element instructionsHTML = userAgent.doc.findFirst("<div class=\"RecipeDirections instructions\">"); //find first div who's class matches "RecipeDirections instructions"
		  	instructions = instructionsHTML.innerText().trim().replaceAll("[\\t\\n\\r]+"," ").replaceAll(" +", " ");
//			System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
//			System.out.println(instructions);
//			System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
		  	
		  	// Nutrition Section
			try {
				Element nutritionDiv = userAgent.doc.findFirst("<div itemprop=\"nutrition\">"); // Find first div who's class matches "nutrition"
				Element nutritionTable = nutritionDiv.findFirst("<table>");	// Find the first table within the nutrition div
				Elements spans = nutritionTable.findEach("<span>");
				String servingSizeLong = nutritionDiv.findFirst("itemprop=\"servingsize\"").innerText();
				servingSize = servingSizeLong.substring(0, servingSizeLong.length() - 1).replaceAll(" +", " ");
				
				nutritionData = new HashMap<String, String>();
				
				List<Element> rows = nutritionTable.findEvery("<td>").toList();
				
				List<Integer> rightArrows1 = findIndexOfMore(rows.get(1).innerHTML(), ">");	// 3rd
				List<Integer> leftArrows1  = findIndexOfMore(rows.get(1).innerHTML(), "<");	// 4th
				List<Integer> rightArrows2 = findIndexOfMore(rows.get(3).innerHTML(), ">");	// 13rd
				List<Integer> leftArrows2  = findIndexOfMore(rows.get(3).innerHTML(), "<");	// 14th
				
				nutritionData.put("energy", rows.get(1).innerHTML().substring(rightArrows1.get(3) + 1, leftArrows1.get(4)));
				nutritionData.put("alcohol", rows.get(3).innerHTML().substring(rightArrows2.get(12) + 1, leftArrows2.get(13)));
				
				for(Element td: spans){				// Iterate through td/th's
					String workingS = td.outerHTML();
					nutritionData.put(workingS.substring(workingS.indexOf("\"") + 1, workingS.indexOf("\">")), workingS.substring(workingS.indexOf('>') + 1, workingS.lastIndexOf('<')).replace("-", "0"));
				}
			}catch(JauntException e){		 //if an HTTP/connection error occurs, handle JauntException.
				if(verbose){
					System.out.println("[WARNING] Couldn't find any nutritional facts!");
				}
				nutritionData = new HashMap<String, String>();
			}
			
			// Write data to file
		  	String fullPath = createFolderStructure(folderBase, folders);
		  	String newLine = "";
			if(new File(fullPath + "/data.drink").exists()){
				newLine = "\n";
			}
			
		  	FileWriter fileWriter = new FileWriter(fullPath + "/data.drink", true);
			BufferedWriter w = new BufferedWriter(fileWriter);
			
			w.write(newLine);
			w.write(title);
			w.write("\t");
			w.write("\t");
			w.write(glassType);
			w.write("\t");
			w.write("\t");
			for(Entry<String, String> e : ingredients.entrySet()){
				w.write(e.getKey() + ":" + e.getValue() + "\t");
			}
			w.write("\t");
			w.write(instructions);
			w.write("\t");
			w.write("\t");
			w.write(servingSize);
			w.write("\t");
			w.write("alchoholPercent=" + alcoholPercent);
			for(Entry<String, String> e : nutritionData.entrySet()){
				w.write("\t" + e.getKey() + "=" + e.getValue());
			}
			
			w.flush();
			w.close();
		  	
			processedDrink++;
		}catch(JauntException e){		 //if an HTTP/connection error occurs, handle JauntException.
			System.err.println(e);
		} catch (IOException e1) {
			e1.printStackTrace();
		}
	}
	
	/**
	 * @param basePath
	 * @param folderArray
	 * @return
	 */
	private String createFolderStructure(String basePath, List<String> folderArray){
		String path = basePath;
		for(String s : folderArray){
			path = path + "/" + s;
		}
		File currentFolder = new File(path);
		if(!currentFolder.exists()){
			currentFolder.mkdirs();
		}
		return path;
	}
	
	/**
	 * @param stringIn
	 * @param regex
	 * @return
	 */
	private List<Integer> findIndexOfMore(String stringIn, String regex){
		List<Integer> output = new ArrayList<Integer>();
		for(int i = 0; i < stringIn.length(); i++){
			try{
				if (stringIn.substring(i, i+1).equals(regex)){
					output.add(i);
				}
			}catch(Exception e){
				System.err.println("Failure within findIndexOf()!!");
				e.printStackTrace();
			}
		}
		return output;
	}
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		new TestingDrinkData();
	}

//	public void printPageInfo(String site){ // need to make it so none of the data is put into variables it's just printed
//		List<String> folders = new ArrayList<String>();
//		
//		String title = "";
//
//		String glassType = "";
//
//		String alcoholPercent = "";
//
//		String instructions = "";
//
//		String servingSize = "";
//
//		Map<String, String> ingredients;
//
//		HashMap<String, String> nutritionData;
//		
//		try{
//			UserAgent userAgent = new UserAgent();	//create new userAgent (headless browser).
//			userAgent.visit(site);//visit a url   
//			
//			// This is the folder section
//			Element drinkTrail = null;
//			try {
//				drinkTrail = userAgent.doc.findFirst("<div class=\"pm\">");
//			} catch(JauntException e){
//				System.out.println("[ERROR] No drink");
//				drinksNoPage++;
//				return;
//			}
//		  	Elements trailElements = drinkTrail.findEvery("<a href>");
//		  	for(Element e : trailElements){	// This is where I make and change the folder
////		  		String folder = e.innerText().replace("&reg;", "\u00AE").replaceAll("*", "~").trim();
//		  		String folder = e.innerText().replace("&reg;", "\u00AE").trim();
////		  		System.out.println(folder);
//		  		if(folder.equals("Tropical / fruity")){
//		  			folder = "Tropical or Fruity";
//		  		}else if(folder.equals("Creamy / milky")){
//		  			folder = "Creamy or Milky";
//		  		}
//		  		folders.add(folder);
//		  	}
//		  	
//		  	// Title Section For File
//			Element drinkName = userAgent.doc.findFirst("<h1 class=\"fn recipe_title\">"); //find first div who's class matches "fn recipe_title"
//			String titleLong = drinkName.innerHTML();
//			title = titleLong.substring(0, titleLong.length() - 7).trim();
//			System.out.println("Working on: " + title);
//			System.out.println("Drink number: " + this.drinkNumber);
//		  	
//			// Glass Type and Alcohol Percentage Section
//			Element recipeStats = userAgent.doc.findFirst("<div class=\"recipeStats\">"); //find first div who's class matches "recipeStats"
//		  	glassType = recipeStats.findFirst("<img>").getAt("title");
//			System.out.println("glassType: " + glassType);
//		  	try {
//				alcoholPercent = recipeStats.findFirst("<b>").innerText().trim();
//			} catch (JauntException e){
//				System.out.println("[WARNING] Couldn't find alcohol Percentage");
//				alcoholPercent = "0";
//			}
//			System.out.println("alcoholPercent: " + alcoholPercent);
//		  	
//		  	// Ingredients Section
//			Element recipe_data = userAgent.doc.findFirst("<div class=\"recipe_data\">"); //find first div who's class matches recipe_data"
//			Elements seperateIngredients = recipe_data.findEvery("<span class=\"ingredient\">");
//			ingredients = new HashMap<String, String>();
//			for(Element section : seperateIngredients){
//				System.out.println(section.findFirst("<span class=\"name\">").innerText().trim() + " : " +  section.findFirst("<span class=\"amount\">").innerText().trim());
//				ingredients.put(section.findFirst("<span class=\"name\">").innerText().trim(), section.findFirst("<span class=\"amount\">").innerText().trim());
//			}
//		  	
//			// Instructions Section
//			Element instructionsHTML = userAgent.doc.findFirst("<div class=\"RecipeDirections instructions\">"); //find first div who's class matches "RecipeDirections instructions"
//		  	instructions = instructionsHTML.innerHTML().trim();
//		  	
//		  	// Nutrition Section
//			try {
//				Element nutritionDiv = userAgent.doc.findFirst("<div itemprop=\"nutrition\">"); // Find first div who's class matches "nutrition"
//				Element nutritionTable = nutritionDiv.findFirst("<table>");	// Find the first table within the nutrition div
//				Elements spans = nutritionTable.findEach("<span>");
//				String servingSizeLong = nutritionDiv.findFirst("itemprop=\"servingsize\"").innerText();
//				servingSize = servingSizeLong.substring(0, servingSizeLong.length() - 1).replaceAll(" +", " ");
//				
//				nutritionData = new HashMap<String, String>();
//				
//				List<Element> rows = nutritionTable.findEvery("<td>").toList();
//				
//				List<Integer> rightArrows1 = findIndexOfMore(rows.get(1).innerHTML(), ">");	// 3rd
//				List<Integer> leftArrows1  = findIndexOfMore(rows.get(1).innerHTML(), "<");	// 4th
//				List<Integer> rightArrows2 = findIndexOfMore(rows.get(3).innerHTML(), ">");	// 13rd
//				List<Integer> leftArrows2  = findIndexOfMore(rows.get(3).innerHTML(), "<");	// 14th
//				
//				nutritionData.put("energy", rows.get(1).innerHTML().substring(rightArrows1.get(3) + 1, leftArrows1.get(4)));
//				nutritionData.put("alcohol", rows.get(3).innerHTML().substring(rightArrows2.get(12) + 1, leftArrows2.get(13)));
//				
//				for(Element td: spans){				// Iterate through td/th's
//					String workingS = td.outerHTML();
//					nutritionData.put(workingS.substring(workingS.indexOf("\"") + 1, workingS.indexOf("\">")), workingS.substring(workingS.indexOf('>') + 1, workingS.lastIndexOf('<')).replace("-", "0"));
//				}
//			}catch(JauntException e){		 //if an HTTP/connection error occurs, handle JauntException.
//				System.out.println("[WARNING] Couldn't find any nutritional facts!");
//				nutritionData = new HashMap<String, String>();
//			}
//		}catch(JauntException e){		 //if an HTTP/connection error occurs, handle JauntException.
////			System.out.println("[WARNING] Couldn't find any nutritional facts!");
////			nutritionData = new HashMap<String, String>();
//			System.out.println("[WARRNING] " + e);
//		}
//	}
	
}
