package org.hyperion.util.login;

/**
 * Created with IntelliJ IDEA.
 * User: Wasay
 * Date: 12/10/14
 * Time: 7:19 PM
 * To change this template use File | Settings | File Templates.
 */
public class StringUtils {

    public static String substring(final String original, final String start, final String end) {
        try {
            int endIndex = original.indexOf(end);
            if(end.equals("TO_THE_END"))
                endIndex = original.length();
            return original.substring(original.indexOf(start) + start.length(), endIndex);
        } catch (Exception e){
            //e.printStackTrace();
            return original;
        }
    }

}
