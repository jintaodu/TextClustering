import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Date;

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.widgets.DirectoryDialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.Label;
import org.eclipse.wb.swt.SWTResourceManager;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;


public class TextClustering {

	protected Shell shell;
	private Text text;
	private Text text_1;
	private Text text_2;
	private Text text_3;

	private String text_source_path;
	private String text_output_path;
	private Text text_4;
	private KmeansRunnable kmeansrunnable = new KmeansRunnable(this);
	private Button btnNewButton_22;
	private Button button;
	private int k;//������
	private int feature_dim;//����ά��
	/**
	 * Launch the application.
	 * @param args
	 */
	public static void main(String[] args) {
		try {
			TextClustering window = new TextClustering();
			window.open();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Open the window.
	 */
	public void open() {
		Display display = Display.getDefault();
		createContents();
		shell.open();
		shell.layout();
		while (!shell.isDisposed()) {
			if (!display.readAndDispatch()) {
				display.sleep();
			}
		}
	}

	/**
	 * Create contents of the window.
	 */
	protected void createContents() {
		shell = new Shell();
		shell.setSize(609, 587);
		shell.setText("\u6587\u672C\u805A\u7C7B\u7CFB\u7EDF");
		
		Button btnNewButton = new Button(shell, SWT.NONE);
		btnNewButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				DirectoryDialog dirselect=new DirectoryDialog(shell,SWT.SINGLE);
				dirselect.setText("Select text sour path");
				
				text_source_path = dirselect.open();
				if(text_source_path == null)
					text_source_path = "";
				text.setText(text_source_path);
				System.out.println("text_source_path = "+text_source_path);
			}
		});
		btnNewButton.setBounds(65, 110, 80, 27);
		btnNewButton.setText("\u6E90\u6587\u4EF6\u76EE\u5F55");
		
		Button btnNewButton_1 = new Button(shell, SWT.NONE);
		btnNewButton_1.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				DirectoryDialog dirselect=new DirectoryDialog(shell,SWT.SINGLE);
				dirselect.setText("Select text cluster output path");
				
				text_output_path = dirselect.open();
				if(text_output_path == null)
					text_output_path = "";
				text_1.setText(text_output_path);
				System.out.println("text_output_path = "+text_output_path);
			}
		});
		btnNewButton_1.setBounds(65, 170, 80, 27);
		btnNewButton_1.setText("\u7ED3\u679C\u76EE\u5F55");
		
		text = new Text(shell, SWT.BORDER);
		text.setBounds(187, 110, 311, 23);
		
		text_1 = new Text(shell, SWT.BORDER);
		text_1.setBounds(189, 172, 309, 23);
		
		Label label = new Label(shell, SWT.NONE);
		label.setAlignment(SWT.CENTER);
		label.setFont(SWTResourceManager.getFont("��������", 22, SWT.NORMAL));
		label.setBounds(109, 34, 353, 38);
		label.setText("\u6587\u672C\u805A\u7C7B\u7CFB\u7EDF");
		
		text_2 = new Text(shell, SWT.BORDER);
		text_2.setBounds(191, 231, 73, 23);
		
		text_3 = new Text(shell, SWT.BORDER);
		text_3.setBounds(191, 276, 73, 23);
		
		

		
		Label lblNewLabel = new Label(shell, SWT.NONE);
		lblNewLabel.setAlignment(SWT.CENTER);
		lblNewLabel.setFont(SWTResourceManager.getFont("΢���ź�", 12, SWT.NORMAL));
		lblNewLabel.setBounds(72, 230, 73, 23);
		lblNewLabel.setText("\u7C7B\u522B\u6570K");
		
		Label label_1 = new Label(shell, SWT.NONE);
		label_1.setAlignment(SWT.CENTER);
		label_1.setFont(SWTResourceManager.getFont("΢���ź�", 12, SWT.NORMAL));
		label_1.setBounds(65, 276, 94, 23);
		label_1.setText("\u7279\u5F81\u7EF4\u6570");
		
		text_4 = new Text(shell, SWT.V_SCROLL | SWT.MULTI);
		text_4.setBounds(72, 346, 449, 164);
		
		Label label_2 = new Label(shell, SWT.HORIZONTAL);
		label_2.setFont(SWTResourceManager.getFont("΢���ź�", 16, SWT.NORMAL));
		label_2.setBounds(32, 361, 23, 118);
		label_2.setText("\u8FD0\r\n\u884C\r\n\u72B6\r\n\u6001");
		
		button = new Button(shell, SWT.NONE);
		button.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {

				try {
					if(text.getText().trim().length() == 0)
					{
						MessageDialog.openInformation(null, "Error", "����������ļ�����Ŀ¼!");
						return;
					}						
					else if(text_1.getText().trim().length() == 0)
					{
						MessageDialog.openInformation(null, "Error", "�������������Ч��ַ!");
						return;
					}
					else if(text_2.getText().trim().length() == 0 || !text_2.getText().trim().matches("[0-9]+") || Integer.valueOf(text_2.getText().trim()) <=0)
					{
						MessageDialog.openInformation(null, "Error", "����������������!");
						return;
					}
					else if(text_3.getText().trim().length() == 0 || !text_3.getText().trim().matches("[0-9]+") || Integer.valueOf(text_3.getText().trim()) <=0)
					{
						MessageDialog.openInformation(null, "Error", "�������������ά��!");
						return;
					}
					
					text_source_path = text.getText();
					text_output_path = text_1.getText();
					k = Integer.valueOf(text_2.getText().trim());
					feature_dim = Integer.valueOf(text_3.getText().trim());
					System.out.println("k="+k);
					System.out.println("feature dim="+feature_dim);
					setButtonState(false);//����ʼ���ఴť����Ϊ��Ч
					
					Thread kmeans = new Thread(kmeansrunnable);//��һ��kmeans�߳�
					kmeans.start();
					
				} catch (Exception e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		});
		button.setBounds(336, 257, 80, 27);
		button.setText("\u5F00\u59CB\u805A\u7C7B");

	}//end of createContents
	
	public void setButtonState(boolean flag)
	{
		if(!button.isDisposed()) button.setEnabled(flag);
	}
	public void consoleText(String str)
	{
		text_4.append("\r\n"+str);
	}
	public String get_text_source_path()
	{
		return text_source_path;
	}
	public String get_text_output_path()
	{
		return text_output_path;
	}
	public int get_feature_dim()
	{
		return feature_dim;
	}
	public int get_k()
	{
		return k;
	}

}

