import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;

public class Clean {
	 public static void main(String[] args) throws IOException {
	    	String line="";
	    	File map =new File ("C:/Users/sinne/Desktop/LATimesData/mapLATimesDataFile.csv");
	        BufferedReader br = new BufferedReader(new FileReader(map));
	        PrintWriter writer = new PrintWriter("CleanMAP.txt","UTF-8");
	        while ((line=br.readLine())!=null)
	        {
	        	String temp[]=line.split(",");
	        	if (temp.length==3){
	        		temp[1]+=","+temp[2];
	        	}
	        	writer.println(temp[0]+" "+temp[1]);
	        }
	        br.close();
	        writer.flush();
	        writer.close();
	 }
}
