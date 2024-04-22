package com.store.search;

import java.util.HashSet;
import java.util.stream.IntStream;

import org.springframework.util.StringUtils;

public class StringAlgos {
	
	public String reverse(String str) {
		if(str==null || str=="" || str.length()==1) return str;
	    char[] charArray=str.toCharArray();
	    char temp='a';
		for(int i=0,j=str.length()-1;i<j;i++,j--)
		{
			temp=charArray[i];
			charArray[i]=charArray[j];
			charArray[j]=temp;
		}
		return new String(charArray);
	}
	/**
	 * Using recursion
	 * @param str
	 * @param l
	 * @param r
	 * @return
	 */
	public String reverse(String str,int l,int r) {
		if(l>r) {
			return str;
		}else {
			str=swap(str, l, r);
			return reverse(str,l+1,r-1);
		}
	}
	
	private String swap(String str,int l,int r) {
		 if(l>-1 && l<str.length() &&r>-1 && r<str.length()) {
			  char[] charArray=str.toCharArray();
			  char temp=charArray[l];
			  charArray[l]=charArray[r];
			   charArray[r]=temp;
			 return new String(charArray);
		 }
		 return str;
	}
	
	/*
	public void permutations(String str, int l, int r,List<String> collector){
		    if(l==r) {
		    	collector.add(str);
		    }else {
		    	for(int i=l;i<r;i++) {
		    		str=SwapUtil.swap(str,i,l);
		    		permutations(str, l+1, r,collector);
			        str=SwapUtil.swap(str, l, i);
		    	}
		    }
	}
	
	
	public void reverseWords(String input) {
		if(StringUtils.isNotBlank(input)) {
			System.out.println(input);
			String[] words =input.split("\\.");
			for(int i=words.length-1;i>=0; i--) {
				System.out.print(words[i]+".");
			}
			System.out.println();
		}
	}
	
	public String longestCommonPrefix(String[] words,int option) {
		 StringBuffer buffer=new StringBuffer(); //O(power(n,2))
		switch (option) {
		case 0: // Brute Force approach
			 int minLen=Stream.of(words).mapToInt(w->w.length()).min().getAsInt();
			 for(int j=0;j<minLen-1;j++) {
				 boolean stop=false;
				 char c=words[j].charAt(j);
				 for(int k=0;k<words.length;k++) {
					 if(!(c==words[k].charAt(j))) {
						 stop=true;
						 break;
					 }
				 }
				 if(stop==true) {
					 break;
				 }
				 buffer.append(c);
			   }
			
			break;
		case 1:  // Sorting and comparing only first and last one.
			Arrays.sort(words);
			Stream.of(words).forEach(System.out::println);
			 int shortest=Stream.of(words).mapToInt(w->w.length()).min().getAsInt();
			  int idx=0;
			 while(idx<shortest) {
				 
				 idx++;
			 }
			
		default:
			break;
		}
		 return buffer.toString();
	}

	/**
	 * Input: strs = ["flower","flow","flight"]
	Output: "fl"
	Example 2: s 

	 BruteForce : Iterating all the strings see matching.
	 
	 Recursion?
	 Data structure?  Trie 
	 Hashing?
	 
	 * @author venugopal
	 *
	 */
	public void commonPrefix(String[] words) {
		// TODO Auto-generated method stub
		 int smallestLength=Integer.MAX_VALUE;
		 String smalleststring=null;
		 for(int index=0;index<words.length;index++) {
			
			 if(words[index].length()<smallestLength) {
				 smallestLength=words[index].length();
				 smalleststring=words[index];
			 }	 
		 }
			 for(int i=0;i<smallestLength;i++) {
				 char charat=smalleststring.charAt(i); //
				 boolean breakOuter=false;
				 for(int j=0;j<words.length;j++) {
					  String temp=words[j];
					  if(charat !=temp.charAt(i)) {
						  breakOuter=true;
						  break;
					  }
				 }
				 if(breakOuter==true) {
					 break;
				 }else {
					 System.out.print(charat);
				 }
			
				 
			 }
	}
	
	
	
	public void decimalTint(int decimal) {
		
	}

