package net.gahfy.muslimcompanion.utils;

public class StringUtils {
    public static String convertToArabicNumber(int number){
        String finalString = "";
        if(number == 0){
            return new String(new byte[]{(byte) 0xD9, (byte) 0xA0});
        }
        while(number > 0){
            int secondByteValue = 0xA0 + number%10;
            finalString = new String(new byte[]{(byte) 0xD9,(byte) secondByteValue}).concat(finalString);
            number/=10;
        }
        return finalString;
    }
}
