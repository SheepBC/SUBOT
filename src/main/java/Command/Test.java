package Command;


import SugoData.SugoData;
import Supporter.Support;
import Supporter.UserDB;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.interactions.components.Modal;
import net.dv8tion.jda.api.interactions.components.text.TextInput;
import net.dv8tion.jda.api.interactions.components.text.TextInputStyle;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;


public class Test {

    MessageReceivedEvent event;
    MessageChannel channel;
    String[] args;
    User user;

    public Test(MessageReceivedEvent event, String[] args){
        this.event = event;
        channel = event.getChannel();
        this.args = args;
        user = event.getAuthor();
    }

    public Test(){

    }

    public void test1(){
        SugoData.charge_Tax();
        double before = Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory();
        before = before/1024/1024;
        System.gc();
        double after = Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory();
        after = after/1024/1024;
        System.out.println("-----------------");
        System.out.println(before+" "+after);
        System.out.println(after-before);
    }

    public void buttonTest(ButtonInteractionEvent event){
        TextInput textInput = TextInput.create("test","test",TextInputStyle.PARAGRAPH)
                .setPlaceholder("Enter a number")
                .build();
        TextInput operand2 = TextInput.create("operand2", "Operand 2", TextInputStyle.SHORT)
                .setPlaceholder("Enter a number")
                .setMinLength(1)
                .setRequired(true)
                .build();

        Modal.Builder modal = Modal.create("ii","김밥");
        //modal.addActionRow(operand2);
        modal.addActionRow(textInput);
        event.replyModal(modal.build()).queue();
    }
}
