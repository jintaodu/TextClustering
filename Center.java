import java.util.Vector;


public class Center extends Object{
	
	public int dimension;
	public double[] weights;
	public Vector<Integer> docindexs;
	public Center(int dim){
		dimension=dim;
		weights=new double[dim];
		docindexs=new Vector<Integer>();
	}
	public void Initialize(){
		int i;
		for(i=0;i<dimension;i++)
			weights[i]=0;
		docindexs.clear();
	}
 
}
