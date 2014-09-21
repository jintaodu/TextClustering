import kevin.zhang.NLPIR;

import java.util.*;
import java.io.*;

public class TestNLPIR {

	public static void main(String[] args) throws Exception
	{
		try
		{
			String sInput = "张华平推出的NLPIR分词系统，又名ICTCLAS2013，新增新词识别、关键词提取、微博分词功能。";

			//自适应分词
			test(sInput);		
			
		}
		catch (Exception ex)
		{
		} 

	}

	public static void test(String sInput)
	{
		try
		{
			NLPIR testNLPIR = new NLPIR();
	
			String argu = "";
			System.out.println("NLPIR_Init");
			if (testNLPIR.NLPIR_Init(argu.getBytes("GB2312"),1) == false)
			{
				System.out.println("Init Fail!");
				return;
			}
			
					//导入用户词典前
			byte nativeBytes[] = testNLPIR.NLPIR_ParagraphProcess(sInput.getBytes("UTF-8"), 1);
			String nativeStr = new String(nativeBytes, 0, nativeBytes.length, "UTF-8");
	
			System.out.println("分词结果为： " + nativeStr);

	
			
			//初始化分词组件
			String argu1 = "E:/NLPIR/test/test.TXT";
			String argu2 = "E:/NLPIR/test/test_result1.TXT";
		
			nativeBytes  =testNLPIR.NLPIR_GetFileNewWords(argu1.getBytes("GB2312"),50,true);
			//如果是处理内存，可以调用testNLPIR.NLPIR_GetNewWords
			nativeStr = new String(nativeBytes, 0, nativeBytes.length, "GB2312");
			System.out.println("新词识别结果为： " + nativeStr);
			
			nativeBytes  =testNLPIR.NLPIR_GetFileKeyWords(argu1.getBytes("GB2312"),50,true);
			//如果是处理内存，可以调用testNLPIR.NLPIR_GetKeyWords
			nativeStr = new String(nativeBytes, 0, nativeBytes.length, "GB2312");
			System.out.println("关键词识别结果为： " + nativeStr);
			
			
			
			testNLPIR.NLPIR_FileProcess(argu1.getBytes("GB2312"), argu2.getBytes("GB2312"), 1);
	
			testNLPIR.NLPIR_NWI_Start();
			testNLPIR.NLPIR_NWI_AddFile(argu1.getBytes("GB2312"));
			
			testNLPIR.NLPIR_NWI_Complete();
			
			nativeBytes= testNLPIR.NLPIR_NWI_GetResult(true);
			nativeStr = new String(nativeBytes, 0, nativeBytes.length, "GB2312");
	
			System.out.println("新词识别结果 " + nativeStr);
		
			testNLPIR.NLPIR_NWI_Result2UserDict();//新词识别结果
			argu2 = "E:/NLPIR/test/test_result2.TXT";
			testNLPIR.NLPIR_FileProcess(argu1.getBytes("GB2312"), argu2.getBytes("GB2312"), 1);
	
			testNLPIR.NLPIR_Exit();
		}
		catch (Exception ex)
		{
		} 
}
}
 
