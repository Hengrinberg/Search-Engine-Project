import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;
import java.util.Vector;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.io.IOException;



public class Parser {
	String path;
	Parser(String p){
		path=p;
	}
	public static void numbersFunc(int i, List<String> l){
		// this function will get the index of the number a
		//will check the previous/next value to check which function should run
		if (l.get(i).contains("$") || l.get(i+1).contains("dollar")|| l.get(i+2).contains("dollar")){
			prices(i, l);
		}
		else if (l.get(i).contains("%") || l.get(i+1).contains("percent")|| l.get(i+2).contains("percent"))
		{
			percent(i, l);
		}
		
	}
	public void percent(int i, List<String> l){
		// this function will change the values for percent numbers and will change the vector
	}
	public void prices(int i, List<String> l){
		// this function will change the values for percent numbers and will change the vector
	}

	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub
		String path="/home/ise/workspace/IR System/";
		Parser pars = new Parser(path);
		System.out.println(pars.path);
		File currentDir = new File(pars.path);
		File[] files = currentDir.listFiles();
		//BufferedReader reader;
		Scanner s ;
		for (File file : files) {
			if (!file.isDirectory())
			{
				try{
					s = new Scanner(file);
					//reader=new BufferedReader(new FileReader(file));
				    List<String> words=new ArrayList<String>();
				    while(s.hasNext()){
				    	words.add(s.next());
				    }
				    for (int i=0; i<words.size();i++){
				    	if (words.get(i).matches("((.*)-?\\d+(\\.\\d+)?(.*))"))
				    	{
				    		System.out.println(words.get(i));
				    		numbersFunc(i, words);
				    	}
				    	
				    }	        
				}
				catch (IOException e) {
					e.printStackTrace();
				}
			}
		
	    }
	  
	}
}
