package Main;

import ButtonCommand.recommend;
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
        if(id.startsWith("Rank")){
            recommend rc = new recommend(event,user,buttons);
            rc.rank();
        }
        if(id.startsWith("UserRank")){
            Command.User.FullRank(event,id.replace("UserRank",""));
        }
    }

}
