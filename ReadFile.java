//package doc_parser;
//
//import java.io.BufferedReader;
//import java.io.File;
//import java.io.FileReader;
//import java.io.FileWriter;
//import java.io.PrintWriter;
//import java.io.IOException;
//
//enum StateMachine{
//	work,
//	stop
//}
//
//public class ReadFile {
//
//	String path;
//	
//	ReadFile(String p)
//	{
//		path=p;
//	}
//	public static void main(String[] args) {
//		//get the dir we should run on
//		File currentDir = new File("/home/ise/Downloads/corpus/");
//		// run that function that runs on all files and seperate them to documents.
//		seperateFiles(currentDir);
//		
//		/*
//		public class RecursiveFileDisplay {
//
//			public static void main(String[] args) {
//				File currentDir = new File("."); // current directory
//				displayDirectoryContents(currentDir);
//			}
//
//			public static void displayDirectoryContents(File dir) {
//				try {
//					File[] files = dir.listFiles();
//					for (File file : files) {
//						if (file.isDirectory()) {
//							System.out.println("directory:" + file.getCanonicalPath());
//							displayDirectoryContents(file);
//						} else {
//							System.out.println("     file:" + file.getCanonicalPath());
//						}
//					}
//				} catch (IOException e) {
//					e.printStackTrace();
//				}
//			}
//
//		}
//		*/
//		
//
//	}
//	public static void seperateFiles(File dir){
//			//create a reader object
//			BufferedReader reader;	
//			//create a new object that receives a path
//			File[] files = dir.listFiles();
//			for (File file : files) {
//				if (file.isDirectory()) {
//					seperateFiles(file);
//				} else {
//					try {
//						//"/home/ise/Downloads/corpus/FB396001/FB396001"
//						reader = new BufferedReader(new FileReader(file));
//						String line = "not null";
//								
//						
//						StateMachine state = StateMachine.stop;
//						
//						
//						int file_index = 0;
//						PrintWriter f0 = new PrintWriter(new FileWriter("output" + file_index + ".txt"));					
//						
//						//main loop						
//						while (true) {	
//							//read a line
//							line = reader.readLine();
//							if (line == null)
//								break;
//							if (state == StateMachine.work) {
//								if (line.equals("</DOC>")) {
//									state = StateMachine.stop;
//									file_index = file_index + 1;
//									f0.close();						
//								}
//								else
//									f0.println(line);
//							}
//							if(state == StateMachine.stop) {
//								if (line.equals("<DOC>")) {
//									state = StateMachine.work;
//									f0 = new PrintWriter(new FileWriter("output" + file_index + ".txt"));
//								}
//							}
//						}
//						reader.close();
//						f0.close();
//						System.out.println("Done");
//					} catch (IOException e) {
//						e.printStackTrace();
//					}
//				}
//			
//			}
//			
//		}
//}*/