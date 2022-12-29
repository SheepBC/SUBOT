package MenuCommand;

import Command.User;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.events.interaction.component.SelectMenuInteractionEvent;

public class User_Menu {

    MessageChannel channel;
    SelectMenuInteractionEvent event;
    String option;


    public User_Menu(SelectMenuInteractionEvent event){
        this.event = event;
        channel = event.getChannel();
        option = event.getValues().get(0);

    }

    public void user_Rank(){
        User user = new User(channel);
        user.User_Rank_System(option);
    }

    public void otherUser_Inform(String userid){
        User user = new User(channel);
        user.otherUser_inform_System(userid);
    }

}
