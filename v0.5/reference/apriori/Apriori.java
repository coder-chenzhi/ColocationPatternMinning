/*
 * Classname
 * 
 * Version info
 * 
 * Copyright notice
 */
package reference.apriori;

//Implement Apriori Algrithm
//Public Interface:
//Apriori(); Init();GenerateFrequence();

import java.io.* ;

class Apriori
{
	//members
	double minSup ,minCon ; //��С֧�ֶȣ���С���Ŷ�
	double deltaSup,deltaCon ;
	int minNum ,num; //��С����
	
	FastVector fullSet ; //��GetSource class�У�ȡ�õ�ȫ��(ԭʼ���ݼ�)
	FastVector frequence; //����Ƶ����
	FastVector[] children ; //����Ӽ���FastVector���飬ʹ��ǰ��new
	FastVector ruleSrc,ruleDest ; //��Ź���Դ��Ŀ�ꣻ����Ϊbig
	FastVector confidence,support ;//�����FastVector����ŵ��Ǻ�rule��Ӧ��confidence,support
	FastVector test ; //��������
	
	//Constuctor
	public Apriori()
	{
		minSup = 0.2 ;
		minCon = 0.4 ;
		minNum = 2 ;
		ruleSrc = new FastVector();
		ruleDest = new FastVector() ;
		confidence = new FastVector() ; //remember:special
		support = new FastVector() ; //remember:special

	}
	
	//methods
	public void WriteBig(FastVector big) //��������
	throws FileNotFoundException,IOException 	{ //big
		FileOutputStream fOut = new FileOutputStream("vector.txt");
		int size1 = big.size() , size2;
		FastVector temp ;
		Item item ;
		int data ;
		
		for(int i=0 ; i<size1 ; i++)
		{
			size2 = ((FastVector)(big.elementAt(i))).size();
			for(int j=0 ; j<size2 ; j++)
			{
				temp = (FastVector)big.elementAt(i);
				item = (Item)temp.elementAt(j);
				fOut.write(item.Key());
				fOut.write(',');
			}
			fOut.write(';');
			
		}
		fOut.close();
	}
	
	public void Write(FastVector vector) //��������
	throws FileNotFoundException,IOException 	{ //itemset
		FileOutputStream fOut = new FileOutputStream("vector.txt");
		int size1 = vector.size();
		Item item ;
		
		for(int i=0 ; i<size1 ; i++)
		{
			item = (Item)vector.elementAt(i);
			fOut.write(item.Key());
			fOut.write(' ');
		}
		fOut.write(';');
		fOut.close();
	}	
	
	public void Print(FastVector vector) //��������
	{ //itemset
		int size1 = vector.size();
		Item item ;

		for(int i=0 ; i<size1 ; i++)
		{
			item = (Item)vector.elementAt(i);
			System.out.print(item.Key()-'0');
			System.out.print(' ');
		}
		System.out.println(' ');
	}	
	
	public void PrintBig(FastVector vector) //��������
	{ //big
		for(int i=0 ; i<vector.size() ; i++)
		{
			Print((FastVector)vector.elementAt(i));
			System.out.println(" ");
		}
	}	
	
	public void PrintChildren() //for test only
	{
		for(int i=0 ; i<children.length ; i++){
			PrintBig(children[i]);
			System.out.println("--------------------");
		}
	}
	
	public void PrintRules() //for test only
	{
		for(int i=0 ; i<ruleSrc.size() ; i++){
			System.out.println("--------- "+i+" -----------");
			Print((FastVector)ruleSrc.elementAt(i));
			System.out.println(" "+"=>"+"    confidence is:"+
				((Double)confidence.elementAt(i)).toString() );
			Print((FastVector)ruleDest.elementAt(i));
			
		}
	}
	
	public void SetSup(double sup){
		double num = fullSet.size(); //��itemsets��
		if(sup <= 1.0)
			minSup = sup ;
		minNum = (int)((num * sup)+1.0) ;//����ȡ��
	}
	
	public void SetCon(double con){
		if(con <= 1.0)
			minCon = con ;
	}
	
	public void Init(String str) //���ļ� 'str'�У���ʼ��ȫ��
	throws IOException,FileNotFoundException
	{ 		
		GetSource src = new GetSource(str);
		fullSet = src.GetAll(); //Get it!
		num = fullSet.size();

	}
	
	private boolean IsContain(FastVector itemset,Item item) //itemset�Ƿ����item��
	{
		Item item0 ;
		for(int i=0;i<itemset.size();i++)
		{
			item0 = (Item)itemset.elementAt(i);
			if (item0.Key() == item.Key()) //found
				return true ;
		}
		return false ;
	}//pass
	
