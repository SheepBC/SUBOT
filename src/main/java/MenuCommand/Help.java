package MenuCommand;

import Main.Setting;
import Supporter.Support;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.events.interaction.component.SelectMenuInteractionEvent;

public class Help {

    MessageChannel channel;
    SelectMenuInteractionEvent event;

    String footer = "\uD83C\uDF49TIP 숩대신 ㅅ,시써도 작동됩니다.[]는 필수,()는 선택";

     public Help(SelectMenuInteractionEvent event){
        this.event = event;
        channel = event.getChannel();
     }

     public void Basic(){
         //event.reply("dsad").setEphemeral(true).queue();
         EmbedBuilder embed = new EmbedBuilder();
         embed.setTitle("기본 명령어");
         embed.setColor(Support.getRandomColor());
         embed.setFooter(footer);
         String[] commandList = {"도움말","숩","잘가","안녕","멍청이","봇","링크","프로필","설명서"};
         for(String s:commandList){
             embed.addField("**"+ Setting.getCommandHelp(s)+"**",Setting.getCommandInform(s),true);
         }
         event.replyEmbeds(embed.build()).setEphemeral(true).queue();

     }


}
