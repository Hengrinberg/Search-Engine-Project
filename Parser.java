package doc_parser;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.List;


public class Parser {	
	String inupt_file = "E:\\sahar_java_project\\output\\FBIS3-50.txt";
	String path;
	String[] regexes_dollars = {"-?\\d+(\\.\\d+)? million dollars"};
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
							//check if and of TEXT
							if(line.equals("</TEXT>"))
								State = StateMachine.stop;
							//fill data base
							else {								
								for(int regex_idx = 0; regex_idx < regexes_dollars.length; regex_idx++) {
									Pattern r = Pattern.compile(regexes_dollars[regex_idx], Pattern.CASE_INSENSITIVE);
							        Matcher m = r.matcher(line);
									if(m.find())
										regex_in_line(line, regexes_dollars[regex_idx]/*, r, m*/);
									else {
										String[] splitted_line = line.split(" ");
										for(int word_idx = 0; word_idx < splitted_line.length; word_idx++)
											regular_words.add(splitted_line[word_idx]);
									}
								}								
							}
						}
						
						//go through lines until start of TEXT
						if(State == StateMachine.stop) {
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
	}
				
				
				
	public void regex_in_line(String line, String regex/*, Pattern r, Matcher m*/){

		Pattern r = Pattern.compile(regex, Pattern.CASE_INSENSITIVE);
        Matcher m = r.matcher(line);
		
		
        String[] arrOfStr = r.split(line); 
        
        while(m.find()) {
        	//regex2 = m.group();
        	dollar_patterns.add(m.group());
        }
        int first_part = arrOfStr[0].split(" ").length;
        int last_part = arrOfStr[1].split(" ").length;
        for (int i = 0; i < first_part + last_part - 1; i++) {
        	if (i < first_part)
        		regular_words.add(arrOfStr[0].split(" ")[i]);
        	else
        		regular_words.add(arrOfStr[1].split(" ")[i-first_part + 1]);        	
        }
	}
	
}

