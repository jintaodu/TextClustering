import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Vector;


public class Kmeans {
	public static Document[] ReadFromFile(String filename)throws Exception{
		//从文件读取文档信息，文件第一行保存待聚类的文档数目，之后是文档向量的稀疏表示
		BufferedReader br=new BufferedReader(new InputStreamReader(new FileInputStream(filename),"utf-8"));
        String temp=br.readLine();
        int docnum=0;
        while(temp!=null){
        	docnum++;
        	temp=br.readLine();
        }
        br.close();
        Document[] docs=new Document[docnum];
        br=new BufferedReader(new InputStreamReader(new FileInputStream(filename),"utf-8"));
        temp=br.readLine();
        int docindex=0;
        while(temp!=null){
        	String[] subs=temp.split(" ");
			int curdim=subs.length;
			docs[docindex]=new Document(curdim);
			int l;
			for(l=0;l<curdim;l++)
			{
				String[] subs2=subs[l].split(":");
				docs[docindex].wordsIndex[l]=Integer.valueOf(subs2[0]);
				docs[docindex].wordsWeight[l]=Double.valueOf(subs2[1]);
			}
			docindex++;
        	temp=br.readLine();
        }
		br.close();
		return docs;
	}
	
	public static double ComputeDoc2DocSim(Document doc1,Document doc2)throws Exception{
		//计算两个稀疏表示的文档的距离
		int len1=doc1.wordsNum,len2=doc2.wordsNum;
		double sim=0;
		int iter1,iter2;
		for(iter1=0,iter2=0;iter1<len1&&iter2<len2;)
		{
			if(doc1.wordsIndex[iter1]<doc2.wordsIndex[iter2])
				iter1++;
			else if(doc1.wordsIndex[iter1]>doc2.wordsIndex[iter2])
				iter2++;
			else
				{
				sim+=doc1.wordsWeight[iter1]*doc2.wordsWeight[iter2];
				iter1++;
				iter2++;
				}	
		}
		return sim;
	}
	
	public static double ComputeDoc2CenterSim(Center cen1,Document doc2)throws Exception{
		//计算文档到类中心的距离
		double sim=0;
		int len=doc2.wordsNum;
		int i;
		for(i=0;i<len;i++)
			sim+=cen1.weights[doc2.wordsIndex[i]]*doc2.wordsWeight[i];
		return sim;
	}
	
	public static void AddDoc2Center(Center cen1,Document doc2, int docindex)throws Exception{
		//将文档doc2加入类中心cen1，并更新类中心权重；
		cen1.docindexs.add(docindex);
		int len=doc2.wordsNum;
		int i;
		for(i=0;i<len;i++)
			cen1.weights[doc2.wordsIndex[i]]+=doc2.wordsWeight[i];
		
	}
	
	public static void NormalizeCenter(Center cen)throws Exception{
		//归一化类中心
		int len=cen.dimension;
		double norm=0;
		int i;
		for(i=0;i<len;i++)
			norm+=cen.weights[i]*cen.weights[i];
		norm=Math.sqrt(norm);
		if(norm>0){
		for(i=0;i<len;i++)
			cen.weights[i]=cen.weights[i]/norm;
		}
	}
	
	public static void ComputeInitialCenters(Vector<Center> cens,Document[] docs)throws Exception{
		//初始化类中心，计算离当前中心的最大-最小距离点
		HashMap<Integer,Integer> hasSelected=new HashMap<Integer,Integer>();
		int cenNum=cens.size();
		AddDoc2Center(cens.elementAt(0),docs[0],0);
		hasSelected.put(0, 1);
		int k;
		SimpleDateFormat df = new SimpleDateFormat("HH:mm:ss");
		for(k=1;k<cenNum;k++){
			System.out.println(df.format(new Date())+" K="+k);
			int minindex=-1;
			double minsim=1;
			for(int i=0;i<docs.length;i++)
				if(hasSelected.get(i)==null){
					double curmaxsim=-1;
					for(int j=0;j<k;j++)
					{
						double sim=ComputeDoc2DocSim(docs[i],docs[cens.elementAt(j).docindexs.elementAt(0)]);
						if(curmaxsim<sim)
							curmaxsim=sim;			
					}
					if(minsim>curmaxsim)
					{
						minsim=curmaxsim;
						minindex=i;
					}
				}
			AddDoc2Center(cens.elementAt(k),docs[minindex],minindex);
			hasSelected.put(minindex, 1);
		}
	}
	
