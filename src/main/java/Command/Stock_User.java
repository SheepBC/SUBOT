package Command;

import Supporter.Stock_Data;
import Supporter.Support;
import Supporter.UserDB;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

import java.math.BigInteger;

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
                embed.addField(id,userDB.getStock_Num(id)+"개",true);
            }
            userDB.addStock_Num(id,1);
        }
        if(embed.getFields().size() == 0){
            channel.sendMessage("소유한 주식이 없습니다.").queue();
        }
        else {
            channel.sendMessageEmbeds(embed.build()).queue();
        }

    }





}
