package master;


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
public class TakeDrinkData {

	TakeDrinkData(){
		int drinkNumber = 4; // Starts from 4 all the way to 12317.
//		String stringNumber = new Integer(drinkNumber).toString();
//		while(stringNumber.length() < 4){
//			stringNumber = "0" + stringNumber;
//		}
		String site = "http://www.drinksmixer.com/drink" + drinkNumber + ".html";
		writeDataToFile(site);
		
	}
	
	public void writeDataToFile(String site){ // Need to actually make this write to file after I make a standardized file
		try{
			UserAgent userAgent = new UserAgent();	//create new userAgent (headless browser).
			userAgent.visit(site);//visit a url   
			
			Element drinkTrail = userAgent.doc.findFirst("<div class=\"pm\">"); // Find first div who's class matches "pm"
			System.out.print("Drink Trail: ");
	      	Elements trailElements = drinkTrail.findEvery("<a href>");
	      	int size = trailElements.size();
	      	int counter = 1;
	      	for(Element e : trailElements){
	      		System.out.print(e.innerText().replace("&reg;", "\u00AE"));
	      		if(counter < size){
	      			System.out.print(" -> ");
	      		}
	      		counter++;
	      	}
			System.out.println("\n");
	      	
			Element drinkName = userAgent.doc.findFirst("<h1 class=\"fn recipe_title\">"); //find first div who's class matches "fn recipe_title"
			String title = drinkName.innerHTML();
	      	System.out.println("Title of drink: " + title.substring(0, title.length() - 7));
			System.out.println();
	      	
			Element recipeStats = userAgent.doc.findFirst("<div class=\"recipeStats\">"); //find first div who's class matches "recipeStats"
	      	String glassType = recipeStats.findFirst("<img>").getAt("title");
	      	String alcoholPercent = recipeStats.findFirst("<b>").innerText();
	      	System.out.println("Serve in and alcohol percentage:");
			System.out.println(glassType);
			System.out.println(alcoholPercent);
			System.out.println();
	      	
			Element recipe_data = userAgent.doc.findFirst("<div class=\"recipe_data\">"); //find first div who's class matches recipe_data"
			Elements seperateIngredients = recipe_data.findEvery("<span class=\"ingredient\">");
			Map<String, String> ingredients = new HashMap<String, String>();
			System.out.println("Ingredients:");
			for(Element section : seperateIngredients){
				System.out.println(section.findFirst("<span class=\"name\">").innerText().replace("&reg;", "\u00AE") + " : " + section.findFirst("<span class=\"amount\">").innerText());
				ingredients.put(section.findFirst("<span class=\"name\">").innerText(), section.findFirst("<span class=\"amount\">").innerText());
			}
			System.out.println();
	      	
			Element instructions = userAgent.doc.findFirst("<div class=\"RecipeDirections instructions\">"); //find first div who's class matches "RecipeDirections instructions"
	      	System.out.println("Instructions:\n" + instructions.innerHTML());
	      	
	      	
			Element nutritionDiv = userAgent.doc.findFirst("<div itemprop=\"nutrition\">"); // Find first div who's class matches "nutrition"
			Element nutritionTable = nutritionDiv.findFirst("<table>");	// Find the first table within the nutrition div
			Elements spans = nutritionTable.findEach("<span>");
			String servingSize = nutritionDiv.findFirst("itemprop=\"servingsize\"").innerText();
	      	System.out.println("\nNutrition information: " + servingSize.substring(0, servingSize.length() - 1));
			
			Map<String, String> data = new HashMap<String, String>();
			
			List<Element> rows = nutritionTable.findEvery("<td>").toList();
			
			List<Integer> rightArrows1 = findIndexOfMore(rows.get(1).innerHTML(), ">");	// 3rd
			List<Integer> leftArrows1  = findIndexOfMore(rows.get(1).innerHTML(), "<");	// 4th
			List<Integer> rightArrows2 = findIndexOfMore(rows.get(3).innerHTML(), ">");	// 13rd
			List<Integer> leftArrows2  = findIndexOfMore(rows.get(3).innerHTML(), "<");	// 14th
			
			data.put("energy", rows.get(1).innerHTML().substring(rightArrows1.get(3) + 1, leftArrows1.get(4)));
			data.put("alcohol", rows.get(3).innerHTML().substring(rightArrows2.get(12) + 1, leftArrows2.get(13)));
			
			for(Element td: spans){				// Iterate through td/th's
				String workingS = td.outerHTML();
				data.put(workingS.substring(workingS.indexOf("\"") + 1, workingS.indexOf("\">")), workingS.substring(workingS.indexOf('>') + 1, workingS.lastIndexOf('<')).replace("-", "0"));
	        }
			for(Entry<String, String> e : data.entrySet()){
				System.out.println(e.getKey() + " = " + e.getValue());
			}
	    }catch(JauntException e){         //if an HTTP/connection error occurs, handle JauntException.
	    	System.err.println(e);
	    }
	}
	
