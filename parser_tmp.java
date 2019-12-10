package doc_parser;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.HashSet;
//import org.apache.commons.lang3.SerializationUtils;

class entry_in_db{
	String token = null;
	String DOC_id = null;
	int tokens_idx_in_file = 0;
	
	entry_in_db(String a, String b, int c){
		this.token = a;
		this.DOC_id = b;
		this.tokens_idx_in_file = c;
	}
}

public class Parser {	
	
	List<entry_in_db> DataBase = new ArrayList<>();
	
	HashMap<String, String> token_mapper  = new HashMap<String, String>();  
	//debug input file
	String inupt_file = "E:\\sahar_java_project\\output\\FBIS3-50.txt";
	
	//identifier to match each term to its index in the file
	int word_idx_in_file = 0;
	String DOC_id = null;

	String path;
	String regex_numbers="-?\\d+(\\.\\d+)?|-?\\d+(\\.\\d+)? thousand|-?\\d+(\\.\\d+)? million|-?\\d+(\\.\\d+)?m|-?\\d+(\\.\\d+)?b|-?\\d+(\\.\\d+)?k|-?\\d+(\\.\\d+)? billion";
	String regex_dollar="-?\\d+(\\.\\d+)? million dollars|-?\\d+(\\.\\d+)? thousand dollars|-?\\d+(\\.\\d+)? dollars|-?\\d+(\\.\\d+)? \\d/\\d dollars|\\$\\d+(\\.\\d+) |\\$\\d+(\\.\\d+)? million|\\$\\d+(\\.\\d+)? billion|-?\\d+(\\.\\d+)? m dollars|-?\\d+(\\.\\d+)? bn dollars|-?\\d+(\\.\\d+)? billion U.S. dollars|-?\\d+(\\.\\d+)? million U.S. dollars|-?\\d+(\\.\\d+)? trillion U.S. dollars";
	String regex_percent="-?\\d+(\\.\\d+)?%|-?\\d+(\\.\\d+)? percent|-?\\d+(\\.\\d+)? percentege";
	List<String> dollar_patterns = new ArrayList<String>();	//data base for "dollar" patterns
	List<String> regular_words = new ArrayList<String>();	//data base for regular words
	
	Parser(String p){
		path=p;
		main();
	}
	
	public void main() {
		StateMachine State = StateMachine.stop;
		System.out.println(path);
		File currentDir = new File(path);
		File[] files = currentDir.listFiles();
		BufferedReader reader;
		String line;
		
		Pattern r_dollar = Pattern.compile(regex_dollar, Pattern.CASE_INSENSITIVE);
		Pattern r_number = Pattern.compile(regex_numbers, Pattern.CASE_INSENSITIVE);
		Pattern r_percent = Pattern.compile(regex_percent, Pattern.CASE_INSENSITIVE);
		
		//go through each file in the "output" directory
		for (File file : files) {
			if (!file.isDirectory())
			{
				try{					
					reader = new BufferedReader(new FileReader(file));					
					line = reader.readLine();
					//go through each line in file
					while(line != null) {
						if(State == StateMachine.work) {
							//check if end of TEXT
							if(line.equals("</TEXT>")) {
								State = StateMachine.stop;
								word_idx_in_file = 0;
							}
							//fill data base
							else {								
						        Matcher m_dollar = r_dollar.matcher(line);						        
						        Matcher m_number = r_number.matcher(line);						        
						        Matcher m_percent = r_percent.matcher(line);
						        if (m_dollar.find()) {
						        	regex_in_line(line, regex_dollar, "dollar");
						        } 
						        else if (m_percent.find()) {
						        	regex_in_line(line, regex_percent, "percent");
						        } 
						        else if (m_number.find()) {
						        	regex_in_line(line, regex_numbers, "numbers");
						        }
						        else {
									String[] splitted_line = line.split(" ");
									for(int word_idx = 0; word_idx < splitted_line.length; word_idx++) {
										regular_words.add(splitted_line[word_idx]);
										word_idx_in_file++;
									}
								}
							}								
						}
						
						//go through lines until start of TEXT
						if(State == StateMachine.stop) {
							if(line.contains("DOCNO"))
								DOC_id = line.split("<DOCNO> ")[1].split(" </DOCNO>")[0];
							if(line.equals("<TEXT>"))
								State = StateMachine.work;
						}
						line = reader.readLine();
					}
						        
				}
				catch (IOException e) {
					e.printStackTrace();
				}
		

			}
		}
		
		parse_capital_letters(this.DataBase);
		Properties properties = new Properties();
		properties.putAll(this.token_mapper);
		try{
		properties.store(new FileOutputStream("home\\ise\\Desktop\\Information Retvievl\\token_mapper.properties"), null);
		}
		catch (IOException e) {
			e.printStackTrace();
		}
	}
				
				
				
