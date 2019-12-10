package doc_parser;


import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;

public class StopWords_hm {
	
	HashMap<String, String> stop_words_hash = new HashMap<>();
	String words_file;
	
	StopWords_hm(String inputttt_file){
//		this.words_file = input_file;
		this.words_file = "E:\\sahar_java_project\\stop_words.txt";
		create_hash();
	}
	
	
	void create_hash(){
		try {
			BufferedReader reader;
			reader = new BufferedReader(new FileReader(words_file));
			String
			line = reader.readLine();
			while(line != null) {
				stop_words_hash.put(line, line);
				line = reader.readLine();
			}
			reader.close();
		}
		catch (IOException e) {
			e.printStackTrace();
		}
	}
}
