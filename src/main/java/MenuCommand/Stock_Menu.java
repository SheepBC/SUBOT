package MenuCommand;

import Command.Stock;
import Supporter.Stock_Data;
import Supporter.Support;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.events.interaction.component.SelectMenuInteractionEvent;
import net.dv8tion.jda.api.interactions.components.selections.SelectMenuInteraction;

import java.io.File;

public class Stock_Menu {

    static public void Tmi(String id, SelectMenuInteraction event){
        EmbedBuilder embed = new EmbedBuilder();
        Stock_Data stock_data = new Stock_Data(id);

        embed.setTitle(id+"의 주식정보");
        embed.setColor(Support.getRandomColor());
        embed.addField("현재 가격",stock_data.getPrice().toString(),true);
        embed.addField("이전 가격",stock_data.getLast_Price().toString(),true);
        String num = stock_data.getALl_Num().subtract(stock_data.getSelling_Num())+"/"+stock_data.getALl_Num();
        embed.addField("개수",num,true);
        embed.addField("시총",stock_data.getALl_Num().multiply(stock_data.getALl_Num()).toString(),true);
        embed.setFooter(stock_data.getTime()+"분/ "+stock_data.getAll_Time()+"분");
        event.replyEmbeds(embed.build()).setEphemeral(true).queue();

    }

    static public void chart(String id, String day,SelectMenuInteraction event){
        System.out.println(id+" "+day);
        String path = Stock.make_Chart(day,id);
        File file = new File(path);
        event.getChannel().sendFile(file).queue();
    }
}
