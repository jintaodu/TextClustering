import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

import org.eclipse.swt.widgets.Display;

import kevin.zhang.NLPIR;


public class WordSegmentation {

	/**
	 * @param args
	 * @throws Exception 
	 * @throws IOException 
	 */
    private NLPIR nlpir;
	public WordSegmentation() throws IOException, Exception
	{
		nlpir = new NLPIR();
		
		String argu = "";
		System.out.println("NLPIR_Init");
		if (nlpir.NLPIR_Init(argu.getBytes("GB2312"),1) == false)
		{
			System.out.println("Init Fail!");
			return;
		}
	}
	public void FileWordSeg(final String sPath, final KmeansRunnable kmeansrunnable) throws IOException
	{
	
				try{
					File sourcedir = new File(sPath);
					File tmpdir = new File("./wordsegmentationresult");
					if(tmpdir.exists())
					{
						String[] tmpfiles = tmpdir.list();
						for(String tmpfile : tmpfiles)
						{
							new File(tmpdir.getAbsolutePath()+"/"+tmpfile).delete();
						}
						System.out.println("删除临时文件结束");
					}else
					{
						tmpdir.mkdir();//创建目录
					}
					System.out.println("sourcedir = "+sourcedir.getAbsolutePath());
					String[] files = sourcedir.list();
					System.out.println("files length = "+files.length);
					for(String filename: files)
					{
						File inf = new File(sourcedir.getAbsolutePath()+"/"+filename);
						kmeansrunnable.log("正在分词："+filename);
						BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(inf)));
						StringBuffer sb = new StringBuffer();
						String line = "";
						String line_separator = System.getProperty("line.separator");
						while((line = br.readLine()) != null)
						{
							sb.append(line+line_separator);
						}
						//System.out.println(sb.toString());
						File outf = new File(tmpdir.getAbsolutePath()+"/"+filename);
						
						BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(outf),"utf-8"));
						System.out.println(filename);
						
						byte nativeBytes[] = nlpir.NLPIR_ParagraphProcess(sb.toString().getBytes("utf-8"), 0);
						String nativeStr = new String(nativeBytes, 0, nativeBytes.length,"utf-8");
						
						bw.write(nativeStr.substring(0, nativeStr.length()-1));
						bw.flush();
						bw.close();
					}

				}catch(Exception e)
				{
					e.printStackTrace();
				}

		
	}
	public static void main(String[] args) throws IOException, Exception {
		// TODO Auto-generated method stub

	    //WordSegmentation ws = new WordSegmentation();
		//ws.FileWordSeg("C:\\Users\\mmdb\\Desktop\\2432");
		//System.out.println(seg.tagFile("H://2.txt"));
	}

}
