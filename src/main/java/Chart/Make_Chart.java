package Chart;

import java.io.*;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

public class Make_Chart {

/*
    public static ArrayList<ArrayList<Chart_Data>> addNotDay(ArrayList<Chart_Data>... lists){
        ArrayList<String> day_list = getDay_LIst(lists);

        for (ArrayList<Chart_Data> cd_list:lists){
            ArrayList<Chart_Data> cds = new ArrayList<>();
            BigDecimal last_Num = new BigDecimal(cd_list.get(0).value);
            for(Chart_Data cd:cd_list){
                if(cd.equals(cd_list.get(0))){
                    cds.add(cd);
                }
                else {

                }
            }
        }

        return null;
    }

     */

    static public void makeLine_Chart(String path,String inform,String ids,String colors,ArrayList<Chart_Data>... lists){
        /**
         * @apiNote inform = "제목/x/y"
         */

        ArrayList<String> list = new ArrayList<>();
        ArrayList<String> days = Chart.getDay_LIst(lists);

        list.add(path);
        list.add(inform);
        list.add(ids);
        list.add(colors);

        for (ArrayList<Chart_Data> cds:lists){
            list.add(make_CDS_File(cds_To_String(cds,days)));
        }

        make_Chart("chart.exe",list);

    }

    static private void make_Chart(String command,ArrayList<String> list){;

        List<String> pb_List = new ArrayList<>();
        pb_List.add("Go/"+command);
        pb_List.addAll(list);
        ProcessBuilder builder = new ProcessBuilder(pb_List);
        Process process;
        try {
             process = builder.start();
            //System.out.println("시작");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        int exitVal = 0;  // 자식 프로세스가 종료될 때까지 기다림
        try {
            exitVal = process.waitFor();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        BufferedReader br = null; // 서브 프로세스가 출력하는 내용을 받기 위해
        try {
            br = new BufferedReader(new InputStreamReader(process.getInputStream(), "euc-kr"));
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
        String line;
        while (true) {
            try {
                if (!((line = br.readLine()) != null)) break;
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            System.out.println(">>>  " + line); // 표준출력에 쓴다
        }
        if(exitVal != 0) {
            BufferedReader br2;
            try {
                br2 = new BufferedReader(new InputStreamReader(process.getErrorStream(),"euc-kr"));
            } catch (UnsupportedEncodingException e) {
                throw new RuntimeException(e);
            }
            String line2;
            while (true) {
                try {
                    if (!((line2 = br2.readLine()) != null)) break;
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                //System.out.println(">>>  " + line2); // 표준출력에 쓴다
            }

            //System.out.println("서브 프로세스가 비정상 종료되었다.");
        };
        System.out.println("완료");
    }
    static private String cds_To_String(ArrayList<Chart_Data> cds,ArrayList<String> days){
        String s ="";
        int ii = 0;
        for(String day:days){
            if(ii >= cds.size()) break;
            if(day.equalsIgnoreCase(cds.get(ii).time)){
                s += days.indexOf(day)+":"+cds.get(ii).value.replace(" ","")+"/";
                ii++;
            }
        }

        return s.substring(0,s.length()-1);
    }

    static private String make_CDS_File(String cds){
        String path = "charts/"+random(6)+".txt";

        File file = new File(path);
        try {
            BufferedWriter bw = new BufferedWriter(new FileWriter(file,true));
            bw.write(cds);
            bw.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return path;
    }

    static private String random(int num){
        String[] list = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789".split("");
        String s ="";
        for (int i =0;i<num;i++){
            s += list[(int)(Math.random()*(list.length-1))];
        }
        return s;
    }

}
