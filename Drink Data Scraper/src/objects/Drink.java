/**
 * 
 */
package objects;

import java.util.List;
import java.util.Map;

/**
 * @author Monsterray
 *
 */
public class Drink implements Comparable<Drink>{
	
	private List<String> folders;
	
	private String drinkTitle;

	private String glassType;

	private String alcoholPercent;

	private String instructions;

	private String servingSize;

	private Map<String, String> ingredients;

	private Map<String, String> nutritionData;
	
	public Drink(){
		
	}
	
	/**
	 * @param folders
	 * @param drinkTitle
	 * @param glassType
	 * @param alcoholPercent
	 * @param ingredients
	 * @param instructions
	 * @param servingSize
	 * @param nutritionData
	 */
	public Drink(List<String> folders, String drinkTitle, String glassType,
			String alcoholPercent, Map<String, String> ingredients,
			String instructions, String servingSize,
			Map<String, String> nutritionData) {
		this.folders = folders;
		this.drinkTitle = drinkTitle;
		this.glassType = glassType;
		this.alcoholPercent = alcoholPercent;
		this.ingredients = ingredients;
		this.instructions = instructions;
		this.servingSize = servingSize;
		this.nutritionData = nutritionData;
	}

	/**
	 * @return the folders
	 */
	public List<String> getFolders() {
		return folders;
	}

	/**
	 * @param folders the folders to set
	 */
	public void setFolders(List<String> folders) {
		this.folders = folders;
	}

	/**
	 * @return the drinkTitle
	 */
	public String getDrinkTitle() {
		return drinkTitle;
	}

	/**
	 * @param drinkTitle the drinkTitle to set
	 */
	public void setDrinkTitle(String drinkTitle) {
		this.drinkTitle = drinkTitle;
	}

	/**
	 * @return the glassType
	 */
	public String getGlassType() {
		return glassType;
	}

	/**
	 * @param glassType the glassType to set
	 */
	public void setGlassType(String glassType) {
		this.glassType = glassType;
	}

	/**
	 * @return the alcoholPercent
	 */
	public String getAlcoholPercent() {
		return alcoholPercent;
	}

	/**
	 * @param alcoholPercent the alcoholPercent to set
	 */
	public void setAlcoholPercent(String alcoholPercent) {
		this.alcoholPercent = alcoholPercent;
	}

	/**
	 * @return the ingredients
	 */
	public Map<String, String> getIngredients() {
		return ingredients;
	}

	/**
	 * @param ingredients the ingredients to set
	 */
	public void setIngredients(Map<String, String> ingredients) {
		this.ingredients = ingredients;
	}

	/**
	 * @return the instructions
	 */
	public String getInstructions() {
		return instructions;
	}

	/**
	 * @param instructions the instructions to set
	 */
	public void setInstructions(String instructions) {
		this.instructions = instructions;
	}

	/**
	 * @return the servingSize
	 */
	public String getServingSize() {
		return servingSize;
	}

	/**
	 * @param servingSize the servingSize to set
	 */
	public void setServingSize(String servingSize) {
		this.servingSize = servingSize;
	}

	/**
	 * @return the nutritionData
	 */
	public Map<String, String> getNutritionData() {
		return nutritionData;
	}

	/**
	 * @param nutritionData the nutritionData to set
	 */
	public void setNutritionData(Map<String, String> nutritionData) {
		this.nutritionData = nutritionData;
	}

	@Override
	public int compareTo(Drink d) {
		return this.drinkTitle.compareTo(d.drinkTitle);
	}
}
