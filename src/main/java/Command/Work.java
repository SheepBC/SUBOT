package Command;

import SugoData.SugoData;
import Supporter.SaveNLoad;
import Supporter.Support;
import Supporter.UserDB;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.HashMap;

public class Work {

    MessageReceivedEvent event;
    String[] arg;
    net.dv8tion.jda.api.entities.User user;
    String userid;
    MessageChannel channel;

    String notUser = "회원가입이 되어있지 않은 유저입니다.";

    String path = "Setting/Work&Gamb/";

    public Work(MessageReceivedEvent event,String[] args){

        this.event = event;
        this.arg = args;
        user = event.getAuthor();
        userid = user.getId();
        channel = event.getChannel();

    }

    public void mining(){
        if(!UserDB.isUser(userid)){
            channel.sendMessage(notUser).queue();
            return;
        }
        UserDB userDB = new UserDB(user);
        if(userDB.getHealth() < -1000){
            channel.sendMessage("건강이 부족합니다.").queue();
            return;
        }
        HashMap<String,Integer> list = new HashMap<>();
        BigInteger all_price = BigInteger.ZERO;
        long health = 20;
        long addExp = 0;
        if(arg.length >= 2){
            if(Support.isLong(arg[1])){
                health = Long.parseLong(arg[1]);
            }
        }

        while (health >= 20 & userDB.getHealth() > -1000){
            String[] mine = mine().split(" ");
            String type = mine[0];
            int num = Integer.parseInt(mine[1]);
            BigInteger price = new BigInteger(mine[2]);
            long addHealth = Long.parseLong(mine[3]);
            long exp = Long.parseLong(mine[4]);

            list.putIfAbsent(type, 0);
            list.put(type,list.get(type)+num);
            health += addHealth;
            all_price = all_price.add(price);
            addExp += exp;
        }

        String s = "";

        for(String type:list.keySet()){
            s += type+": "+list.get(type)+"개\n";
        }
        EmbedBuilder embed = new EmbedBuilder();
        embed.setColor(Support.getRandomColor());
        embed.setTitle(user.getName()+"님의 광질");
        embed.addField(Support.unit(all_price)+"베리","```"+s+"```",true);
        embed.setFooter("+"+addExp+" 경험치");
        channel.sendMessageEmbeds(embed.build()).queue();

    }

    public String mine(){
        UserDB userDB = new UserDB(userid);
        int addHealth = (int) (Math.random()*40+1)*-1;
        int addExp = (int) (Math.random()*20)*+5;
        double r = SaveNLoad.getRandom(path+"mine.txt");

        String type = "";
        int num = 0;
        double price = 1;

        switch ((int) r){
            case 1:
                type = "다이아";
                num = (int) (Math.random()*5);
                price = 0.5;
                break;
            case 2:
                type = "큰_사고";
                num = (int) (Math.random()*2);
                price = -0.2;
                break;
            case 3:
                type = "금";
                num = (int) (Math.random()*5);
                price = 0.1;
                break;
            case 5:
                type = "사고";
                num = (int) (Math.random()*2);
                price = -0.023;
                break;
            case 6:
                type = "철";
                num = (int) (Math.random()*5);
                price = 0.02;
                break;
            case 7:
                type = "석탄";
                num = (int) (Math.random()*5);
                price = 0.005;
                break;
            case 9:
                type = "돌";
                num = (int) (Math.random()*5);
                price = 0.003;
                break;
        }

        num++;
        BigDecimal bd_sp = new BigDecimal(SugoData.getSP());
        BigDecimal bd_p = new BigDecimal(String.valueOf(price));
        BigDecimal bd_num = new BigDecimal(String.valueOf(num));
        BigInteger addPrice = bd_sp.multiply(bd_p).multiply(bd_num).toBigInteger();
        userDB.addHealth(addHealth);
        userDB.addMoney(addPrice);
        userDB.addExp(addExp);
        SugoData.addMoney(addPrice.multiply(new BigInteger("-1")));

        return type+" "+num+" "+addPrice+" "+addHealth+" "+addExp;

    }

    public void Hard_Working(){
        if(!UserDB.isUser(userid)){
            channel.sendMessage(notUser).queue();
            return;
        }
        UserDB userDB = new UserDB(user);
        if(userDB.getHealth() < -1500){
            channel.sendMessage("건강이 부족합니다.").queue();
            return;
        }
        BigInteger all_price = BigInteger.ZERO;
        HashMap<String,Integer> lists = new HashMap<>();
        long health = 25;
        long addExp = 0;
        if(arg.length >= 2){
            if(Support.isLong(arg[1])){
                health = Long.parseLong(arg[1]);
            }
        }
        while (health>=25 & userDB.getHealth() > -1500){
            String[] list = Hard_Work().split(" ");
            long addHealth = Long.parseLong(list[0]);
            long exp = Long.parseLong(list[1]);
            BigInteger addPrice = new BigInteger(list[2]);
            String type = list[3];

            lists.putIfAbsent(type, 0);
            lists.put(type,lists.get(type)+1);
            health += addHealth;
            all_price = all_price.add(addPrice);
            addExp += exp;

        }
        String s = "";

        for(String type:lists.keySet()){
            s += type+": "+lists.get(type)+"개\n";
        }
        EmbedBuilder embed = new EmbedBuilder();
        embed.setColor(Support.getRandomColor());
        embed.setTitle(user.getName()+"님의 광질");
        embed.addField(Support.unit(all_price)+"베리","```"+s+"```",true);
        embed.setFooter("+"+addExp+" 경험치");
        channel.sendMessageEmbeds(embed.build()).queue();
    }

    public String Hard_Work(){
        UserDB userDB = new UserDB(userid);
        int addHealth = (int) ((Math.random()*165)+150)*-1;
        int addExp = (int) (Math.random()*10)*+5;
        String type = "고강도일";
        double p = Math.random()*1.125+1;
        int r = (int) (Math.random() * 10);
        if(r == 0){
            p *= -0.85;
            type = "병원비";
        }
        BigDecimal bd_sp = new BigDecimal(SugoData.getSP());
        BigDecimal bd_p = new BigDecimal(String.valueOf(p));
        BigInteger addPrice = bd_sp.multiply(bd_p).toBigInteger();

        userDB.addExp(addExp);
        userDB.addHealth(addHealth);
        userDB.addMoney(addPrice);
        SugoData.addMoney(addPrice.multiply(new BigInteger("-1")));

        return addHealth+" "+addExp+" "+addPrice+" "+type;
    }

}
