package Supporter;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;

public class Python {

    public static List<String> getPython(String PytonPath, String...arg){

        try {
            String command = "D:\\자바파일\\bot\\untitled\\untitled1\\venv\\Scripts\\python.exe";// 명령어

            String[] commands = Support.addStrings(new  String[]{command,PytonPath},arg);
            ProcessBuilder builder = new ProcessBuilder(commands);
            Process process = builder.start();
            int exitVal = process.waitFor();  // 자식 프로세스가 종료될 때까지 기다림
            BufferedReader br = new BufferedReader(new InputStreamReader(process.getInputStream()));// 서브 프로세스가 출력하는 내용을 받기 위해
            BufferedReader br2 = new BufferedReader(new InputStreamReader(process.getErrorStream()));
            String line;
            while ((line = br2.readLine()) != null){
                System.out.println("error: "+line);
            }
            if(exitVal != 0) {
                System.out.println("서브 프로세스가 비정상 종료되었다.");
            }else{
                System.out.println("서브 프로세스가 종료되었다.");
            }

            return br.lines().toList();
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

    }

}
