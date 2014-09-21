import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Vector;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;


public class FeatureSelection {
	//特征选择，筛选部分重要的词
	
	public static int CountWordsNum(String dirname ) throws Exception{
		HashMap<String,Integer> words=new HashMap<String,Integer>();
		Vector WordList=new Vector();
		File dir = new File(dirname);
        File file[] = dir.listFiles();
        int count=0;
        for (int i = 0; i < file.length; i++) {
        	if(i%100==0)
        		System.out.println(i);
        	BufferedReader br=new BufferedReader(new InputStreamReader(new FileInputStream(file[i].toString()),"utf-8"));
			String temp=br.readLine();
			while(temp!=null)
			{
				String[] subs=temp.split(" +");
				for(int j=0;j<subs.length;j++)
					if(words.get(subs[j])==null)
					{
						words.put(subs[j], count);
						WordList.add(subs[j]);
						count++;
					}
				temp=br.readLine();	
			}
			br.close();
        }
        System.out.println(count);
        return count;
	}
	public static void ComputeTFIDF(String dirname,int wordNum,String feafilename ) throws Exception{
		HashMap<String,Integer> words=new HashMap<String,Integer>();
		Vector WordList=new Vector();
		int[] TF=new int[wordNum];
		int[] DF=new int[wordNum];
		int[] mark=new int[wordNum];
		File dir = new File(dirname);
        File file[] = dir.listFiles();
        for (int i = 0; i < file.length; i++) {
        	if(i%100==0)
        	System.out.println(i);
        	for(int j=0;j<wordNum;j++)
        		mark[j]=0;
			BufferedReader br=new BufferedReader(new InputStreamReader(new FileInputStream(file[i].toString()),"UTF-8"));
			String temp=br.readLine();
			while(temp!=null)
			{
				String[] subs=temp.split(" +");
				for (int k = 0; k < subs.length; k++)
				{
					String s=subs[k];
					if(words.get(s)==null)
						{
						WordList.add(s);
						int index=WordList.size()-1;
						words.put(s, index);
						TF[index]=1;
						DF[index]=1;
						mark[index]=1;
						}
					else{
						int index=words.get(s);
						TF[index]++;
						if(mark[index]==0)
						{
							DF[index]++;
							mark[index]=1;
						}
					}
				}
				temp=br.readLine();
				
			}
			br.close();
			
        }
        
        int totalcatnum=file.length;
        double[] TfIdf=new double[wordNum];
        for(int i=0;i<WordList.size();i++)
        	TfIdf[i]=(double)TF[i]/wordNum*Math.log((double)totalcatnum/DF[i]);
        PrintWriter pw = new PrintWriter(new OutputStreamWriter(new FileOutputStream(feafilename),"UTF-8"),true);
        for(int i=0;i<WordList.size();i++)
        {
        	pw.println(WordList.elementAt(i).toString()+" "+TfIdf[i]);
        }
        pw.close();
	}
	
	public static void FeatureSelection(String stopwordsfile,String initfeaturesfile,String selectedfeaturefile,int initnum,int feanum ) throws Exception{
		HashMap<String,Integer> stopwords=new HashMap<String,Integer>();
		BufferedReader br1=new BufferedReader(new InputStreamReader(new FileInputStream(stopwordsfile),"UTF-8"));
        String tp=br1.readLine();
        while(tp!=null)
        {
        	stopwords.put(tp, 1);
        	tp=br1.readLine();
        }
		br1.close();
		
		BufferedReader br=new BufferedReader(new InputStreamReader(new FileInputStream(initfeaturesfile),"UTF-8"));
		String temp=br.readLine();
		double[] values=new double[initnum];
        int[] indexs=new int[initnum];
		Vector WordList=new Vector();
		int i;
        for(i=0;i<initnum;i++)
        	indexs[i]=i;
        i=0;
		while(temp!=null)
		{
			String[] subs=temp.split(" ");
			WordList.add(subs[0]);
			double val=Double.valueOf(subs[1]);
			values[i++]=val;
			temp=br.readLine();
		}
		br.close();
		MergeSort.merge_sort(values, 0, initnum-1, indexs);
        PrintWriter pw = new PrintWriter(new OutputStreamWriter(new FileOutputStream(selectedfeaturefile),"UTF-8"),true);
        int selecednum=0;
        for(i=0;selecednum<feanum;i++){
        	String s=WordList.elementAt(indexs[i]).toString();
        	if(stopwords.get(s)==null)
        		{
        		pw.println(s+" "+values[i]);
        		selecednum++;
        		}
        }
        pw.close();
	}
	
