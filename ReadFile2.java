package doc_parser;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.io.IOException;

enum StateMachine{
work,
stop
}

public class ReadFile2 {

	String output_path;	
	String input_path;
	int dbg_count_files;

	ReadFile2(String p1, String p2)
	{
		input_path = p1;
		output_path = p2;
		dbg_count_files = 0;
		main(null);
	}
	public void main(String[] args) {
		//get the dir we should run on
		File currentDir = new File(input_path);
		// run that function that runs on all files and seperate them to documents.
		seperateFiles(currentDir);

			/*
			public class RecursiveFileDisplay {
			
			public static void main(String[] args) {
			File currentDir = new File("."); // current directory
			displayDirectoryContents(currentDir);
			}
			
			public static void displayDirectoryContents(File dir) {
			try {
			File[] files = dir.listFiles();
			for (File file : files) {
			if (file.isDirectory()) {
			System.out.println("directory:" + file.getCanonicalPath());
			displayDirectoryContents(file);
			} else {
			System.out.println("     file:" + file.getCanonicalPath());
			}
			}
			} catch (IOException e) {
			e.printStackTrace();
			}
			}
			
			}
			*/
	}
	public void seperateFiles(File dir){
		//initialize a debug counter for number of directories in corpus
		int dbg_count_directories = 0;
		//create a reader object
		BufferedReader reader;
		//create a new object that receives a path
		File[] files = dir.listFiles();
		for (File file : files) {
			if (file.isDirectory()) {
				dbg_count_directories += 1;
				seperateFiles(file);
			} else {
				try {
					reader = new BufferedReader(new FileReader(file));
					String line = "not null";


					StateMachine state = StateMachine.stop;
					
					
					int file_index = 0;
					String DOCs_name = null;
					PrintWriter f0 = null;// = new PrintWriter(new FileWriter(null));
					
					//main loop
					while (true) {
						//read a line
						line = reader.readLine();
						if (line == null)
							break;
						if (state == StateMachine.work) {
							//get the DOCs name
							if (line.contains("<DOCNO>")) {
								DOCs_name = line.split("<DOCNO> ", 2)[1];
								DOCs_name = DOCs_name.split(" <")[0];
								f0 = new PrintWriter(new FileWriter(output_path + "\\" + DOCs_name + ".txt"));
								f0.println(line);
							}//end of DOCNO
							else if (line.equals("</DOC>")) {
								state = StateMachine.stop;
								file_index = file_index + 1;
								f0.close();
							}//end of DOC
							else
								f0.println(line);
						}
						if(state == StateMachine.stop) {
							//start of a new DOC
							if (line.equals("<DOC>")) {
								state = StateMachine.work;								
							}
						}
					}
					reader.close();
					f0.close();
					System.out.printf("ReadFile2 : Number of DOCs in directory = %d\n", file_index);
					dbg_count_files += file_index;
					}
				catch (IOException e) {
					e.printStackTrace();
				}
			}			
		}
		if(dbg_count_directories != 0)
			System.out.printf("ReadFile2 : Number of directories in corpus = %d\n", dbg_count_directories);
	}
}