package Command;

import Main.Setting;
import SugoData.SugoData;
import Supporter.Support;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.Emoji;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.interaction.ModalInteractionEvent;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.interactions.components.Modal;
import net.dv8tion.jda.api.interactions.components.buttons.Button;
import net.dv8tion.jda.api.interactions.components.selections.SelectMenu;
import net.dv8tion.jda.api.interactions.components.text.TextInput;
import net.dv8tion.jda.api.interactions.components.text.TextInputStyle;

import java.awt.*;
import java.math.BigInteger;

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
                .addOption("기본","Basic","기본적인 명령어", Emoji.fromUnicode("🍉"))
                .addOption("추천","Recommend","추천또는 골라주는 명령어", Emoji.fromUnicode("😄"))
                .addOption("숫자","Number","숫자관련된 명령어", Emoji.fromUnicode("\uD83D\uDD22"))
                .addOption("유저","User","회원가입후 사용할수있는 명령어", Emoji.fromUnicode("\uD83D\uDC64"))
                .addOption("일과 도박","Work_Garmb","일과 도박 명령어", Emoji.fromUnicode("\uD83D\uDCB8"))
                .addOption("건강","Health","건강에 대한 명령어", Emoji.fromUnicode("\uD83C\uDFE3"))
                .addOption("주식","Stock","주식관련명령어", Emoji.fromUnicode("\uD83D\uDCC8"));

        if(userID.equalsIgnoreCase("336828850358976512"))
            menu.addOption("관리자","Manager","관리자 명령어",Emoji.fromUnicode("⚙"));

        channel.sendMessage("숩봇의 명령어를 알려줍니다.밑에서 선택해주세요!").setActionRow(menu.build()).queue();
    }

    public void randomMessage(String[] List){
        channel.sendMessage(List[(int) (Math.random()*List.length)]).queue();
    }

    public void botInform(JDA jda){
        EmbedBuilder embed = new EmbedBuilder();
        BigInteger sp = SugoData.getMoney().divide(SugoData.getSP());

        embed.setTitle("**수박고기**");
        embed.setColor(new Color(23, 94, 29, 255));
        embed.addField("**가진 돈**", Support.unit(SugoData.getMoney()) +"베리",true);
        embed.addField("**가진 돈(SP)**", Support.unit(sp) +"베리",true);
        embed.addField("**SP(기준 물가)**", Support.unit(SugoData.getSP()) +"베리",true);
        embed.addField("**어제 SP(기준 물가)**", Support.unit(SugoData.getLast_SP()) +"베리",true);
        embed.addField("**MRP**", String.valueOf(SugoData.getMRP()),true);
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

    public void Inquiry(){
        Button button = Button.primary("Inquiry", "문의하러가기");
        channel.sendMessage("문의를 하실려면 아래버튼을 눌러주세요!!!").setActionRow(button).queue();
    }

    public static void Button_Inquiry(ButtonInteractionEvent event){
        TextInput textInput = TextInput.create("text","문의 내용", TextInputStyle.PARAGRAPH)
                .setMaxLength(555)
                .setMinLength(3)
                .setPlaceholder("제돈이 사라졌었요!!!!")
                .build();

        Modal.Builder modal = Modal.create("Inquiry","문의");
        modal.addActionRow(textInput);
        event.replyModal(modal.build()).queue();
    }

    public static void Modal_Inquiry(ModalInteractionEvent event){
        JDA jda = event.getJDA();
        EmbedBuilder embed = new EmbedBuilder();
        embed.setColor(Support.getRandomColor());
        embed.setFooter(event.getUser().getId());
        embed.setTitle(event.getUser().getName()+"님의 문의");
        embed.addField("문의내용","```"+event.getValue("text").getAsString()+"```",true);
        event.reply("문의가 완료되었습니다.").queue();
        Button button = Button.primary("Inquiry_Reply", "답장하러가기");
        jda.getTextChannelById(Setting.getInquiry()).sendMessageEmbeds(embed.build()).setActionRow(button).queue();
        jda.retrieveUserById(event.getUser().getId()).flatMap(User::openPrivateChannel).queue(privateChannel -> privateChannel.sendMessageEmbeds(embed.build()).queue());
    }

}
