package app.model;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import javafx.scene.control.CheckBox;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.math.BigInteger;
import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class Actions {

    private static String hexToBin(String s) {
        return new BigInteger(s, 16).toString(2);
    }

    private static String binToHex(String s) {
        return new BigInteger(s, 2).toString(16);
    }

    private Path getApplicationStartUp() {
        Path path;
        URL startupUrl = getClass().getProtectionDomain().getCodeSource().getLocation();
        try {
            path = Paths.get(startupUrl.toURI());
        } catch (Exception e) {
            try {
                path = Paths.get(new URL(startupUrl.getPath()).getPath());
            } catch (Exception ipe) {
                path = Paths.get(startupUrl.getPath());
            }
        }
        return path.getParent();
    }

    public static String fromCheckBoxesToHex(List<CheckBox> allCheckBoxes){
        String binString = "";
        for (int i = allCheckBoxes.size() - 1; i >= 0 ; i--) {
            if(allCheckBoxes.get(i).isSelected()){
                binString = binString.concat("1");
            }else {
                binString = binString.concat("0");
            }
        }
        String hex = String.format("%8s", binToHex(binString)).replace(" ", "0");
        System.out.println(binString + " --> " + hex);
        return hex;
    }

    public static String fromHexToCheckBoxes(String hexAccessMask, List<CheckBox> allCheckBoxes){
        String binString = String.format("%32s", hexToBin(hexAccessMask)).replace(" ", "0");
        int iterator = 0;
        for (int i = binString.length() - 1; i >= 0 ; i--) {
            if(binString.charAt(i) == 48){
                allCheckBoxes.get(iterator).setSelected(false);
            }else {
                allCheckBoxes.get(iterator).setSelected(true);
            }

            if(iterator < 50){
                iterator++;
            }else break;
        }
        System.out.println(hexAccessMask + " -->" + binString);
        return binString;
    }

    public Map getCollectionFromJson(String fileName, String collection) throws FileNotFoundException {
        Path path = getApplicationStartUp();
        System.out.println(path);
        FileReader reader = new FileReader(path + File.separator + fileName);
        Gson gson = new Gson();
        JsonParser jp = new JsonParser();
        JsonObject jo = jp.parse(reader).getAsJsonObject();
        return gson.fromJson(jo.get(collection).getAsJsonObject(), HashMap.class);
    }

}