	public boolean hasUniqueChars(String input, int option) {
		switch (option) {
		case 0: // Method-1 
			 long count = IntStream.range(0, input.length()).distinct().count();
			    if(count==input.length()) {
			    	return true;
			    }else {
			    	return false;
			    }
		case 1 :{
			HashSet<Character> uniqueSet=new HashSet<>();
			for(char c:input.toCharArray()) {
			    if(!uniqueSet.add(c)) {
			    	return false;
			    }
			}
			return true;
		}
		case 2: {
			boolean[] asciArray=new boolean[128];
			for(char c:input.toCharArray()) {
				int number=Character.getNumericValue(c);
			    if(asciArray[number]==true) {
			    	return false;
			    }
			    asciArray[number]=true;
			}
			return true;
		}
		default:
			return false;
		}
		
	}

	public boolean isPalidrome(String string,int l,int r) {
		if(l>=r) {
			return true;
		 }else if(string.charAt(l)!=string.charAt(r)) {
		    return false;
		}else {
			return isPalidrome(string,l+1,r-1);
		}
	}

	@SuppressWarnings("deprecation")
	public int strToNum(String string) {
		if(StringUtils.isEmpty(string)) {
			throw new IllegalArgumentException("Not a valid numeric string :"+ string);
		}
		int result=0;
		/* //Apraoch-1
		
		for(int i=string.length()-1,j=0;i>=0;i--,j++) {
			if(string.charAt(i)=='-') break;
			int numericValue = Character.getNumericValue(string.charAt(i));
			result=result+numericValue * ((int)Math.pow(10, j));
		}
		//Apraoch-1
		*/
		int index=0;
		result=0;
		if(string.charAt(index)=='-') {
			index++;
		}
		while (index<string.length()) {
			result=result*10+Character.getNumericValue(string.charAt(index));
			index++;
		}
		return string.charAt(0)=='-'?-result:result;
	}
	
	public String numToStr(int number) {
		boolean isNegative=false;
		if(number<0) {
			number=Math.abs(number);
			isNegative=true;
		}
		String result="";
		while(number>9) {
			int remainder=number%10;
			result=remainder+result;
			number=number/10;
		}
		result=number+result;
		return isNegative?"-"+result:result;
		
	}
	
	public int romanToDecimal(String romanNumb) {
		int integer=0;
		Roman PRE=Roman.I;		
		for(int i=romanNumb.length()-1;i>=0;i--) {
		 Roman symbol=Roman.valueOf(String.valueOf(romanNumb.charAt(i)));
		 if(symbol==Roman.I && (PRE==Roman.V || PRE==Roman.X)) {
			 integer=integer-symbol.getInt();
		 }else if(symbol==Roman.X && (PRE==Roman.L || PRE==Roman.C)) {
			 integer=integer-symbol.getInt();
		 }
		 else if(symbol==Roman.C && (PRE==Roman.D || PRE==Roman.M)) {
			 integer=integer-symbol.getInt();
		 }
		 else {
			 integer=integer+symbol.getInt();
		 }
		 PRE=symbol;
		}
		return integer;
	}
	
	public boolean validIp(String ip) {
		 if(ip==null || ip=="") return false;
		 String[] ipparts=ip.split("\\.");
		 if (ipparts.length!=4) return false;
		 for(String part:ipparts) {
			 int parseInt = Integer.parseInt(part);
			 if(parseInt<0 || parseInt>255) {
				 return false;
			 }
		 }
		return true;
	}
	public String makeIP(String str) {
		 if(str==null || str=="" || str.length()<=3 || str.length()>=13) {
			 throw new IllegalArgumentException("IP can't be make with : "+str);
		 }
		 int n=str.length(); // n=4*a+r;
		 int a=n/4;
		 int re=n%1;
		 int l=0,r=0;
		 int[][] lr=new int[4][2];
		 
		
		return "";
	}
	
}

enum Roman {
	I(1),V(5),X(10),L(50),C(100),D(500),M(1000);
	Roman(int num){
		dec=num;
	}
	int dec;
	public int getInt() {
		return dec;
	}
}

