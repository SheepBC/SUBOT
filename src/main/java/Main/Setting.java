package Main;

import Supporter.SaveNLoad;

public class Setting {

    public static String getBasic(String key){
        return SaveNLoad.loadP("Setting/Basic.txt",key);
    }

    public static String getPythonPath(String path){
        String dir = System.getProperty("user.dir");
        String pp = SaveNLoad.loadP("Setting/PythonPath.txt",path);
        return dir+"/Python/"+pp;
    }

    public static String getCommandHelp(String command){
        return SaveNLoad.loadP("Setting/Commands/Help.txt",command);
    }

    public static String getCommandInform(String command){
        return SaveNLoad.loadP("Setting/Commands/Inform.txt",command);
    }

}
