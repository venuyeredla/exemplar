package com.store.search;

public class PatternMatching {
	
	public int searchNaive(String text, String pattern) {
		  int index=-1;
		  boolean matched=true;
        for(int tIdx=0;tIdx<=text.length()-pattern.length();tIdx++) {
      	  index=tIdx;
      	  matched=true;
      	  for(int pIdx=0;pIdx<pattern.length();pIdx++) {
               if(pattern.charAt(pIdx)!=text.charAt(pIdx+tIdx)) {
              	 matched=false;
              	 index=-1;
              	 break;
               }
      	  }
      	  if(matched==true) {
      		  break;
      	  }
        }
		return index;
	}
	
	
	public int searchKMP(String txt, String pat) {
		//int[] buildLps = this.buildLps(pat);
		for(int i=0;i<txt.length();i++) {
			
		}
		
	  return 0;
	}
	
	public int[] buildLps(String pat) {
		  int[] lps=new int[pat.length()];
		  int i=0,j=1;
		  lps[i]=0;
		  while(j<pat.length()) {
			  if(pat.charAt(i)==pat.charAt(j)) {
				  lps[j]=i+1;
				  i++; j++;
			  }else {
				  if(i!=0) {
					  i=lps[i-1];
				  }else {
					  lps[j]=i;
					  j++;
				  }
			  }
		  }
		return lps;
	}

	

}
