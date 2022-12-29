package Command;

import Supporter.Item_ID;
import Supporter.Support;
import Supporter.UserDB;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

public class Enhance {

    MessageReceivedEvent event;
    String[] arg;
    net.dv8tion.jda.api.entities.User user;
    String userid;
    MessageChannel channel;

    String notUser = "회원가입이 되어있지 않은 유저입니다.";

    public Enhance(MessageReceivedEvent event,String[] args){

        this.event = event;
        this.arg = args;
        user = event.getAuthor();
        userid = user.getId();
        channel = event.getChannel();

    }

    public Enhance(MessageChannel channel){
        this.channel = channel;
    }

    public void Enhance_Stone_Gambling(){
        String commend = "숩강화석도박 [개수]";

        if(!UserDB.isUser(userid)){
            channel.sendMessage(notUser).queue();
            return;
        }

        if(arg.length <2){
            channel.sendMessage(commend).queue();
            return;
        }

        if(!Support.isLong(arg[1])){
            channel.sendMessage(commend).queue();
            return;
        }

        long num = Long.parseLong(arg[1]);
        UserDB userDB = new UserDB(user);
       if(num > userDB.getItem_NUm(Item_ID.Upgrade_Stone)){
           channel.sendMessage("강화석 개수가 부족합니다.").queue();
           return;
       }

    }

}
