package Main;

import Command.Stock;
import Command.User_Health;
import Supporter.SaveNLoad;
import Supporter.UserDB;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.events.ReadyEvent;

import java.time.LocalDate;
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
        //Test();
    }

    public void User_System(){
        System.out.println("유저시스템 작동시작");
        for(String id: UserDB.getUserList()){
            Health(id);
            User_Chart(id);
        }
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

    public void User_Chart(String id){
        UserDB userDB = new UserDB(id);
        userDB.update_Chart();
    }

    public void Stock(){
        System.out.println("주식시스템 작동시작");
        Stock.Price_Update();
    }

    public void Test(){

        Timer timer = new Timer();
        Date date = new Date();
        date.setMinutes(20);
        System.out.println(date);
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                System.out.println(1);
            }
        },date,1000*60);

    }

    public void BackUp(){
        System.out.println("백업시스템 작동시작");
        Timer timer = new Timer();
        Date date = new Date();
        date.setHours(1);
        date.setMinutes(30);

        int second = 1000;
        int min = 60*second;
        int hour = 60*min;

        LocalDate dat = LocalDate.now();

        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                SaveNLoad.zip("BackUP/"+dat+"_userDB.zip","UserDB");
                SaveNLoad.zip("BackUP/"+dat+"_Stock.zip","Stock");
                SaveNLoad.zip("BackUP/"+dat+"_Sub.zip","Sub");
            }
        },date,24*hour);
    }


}
