package Main;

import Command.Basic;
import Command.Maneger;
import net.dv8tion.jda.api.events.interaction.ModalInteractionEvent;

public class Modal_Event implements Runnable{

    ModalInteractionEvent event;
    String Modal_Id;

    public Modal_Event(ModalInteractionEvent event){
        this.event = event;
        Modal_Id = event.getModalId();
    }

    @Override
    public void run() {
        if(Modal_Id.equalsIgnoreCase("Inquiry")){
            Basic.Modal_Inquiry(event);
        }
        if(Modal_Id.startsWith("Inquiry_Reply")){
            Maneger.Modal_Inquiry_Reply(event);
        }
    }
}
