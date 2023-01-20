package Command;

import Supporter.Stock_Data;
import Supporter.Support;
import Supporter.UserDB;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;
import java.util.Locale;

public class Stock_User {


    MessageReceivedEvent event;
    String[] arg;
    net.dv8tion.jda.api.entities.User user;
    String userid;
    MessageChannel channel;

    String notUser = "회원가입이 되어있지 않은 유저입니다.";

    public Stock_User(MessageReceivedEvent event, String[] args){

        this.event = event;
        this.arg = args;
        user = event.getAuthor();
        userid = user.getId();
        channel = event.getChannel();

    }

    public void user(){
        if(!UserDB.isUser(userid)){
            channel.sendMessage(notUser).queue();
            return;
        }
        UserDB userDB = new UserDB(user);
        EmbedBuilder embed = new EmbedBuilder();
        embed.setTitle(user.getName()+"의 주식");
        embed.setColor(Support.getRandomColor());
        for(String id: Stock_Data.list()){
            if(userDB.getStock_Num(id) != 0){
                Stock_Data stock_data = new Stock_Data(id);
                BigDecimal now = new BigDecimal(stock_data.getPrice()).multiply(new BigDecimal("100"));
                BigDecimal last = new BigDecimal(userDB.getStock_Price(id));
                BigDecimal up_P = now.divide(last,2, RoundingMode.HALF_EVEN).subtract(new BigDecimal("100"));
                String add = up_P.min(BigDecimal.ZERO).equals(BigDecimal.ZERO) ? "+":"";
                String value = "``` 개수: "+Support.unit(userDB.getStock_Num(id))+"개\n 주당구매 가격: "
                        +Support.unit(userDB.getStock_Price(id))+"베리"+"("+add+up_P+"%)"+"```";
                embed.addField(id,value,true);
            }
        }
        if(embed.getFields().size() == 0){
            channel.sendMessage("소유한 주식이 없습니다.").queue();
        }
        else {
            channel.sendMessageEmbeds(embed.build()).queue();
        }

    }

    public void Stock_Buy(){

        String command = "ㅅ매수 [id] [개수]";

        if(!UserDB.isUser(userid)){
            channel.sendMessage(notUser).queue();
            return;
        }
        if(arg.length < 3){
            channel.sendMessage(command).queue();
            return;
        }
        if(!Stock_Data.list().contains(arg[1].toUpperCase(Locale.ROOT)) || !Support.isLong(arg[2])){
            channel.sendMessage(command).queue();
            return;
        }

        String id = arg[1].toUpperCase(Locale.ROOT);
        double num = Double.parseDouble(arg[2]);

        EmbedBuilder embed = new EmbedBuilder();
        embed.setTitle(user.getName()+"님의 주식 매수");
        embed.setColor(Support.getRandomColor());
        embed.addField("매수내역","```"+buy(id,num)+"```",true);
        channel.sendMessageEmbeds(embed.build()).queue();
    }

    public void Stock_Buy_All(){

        String command = "ㅅ풀매수 [id]";

        if(!UserDB.isUser(userid)){
            channel.sendMessage(notUser).queue();
            return;
        }
        if(arg.length < 2){
            channel.sendMessage(command).queue();
            return;
        }
        if(!Stock_Data.list().contains(arg[1].toUpperCase(Locale.ROOT))){
            channel.sendMessage(command).queue();
            return;
        }

        String id = arg[1].toUpperCase(Locale.ROOT);
        double num = 100000000000000d;

        EmbedBuilder embed = new EmbedBuilder();
        embed.setTitle(user.getName()+"님의 주식 매수");
        embed.setColor(Support.getRandomColor());
        embed.addField("매수내역","```"+buy(id,num)+"```",true);
        channel.sendMessageEmbeds(embed.build()).queue();
    }

