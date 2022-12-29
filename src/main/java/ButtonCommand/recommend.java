package ButtonCommand;

import SheepBC.BaseN;
import Supporter.Rank;
import Supporter.Support;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.interactions.components.buttons.Button;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class recommend {

    ButtonInteractionEvent event;
    User user;
    List<Button> buttons;
    String footer;

    public recommend(ButtonInteractionEvent event, User user, List<Button> buttons) {
        this.event = event;
        this.user = user;
        this.buttons = buttons;
        try {
            footer = event.getMessage().getEmbeds().get(0).getFooter().getText();
        }catch (Exception e){
            footer = "";
        }
    }
    public void rank(){
        String s = "";

        for (Button b:buttons){
            s += b.getId().replace("Rank","");
        }
        s += footer;
        String[] list = (String[]) Support.toObject(s);
        EmbedBuilder embed = new EmbedBuilder();
        embed.setTitle("추천");
        embed.setColor(Support.getRandomColor());

        int i = 1;
        for(String sr:list){
            String[] srs = sr.split("/.");
            BaseN baseM = new BaseN(Support.getListM());
            Rank rank = new Rank(srs[0],Double.parseDouble(baseM.BaseNToDecimal(srs[1])));
            if(!rank.getNumber().equals(BigDecimal.ZERO)){
                embed.addField(i+"위 ",rank.getname()+":"+ rank.getNumber(),true);
                i++;
            }
        }

        event.replyEmbeds(embed.build()).setEphemeral(true).queue();

    }
}
