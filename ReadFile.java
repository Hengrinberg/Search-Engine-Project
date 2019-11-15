package doc_parser;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.io.IOException;

enum StateMachine{
	work,
	stop
}

public class ReadFile {

	public static void main(String[] args) {
		//create a reader object
		BufferedReader reader;
		try {
			reader = new BufferedReader(new FileReader("/home/ise/Downloads/corpus/FB396001/FB396001"));
			String line = "not null";
					
			
			StateMachine state = StateMachine.stop;
			
			
			int file_index = 0;
			PrintWriter f0 = new PrintWriter(new FileWriter("output" + file_index + ".txt"));					
			
			//main loop						
			while (true) {	
				//read a line
				line = reader.readLine();
				if (line == null)
					break;
				if (state == StateMachine.work) {
					if (line.equals("</DOC>")) {
						state = StateMachine.stop;
						file_index = file_index + 1;
						f0.close();						
					}
					else
						f0.println(line);
				}
				if(state == StateMachine.stop) {
					if (line.equals("<DOC>")) {
						state = StateMachine.work;
						f0 = new PrintWriter(new FileWriter("output" + file_index + ".txt"));
					}
				}
			}
			reader.close();
			f0.close();
			System.out.println("Done");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}