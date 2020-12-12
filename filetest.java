import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.lang.NullPointerException; 

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.Scanner;


public class filetest {
    static Socket client = new Socket();
    
    public static void main(String[] args) throws Exception{
       
       ServerSocket soc = new ServerSocket(11111);  //포트 11111로 서버를 개통합니다.
        System.out.println("Server Start");
        client = soc.accept();                       //클라이언트의 접속을 받습니다.
        InputStream in = null;                       
        FileOutputStream out = null;
        in = client.getInputStream();                //클라이언트로 부터 바이트 단위로 입력을 받는 InputStream을 얻어와 개통합니다.
        DataInputStream din = new DataInputStream(in);  //InputStream을 이용해 데이터 단위로 입력을 받는 DataInputStream을 개통합니다.
        System.out.println("client accept!");
        
        
        while(true){
           int data = din.readInt();           //Int형 데이터를 전송받습니다.       
            String filename = din.readUTF();            //String형 데이터를 전송받아 filename(파일의 이름으로 쓰일)에 저장합니다.      
            File file = new File(filename);             //입력받은 File의 이름으로 복사하여 생성합니다.      
            out = new FileOutputStream(file);           //생성한 파일을 클라이언트로부터 전송받아 완성시키는 FileOutputStream을 개통합니다.
            int datas = data;                            //전송횟수, 용량을 측정하는 변수입니다.
            byte[] buffer = new byte[1024];        //바이트단위로 임시저장하는 버퍼를 생성합니다.
            int len;                               //전송할 데이터의 길이를 측정하는 변수입니다.

            for(;data>0;data--){                   //전송받은 data의 횟수만큼 전송받아서 FileOutputStream을 이용하여 File을 완성시킵니다.
               len = in.read(buffer);
               out.write(buffer,0,len);
            }
            
            System.out.println("약: "+datas+" kbps");
            System.out.println("---------------------------------------");
   
            FileInputStream fin;
            OutputStream outt;
            
            
            fin = new FileInputStream(new File(filename)); //FileInputStream - 파일에서 입력받는 스트림을 개통합니다.
            outt =client.getOutputStream(); 
            DataOutputStream dout = new DataOutputStream(outt); //OutputStream을 이용해 데이터 단위로 보내는 스트림을 개통합니다
            
            buffer = new byte[1024];        //바이트단위로 임시저장하는 버퍼를 생성합니다
            
            len=0;                               //전송할 데이터의 길이를 측정하는 변수입니다. 
            data=0;                            //전송횟수, 용량을 측정하는 변수입니다.
    
            while((len = fin.read(buffer))>0){     //FileInputStream을 통해 파일에서 입력받은 데이터를 버퍼에 임시저장하고 그 길이를 측정합니다.
               data++;                        //데이터의 양을 측정합니다.
            }
    
            datas = data;                      //아래 for문을 통해 data가 0이되기때문에 임시저장한다.

            fin.close();
            fin = new FileInputStream(filename);   //FileInputStream이 만료되었으니 새롭게 개통합니다.
   
            dout.writeInt(data);                   //데이터 전송횟수를 서버에 전송하고, 
            dout.writeUTF(filename);               //파일의 이름을 서버에 전송합니다.
    
            len = 0;      
            for(;data>0;data--){                   //데이터를 읽어올 횟수만큼 FileInputStream에서 파일의 내용을 읽어옵니다.
               len = fin.read(buffer);        //FileInputStream을 통해 파일에서 입력받은 데이터를 버퍼에 임시저장하고 그 길이를 측정합니다.
               outt.write(buffer,0,len);       //서버에게 파일의 정보(1kbyte만큼보내고, 그 길이를 보냅니다.
            }
            System.out.println("약 "+datas+" kbyte");       
            System.out.println("------------------------------------------");

            out.close();
            fin.close();
            
            if( file.exists() ){
            	if(file.delete()){
            		System.out.println("파일삭제 성공"); 
            	}
            	else{ 
            		System.out.println("파일삭제 실패");  
            	} 
            }
            else{
            	System.out.println("파일이 존재하지 않습니다.");  	
            }
        }
        
    }
 
}
