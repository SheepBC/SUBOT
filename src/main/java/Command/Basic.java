package Command;

import SugoData.SugoData;
import Supporter.Support;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.Emoji;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.interactions.components.selections.SelectMenu;

import java.awt.*;

public class Basic
{

    MessageReceivedEvent event;
    String userID;
    MessageChannel channel;

    public Basic(MessageReceivedEvent event) {
        this.event = event;
        userID = event.getAuthor().getId();
        channel = event.getChannel();
    }

    public void Help() {
        SelectMenu.Builder menu = SelectMenu.create("Help")
                .addOption("기본","Basic","기본적인 명령어", Emoji.fromUnicode("🍉"));

        if(userID.equalsIgnoreCase("336828850358976512"))
            menu.addOption("관리자","Manager","관리자 명령어",Emoji.fromUnicode("⚙"));

        channel.sendMessage("숩봇의 명령어를 알려줍니다.밑에서 선택해주세요!").setActionRow(menu.build()).queue();
    }

    public void randomMessage(String[] List){
        channel.sendMessage(List[(int) (Math.random()*List.length)]).queue();
    }

    public void botInform(JDA jda){
        EmbedBuilder embed = new EmbedBuilder();

        embed.setTitle("**수박고기**");
        embed.setColor(new Color(23, 94, 29, 255));
        embed.addField("**가진 돈**", Support.unit(SugoData.getMoney()) +"베리",true);
        embed.addField("**버전**",String.valueOf(SugoData.getVersion),true);
        embed.addField("**기본 세율**",SugoData.getTax()+"%",true);
        embed.addField("**접속한 서버**",jda.getGuilds().size()+"개",true);
        embed.setThumbnail("https://lh3.googleusercontent.com/a-/AOh14GgDzMM93Ku5VERrDuCFdGqgiTrL04yUOnbLdJMK=s600-k-no-rp-mo");
        embed.setFooter("제작자: 양불리");

        channel.sendMessageEmbeds(embed.build()).queue();

    }

    public void linK(){
        EmbedBuilder embed = new EmbedBuilder();

        embed.setTitle("수박고기 봇 초대링크", "https://discord.com/oauth2/authorize?client_id=852699897924026378&permissions=3533904&scope=bot");
        if((int) (Math.random()*5) == 0)
            embed.setAuthor("제작자 유튜브","https://www.youtube.com/channel/UC-V-itSa8D-IwX9_Uzq_L7w");
        channel.sendMessageEmbeds(embed.build()).queue();
    }

}