	private int Num(FastVector big,FastVector itemset) //itemset��big�г��ֵĴ���
	{
		int num = 0;
		FastVector temp ;
		for(int i=0 ; i<big.size() ; i++)
		{
			temp = (FastVector)big.elementAt(i) ;
			if( IsContain(temp,itemset) )
				num ++ ;
		}
		return num ;
	}//pass
	
	private boolean IsContain(FastVector itemset1,FastVector itemset2) //itemset1�Ƿ����itemset2
	{
		for(int i=0 ; i<itemset2.size() ; i++)
		{
			if(!IsContain(itemset1,(Item)itemset2.elementAt(i)))
				return false ;
		}
		return true ;
	}//pass
	
	private boolean IsEqual(FastVector itemset1,FastVector itemset2) //itemset1�Ƿ����itemset2
	{
		if(itemset1.size()!=itemset2.size()) return false;
		for(int i=0 ; i<itemset2.size() ; i++)
		{
			if(!IsContain(itemset1,(Item)itemset2.elementAt(i)))
				return false ;
		}
		return true ;
	}//pass
	
	private boolean IsContain2(FastVector big,FastVector itemset) //big�Ƿ����itemset?
	{
		FastVector temp ;
		for(int i=0 ; i<big.size() ; i++)
		{
			temp = (FastVector)big.elementAt(i) ; //ȡ�õ�ǰitemset
			if(IsEqual(temp,itemset)) //��ǰitemset�����˲���2
				return true ;
		}
		return false ;
	}//pass
	
	private FastVector BreakOne(FastVector big) //��big�ָ��item�ļ���
	{
		int size1 = big.size() , size2;
		FastVector pieces = new FastVector();
		FastVector temp ;
		
		for(int i=0 ; i<size1 ; i++)
		{
			size2 = ((FastVector)(big.elementAt(i))).size();
			for(int j=0 ; j<size2 ; j++)
			{
				temp = (FastVector)big.elementAt(i);
				if(!IsContain( pieces,(Item)temp.elementAt(j) ))
					pieces.addElement((Item)temp.elementAt(j));
			}
		}
		return pieces ;
	}//pass
	
	private FastVector ToC1(FastVector vector) //��item�ļ��ϰ�װ��һ��big
	{/*�����FastVector�������FastVector�������ڣ������FastVectorÿ��Ԫ����Item��
	         �����FastVectorÿ��Ԫ������һ��FastVector*/
		FastVector big = new FastVector() ,
					  itemset ;
		Item item ;
		int size = vector.size();
		for(int i=0 ; i<size ; i++)
		{
			itemset = new FastVector();
			item = (Item)vector.elementAt(i);
			itemset.addElement(item);
			big.addElement(itemset);
		}
		return big ;		
	}//pass
	
	private FastVector SelfConnect(FastVector La)//�����ᣬ��Li����Ci+1,Ȼ��ü�Ci+1����ΪƵ����
	{ //����Ϊbig������big
		int i,j;
		FastVector Cb = new FastVector();
		FastVector pieces = BreakOne(La); //����item�ļ���
		FastVector temp ,swp,add ;		
		Item item ;

		//��ÿһ��item��itemset������,�������ʵ�itemset����Cb��
		for(i=0 ; i<La.size() ; i++)
		{
			for(j=0 ; j<pieces.size() ; j++) //ȡ��ÿһ��item
			{
				temp = (FastVector)La.elementAt(i) ; //ȡ��ÿһ��itemset
				item = (Item)pieces.elementAt(j);
				if( !IsContain(temp,item) ) //���item��������itemset��
				 {
				 	add = new FastVector(); //new
					add.appendElements(temp);	 		 					
				 	add.addElement( item ) ; //�����֮					
					if( !IsContain2(Cb,add) ) //Cb��û��
						Cb.addElement( add ) ; //����Cb��
				}
			}
		}//pass
		
		//ȥ��Cb�в�����minNum��itemset
		for(i=Cb.size() - 1 ; i>=0 ; i--)
		{
			temp = (FastVector)Cb.elementAt(i) ;
			if( Num(fullSet,temp) < minNum ) //�������С����С����
				Cb.removeElementAt(i) ; //��ɾȥ��itemset
		}	
		
		return Cb;
	}//pass
	
