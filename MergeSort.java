
public class MergeSort {
	public static void merge(double[] data, int p, int q, int r,int[] Index) throws Exception{
    	int i, j, k, n1, n2;     
        n1 = q - p + 1;     
        n2 = r - q;     
        double[] L=new double[n1]; 
        int[] LIndex=new int[n1];
        double[] R=new double[n2];
    	int[] RIndex=new int[n2];
        for(i = 0, k = p; i < n1; i++, k++)
    	{
            L[i] = data[k];
    		LIndex[i]=Index[k];
    	}
        for(i = 0, k = q + 1; i < n2; i++, k++)
    	{
            R[i] = data[k];
    		RIndex[i]=Index[k];
    	}
        for(k = p, i = 0, j = 0; i < n1 && j < n2; k++)     
        {         
            if(L[i] > R[j])         
            {             
                data[k] = L[i];
    			Index[k]=LIndex[i];
                i++;         
            }         
            else         
            {             
                data[k] = R[j];    
    			Index[k]=RIndex[j];
                j++;         
            } 
        }
        if(i < n1)     
        {         
            for(j = i; j < n1; j++, k++)
    		{
    			data[k] = L[j];
    			Index[k]=LIndex[j];
    		}
        }     
        if(j < n2)     
        {         
            for(i = j; i < n2; i++, k++)
    		{
                data[k] = R[i];
    			Index[k]=RIndex[i];
    		}
        }  
    	
    }
    
    public static void merge_sort(double[] data, int p, int r,int[] Index) throws Exception
    {
    	if(p < r)     
        {         
            int q = (p + r) / 2;         
            merge_sort(data, p, q,Index);         
            merge_sort(data, q + 1, r,Index);         
            merge(data, p, q, r,Index);     
        } 
    	
    }

}
