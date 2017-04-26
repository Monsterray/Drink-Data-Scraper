/**
 * 
 */
package master;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import objects.Drink;

/**
 * @author Monsterray
 *
 */
public class DrinkDatabase {
	
	private List<File> dataFiles;
	private List<Drink> allDrinks;
	
	/**
	 * @param path
	 */
	public DrinkDatabase(String path){
		allDrinks = new ArrayList<Drink>();
		dataFiles = new ArrayList<File>();
		crawlFolders(new File(path));
		System.out.println("Amount of files collected:" + dataFiles.size());
		
		try {
			for(File f : dataFiles){
				parseData(f);
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		System.out.println("Amount of Drinks currently in db:" + allDrinks.size());
		Collections.sort(allDrinks);
	}
	
	/**
	 * @param parent
	 */
	private void crawlFolders(File parent){
		File[] fileList = parent.listFiles();
		
		for(File current : fileList){
			if(current.isDirectory()){
				crawlFolders(current);
			}else{
				if(current.getName().substring(current.getName().lastIndexOf('.'), current.getName().length()).equals(".drink")){
					dataFiles.add(current);
				}
			}
		}
	}
	
	/**
	 * @param dataFile
	 * @throws FileNotFoundException
	 * @throws IOException
	 */
	private void parseData(File dataFile) throws FileNotFoundException, IOException{
		String folderPath = dataFile.getPath().replace("..\\Data Drinks", "").replace("\\", ":");
		String[] folderNames = folderPath.split(":");
		
		BufferedReader in = new BufferedReader(new FileReader(dataFile));
		String line = null;
		while((line = in.readLine()) != null){
		    String[] sections = line.split("\t\t");
		    String[] ingredients = sections[2].split("\t");
		    Map<String, String> ingreds = new HashMap<String, String>();
		    for(String combin : ingredients){
		    	ingreds.put(combin.substring(0, combin.indexOf(":")), combin.substring(combin.indexOf(":") + 1, combin.length()));
		    }
		    String[] nutrition = sections[4].split("\t");
		    Map<String, String> nutrits = new HashMap<String, String>();
		    for(int i = 2; i < nutrition.length; i++){
		    	nutrits.put(nutrition[i].substring(0, nutrition[i].indexOf("=")), 
		    				nutrition[i].substring(nutrition[i].indexOf("=") + 1, nutrition[i].length()));
		    }

			allDrinks.add(new Drink(Arrays.asList(folderNames), sections[0], sections[1],
					nutrition[1].substring(nutrition[1].indexOf("="), nutrition[1].length()), 
					ingreds, sections[3], nutrition[0], nutrits));
		}
		in.close();
	}
}