class KmeansRunnable implements Runnable{
	
	
	private TextClustering textclustering;
	public KmeansRunnable(TextClustering atextclustering)
	{
		this.textclustering = atextclustering;
	}
	@Override
	public void run()
	{

		WordSegmentation ws;
			try {
				ws = new WordSegmentation();
				log("��ʼ�ִʹ���,���Ժ�");//log�����ں�̨�߳������ǰ̨���棬��ǰ̨�����߳��첽����
				log(textclustering.get_text_source_path());
				Date start = new Date();
				log("��ʼ�ִ�ʱ�䣺"+start.toString());
				ws.FileWordSeg(textclustering.get_text_source_path(),this);
				Date end = new Date();
				log("�ִʳ������ʱ�䣺"+end.toString());
				log("�ִʻ���ʱ�䣺"+(end.getTime()-start.getTime())/1000+"��");
				log("��ʼ��ѡ����");
				
				//FeatureSelection.TextProcessing("./wordsegmentationresult", textclustering.get_feature_dim());
				
				log("��ѡ��������");
				
				log("��ʼ�������");
				//Kmeans.KmeansClustering(textclustering.get_feature_dim(), textclustering.get_k(), textclustering.get_text_output_path());
				log("����������н�����");
				//System.out.println("�ִʳ������");
				//text_4.setText(text_4.getText()+"\r\n"+"�ִʳ������");
				
				//notifyAllTaskFinish();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				
			}
			
		}//end of run


	public void log(final String str)
	{
		Display.getDefault().asyncExec(new Runnable(){
		public void run(){
			textclustering.consoleText(str);//text_4.setText(text_4.getText()+"\r\n"+str);
		}
		});
	}//end of log
	
	public void notifyAllTaskFinish()
	{
		Display.getDefault().asyncExec(new Runnable(){
			public void run(){
				try {
					textclustering.consoleText("���������£�");//text_4.setText(text_4.getText()+"\r\n"+"���������£�");
					BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(textclustering.get_text_output_path()+"/result.txt"),"utf-8"));
					String line = null;
					while( ( line = br.readLine() )!= null)
					{
						textclustering.consoleText(line);//text_4.setText(text_4.getText()+"\r\n"+line);
					}
				} catch (FileNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				textclustering.setButtonState(true);
			}
		});
	}//end of log
	
}