	public void printPageInfo(String site){ // need to make it so none of the data is put into variables it's just printed
		try{
			UserAgent userAgent = new UserAgent();	//create new userAgent (headless browser).
			userAgent.visit(site);//visit a url   
			
			Element drinkTrail = userAgent.doc.findFirst("<div class=\"pm\">"); // Find first div who's class matches "pm"
			System.out.print("Drink Trail: ");
	      	Elements trailElements = drinkTrail.findEvery("<a href>");
	      	int size = trailElements.size();
	      	int counter = 1;
	      	for(Element e : trailElements){
	      		System.out.print(e.innerText().replace("&reg;", "\u00AE"));
	      		if(counter < size){
	      			System.out.print(" -> ");
	      		}
	      		counter++;
	      	}
			System.out.println("\n");
	      	
			Element drinkName = userAgent.doc.findFirst("<h1 class=\"fn recipe_title\">"); //find first div who's class matches "fn recipe_title"
			String title = drinkName.innerHTML();
	      	System.out.println("Title of drink: " + title.substring(0, title.length() - 7));
			System.out.println();
	      	
			Element recipeStats = userAgent.doc.findFirst("<div class=\"recipeStats\">"); //find first div who's class matches "recipeStats"
	      	String glassType = recipeStats.findFirst("<img>").getAt("title");
	      	String alcoholPercent = recipeStats.findFirst("<b>").innerText();
	      	System.out.println("Serve in and alcohol percentage:");
			System.out.println(glassType);
			System.out.println(alcoholPercent);
			System.out.println();
	      	
			Element recipe_data = userAgent.doc.findFirst("<div class=\"recipe_data\">"); //find first div who's class matches recipe_data"
			Elements seperateIngredients = recipe_data.findEvery("<span class=\"ingredient\">");
			Map<String, String> ingredients = new HashMap<String, String>();
			System.out.println("Ingredients:");
			for(Element section : seperateIngredients){
				System.out.println(section.findFirst("<span class=\"name\">").innerText().replace("&reg;", "\u00AE") + " : " + section.findFirst("<span class=\"amount\">").innerText());
				ingredients.put(section.findFirst("<span class=\"name\">").innerText(), section.findFirst("<span class=\"amount\">").innerText());
			}
			System.out.println();
	      	
			Element instructions = userAgent.doc.findFirst("<div class=\"RecipeDirections instructions\">"); //find first div who's class matches "RecipeDirections instructions"
	      	System.out.println("Instructions:\n" + instructions.innerHTML());
	      	
	      	
			Element nutritionDiv = userAgent.doc.findFirst("<div itemprop=\"nutrition\">"); // Find first div who's class matches "nutrition"
			Element nutritionTable = nutritionDiv.findFirst("<table>");	// Find the first table within the nutrition div
			Elements spans = nutritionTable.findEach("<span>");
			String servingSize = nutritionDiv.findFirst("itemprop=\"servingsize\"").innerText();
	      	System.out.println("\nNutrition information: " + servingSize.substring(0, servingSize.length() - 1));
			
			Map<String, String> data = new HashMap<String, String>();
			
			List<Element> rows = nutritionTable.findEvery("<td>").toList();
			
			List<Integer> rightArrows1 = findIndexOfMore(rows.get(1).innerHTML(), ">");	// 3rd
			List<Integer> leftArrows1  = findIndexOfMore(rows.get(1).innerHTML(), "<");	// 4th
			List<Integer> rightArrows2 = findIndexOfMore(rows.get(3).innerHTML(), ">");	// 13rd
			List<Integer> leftArrows2  = findIndexOfMore(rows.get(3).innerHTML(), "<");	// 14th
			
			data.put("energy", rows.get(1).innerHTML().substring(rightArrows1.get(3) + 1, leftArrows1.get(4)));
			data.put("alcohol", rows.get(3).innerHTML().substring(rightArrows2.get(12) + 1, leftArrows2.get(13)));
			
			for(Element td: spans){				// Iterate through td/th's
				String workingS = td.outerHTML();
				data.put(workingS.substring(workingS.indexOf("\"") + 1, workingS.indexOf("\">")), workingS.substring(workingS.indexOf('>') + 1, workingS.lastIndexOf('<')).replace("-", "0"));
	        }
			for(Entry<String, String> e : data.entrySet()){
				System.out.println(e.getKey() + " = " + e.getValue());
			}
	    }catch(JauntException e){         //if an HTTP/connection error occurs, handle JauntException.
	    	System.err.println(e);
	    }
	}
	
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
		new TakeDrinkData();
	}

}
