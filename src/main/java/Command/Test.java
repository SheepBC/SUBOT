package Command;

import Supporter.Item_ID;
import Supporter.Stock_Data;
import Supporter.UserDB;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

public class Test {

    MessageReceivedEvent event;
    MessageChannel channel;
    String[] args;

    public Test(MessageReceivedEvent event, String[] args){
        this.event = event;
        channel = event.getChannel();
        this.args = args;
    }

    public void test1(){
        System.exit(0);
    }
}
