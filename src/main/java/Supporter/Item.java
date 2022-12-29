package Supporter;

import java.lang.reflect.Field;

public class Item {

    public String English_Name;
    public String Korean_Name;

    public Item(String English_Name,String Korean_Name){
        this.English_Name = English_Name;
        this.Korean_Name = Korean_Name;
    }

    public static Item find_Item(String item_English_Name){
        for(Field f:Item_ID.class.getFields()){
            if(f.getName().equals(item_English_Name)){
                try {
                    return (Item) f.get(new Item("",""));
                } catch (IllegalAccessException e) {
                    throw new RuntimeException(e);
                }
            }
        }
        return new Item("-1","-1");
    }

}
