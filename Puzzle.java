import java.util.*;
import java.io.*;
import java.nio.*;

public class Puzzle {

	public static void main(String[] args) {
		String input_filename = "combos-in.txt";
		File_IO io_interface = new File_IO();

		//records = list of input strings, results = list of output strings
		List<String> records = new ArrayList<String>();
		List<String> results = new ArrayList<String>();
		ListIterator<String> records_enum = null;
		int counter = 1;

		//read file, store as a list in records
		records = io_interface.read(input_filename);		
		records_enum=records.listIterator();

		//Traverse the records list, only first 149 entries
		while(records_enum.hasNext() && (counter <150)){
			results.add("Case #" + String.valueOf(counter));
			String str = records_enum.next();

			//skip those records with length larger than 7
			if (str.length()>=8){
				results.add("Entry with >= 8 letters, skipped.");
			} else {

			//combine() returns a list of all possible combinations of a given string
			//results.addAll() appends new results to the comprehensive list of combinations from previous entries
			results.addAll(combine(str)); }
			counter++;
		}

		//Write list with results to file
		io_interface.write("output.txt",results);

		if (counter >= 150){
			System.out.println("There are more than 150 records. Only 149 were considered.");
		}
	}

	public static List<String> combine(String letters) {
		//this divide&conquer recursive function contains the main logic implementing string permutations
		//the entry parameter is a string of letters, letters[0..n]
		//each recursive call removes the first letter: letters[0] 
		//and calls itself passing as parameter the string reminder letters[1..n]
		//the recursive call returns a list with all possible permutations of letters[1..n]
		//for every combination of letters[1..n], char letter[0] will be inserted in
		//every position,example: leters[1..n] ="az", letters[0]="f" 
		//every possible combination of letters[1..n]="az" is ["az","za"]
		//combine("faz")-> ["afz","azf","faz","fza","zaf","zfa"]
		//the returned list will be ordered alphabetically

		List<String> result = new ArrayList<String>();
		StringBuilder str = new StringBuilder();
		ListIterator<String> litr = null;

		if (letters.length() == 1) {
			//if letters has only one letter, return a list with a single element, ie that letter
			result.add(letters);
			return result;
		} else {	
			//here takes place the recursive call.
			//litr is an enumerator for the list of all possible combinations of letters[1..n]
			litr=combine(letters.substring(1)).listIterator();

			while(litr.hasNext()){
				//for every combination of letters[1..n]
				//char_all_positions will place letter[0] in each position
				result.addAll(char_all_positions(litr.next(),letters.charAt(0)));
			}
			//sort result list alphabetically
			Collections.sort(result);
			return result;
		}
	} 

	public static List<String> char_all_positions(String letters, char character ){
		//this method inserts character in every possible position of string letters
		//leters="az", char="f" -> ["faz","afz","azf"]
		List<String> records = new ArrayList<String>();
		for(int i=0; i<=letters.length(); i++){
			records.add(insert_char(letters,character,i));
		}
		return records;
	}

	public static String insert_char(String letters, char character, int index ){
		//this method inserts char into a string in a given index
		StringBuilder str = new StringBuilder(letters);
		str.insert(index, character);
		return str.toString();
	}

}

class  File_IO{
	//this class defines: 1) a reader method which reads a file and returns a string list
	//2) a writer method that writes a given string list to a file whose name is provided

	List<String> records = new ArrayList<String>();

	public List<String> read(String filename) {

		try
		{
			FileReader file = new FileReader(filename);
			BufferedReader reader = new BufferedReader(file);
			String line;
			//read line by line and store in a list called records
			while ((line = reader.readLine()) != null)
			{
				records.add(line);
			}
			reader.close();
			return records;
		}
		catch (Exception e)
		{
			System.err.format("Exception occurred trying to read '%s'.", filename);
			e.printStackTrace();
			return null;
		}
	}

	public Boolean write(String output_filename, List<String> results){

		try {
			//append = false, it will destroy old file, if any
            FileWriter output_writer = new FileWriter(output_filename, false);   

           	ListIterator<String>  results_enum = null;
           	results_enum=results.listIterator();

           	//write each line of results to the file. carriage return + line feed = \n
           	while(results_enum.hasNext()){
		       output_writer.write(results_enum.next()+"\r\n");
            }
        	output_writer.close(); 
    	}
        catch (IOException  e) {
	        System.err.format("IO Exception occurred '%s'.", output_filename);
	        e.printStackTrace();
	        return false;
        }
      return true;
    }
}