	public void regex_in_line(String line, String regex, String Type){

		
		Pattern r = Pattern.compile(regex, Pattern.CASE_INSENSITIVE);
        Matcher m = r.matcher(line);
		String term=null;
		
        String[] arrOfStr = r.split(line); 
        String word;
        
        while(m.find()) {
        	if(Type=="dollar") {
        		term = dollar_func(m.group()); 
        	}
        	else if (Type=="percent") {
        		term = percent_func(m.group()); 
        	}
        	else {
        		term = number_func(m.group());
        	}
        }
        int first_part = arrOfStr[0].split(" ").length;
        int last_part = arrOfStr[1].split(" ").length;
        for (int i = 0; i < first_part + last_part ; i++) {
        	if (i < first_part) {
        		word = arrOfStr[0].split(" ")[i];
        	}
        	else if (i==first_part) {
        		word = term;
        	}
        	else {
        		word = arrOfStr[1].split(" ")[i-first_part];
        	}       
        	DataBase.add(new entry_in_db(word, DOC_id, word_idx_in_file));
        	word_idx_in_file++;
        }
	}
	public String dollar_func(String s) {
		
		return s;
	}
	public String number_func(String s) {
		return s;
	}
	public String percent_func(String s) {
		s = s.replaceAll("[^\\.0123456789]","");
        s = s+"%";
		return s;
	}
	
	
	
	//public Map<String, String> parse_capital_letters(List<entry_in_db> tokenList){
	//public void parse_capital_letters(entry_in_db[] tokenList){  
	public void parse_capital_letters(List<entry_in_db> tokenList){
		//String[] tokens = doc.split(" ");
		//HashMap<String, String> token_mapper  = new HashMap<String, String>(); // final dict to map tokens
		ArrayList<String> tokenArrayList = new ArrayList<String>();
		

		for (int i = 0; i < tokenList.size(); i++){
			tokenArrayList.add(tokenList.get(i).token);
			}
		
		//for (int i = 0; i < tokenList.length; i++){
		//	tokenArrayList.add(tokenList[i].token);
		//	}
		
		// create deep copy of tokenArrayList array
		
		ArrayList<String> stopWords = new ArrayList<String>() { 
				            { 
				                add("the"); 
				                add("in"); 
				                add("of");
				                add("as");
				                add("i");
				                add("me");
				                add("my");
				                add("our");
				                add("we");
				                add("her");
				            } 
				        };  // later change to generateStopWordsList function
		
		ArrayList<String> UniqueTokens = (ArrayList<String>)tokenArrayList.clone();
		
		// convert each token to lowercase
		for(int i=0,l=UniqueTokens.size();i<l;++i)
		{
			UniqueTokens.set(i, UniqueTokens.get(i).toLowerCase());
		}
		HashSet<String> UniqueTokensl = new HashSet<String>(UniqueTokens); // set of unique tokens
		ArrayList<String> UniqueTokenslist = (ArrayList<String>)UniqueTokensl.clone();
		
		for(int i=0,l=UniqueTokensl.size();i<l;++i)
		{
			ArrayList<String> tokenVersoins = new ArrayList<String>();
			String token = (UniqueTokenslist.get(i));
			String flag = "";
			for(int j=0,k=tokenArrayList.size();j<k;++j)
			{
				if(tokenArrayList.get(j).toLowerCase().equals(token)){
					//tokenVersoins.add(tokenArrayList.get(j));
					if (Character.isUpperCase(tokenArrayList.get(j).charAt(0)) && !flag.equals("lower")){
						tokenVersoins.add(tokenArrayList.get(j));
						flag = "upper";
					}
					else if(Character.isLowerCase(tokenArrayList.get(j).charAt(0)) && !flag.equals("upper")){
						tokenVersoins.add(tokenArrayList.get(j));
						flag = "lower";
					}
					else if(Character.isLowerCase(tokenArrayList.get(j).charAt(0)) && flag.equals("upper")){
						token_mapper.put(token, token);
						break;
					}
					else if(Character.isUpperCase(tokenArrayList.get(j).charAt(0)) && flag.equals("lower")){
						token_mapper.put(token, token);
						break;
					}
			}
				
		  }
			if (flag.equals("upper")){
				this.token_mapper.put(token, tokenVersoins.get(0));
			}
			else if (flag.equals("lower")) {
				this.token_mapper.put(token, token);
				
		}
			
		}
		//Creating a File object for directory
		//File directoryPath = new File(tokenList);
		//List of all files and directories
	    //String contents[] = directoryPath.list();
	    // loop through each document parse each document by " " 
	    // and add each token into tokenArrayList
	    //for (int i = 0; i < contents.length; i++){
	     //   String doc = contents[i]; 
	     //   String[] tokens = doc.split(" ");
	     //  Collections.addAll(tokenArrayList, tokens);
	        		
	        	
		//return token_mapper;   		
	        
	}    
	
	
}
	
	
