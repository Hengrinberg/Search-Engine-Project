package doc_parser;

import java.util.Scanner;
import java.io.IOException;

public class Debugger {
	
	String corpus, output;
	
	Debugger(String corpus_path, String output_path){
		corpus = corpus_path;
		output = output_path;
//		main();
	}
	
	public static void main(String[] args) {	
		
		//IN ORDER FOR THE PROJECT TO RUN, SET THE FIRST ARGUMENT TO THE CORPUS PATH AND THE SECOND ARGUMENT TO THE OUTPUT PATH
		Debugger debug_obj = new Debugger("E:\\sahar_java_project\\corpus", "E:\\sahar_java_project\\output");
		
		long startTime, stopTime;		
		
		/*------------------------read file process------------------------*/
		startTime = System.nanoTime();		//start the timer for the readfile process		
//		ReadFile2 read_file_obj = new ReadFile2(corpus, output);
		ReadFile2 read_file_obj = new ReadFile2(debug_obj.corpus, debug_obj.output);
		System.out.println("Debugger : file reader finished\n");
		System.out.printf("Number of files in corpus = %d\n", read_file_obj.dbg_count_files);
		stopTime = System.nanoTime();		//stop the timer for the readfile process
		System.out.println("The file reader took:");
		System.out.println(stopTime - startTime);
		
		/*------------------------stop words process------------------------*/
		StopWords_hm stop_words = new StopWords_hm("E:\\sahar_java_project\\stop_words.txt");
		
		/*------------------------parser process------------------------*/
		startTime = System.nanoTime();		
//		Parser parse = new Parser(output, stop_words.stop_words_hash);
		Parser parse_obj = new Parser(debug_obj.output, stop_words.stop_words_hash);
		stopTime = System.nanoTime();
		System.out.println("The parser took:");
		System.out.println(stopTime - startTime);
		
		
		//the data base you should use for your functions is : parse_obj.DataBase
		/*
		{
			enter your code here
		}*/
		
	}

}
