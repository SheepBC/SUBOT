package Main;

import Command.Sing;
import Command.Stock;
import MenuCommand.Help;
import MenuCommand.School_Menu;
import MenuCommand.Stock_Menu;
import MenuCommand.User_Menu;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.events.interaction.component.SelectMenuInteractionEvent;

public class SelectMenu implements Runnable {

    SelectMenuInteractionEvent event;
    String option;
    String id;
    MessageChannel channel;

    public SelectMenu(SelectMenuInteractionEvent event){
        this.event = event;
        option = event.getValues().get(0);
        channel = event.getChannel();
        id = event.getComponentId();
    }


    @Override
    public void run() {

        if(id.equalsIgnoreCase("Help")){
            Help help = new Help(event);
            if(option.equalsIgnoreCase("Basic")){
                help.Basic();
            }
            if(option.equalsIgnoreCase("Recommend")){
                help.Recommend();
            }
            if(option.equalsIgnoreCase("Number")){
                help.Number();
            }
            if(option.equalsIgnoreCase("User")){
                help.User();
            }
            if(option.equalsIgnoreCase("Work_Garmb")){
                help.Work_Garmb();
            }
            if(option.equalsIgnoreCase("Health")){
                help.Health();
            }
            if(option.equalsIgnoreCase("Stock")){
                help.Stock();
            }
            if(option.equalsIgnoreCase("Manager")){
                help.Manager();
            }
            if(option.equalsIgnoreCase("School")){
                help.School();
            }
            if(option.equalsIgnoreCase("Sing")){
                help.Sing();
            }
        }
        if(id.equalsIgnoreCase("USerRank")){
            User_Menu user_menu = new User_Menu(event);
            user_menu.user_Rank();
        }
        if(id.equalsIgnoreCase("otherUser-Inform")){
            User_Menu user_menu = new User_Menu(event);
            user_menu.otherUser_Inform(option);
        }
        if(id.equalsIgnoreCase("Stock")){
            Stock_Menu.Tmi(option,event);
        }
        if(id.startsWith("Stock_Chart")){
            String dat = id.replace("Stock_Chart","");
            dat = dat.equalsIgnoreCase("") ? "전체":dat;
            Stock_Menu.chart(option,dat,event);
        }
        if(id.startsWith("meal")){
            School_Menu.meal(option,channel);
        }
        if(id.equalsIgnoreCase("School")){
            School_Menu.School(option,event);
        }
        if(id.equalsIgnoreCase("Sing")){
            Sing.menu(event,option);
        }

    }
}
