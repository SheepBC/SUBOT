package Main;

import Command.Stock;
import Command.User_Health;
import SugoData.SugoData;
import Supporter.SaveNLoad;
import Supporter.UserDB;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.events.ReadyEvent;

import java.io.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

public class Ready implements Runnable{

    ReadyEvent event;
    JDA jda;

    public Ready(ReadyEvent event){
        this.event = event;
        jda = event.getJDA();
    }

    @Override
    public void run() {
        User_System();
        Stock();
        BackUp();
        ChangeActivity();
        SuGo();
        //log();
    }

    public void User_System(){
        for(String id: UserDB.getUserList()){
            Health(id);
            User_Debt(id);
        }
        UserDB.Update_User_Charts();
        UserDB.Tax_Update();
        System.out.println("유저시스템 작동시작");
    }

    public void Health(String id){
        UserDB userDB = new UserDB(id);
        if(userDB.isHealth()){
            User_Health.Start_Health(jda,id);
        }
        if(userDB.isSleeping()){
            User_Health.Start_Sleep(jda,id);
        }
    }

    public void User_Debt(String id){
        UserDB userDB = new UserDB(id);
        userDB.Debt_update(userDB);
    }

    public void Stock(){
        Stock.Price_Update();
        System.out.println("주식시스템 작동시작");

    }

    public void log(){
        main.change();
        Timer timer = new Timer();
        BufferedReader br = null;
        try {
            br = new BufferedReader(new FileReader("output.txt"));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        BufferedReader finalBr = br;
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                try {
                    String s = finalBr.readLine();
                    if(s == null) return;
                    System.setOut(main.console);
                    String time = LocalTime.now().getMinute()+":"+LocalTime.now().getSecond();
                    String send = "["+time+"] "+s;
                    System.out.println(s);
                    jda.getTextChannelById(Setting.getlog()).sendMessage(send).queue();
                    main.change();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        },100,300);

    }

    public void BackUp(){
        Timer timer = new Timer();
        Date date = new Date();
        date.setHours(1);
        date.setMinutes(30);

        int second = 1000;
        int min = 60*second;
        int hour = 60*min;

        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                LocalDate dat = LocalDate.now();
                LocalTime time = LocalTime.now();
                String ts = time.toString().replace(":","");
                int i = ts.indexOf(".");
                ts = ts.substring(0,i);
                main.isBackup = true;
                jda.getPresence().setActivity(Activity.playing("백업중"));
                SaveNLoad.zip("BackUP/"+dat+"_"+ts+"_userDB.zip","UserDB");
                SaveNLoad.zip("BackUP/"+dat+"_"+ts+"_Stock.zip","Stock");
                SaveNLoad.zip("BackUP/"+dat+"_"+ts+"_Sub.zip","Sub");
                main.isBackup = false;
                System.gc();
                System.out.println("백업완료");
                jda.getPresence().setActivity(Activity.playing("\"숩명령어\"를 쳐보라고 말하는중 "));
            }
        },date,8*hour);
        System.out.println("백업시스템 작동시작");
    }

    public void SuGo(){
        SugoData.Update_Chart_SP();
        System.out.println("숩봇차트시스템 작동시작");
    }

    public void ChangeActivity(){
        Timer timer = new Timer();

        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                if(main.isBackup) jda.getPresence().setActivity(Activity.playing("백업중"));
                else jda.getPresence().setActivity(Activity.playing("\"숩명령어\"를 쳐보라고 말하는중 "));
            }
        },0,10000);

    }


}
