package reference.apriori;

//Class to implement some basic file operations
import java.io.* ;
 
public class FileOpr
{
	FileInputStream fIn ;
	FileOutputStream fOut ;
	
	public FileOpr(String mode,String filename) throws FileNotFoundException
	{
		if (mode == "r"){ //read file
			fIn = new FileInputStream(filename) ;
		}
		if (mode == "w"){ //write file
			fOut = new FileOutputStream(filename) ;
		}
	}	
}