/**** @author RAJESH Kharche */
//open Netbeans
//Choose Java->project
//name it GoogleSearchAPP

package googlesearchapp;

import java.io.*;
import java.net.*;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.io.FileOutputStream;

public class GoogleSearchAPP {
    public static void main(String[] args) throws InterruptedException {

		FileReader fr = null;
		FileWriter fw = null;
		
		BufferedReader br = null;
		BufferedWriter bw = null;
    	
    	
    	try {
            // TODO code application logic here

            final int Result;

            //Scanner s1=new Scanner(System.in);
            //System.out.println("Enter Query to search: ");//get the query to search
            //Str=s1.next();

            //Variable Setting
            String FResult = "";
            Date d = null;
            

            // File Read
            br = new BufferedReader(new FileReader("c:/Temp/GoogleSearch/search15.txt"));
            // File output
            fw = new FileWriter("c:/Temp/GoogleSearch/result15.txt");
            bw = new BufferedWriter(fw);
            
            d = new Date();
            String covd = "";
            SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd");
            covd = format.format(d);
            
            while(true) {
                String line = br.readLine();
                if (line==null) break;
                
                //Search Result

              
                
//                String SearchGood = getResultsCount(line + " good");
                
//                if(getResultsCount(line + " good") = "") 
//                	{ SearchGood = 0;} else {int SearchGood =  getResultsCount(line + " good");};
                
                int SearchGood =  getResultsCount(line + " good");
                TimeUnit.SECONDS.sleep(20);
                
                int SearchBad =  getResultsCount(line + " bad");
                TimeUnit.SECONDS.sleep(20);
                
                
                
                System.out.println(line + " good");
                System.out.println(SearchGood);
                System.out.println(line + " bad");
                System.out.println(SearchBad);
                
                FResult = covd + ",\"" + line + "\"" + "," + Integer.toString(SearchGood)+"," + Integer.toString(SearchBad);
                System.out.println(FResult);
                
                bw.write(FResult);
                bw.newLine();
            }
            
        } catch (IOException ex) {
            Logger.getLogger(GoogleSearchAPP.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
			// BufferedReader FileReader를 닫아준다.
			if(br != null) try{br.close();}catch(IOException e){}
			if(fr != null) try{fr.close();}catch(IOException e){}
			
			// BufferedWriter FileWriter를 닫아준다.
			if(bw != null) try{bw.close();}catch(IOException e){}
			if(fw != null) try{fw.close();}catch(IOException e){}
        }
    }

    private static int getResultsCount(final String query) throws IOException {
        final URL url;
        url = new URL("https://www.google.com/search?q=" + URLEncoder.encode(query, "UTF-8"));
        final URLConnection connection = url.openConnection();

        connection.setConnectTimeout(60000);
        connection.setReadTimeout(60000);
        connection.addRequestProperty("User-Agent", "Google Chrome/36");//put the browser name/version
        
        final Scanner reader = new Scanner(connection.getInputStream(), "UTF-8");  //scanning a buffer from object returned by http request

        while(reader.hasNextLine()){   //for each line in buffer
            final String line = reader.nextLine();

            if(!line.contains("\"resultStats\">"))//line by line scanning for "resultstats" field because we want to extract number after it
                continue;

            try{        
                return Integer.parseInt(line.split("\"resultStats\">")[1].split("<")[0].replaceAll("[^\\d]", ""));//finally extract the number convert from string to integer
            }finally{
                reader.close();
            }
        }
        reader.close();
        return 0;
    }
}