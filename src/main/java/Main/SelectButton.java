package Main;

import ButtonCommand.recommend;
import Command.Basic;
import Command.Maneger;
import Command.Test;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageReference;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.interactions.components.buttons.Button;

import java.util.List;

public class SelectButton implements Runnable{

    ButtonInteractionEvent event;
    String id;
    List<Button> buttons;
    User user;
    public SelectButton(ButtonInteractionEvent event){
        this.event = event;
        id = event.getButton().getId();
        buttons = event.getMessage().getButtons();
        user = event.getUser();
    }

    @Override
    public void run() {
        maneger();
        if(id.startsWith("Rank")){
            recommend rc = new recommend(event,user,buttons);
            rc.rank();
        }
        if(id.startsWith("UserRank")){
            Command.User.FullRank(event,id.replace("UserRank",""));
        }
        if(id.equalsIgnoreCase("Inquiry")){
            Basic.Button_Inquiry(event);
        }
        if(id.startsWith("UserBankruptcy")){
            Command.User.Button_bankruptcy(event);
        }

    }

    void maneger(){
        if(!event.getUser().getId().equalsIgnoreCase("336828850358976512")) return;
        if(id.equalsIgnoreCase("Inquiry_Reply")){
            Maneger.Button_Inquiry_Reply(event);
        }
        if(id.equalsIgnoreCase("notice")){
            Maneger.notice_Button(event);
        }
    }

}
