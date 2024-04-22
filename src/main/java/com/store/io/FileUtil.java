package com.store.io;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class FileUtil {


    
    public static void getMemorInfo() {
    	ProcessBuilder builder=new ProcessBuilder("/sbin/fdisk");
    	builder.redirectErrorStream(true);
    	try {
		   Process process=builder.start();
		   BufferedReader r = new BufferedReader(new InputStreamReader(process.getInputStream()));
	        String line;
	        while (true) {
	            line = r.readLine();
	            if (line == null) { break; }
	            System.out.println(line);
	        }
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
    
}
