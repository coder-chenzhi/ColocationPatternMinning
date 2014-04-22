package reference.apriori;

import java.io.*;

class Output
{
	public static void WriteRules(Apriori app) throws FileNotFoundException,IOException
	{
		int i,j,data;
		String str ;
		FileWriter out = new FileWriter("out.txt");
		FastVector vector ;
		
		for(i=0 ; i<app.ruleSrc.size() ; i++)
		{
			vector = (FastVector)app.ruleSrc.elementAt(i);
			for(j=0 ; j<vector.size() ; j++)
			{
				data = ((Item)vector.elementAt(j)).Key();
				out.write(data);
				out.write(',');
			}			
			out.write(' ');out.write('=');out.write('>');out.write(' ');
			
			vector = (FastVector)app.ruleDest.elementAt(i);
			for(j=0 ; j<vector.size() ; j++)
			{
				data = ((Item)vector.elementAt(j)).Key();
				out.write(data);
				out.write(',');
			}
			out.write(' ');out.write('C');out.write(':');out.write(' ');
			str = ((Double)app.confidence.elementAt(i)).toString();
			out.write(str);
			
			out.write("   ");out.write('S');out.write(':');out.write(' ');
			str = ((Double)app.support.elementAt(i)).toString();
			out.write(str);
			
			out.write(13);out.write(10);
		}
		out.close();
	}
	
	public static void main(String arg[])
	{
	}
}