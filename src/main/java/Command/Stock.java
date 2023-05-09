package Command;

import Chart.*;
import Supporter.Stock_Data;
import Supporter.Support;
import Supporter.UserDB;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.interactions.components.selections.SelectMenu;
import net.dv8tion.jda.api.interactions.components.selections.SelectMenuInteraction;

import java.io.File;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.*;

import static Chart.Make_Chart.makeLine_Chart;

public class Stock {

    MessageReceivedEvent event;
    String[] arg;
    net.dv8tion.jda.api.entities.User user;
    String userid;
    MessageChannel channel;

    String notUser = "회원가입이 되어있지 않은 유저입니다.";

    public Stock(MessageReceivedEvent event,String[] args){

        this.event = event;
        this.arg = args;
        user = event.getAuthor();
        userid = user.getId();
        channel = event.getChannel();

    }

    public void inform(){
        EmbedBuilder embed = new EmbedBuilder();
        embed.setTitle("주식정보");
        embed.setColor(Support.getRandomColor());
        SelectMenu.Builder menu = SelectMenu.create("Stock");

        for (String id:Stock_Data.list()){
            Stock_Data stock_data = new Stock_Data(id);
            menu.addOption(stock_data.getName(),id,"ID:"+id);
            embed.addField(stock_data.getName()+"("+id+")",price(stock_data),true);
        }
        channel.sendMessageEmbeds(embed.build()).setActionRow(menu.build()).queue();

    }

    private String price(Stock_Data stock_data){
        String p = "";
        BigDecimal now = new BigDecimal(stock_data.getPrice()).multiply(new BigDecimal("100"));
        BigDecimal last = new BigDecimal(stock_data.getLast_Price());
        last = last.equals(BigDecimal.ZERO) ? BigDecimal.ONE:last;
        BigDecimal up_P = now.divide(last,2, RoundingMode.HALF_EVEN).subtract(new BigDecimal("100"));
        String add = up_P.min(BigDecimal.ZERO).equals(BigDecimal.ZERO) ? "+":"";
        p += Support.unit(stock_data.getPrice())+"("+add+up_P+"%)";
        return p;
    }

    public void Chart(){

        String command = "숩그래프 (주식아이디 or 오늘/일주일/한달/일년) (오늘/일주일/한달/일년 or 시작날짜(예: 2022/12/30)) (끝날짜(예: 2022/12/30))";

        ArrayList<String> days = new ArrayList<>();
        days.add("오늘");days.add("일주일");days.add("한달");days.add("일년");

        if(arg.length == 1){
            SelectMenu.Builder menu = SelectMenu.create("Stock_Chart");
            for (String id:Stock_Data.list()){
                Stock_Data stock_data = new Stock_Data(id);
                menu.addOption(stock_data.getName(),id,"ID:"+id);
            }
            channel.sendMessage("그래프를 볼 주식을 고르시오").setActionRow(menu.build()).queue();
        }
        if(arg.length == 2){
            String a2 = arg[1];
            String[] a2s = a2.split(",");
            if(days.contains(a2)){
                SelectMenu.Builder menu = SelectMenu.create("Stock_Chart"+a2);
                for (String id:Stock_Data.list()){
                    Stock_Data stock_data = new Stock_Data(id);
                    menu.addOption(stock_data.getName(),id,"ID:"+id);
                }
                channel.sendMessage("그래프를 볼 주식을 고르시오").setActionRow(menu.build()).queue();
            }
            else if(checkIDs(a2s)){
                channel.sendFile(new File(make_Chart("전체",a2s))).queue();
            }
            else {
                channel.sendMessage(command).queue();
            }
        }
        if(arg.length == 3){
            String a2 = arg[1];
            String[] a2s = a2.split(",");
            String a3 = arg[2];
            if(checkIDs(a2s) & days.contains(a3)){
                channel.sendFile(new File(make_Chart(a3,a2s))).queue();
            }
            else {
                channel.sendMessage(command).queue();
            }
        }
        if(arg.length == 4){
            String a2 = arg[1];
            String[] a2s = a2.split(",");
            String a3 = arg[2];
            String a4 = arg[3];

            if(checkTime(a3) & checkTime(a4) &checkIDs(a2s)){

                String[] list = {a3,a4};
                Arrays.sort(list);

                ArrayList<String> check_times = new  ArrayList<>();

                String idss = "";
                ArrayList<ArrayList<Chart_Data>> cdss = new ArrayList<>();
                String colors = "";
                String start_time = list[0].replace("/","-");

                for(String id:a2s){
                    Stock_Data stock_data = new Stock_Data(id.toUpperCase());
                    Chart chart = stock_data.getChart();
                    ArrayList<Chart_Data> cds = chart.getChart_Data_list(list[0],list[1]);
                    cdss.add(cds);
                    idss += id+"/";
                    colors += stock_data.getColor()+"/";
                    check_times.add(cds.get(0).time.substring(0,10).replace("/","-"));
                }

                idss = idss.substring(0,idss.length()-1);
                String path = "charts/"+idss.replace("/","-")+"_"+start_time+"_"+list[1].replace("/","-")+".png";

                String[] ctList = check_times.toArray(new String[0]);

                Arrays.sort(ctList);
                String check_time = ctList[0];

                if(checkStartTime(start_time,check_time)){
                    start_time = check_time;
                }

                String endTime = list[1];
                if(checkEndTime(endTime)){
                    endTime = LocalDate.now().toString();
                }

                makeLine_Chart(path,idss.replace("/","-")+"("+start_time+"~"+endTime.replace("/","-")+")/Day/Berry",
                        idss,colors, cdss.toArray(new ArrayList[0]));
                channel.sendFile(new File(path)).queue();

            }
            else {
                channel.sendMessage(command).queue();
            }
        }
    }

