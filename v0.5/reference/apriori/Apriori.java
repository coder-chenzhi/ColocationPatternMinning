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
	double minSup ,minCon ; //最小支持度，最小置信度
	double deltaSup,deltaCon ;
	int minNum ,num; //最小计数
	
	FastVector fullSet ; //从GetSource class中，取得的全集(原始数据集)
	FastVector frequence; //最大的频繁集
	FastVector[] children ; //存放子集的FastVector数组，使用前需new
	FastVector ruleSrc,ruleDest ; //存放规则：源，目标；本身为big
	FastVector confidence,support ;//特殊的FastVector，存放的是和rule对应的confidence,support
	FastVector test ; //仅供测试
	
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
	public void WriteBig(FastVector big) //仅供测试
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
	
	public void Write(FastVector vector) //仅供测试
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
	
	public void Print(FastVector vector) //仅供测试
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
	
	public void PrintBig(FastVector vector) //仅供测试
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
		double num = fullSet.size(); //总itemsets数
		if(sup <= 1.0)
			minSup = sup ;
		minNum = (int)((num * sup)+1.0) ;//向上取整
	}
	
	public void SetCon(double con){
		if(con <= 1.0)
			minCon = con ;
	}
	
	public void Init(String str) //从文件 'str'中，初始化全集
	throws IOException,FileNotFoundException
	{ 		
		GetSource src = new GetSource(str);
		fullSet = src.GetAll(); //Get it!
		num = fullSet.size();

	}
	
	private boolean IsContain(FastVector itemset,Item item) //itemset是否包含item？
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
	
	private int Num(FastVector big,FastVector itemset) //itemset在big中出现的次数
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
	
	private boolean IsContain(FastVector itemset1,FastVector itemset2) //itemset1是否包含itemset2
	{
		for(int i=0 ; i<itemset2.size() ; i++)
		{
			if(!IsContain(itemset1,(Item)itemset2.elementAt(i)))
				return false ;
		}
		return true ;
	}//pass
	
	private boolean IsEqual(FastVector itemset1,FastVector itemset2) //itemset1是否包含itemset2
	{
		if(itemset1.size()!=itemset2.size()) return false;
		for(int i=0 ; i<itemset2.size() ; i++)
		{
			if(!IsContain(itemset1,(Item)itemset2.elementAt(i)))
				return false ;
		}
		return true ;
	}//pass
	
	private boolean IsContain2(FastVector big,FastVector itemset) //big是否包含itemset?
	{
		FastVector temp ;
		for(int i=0 ; i<big.size() ; i++)
		{
			temp = (FastVector)big.elementAt(i) ; //取得当前itemset
			if(IsEqual(temp,itemset)) //当前itemset包含了参数2
				return true ;
		}
		return false ;
	}//pass
	
	private FastVector BreakOne(FastVector big) //将big分割成item的集合
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
	
	private FastVector ToC1(FastVector vector) //将item的集合包装成一个big
	{/*输入的FastVector与输出的FastVector区别在于，输入的FastVector每个元素是Item，
	         输出的FastVector每个元素又是一个FastVector*/
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
	
	private FastVector SelfConnect(FastVector La)//自联结，由Li生成Ci+1,然后裁剪Ci+1，成为频繁集
	{ //参数为big，返回big
		int i,j;
		FastVector Cb = new FastVector();
		FastVector pieces = BreakOne(La); //所有item的集合
		FastVector temp ,swp,add ;		
		Item item ;

		//将每一个item与itemset相联结,并将合适的itemset加入Cb中
		for(i=0 ; i<La.size() ; i++)
		{
			for(j=0 ; j<pieces.size() ; j++) //取出每一个item
			{
				temp = (FastVector)La.elementAt(i) ; //取出每一个itemset
				item = (Item)pieces.elementAt(j);
				if( !IsContain(temp,item) ) //如果item不包含在itemset中
				 {
				 	add = new FastVector(); //new
					add.appendElements(temp);	 		 					
				 	add.addElement( item ) ; //则加入之					
					if( !IsContain2(Cb,add) ) //Cb中没有
						Cb.addElement( add ) ; //加入Cb中
				}
			}
		}//pass
		
		//去除Cb中不满足minNum的itemset
		for(i=Cb.size() - 1 ; i>=0 ; i--)
		{
			temp = (FastVector)Cb.elementAt(i) ;
			if( Num(fullSet,temp) < minNum ) //如果计数小于最小计数
				Cb.removeElementAt(i) ; //则删去该itemset
		}	
		
		return Cb;
	}//pass
	
	public void GenerateFrequence() //由最初的原始数据集，生成满足最小计数的最大频繁集
	{
		FastVector swp ;
		FastVector vector = BreakOne(fullSet); //get items
		
		vector = ToC1(vector); //get C1
		while(vector.size() > 0) //Ck to Ck+1
		{
			//频繁项没有要求项数最多，这里存在错误，每次自连接以后，之前的频繁项就被覆盖了，
			//应该把Ck+1中不包含的Ck项也保留下来
			frequence = SelfConnect(vector);
			swp = vector ;
			vector = frequence ;
			frequence = swp ;
		}
		
		children = new FastVector[frequence.size()]; //取得频繁集后，初始化子集的数组	
		for(int i=0 ; i<children.length ; i++) //申请空间 
			children[i] = new FastVector();	
	}//pass
	
	public void Recursion(FastVector vector,int pos) //pos指定Children数组的位置
	{
		int i ;
		FastVector temp ;
		if(vector.size() <= 2)//没有大于一个item的真子集了
			return ;
		for(i=0 ; i<vector.size() ; i++)
		{
			temp = new FastVector();
			temp.appendElements(vector);
			temp.removeElementAt(i); //每次去除一个i位置的元素
			if(!IsContain2(children[pos],temp)) //大头加入children数组的相应位置，然后对大头递归
				children[pos].addElement(temp);
			Recursion(temp,pos);
		}
	}//pass
	
	public void Children(FastVector mother,int pos)//生成一个itemset的所有真子集的big集合
	{
		int i ;
		FastVector temp ;
		if (mother.size() == 1) //没有意义
			return ;
			
		for(i=0 ; i<mother.size() ; i++) //将mother的每一个单个的item，当作子集加入children相应位置
		{
			temp = new FastVector();
			temp.addElement(mother.elementAt(i));
			children[pos].addElement(temp);
		}
		
		Recursion(mother,pos) ; //用递归来求的其余的真子集
	}//pass
	
	public void AllChildren(FastVector vector) //vector是求得的频繁集的集合
	{
		int i ;
		FastVector mother ;
		for(i=0 ; i<vector.size() ; i++) //取得特定频繁集
		{
			mother = (FastVector)vector.elementAt(i); //求得该频繁集的所有真子集
			Children(mother,i);			
		}
	}//pass
	
	public FastVector Odds(FastVector full,FastVector part) //求差集
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
	
	public double Support(FastVector mother,FastVector child)//求支持度,m和c都是itemset
	{	
		return (double)Num(fullSet,mother)/(double)Num(fullSet,child) ;
	} //pass
	
	public void GenerateRules()//由children[]，生成ruleSrc和ruleDest
	{
		int i,j,length = children.length ;
		FastVector odds,childset ;
		FastVector mother ,child ;
		double con ,sup ;
		
		for(i=0 ; i<length ; i++) //每一个最大频繁集
		{
			childset = children[i] ;
			mother = new FastVector();
			mother.appendElements( (FastVector)frequence.elementAt(i) );
			for(j=0 ; j<childset.size() ;j++ ) //对每一个子集
			{
				child = new FastVector() ;
				child.appendElements( (FastVector)childset.elementAt(j) );
				con = Support(mother,child) ;
				if( con > minCon ) //满足最小置信度，填写rule
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