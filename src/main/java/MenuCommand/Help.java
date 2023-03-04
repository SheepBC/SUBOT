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
         String[] commandList = {"도움말","숩","잘가","안녕","멍청이","봇","링크","프로필","문의","평균유저그래프"};
         for(String s:commandList){
             embed.addField("**"+ Setting.getCommandHelp(s)+"**",Setting.getCommandInform(s),true);
         }
         event.replyEmbeds(embed.build()).setEphemeral(true).queue();

     }

    public void Recommend(){
        //event.reply("dsad").setEphemeral(true).queue();
        EmbedBuilder embed = new EmbedBuilder();
        embed.setTitle("추천 명령어");
        embed.setColor(Support.getRandomColor());
        embed.setFooter(footer);
        String[] commandList = {"게임추천","산추천","주사위","추천"};
        for(String s:commandList){
            embed.addField("**"+ Setting.getCommandHelp(s)+"**",Setting.getCommandInform(s),true);
        }
        event.replyEmbeds(embed.build()).setEphemeral(true).queue();

    }

    public void Number(){
        //event.reply("dsad").setEphemeral(true).queue();
        EmbedBuilder embed = new EmbedBuilder();
        embed.setTitle("숫자 명령어");
        embed.setColor(Support.getRandomColor());
        embed.setFooter(footer);
        String[] commandList = {"소수","인수분해","전개","근","연립"};
        for(String s:commandList){
            embed.addField("**"+ Setting.getCommandHelp(s)+"**",Setting.getCommandInform(s),true);
        }
        event.replyEmbeds(embed.build()).setEphemeral(true).queue();

    }

    public void User(){
        //event.reply("dsad").setEphemeral(true).queue();
        EmbedBuilder embed = new EmbedBuilder();
        embed.setTitle("유저 명령어");
        embed.setColor(Support.getRandomColor());
        embed.setFooter(footer);
        String[] commandList = {"회원가입","내정보","자산","건강","순위","유저정보","송금"
                ,"유저그래프","경제알람","대출","갚기","파산","출석","도둑질","세금"};
        for(String s:commandList){
            embed.addField("**"+ Setting.getCommandHelp(s)+"**",Setting.getCommandInform(s),true);
        }
        event.replyEmbeds(embed.build()).setEphemeral(true).queue();

    }

    public void Work_Garmb(){
        //event.reply("dsad").setEphemeral(true).queue();
        EmbedBuilder embed = new EmbedBuilder();
        embed.setTitle("일과 도박 명령어");
        embed.setColor(Support.getRandomColor());
        embed.setFooter(footer);
        String[] commandList = {"광질","고강도일","도박","주사위도박","슬롯머신","경험치도박"};
        for(String s:commandList){
            embed.addField("**"+ Setting.getCommandHelp(s)+"**",Setting.getCommandInform(s),true);
        }
        event.replyEmbeds(embed.build()).setEphemeral(true).queue();

    }

    public void Health(){
        //event.reply("dsad").setEphemeral(true).queue();
        EmbedBuilder embed = new EmbedBuilder();
        embed.setTitle("건강 명령어");
        embed.setColor(Support.getRandomColor());
        embed.setFooter(footer);
        String[] commandList = {"회복","그만회복","잠","그만잠"};
        for(String s:commandList){
            embed.addField("**"+ Setting.getCommandHelp(s)+"**",Setting.getCommandInform(s),true);
        }
        event.replyEmbeds(embed.build()).setEphemeral(true).queue();

    }

    public void Stock(){
        //event.reply("dsad").setEphemeral(true).queue();
        EmbedBuilder embed = new EmbedBuilder();
        embed.setTitle("주식 명령어");
        embed.setColor(Support.getRandomColor());
        embed.setFooter(footer);
        String[] commandList = {"주식","그래프","내주식","매수","풀매수","매도","풀메도"};
        for(String s:commandList){
            embed.addField("**"+ Setting.getCommandHelp(s)+"**",Setting.getCommandInform(s),true);
        }
        event.replyEmbeds(embed.build()).setEphemeral(true).queue();

    }

    public void Manager(){
        //event.reply("dsad").setEphemeral(true).queue();
        EmbedBuilder embed = new EmbedBuilder();
        embed.setTitle("개발자 명령어");
        embed.setColor(Support.getRandomColor());
        embed.setFooter(footer);
        String[] commandList = {"백업","봇끄기","사용횟수","공지"};
        for(String s:commandList){
            embed.addField("**"+ Setting.getCommandHelp(s)+"**",Setting.getCommandInform(s),true);
        }
        event.replyEmbeds(embed.build()).setEphemeral(true).queue();

    }

    public void School(){
        //event.reply("dsad").setEphemeral(true).queue();
        EmbedBuilder embed = new EmbedBuilder();
        embed.setTitle("학교 명령어");
        embed.setColor(Support.getRandomColor());
        embed.setFooter(footer);
        String[] commandList = {"학교","급식"};
        for(String s:commandList){
            embed.addField("**"+ Setting.getCommandHelp(s)+"**",Setting.getCommandInform(s),true);
        }
        event.replyEmbeds(embed.build()).setEphemeral(true).queue();

    }

    public void Sing(){
        EmbedBuilder embed = new EmbedBuilder();
        embed.setTitle("노래 명령어");
        embed.setColor(Support.getRandomColor());
        embed.setFooter(footer);
        String[] commandList = {"노래","끄기","다음","지금","목록"};
        for(String s:commandList){
            embed.addField("**"+ Setting.getCommandHelp(s)+"**",Setting.getCommandInform(s),true);
        }
        event.replyEmbeds(embed.build()).setEphemeral(true).queue();
    }


}
