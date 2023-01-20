package Command;

import Chart.*;
import SugoData.SugoData;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

import java.awt.*;
import java.io.File;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static Chart.Make_Chart.makeLine_Chart;

public class Basic_Chart {
    MessageReceivedEvent event;
    String userID;
    MessageChannel channel;
    String[] arg;

    public Basic_Chart(MessageReceivedEvent event,String[] args) {
        this.event = event;
        userID = event.getAuthor().getId();
        channel = event.getChannel();
        arg = args;
    }

    public void chart(){
        String command = "숩평균유저그래프 (SP/건강/수박고기SP/수박고기자산) (오늘/일주일/한달/일년/전체)";
        String days = "전체";
        String[] day_list = {"일주일","한달","일년","전체","오늘"};
        String type = "SP";
        String[] type_list = {"SP","건강","수박고기SP","수박고기자산"};
        if(arg.length == 2){
            if(List.of(day_list).contains(arg[1])) days = arg[1];
            else if(List.of(type_list).contains(arg[1])) type = arg[1];
            else {
                channel.sendMessage(command).queue();
                return;
            }
        }else if(arg.length >= 3){
            if(List.of(type_list).contains(arg[1])) type = arg[1];
            else {
                channel.sendMessage(command).queue();
                return;
            }
            if(List.of(day_list).contains(arg[2])) days = arg[2];
            else {
                channel.sendMessage(command).queue();
                return;
            }
        }

        String path = makeChart(days,type);
        File file = new File(path);
        channel.sendFile(file).queue();
    }

    public String makeChart(String days,String type){
        String danwe = "berry";
        String type_E = "SP";
        switch (type){
            case "건강":
                type_E = SugoData.am_h;
                danwe = "health";
                break;
            case "SP":
                type_E = SugoData.sp_m;
                break;
            case "수박고기SP":
                type_E = SugoData.sg_sp;
                break;
            case "수박고기자산":
                type_E = SugoData.sg_m;
                danwe = "berry";
                break;
        }

        long plus = 0;
        if(days.equalsIgnoreCase("일주일")){
            plus = -7;
        }
        if(days.equalsIgnoreCase("한달")){
            plus = -30;
        }
        if(days.equalsIgnoreCase("일년")){
            plus = -365;
        }
        if(days.equalsIgnoreCase("전체")){
            plus = -12000;
        }

        ArrayList<String> check_times = new  ArrayList<>();

        String now = LocalDate.now().toString().replace("-","/");
        String old = LocalDate.now().plusDays(plus).toString().replace("-","/");
        Color color = new Color(1, 182, 20, 255);
        String colors = "1:182:20";
        String start_time  = LocalDate.now().plusDays(plus).toString();

        Chart chart = new Chart(SugoData.ChartPath+type_E);
        ArrayList<Chart_Data> cds = chart.getChart_Data_list(old,now);
        check_times.add(cds.get(0).time.substring(0,10).replace("/","-"));
        String path = "Sub/images/"+type_E+"_"+days+".png";

        String[] ctList = check_times.toArray(new String[0]);
        Arrays.sort(ctList);
        String check_time = ctList[0];

        if(checkStartTime(start_time,check_time)){
            start_time = check_time;
        }

        makeLine_Chart(path,type_E.replace("/","-")+"("+start_time+"~"+LocalDate.now()+")/Day/"+danwe,type_E,colors, cds);
        return path;
    }

    static private boolean checkStartTime(String startTime,String checkTime){
        if(checkTime.equalsIgnoreCase(""))return false;
        long s = Long.parseLong(startTime.replace("-",""));
        long c = Long.parseLong(checkTime.replace("-",""));
        if(s < c) return true;
        return false;
    }
}
