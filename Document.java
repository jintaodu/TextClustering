
public class Document extends Object{
	
	public int wordsNum;
	public int[] wordsIndex;
	public double[] wordsWeight;
	public int category;
	public Document(int num){
		wordsNum=num;
		wordsIndex=new int[wordsNum];
		wordsWeight=new double[wordsNum];
		
	}
}
