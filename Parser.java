package doc_parser;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.List;

class remove_from_LUT{
	static String LUT_of_bad_characters[] = {"-", ":", ",", ".", ";", "[", "]", "(", ")"};
	static String LUT_of_bad_sections[] = {"<", ">"/*, "[", "]"*/};
}


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
	
	//debug input file
	String inupt_file = "E:\\sahar_java_project\\output\\FBIS3-50.txt";
	
	//identifier to match each term to its index in the file
	int word_idx_in_file = 0;
	String DOC_id = null;

	HashMap<String, String> local_stop_words = new HashMap<>();
	String path;
	String regex_numbers="-?\\d+(\\.\\d+)?|-?\\d+(\\.\\d+)? thousand|-?\\d+(\\.\\d+)? million|-?\\d+(\\.\\d+)?m|-?\\d+(\\.\\d+)?b|-?\\d+(\\.\\d+)?k|-?\\d+(\\.\\d+)? billion";
	String regex_dollar="-?\\d+(\\.\\d+)? million dollars|-?\\d+(\\.\\d+)? thousand dollars|-?\\d+(\\.\\d+)? dollars|-?\\d+(\\.\\d+)? \\d/\\d dollars|\\$\\d+(\\.\\d+) |\\$\\d+(\\.\\d+)|\\$\\d+(\\.\\d+)? million|\\$\\d+(\\.\\d+)? billion|-?\\d+(\\.\\d+)? m dollars|-?\\d+(\\.\\d+)? bn dollars|-?\\d+(\\.\\d+)? billion U.S. dollars|-?\\d+(\\.\\d+)? million U.S. dollars|-?\\d+(\\.\\d+)? trillion U.S. dollars";
	String regex_percent="-?\\d+(\\.\\d+)?%|-?\\d+(\\.\\d+)? percent|-?\\d+(\\.\\d+)? percentege";
	List<String> dollar_patterns = new ArrayList<String>();	//data base for "dollar" patterns
	List<String> regular_words = new ArrayList<String>();	//data base for regular words
	Pattern r_dollar;
	Pattern r_number;
	Pattern r_percent;
	
	/*--------------------------constructor--------------------------*/
	Parser(String p, HashMap<String, String> stop_words){
		local_stop_words = stop_words;
		path=p;
		r_dollar = Pattern.compile(regex_dollar, Pattern.CASE_INSENSITIVE);
		r_number = Pattern.compile(regex_numbers, Pattern.CASE_INSENSITIVE);
		r_percent = Pattern.compile(regex_percent, Pattern.CASE_INSENSITIVE);
		main();
	}
	
	/*--------------------------class member function--------------------------*/
	public void main() {
		StateMachine State = StateMachine.stop;
		System.out.println(path);
		File currentDir = new File(path);
		File[] files = currentDir.listFiles();
		BufferedReader reader;
		String line;
		
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
								
								//DEBUG
//								line = "6% without Portfolio        --          23         31              24";
								
								//if line is empty, continue to next line;							
								if(line.length() == 0) {
									line = reader.readLine();									
									continue;
								}
//								//get rid of <> [] sections from line
								line = remove_sections_from_line(line);
								
						        Matcher m_dollar = r_dollar.matcher(line);						        
						        Matcher m_number = r_number.matcher(line);						        
						        Matcher m_percent = r_percent.matcher(line);
						        
						        //check if a regex exist in current line
						        if (m_dollar.find())	 
						        {
						        	regex_in_line_improved(line, regex_dollar, "dollar");
						        } 
						        else if (m_percent.find()) 
						        {
						        	regex_in_line_improved(line, regex_percent, "percent");
						        } 
						        else if (m_number.find()) 
						        {
						        	regex_in_line_improved(line, regex_numbers, "number");
						        }						        						        
						        //in case of no regex in line
						        else	 
						        {
									//get rid of <> , . : ;  characters from line
									line = remove_chars_from_line(line);
						        	String[] splitted_line = line.split("\\s+");
									for(int word_idx = 0; word_idx < splitted_line.length; word_idx++) {
										if(!splitted_line[word_idx].isEmpty()) 
										{
											if(!local_stop_words.containsKey(splitted_line[word_idx].toLowerCase()))
											{
												DataBase.add(new entry_in_db(splitted_line[word_idx], DOC_id, word_idx_in_file));
											}
											word_idx_in_file++;
										}
									}
								}
							}								
						}
						
						//go through lines until start of TEXT
						if(State == StateMachine.stop) {
							if(line.indexOf("<DOCNO>") > -1)
							{
								DOC_id = line.replace("<DOCNO>", "");
								DOC_id = DOC_id.replace("</DOCNO>", "");								
								//DEBUG ONLY
								System.out.printf("DOC_id = %s\n", DOC_id);
							}
//							if(line.contains("DOCNO"))
//								DOC_id = line.split("<DOCNO> ")[1].split(" </DOCNO>")[0];
							if(line.equals("<TEXT>")) {
								State = StateMachine.work;
							}
						}
						line = reader.readLine();
					}
						        
				}
				catch (IOException e) {
					e.printStackTrace();
				}
		

			}
		}
	}
				
				
	/*--------------------------class member function--------------------------*/			
	public void regex_in_line_improved(String line, String regex, String Type)
	{
		Pattern r = Pattern.compile(regex, Pattern.CASE_INSENSITIVE);
        Matcher m = r.matcher(line);
		String term=null;
		
        String[] arrOfStr = r.split(line); 
        String word;  
        
        Matcher m_dollar_line = r_dollar.matcher(line);						        
        Matcher m_number_line = r_number.matcher(line);						        
        Matcher m_percent_line = r_percent.matcher(line);
        
        //check if arrOfStr substrings contains regex
        for(int i = 0; i < arrOfStr.length; i++)
        {       	
            Matcher m_dollar_substring = r_dollar.matcher(arrOfStr[i]);						        
            Matcher m_number_substring = r_number.matcher(arrOfStr[i]);						        
            Matcher m_percent_substring = r_percent.matcher(arrOfStr[i]);
            if (m_dollar_substring.find()) 
            {//dollar regex
//            	term = dollar_func(m.group());
//            	DataBase.add(new entry_in_db(term, DOC_id, word_idx_in_file));
//            	word_idx_in_file++;
            	regex_in_line_improved(arrOfStr[i], regex_dollar, "dollar");            	
            } 
            else if (m_percent_substring.find()) 
            {
//            	term = percent_func(m.group());
//            	DataBase.add(new entry_in_db(term, DOC_id, word_idx_in_file));
//            	word_idx_in_file++;
            	regex_in_line_improved(arrOfStr[i], regex_percent, "percent");             	
            } 
            else if (m_number_substring.find()) 
            {
//            	term = number_func(m.group());
//            	DataBase.add(new entry_in_db(term, DOC_id, word_idx_in_file));
//            	word_idx_in_file++;
            	regex_in_line_improved(arrOfStr[i], regex_numbers, "number");            	
            }
        	else	 
            {//case of no regex in the substring        
        		arrOfStr[i] = remove_chars_from_line(arrOfStr[i]);
        		String[] new_arrOfStr = arrOfStr[i].split("\\s+");						//split substring to words 
        		for(int word_idx = 0; word_idx < new_arrOfStr.length; word_idx++) 		//add unEmpty words to db
        		{
        			if(!new_arrOfStr[word_idx].isEmpty())
        			{
	    				if(!local_stop_words.containsKey(new_arrOfStr[word_idx].toLowerCase()))
    					{//check if stop words exist in substring
	    					DataBase.add(new entry_in_db(new_arrOfStr[word_idx], DOC_id, word_idx_in_file));
	    				}
    					word_idx_in_file++;
        			}
        		}
            }
            if(Type == "percent") 
            {
	            if(m_percent_line.find())
	            {
	            	term = percent_func(m_percent_line.group());
	            	DataBase.add(new entry_in_db(term, DOC_id, word_idx_in_file));
	            	word_idx_in_file++;
	            }
            }
            else if(Type == "number")
            {
            	if(m_number_line.find())
	            {
	            	term = number_func(m_number_line.group());
	            	DataBase.add(new entry_in_db(term, DOC_id, word_idx_in_file));
	            	word_idx_in_file++;
	            }  
            }
        }		
	}
	
	/*--------------------------class member function--------------------------*/
	public String dollar_func(String term) {
		String new_term;
        term=(term.toLowerCase()).replaceAll(" u.s. dollars| dollars|$", "");
        
        double num=Double.parseDouble(term.replaceAll("[^\\.0123456789]",""));
        DecimalFormat df = new DecimalFormat("#,###.##");
        DecimalFormat df2 = new DecimalFormat("#.##");
        if (term.contains("/")){
            new_term=term+" Dollars";
        }
        else if((term.toLowerCase()).contains("m"))
        {
            new_term = String.valueOf(df.format(num))+" M Dollars";
        }
        else if((term.toLowerCase()).contains("b")){
            num=num*1000;
            new_term = String.valueOf(df2.format(num))+" M Dollars"; 
        }
        else if ((term.toLowerCase()).contains("t")){
            num=num*1000000;
            new_term = String.valueOf(df2.format(num))+" M Dollars";
        }
        else {
            if (num>999999){
                num=num/1000000;
                new_term =String.valueOf(df2.format(num))+" M Dollars";
            }
            else{
                new_term = String.valueOf(df.format(num))+" Dollars";
            }
        }
		return new_term;
	}
	
	/*--------------------------class member function--------------------------*/
	public String number_func(String term) {
		String new_term;
//        System.out.println(term);
        double num=Double.parseDouble(term.replaceAll("[^\\.0123456789]",""));
        DecimalFormat df = new DecimalFormat("#.###");
        if((term.toLowerCase()).contains("m"))
        {
            new_term = String.valueOf(df.format(num))+"M";

        }
        else if((term.toLowerCase()).contains("b")){
            new_term = String.valueOf(df.format(num))+"B"; 
        }
        else if ((term.toLowerCase()).contains("t")){
            
            new_term = String.valueOf(df.format(num))+"K";
        }
        else {
            if (num>999999999){
                num=num/1000000000;
                new_term =String.valueOf(df.format(num))+"B";
            }
            else if(num>999999){
                num=num/1000000;
                new_term =String.valueOf(df.format(num))+"M";
            }
            else if(num>=1000){
                num=num/1000;
                new_term =String.valueOf(df.format(num))+"K";
            }
            else{
                if (term.contains("/")){
                    new_term = term;
                }
                else {
                    new_term = String.valueOf(df.format(num));
                }
            }
        }
		return new_term;
	}
	
	/*--------------------------class member function--------------------------*/
	public String percent_func(String s) {
		s = s.replaceAll("[^\\.0123456789]","");
        s = s+"%";
		return s;
	}
	
	/*--------------------------class member function--------------------------*/
	public String remove_chars_from_line(String input_line) {
		String output_line, bad_character;

		for(int i = 0; i < remove_from_LUT.LUT_of_bad_characters.length; i++) {
			//check if line contains character to get rid of
			if(input_line.indexOf(remove_from_LUT.LUT_of_bad_characters[i]) >= 0) {
				bad_character = remove_from_LUT.LUT_of_bad_characters[i];
				output_line = input_line.replace(bad_character, "");
				return remove_chars_from_line(output_line);
			}						
		}
		output_line = input_line;
		return output_line;
	}
	
	/*--------------------------class member function--------------------------*/
	public String remove_sections_from_line(String input_line) {
		String output_line, toBeReplaced, bad_section_start, bad_section_end;
		int StartIndex, EndIndex;			
		for(int i = 0; i < remove_from_LUT.LUT_of_bad_sections.length; i+=2) {
			//check if line contains section to get rid of
			if(input_line.indexOf(remove_from_LUT.LUT_of_bad_sections[i]) >= 0) {
				bad_section_start = remove_from_LUT.LUT_of_bad_sections[i];
				bad_section_end = remove_from_LUT.LUT_of_bad_sections[i+1];
				StartIndex = input_line.indexOf(bad_section_start);
				EndIndex = input_line.indexOf(bad_section_end);
				toBeReplaced = input_line.substring(StartIndex, EndIndex + 1);
				output_line = input_line.replace(toBeReplaced, "");
				return remove_sections_from_line(output_line);
			}			
		}
		output_line = input_line;
		return output_line;
	}
}