    static public String make_Chart(String days,String... ids){

        ArrayList<ArrayList<Chart_Data>> cdss = new ArrayList<>();

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
        String idss ="";
        String colors = "";
        String start_time  = LocalDate.now().plusDays(plus).toString();
        for(String id:ids){
            Stock_Data stock_data = new Stock_Data(id.toUpperCase());
            Chart chart = stock_data.getChart();
            ArrayList<Chart_Data> cds = chart.getChart_Data_list(old,now);
            cdss.add(cds);
            idss += id+"/";
            colors += stock_data.getColor()+"/";
            String st = "9999/99/99";
            int sti = 999999999;
            for(Chart_Data cd:cds){
                String cdt = cd.time.substring(0,10);
                int cdti = Integer.parseInt(cdt.replace("/",""));
                if(cdti < sti){
                    st = cdt;
                    sti = cdti;
                }
            }
            check_times.add(st.replace("/","-"));
        }
        idss = idss.substring(0,idss.length()-1);
        String path = "charts/"+idss.replace("/","-")+"_"+days+".png";

        String[] ctList = check_times.toArray(new String[0]);
        Arrays.sort(ctList);
        String check_time = ctList[0];

        if(checkStartTime(start_time,check_time)){
            start_time = check_time;
        }

        makeLine_Chart(path,idss.replace("/","-")+"("+start_time+"~"+LocalDate.now()+")/Day/Berry",idss,colors, cdss.toArray(new ArrayList[0]));
        return path;
    }

    private boolean checkIDs(String[] ids){
        for(String id:ids){
            if(!Stock_Data.list().contains(id.toUpperCase())){
                return false;
            }
        }
        return true;
    }
    static private boolean checkStartTime(String startTime,String checkTime){
        if(checkTime.equalsIgnoreCase(""))return false;
        long s = Long.parseLong(startTime.replace("-",""));
        long c = Long.parseLong(checkTime.replace("-",""));
        if(s < c) return true;
        return false;
    }

    static private boolean checkEndTime(String checkTime){
        if(checkTime.equalsIgnoreCase(""))return false;
        long s = Long.parseLong(LocalDate.now().toString().replace("-",""));
        long c = Long.parseLong(checkTime.replace("/",""));
        return s < c;
    }

    static private boolean checkTime(String time){
        String[] times = time.split("/");
        if(times.length != 3) return false;
        if(times[0].length() != 4) return false;

        if(times[1].length() != 2) return false;

        if(times[2].length() != 2) return false;

        for(String s:times){
            if(!Support.isLong(s)) return false;
        }

        return true;
    }

    static public void Price_Update(){
        Timer timer = new Timer();
        int second = 1000;
        int min = 60*second;

        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                //System.out.println(LocalTime.now());
                for(String id:Stock_Data.list()){
                    Stock_Data stock_data = new Stock_Data(id);
                    stock_data.addTime(1);
                    if(stock_data.getAll_Time() <= stock_data.getTime()){
                        stock_data.update_Price();
                        stock_data.setTime(0);
                        if(id.equalsIgnoreCase("HM")){
                            System.out.println("주식업데이트");
                        }
                    }
                }
            }
        },min,min);

    }

}