	public static int notChanged(int[] l1, int[] l2)throws Exception{
		int ret;
		int len=l1.length;
		int i;
		for(i=0;i<len&&l1[i]==l2[i];i++);
		if(i>=len)
			ret=1;
		else
			ret=0;
		return ret;
	}
	
	public static void Kmeans(Document[] docs,int dim,int K,String filename,String OutputPath)throws Exception{
		//Kmeans聚类算法
		Vector<Center> cens=new Vector<Center>();
		int i;
		for(i=0;i<K;i++)
		{
			cens.add(new Center(dim));
			cens.elementAt(i).Initialize();
		}
		ComputeInitialCenters(cens,docs);
		int[] oldlabels=new int[docs.length];
		int[] labels=new int[docs.length];
		for(i=0;i<docs.length;i++)
			{
			labels[i]=-1;
			oldlabels[i]=-2;
			}
		int nowIter=1;
		while(notChanged(oldlabels,labels)==0){
			System.out.println("nowIter="+(nowIter++));
			for(i=0;i<docs.length;i++)
				oldlabels[i]=labels[i];
			for(i=0;i<docs.length;i++){
				int maxindex=-1;
				double max=-1;
				for(int j=0;j<K;j++)
				{
					double sim=ComputeDoc2CenterSim(cens.elementAt(j),docs[i]);
					if(max<sim)
					{
						max=sim;
						maxindex=j;
					}
				}
				labels[i]=maxindex;
			}
			for(i=0;i<K;i++)
				cens.elementAt(i).Initialize();
			for(i=0;i<docs.length;i++){
				AddDoc2Center(cens.elementAt(labels[i]),docs[i],i);
				docs[i].category=labels[i];
			}
			for(i=0;i<K;i++)
				NormalizeCenter(cens.elementAt(i));
		}
		
		Vector<String> namelist=new Vector<String>();
		BufferedReader br=new BufferedReader(new InputStreamReader(new FileInputStream(filename),"utf-8"));
		String temp=br.readLine();
		while(temp!=null){
			namelist.add(temp);
			temp=br.readLine();
		}
        br.close();
		//输出结果
		PrintWriter pw = new PrintWriter(new OutputStreamWriter(new FileOutputStream(OutputPath+"\\result.txt"),"utf-8"),true);

		for(i=0;i<K;i++){
			pw.println("第"+(i+1)+"类的成员：");
			for(int j=0;j<cens.elementAt(i).docindexs.size();j++)
				{
				pw.println(namelist.elementAt(cens.elementAt(i).docindexs.elementAt(j)));
				}
			pw.print("\n");
		}
		pw.close();
	}
	
public static void KmeansClustering(int feaNum,int ClusterNum,String OutputPath) throws Exception{
		
		/*BufferedReader br=new BufferedReader(new InputStreamReader(new FileInputStream("E:/sogou_sparse.txt")));
		PrintWriter pw = new PrintWriter(new OutputStreamWriter(new FileOutputStream("E:\\sogou_sparse2.txt")),true);
        String temp=br.readLine();
        int docnum=Integer.valueOf(temp);
        pw.println(docnum-2);
        temp=br.readLine();
        while(temp!=null)
        	{
        	pw.println(temp);
        	temp=br.readLine();
        	}
        br.close();
        pw.close();*/
		Document[] docs=ReadFromFile("./textvector_sparse.txt");
		SimpleDateFormat df = new SimpleDateFormat("HH:mm:ss");
		System.out.println(df.format(new Date())+" start!");
		Kmeans(docs,feaNum,ClusterNum,"./filenamelist.txt",OutputPath);
		System.out.println(df.format(new Date())+" end!");
	}
	
	public static void main(String[] args) throws Exception{
		
		/*BufferedReader br=new BufferedReader(new InputStreamReader(new FileInputStream("E:/sogou_sparse.txt")));
		PrintWriter pw = new PrintWriter(new OutputStreamWriter(new FileOutputStream("E:\\sogou_sparse2.txt")),true);
        String temp=br.readLine();
        int docnum=Integer.valueOf(temp);
        pw.println(docnum-2);
        temp=br.readLine();
        while(temp!=null)
        	{
        	pw.println(temp);
        	temp=br.readLine();
        	}
        br.close();
        pw.close();
		Document[] docs=ReadFromFile("E:/sogou_sparse.txt");
		SimpleDateFormat df = new SimpleDateFormat("HH:mm:ss");
		System.out.println(df.format(new Date())+" start!");
		Kmeans(docs,1000,5,"E:\\sogou_mapname.txt");
		System.out.println(df.format(new Date())+" end!");*/
		KmeansClustering(1000,5,"E:");
	}

}
