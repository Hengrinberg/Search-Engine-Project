package doc_parser;

import java.util.Scanner;
import java.io.IOException;

public class Debugger {
	
	String corpus, output;
	
	Debugger(String corpus_path, String output_path){
		corpus = corpus_path;
		output = output_path;
		main();
	}
	
	public void main() {
//		String input_path, output_path; 
//		Scanner scanIn = new Scanner(System.in);
//		
//		System.out.println("Debugger : Please enter a valid path for the corpus directory");		
//		input_path = scanIn.nextLine();
//		System.out.printf("Chosen corpus directory is %s\n", input_path);
//		System.out.println("Debugger : Please enter a valid path for the output directory");		
//		output_path = scanIn.nextLine();
//		System.out.printf("Chosen output directory is %s\n", output_path);
//		scanIn.close();
		
		long startTime, stopTime;
		
		//create a readfile object
//		startTime = System.nanoTime();
//		ReadFile2 read_file_obj = new ReadFile2(corpus, output);
//		System.out.println("Debugger : file reader finished\n");
//		System.out.printf("Number of files in corpus = %d\n", read_file_obj.dbg_count_files);
//		stopTime = System.nanoTime();
//		System.out.println("The file reader took:");
//		System.out.println(stopTime - startTime);
		
		StopWords_hm stop_words = new StopWords_hm(/*"E:\\sahar_java_project\\stop_words.txt"*/"shtut");
		
		
		startTime = System.nanoTime();		
//		Parser parse = new Parser(output);
		Parser parse = new Parser("E:\\sahar_java_project\\output", stop_words.stop_words_hash);
		stopTime = System.nanoTime();
		System.out.println("The parser took:");
		System.out.println(stopTime - startTime);
	}

}