    public String buy(String id,double num){
        UserDB userDB = new UserDB(user);
        if(num > endLine(userDB,id)) num = endLine(userDB,id);
        BigInteger price = price(userDB,id,num);
        userDB.setStock_Price(id,price);
        userDB.addStock_Num(id,(int)(num));
        Stock_Data stock_data = new Stock_Data(id);
        BigInteger addMoney = stock_data.getPrice().multiply(new BigInteger(String.valueOf((int)(num*-1))));
        System.out.println(addMoney);
        userDB.addMoney(addMoney);
        stock_data.addSelling_Num(new BigInteger(String.valueOf((int)(num))));
        stock_data.addWeek_Selling_Price(new BigInteger(String.valueOf((int)(num))));
        String re = stock_data.getName()+"을(를) "+Support.unit(addMoney.multiply(new BigInteger("-1")))+"베리로 "+(int)(num)+"주 매수하였습니다.";
        return re;
    }

    public double endLine(UserDB userDB,String id){
        Stock_Data stock_data = new Stock_Data(id);
        double end1 = 0.7*stock_data.getLeftNum()-0.3*userDB.getStock_Num(id);
        if(end1 < 0) end1 = 0;
        double end2 = userDB.getMoney().divide(stock_data.getPrice()).doubleValue();
        return Math.min(end1,end2);
    }

    public BigInteger price(UserDB userDB,String id,double num){
        BigInteger user_num = new BigInteger(String.valueOf(userDB.getStock_Num(id)).replace(".0",""));
        BigInteger user = userDB.getStock_Price(id).multiply(user_num);
        Stock_Data stock_data = new Stock_Data(id);
        BigInteger stock_num = new BigInteger(String.valueOf((int) (num)));
        BigInteger stock = stock_data.getPrice().multiply(stock_num);
        user = user.add(stock);
        BigInteger numbi = user_num.add(stock_num);
        return user.divide(numbi);

    }

    public void Stock_Sell(){

        String command = "ㅅ매도 [id] [개수]";

        if(!UserDB.isUser(userid)){
            channel.sendMessage(notUser).queue();
            return;
        }
        if(arg.length < 3){
            channel.sendMessage(command).queue();
            return;
        }
        if(!Stock_Data.list().contains(arg[1].toUpperCase(Locale.ROOT)) || !Support.isLong(arg[2])){
            channel.sendMessage(command).queue();
            return;
        }

        String id = arg[1].toUpperCase(Locale.ROOT);
        double num = Double.parseDouble(arg[2]);

        EmbedBuilder embed = new EmbedBuilder();
        embed.setTitle(user.getName()+"님의 주식 매도");
        embed.setColor(Support.getRandomColor());
        embed.addField("매도내역","```"+sell(id,num)+"```",true);
        channel.sendMessageEmbeds(embed.build()).queue();
    }

    public void Stock_Sell_All(){

        String command = "ㅅ매도 [id]";

        if(!UserDB.isUser(userid)){
            channel.sendMessage(notUser).queue();
            return;
        }
        if(arg.length < 2){
            channel.sendMessage(command).queue();
            return;
        }
        if(!Stock_Data.list().contains(arg[1].toUpperCase(Locale.ROOT))){
            channel.sendMessage(command).queue();
            return;
        }

        String id = arg[1].toUpperCase(Locale.ROOT);
        double num = 100000000000000d;

        EmbedBuilder embed = new EmbedBuilder();
        embed.setTitle(user.getName()+"님의 주식 매도");
        embed.setColor(Support.getRandomColor());
        embed.addField("매도내역","```"+sell(id,num)+"```",true);
        channel.sendMessageEmbeds(embed.build()).queue();
    }


    public String sell(String id,double num){
        UserDB userDB = new UserDB(user);
        Stock_Data stock_data = new Stock_Data(id);
        if(num > userDB.getStock_Num(id)) num = userDB.getStock_Num(id);
        BigInteger price = stock_data.getPrice().multiply(new BigInteger(String.valueOf((int) (num))));
        userDB.addMoney(price);
        userDB.addStock_Num(id,num*-1);
        stock_data.addSelling_Num(new BigInteger(String.valueOf((int) (num*-1))));
        stock_data.addWeek_Selling_Price(new BigInteger(String.valueOf((int)(num*-1))));
        String re = stock_data.getName()+"을(를) "+Support.unit(price)+"베리로 "+(int)(num)+"주 매도하였습니다.";
        return re;
    }





}
