package reference.apriori;

//File Interface
//Get data source from a file 
import java.io.*;

public class GetSource{
	
	//Data
	static byte[] t = {'t','r','u','e'};
	static byte[] f = {'f','a','l','s','e'};
	FileInputStream file ; //source file
	FastVector vector ; //goal container
	String fName ; //store source file name
	Item item ;
	
	//Methods
	public GetSource(String filename) throws FileNotFoundException{
		file  = new FileInputStream(filename) ;
		fName = filename ;
	}
	
	public void Reset() throws IOException,FileNotFoundException{ //reload source file
		file.close();
		file = new FileInputStream(fName) ;
	}
	
	public int NumOfSets() throws IOException,FileNotFoundException{ //return the number of sets
		int num = 0 ; int data = 0;
		Reset();
		
		while(data != '%'){
			data = file.read();
			if(data == 13)
				num ++;
		}
		Reset();		
		return num ;
	}
	
	public FastVector ReadSet() throws IOException,FileNotFoundException{ //Get and return the current set
		int data = -2; //nonsence
		FastVector vector = new FastVector();
		byte[] b = new byte[30] ; int pos = 0 ; int item = 0 ;
		
		while(data != 13){
			if(data == ',')
			{
				item ++ ;
				data = file.read();
				if(data == 't')
				{					vector.addElement(new Item(item + 'A'));
				}
				if(data == 'f')
				{
		 			vector.addElement(new Item(item + 'a'));
				}
			}
			data = file.read();
			
		}
		return vector ;
	}
	
	public FastVector GetSet(int k) throws IOException{ //get the kth set,return it as a vector
		int n=0;                                         //begin from 0
		int data=0; 
		FastVector vector = new FastVector();
		Reset();
		while(!(n == k)){ //locate the kth set
			data = file.read();
			if(data == 13) //end of last set
				n++ ;			
		}
		/*while(data != 10){ //read until '@'
			data = file.read();						
		}
		*/
		//then the asked set started	
		vector = ReadSet(); 	
		return vector ;	
	}
	
	public FastVector GetAll() throws IOException{ //return a vector including all sets
		int n = NumOfSets();
		int i = 0;
		FastVector vector = new FastVector();
		for(i=0;i<n;i++){
			vector.addElement(GetSet(i));
		}
		return vector ;
	}
	
	public static void main(String arg[])throws IOException,FileNotFoundException{
		int num;
		FastVector vector = new FastVector();
		GetSource src = new GetSource("zoo.arff"); 
		num = src.NumOfSets();
		System.out.println("GetSource num"+num);
		
		vector = src.GetAll();
	}	
}