	public void GenerateFrequence() //�������ԭʼ���ݼ�������������С���������Ƶ����
	{
		FastVector swp ;
		FastVector vector = BreakOne(fullSet); //get items
		
		vector = ToC1(vector); //get C1
		while(vector.size() > 0) //Ck to Ck+1
		{
			//Ƶ����û��Ҫ��������࣬������ڴ���ÿ���������Ժ�֮ǰ��Ƶ����ͱ������ˣ�
			//Ӧ�ð�Ck+1�в�������Ck��Ҳ��������
			frequence = SelfConnect(vector);
			swp = vector ;
			vector = frequence ;
			frequence = swp ;
		}
		
		children = new FastVector[frequence.size()]; //ȡ��Ƶ�����󣬳�ʼ���Ӽ�������	
		for(int i=0 ; i<children.length ; i++) //����ռ� 
			children[i] = new FastVector();	
	}//pass
	
	public void Recursion(FastVector vector,int pos) //posָ��Children�����λ��
	{
		int i ;
		FastVector temp ;
		if(vector.size() <= 2)//û�д���һ��item�����Ӽ���
			return ;
		for(i=0 ; i<vector.size() ; i++)
		{
			temp = new FastVector();
			temp.appendElements(vector);
			temp.removeElementAt(i); //ÿ��ȥ��һ��iλ�õ�Ԫ��
			if(!IsContain2(children[pos],temp)) //��ͷ����children�������Ӧλ�ã�Ȼ��Դ�ͷ�ݹ�
				children[pos].addElement(temp);
			Recursion(temp,pos);
		}
	}//pass
	
	public void Children(FastVector mother,int pos)//����һ��itemset���������Ӽ���big����
	{
		int i ;
		FastVector temp ;
		if (mother.size() == 1) //û������
			return ;
			
		for(i=0 ; i<mother.size() ; i++) //��mother��ÿһ��������item�������Ӽ�����children��Ӧλ��
		{
			temp = new FastVector();
			temp.addElement(mother.elementAt(i));
			children[pos].addElement(temp);
		}
		
		Recursion(mother,pos) ; //�õݹ��������������Ӽ�
	}//pass
	
	public void AllChildren(FastVector vector) //vector����õ�Ƶ�����ļ���
	{
		int i ;
		FastVector mother ;
		for(i=0 ; i<vector.size() ; i++) //ȡ���ض�Ƶ����
		{
			mother = (FastVector)vector.elementAt(i); //��ø�Ƶ�������������Ӽ�
			Children(mother,i);			
		}
	}//pass
	
	public FastVector Odds(FastVector full,FastVector part) //��
	{
		int i ;
		FastVector odds = new FastVector();
		Item item ;		
		for(i=0 ; i<full.size() ; i++)
		{
			item = (Item)full.elementAt(i);
			if( !IsContain(part,item) )
				odds.addElement(item);
		}
		return odds ;
	} //
	
	public double Support(FastVector mother,FastVector child)//��֧�ֶ�,m��c����itemset
	{	
		return (double)Num(fullSet,mother)/(double)Num(fullSet,child) ;
	} //pass
	
	public void GenerateRules()//��children[]������ruleSrc��ruleDest
	{
		int i,j,length = children.length ;
		FastVector odds,childset ;
		FastVector mother ,child ;
		double con ,sup ;
		
		for(i=0 ; i<length ; i++) //ÿһ�����Ƶ����
		{
			childset = children[i] ;
			mother = new FastVector();
			mother.appendElements( (FastVector)frequence.elementAt(i) );
			for(j=0 ; j<childset.size() ;j++ ) //��ÿһ���Ӽ�
			{
				child = new FastVector() ;
				child.appendElements( (FastVector)childset.elementAt(j) );
				con = Support(mother,child) ;
				if( con > minCon ) //������С���Ŷȣ���дrule
				{
					sup = (double)Num(fullSet,child) / num ;
					odds = Odds(mother,child);
					
					ruleSrc.addElement(child);
					ruleDest.addElement(odds);	
					confidence.addElement(new Double(con));	
					support.addElement(new Double(sup))	;
				}				
			}
		}
	}
		
	public static void main(String arg[])
	throws FileNotFoundException,IOException 
	{
		FastVector temp,big,itemset;
		Item item;
		
		Apriori app = new Apriori();
		
		app.Init("zoo.arff");             // get the full set
		app.SetSup(0.6);
		app.SetCon(0.8);
		//app.PrintBig(app.fullSet);
   
      //Going Apriori	
		app.GenerateFrequence();
		app.PrintBig(app.frequence);
		app.WriteBig(app.frequence);
		app.AllChildren(app.frequence);
		app.PrintChildren();
		app.GenerateRules();
//		app.PrintRules();			
		
		Output.WriteRules(app);
	}	
}