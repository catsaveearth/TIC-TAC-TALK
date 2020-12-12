import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;
 
public class test{
    public static void main(String[] args){
    	Scanner s = new Scanner(System.in);   //파일 이름을 입력받기위해 스캐너를 생성합니다.
                        
        try{
            Socket soc = new Socket("127.0.0.1",11111); //127.0.0.1은 루프백 아이피로 자신의 아이피를 반환해주고,
            System.out.println("Server Start!");        //11111은 서버접속 포트입니다.
            
            
            OutputStream out =soc.getOutputStream();                 //서버에 바이트단위로 데이터를 보내는 스트림을 개통합니다.
            DataOutputStream dout = new DataOutputStream(out); //OutputStream을 이용해 데이터 단위로 보내는 스트림을 개통합니다.            
            
            while(true){
            	/* 보내는 Client 입장의 코드 */
            	String filename = s.next();    //스캐너를 통해 파일의 이름을 입력받고,
                FileInputStream fin = new FileInputStream(new File(filename)); //FileInputStream - 파일에서 입력받는 스트림을 개통합니다.
                byte[] buffer = new byte[1024];        //바이트단위로 임시저장하는 버퍼를 생성합니다.        
                int len;                               //전송할 데이터의 길이를 측정하는 변수입니다. 
                int data=0;                            //전송횟수, 용량을 측정하는 변수입니다.
                while((len = fin.read(buffer))>0){     //FileInputStream을 통해 파일에서 입력받은 데이터를 버퍼에 임시저장하고 그 길이를 측정합니다.
                   data++;                        //데이터의 양을 측정합니다.
                }        
                int datas = data;                      //아래 for문을 통해 data가 0이되기때문에 임시저장한다.
                fin.close();
                fin = new FileInputStream(filename);   //FileInputStream이 만료되었으니 새롭게 개통합니다.
       
                dout.writeInt(data);                   //데이터 전송횟수를 서버에 전송하고, 
                dout.writeUTF("c_"+filename);               //"c_파일의 이름"을 서버에 전송합니다.
        
                len = 0;      
                for(;data>0;data--){                   //데이터를 읽어올 횟수만큼 FileInputStream에서 파일의 내용을 읽어옵니다.
                   len = fin.read(buffer);        //FileInputStream을 통해 파일에서 입력받은 데이터를 버퍼에 임시저장하고 그 길이를 측정합니다.
                   out.write(buffer,0,len);       //서버에게 파일의 정보(1kbyte만큼보내고, 그 길이를 보냅니다.
                }
                System.out.println("약 "+datas+" kbyte");       
                System.out.println("------------------------------------------");
          
             
                /* 받는 Client 입장의 코드 */
                InputStream in = null;                       
                FileOutputStream outt = null;
                in = soc.getInputStream();                //클라이언트로 부터 바이트 단위로 입력을 받는 InputStream을 얻어와 개통합니다.
                DataInputStream din = new DataInputStream(in);  //InputStream을 이용해 데이터 단위로 입력을 받는 DataInputStream을 개통합니다.
 
                data = din.readInt();           //Int형 데이터를 전송받습니다.       
                filename = din.readUTF();            //String형 데이터를 전송받아 filename(파일의 이름으로 쓰일)에 저장합니다.      
  
                File file = new File(filename);             //입력받은 File의 이름으로 복사하여 생성합니다.      
                outt = new FileOutputStream(file);           //생성한 파일을 클라이언트로부터 전송받아 완성시키는 FileOutputStream을 개통합니다.
                datas = data;                            //전송횟수, 용량을 측정하는 변수입니다.
                buffer = new byte[1024];        //바이트단위로 임시저장하는 버퍼를 생성합니다.
                
                len=0;                               //전송할 데이터의 길이를 측정하는 변수입니다.
                for(;data>0;data--){                   //전송받은 data의 횟수만큼 전송받아서 FileOutputStream을 이용하여 File을 완성시킵니다.
                   len = in.read(buffer);
                   outt.write(buffer,0,len);
                }    
                System.out.println("약: "+datas+" kbps");
                System.out.println("---------------------------------------");
        }
        
    }catch(Exception e){
    }       
}
}