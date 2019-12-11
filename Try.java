package doc_parser;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class Try {
	public static void main(String[] args) {	
	Stemmer stem_obj = new Stemmer();
	
	String word1 = "devestation";
	String word2 = "some";
	String word3 = "shitted";
	
	stem_obj.add(word1.toCharArray(), word1.length());
	stem_obj.stem();
	
	char[] output = stem_obj.getResultBuffer();
	int a;
	String output_s = stem_obj.toString();
	}
	
//public static void main(String[] args) {
//   String s="a a a A A";
//   String[] splitedString=s.split(" ");
//   Map m=new HashMap();
//   int count=1;
//   for(String s1 :splitedString){
//        count=m.containsKey(s1)?count+1:1;
//         m.put(s1, count);
//       }
//   Iterator<Try> itr=m.entrySet().iterator();
//   while(itr.hasNext()){
//       System.out.println(itr.next());         
//   }
//   }

}