	public static void ComputeSparseVectors(String dirname,int feasNum,String feafilename,String outputfile, String mapname ) throws Exception{
		HashMap<String,Integer> words=new HashMap<String,Integer>();
		Vector WordList=new Vector();
		BufferedReader br=new BufferedReader(new InputStreamReader(new FileInputStream(feafilename),"UTF-8"));
		String temp=br.readLine();
		int n=0;
		while(temp!=null)
		{
			String[] subs=temp.split(" ");
			WordList.add(subs[0]);
			words.put(subs[0], n);
			n++;
			temp=br.readLine();
		}
		br.close();
		int[] DF=new int[feasNum];
		for(int i=0;i<feasNum;i++)
			DF[i]=0;
		int[] mark=new int[feasNum];
		File dir = new File(dirname);
        File file[] = dir.listFiles();
        for (int i = 0; i < file.length; i++) {
        	for(int j=0;j<feasNum;j++)
        		mark[j]=0;
			br=new BufferedReader(new InputStreamReader(new FileInputStream(file[i].toString()),"UTF-8"));
		    temp=br.readLine();
			while(temp!=null)
			{
				String[] subs=temp.split(" +");
				for (int k = 0; k < subs.length; k++)
				{
					String s=subs[k];
					if(words.get(s)!=null)
						{
						int index=words.get(s);
						if(mark[index]==0)
						{
							DF[index]++;
							mark[index]=1;
						}
					}
				}
				temp=br.readLine();
				
			}
			br.close();
			
        }
        int totalfilenum=file.length;
        PrintWriter pw = new PrintWriter(new OutputStreamWriter(new FileOutputStream(outputfile),"UTF-8"),true);
        PrintWriter pw2 = new PrintWriter(new OutputStreamWriter(new FileOutputStream(mapname),"UTF-8"),true);
        //pw.println(totalfilenum);
        for (int i = 0; i < file.length; i++) {
        	pw2.println(file[i].getName());
        	if(i%10==0)
        	System.out.println(i);
        	int[] TF=new int[feasNum];
        	for(int j=0;j<feasNum;j++)
        		TF[j]=0;
        	br=new BufferedReader(new InputStreamReader(new FileInputStream(file[i].toString()),"UTF-8"));
		    temp=br.readLine();
			while(temp!=null)
			{
				String[] subs=temp.split(" +");
				for (int k = 0; k < subs.length; k++)
				{
					String s=subs[k];
					if(words.get(s)!=null)
						{
						int index=words.get(s);
						TF[index]++;	
						}
				}
				temp=br.readLine();
			}
			br.close();
        	double[] TfIdf=new double[feasNum];
        	double norm=0;
        	for(int j=0;j<feasNum;j++)
        	{
        		if(DF[j]!=0)
        		    TfIdf[j]=(double)TF[j]/feasNum*Math.log((double)totalfilenum/DF[j]);
        		else
        			TfIdf[j]=0;
        		norm+=TfIdf[j]*TfIdf[j];
        	}
        	norm=Math.sqrt(norm);
        	if(norm!=0){
        	for(int j=0;j<feasNum;j++)
        	{
        		if(TfIdf[j]>0)
        			pw.print(j+":"+TfIdf[j]/norm+" ");
        	}
        	pw.print("\n");
        	}
        	
        }
        pw.close();
        pw2.close();
	}
	
	public static void TextProcessing(String srcPath, int feaNum)throws Exception{
		int cnt=CountWordsNum(srcPath);
		ComputeTFIDF(srcPath,cnt,"./wordlist.txt" );
		FeatureSelection("./stopwords.txt","./wordlist.txt","./wordlist_selected.txt",cnt,feaNum);
		ComputeSparseVectors(srcPath,feaNum,"./wordlist_selected.txt","./textvector_sparse.txt","./filenamelist.txt");
	}
	
	public static void main(String[] args)throws Exception{
		//int cnt=CountWordsNum("E:\\Data4Classify\\SogouC\\ClassFile\\3000\\");
		//ComputeTFIDF("E:\\Data4Classify\\SogouC\\ClassFile\\3000\\",cnt,"E:\\sogouwords.txt" );
		//FeatureSelection("E:\\stopwords.txt","E:\\sogouwords.txt","E:\\sogouwords_selected.txt",cnt,1000);
		//ComputeSparseVectors("E:\\Data4Classify\\SogouC\\ClassFile\\3000\\",1000,"E:\\sogouwords_selected.txt","E:\\sogou_sparse.txt","E:\\sogou_mapname.txt");
		TextProcessing("E:\\Data4Classify\\SogouC\\ClassFile\\3000", 1000);
	}
	

}
