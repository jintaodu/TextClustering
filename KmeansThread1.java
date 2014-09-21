import org.eclipse.swt.widgets.Display;


public class KmeansThread1 implements Runnable{
	public void run()
	{
		while(true)
		{
			Display.getDefault().asyncExec(new Runnable(){
				public void run()
				{
					//text_4.
				}
			});
		}
	}
	public static void main(String[] args) throws Exception{
		
	    FeatureSelection.TextProcessing("./wordsegmentationresult", 1000);
		Kmeans.KmeansClustering(1000,5,"E:");
		//System.out.println("line s="+System.getProperty("line.separator"));
	}

}
