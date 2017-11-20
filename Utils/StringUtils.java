package Utils;

import lexicalAnalyzer.Token;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StringUtils
{
    private static StringUtils stringUtilsInstance = null;
    private StringUtils()
    {

    }

    public static StringUtils getInstance()
    {
        if(stringUtilsInstance == null)
        {
            stringUtilsInstance = new StringUtils();
        }

        return stringUtilsInstance;
    }

    public int getIntFromString(String results)
    {
        int resultsQuantity = 0;
        String regInt = "\\d+(\\.\\d+)*";
        Pattern pInt = Pattern.compile(regInt);
        Matcher mInt = pInt.matcher(results);

        if(mInt.find())
        {
            resultsQuantity = Integer.parseInt(mInt.group().replaceAll("\\.", ""));
        }
        return resultsQuantity;
    }

    public String tokensToString(ArrayList<Token> tokens)
    {
        int i = 0;
        StringBuilder text = new StringBuilder();
        for(Token token : tokens)
        {
            if(token.getSpecification().equals("point")) text.deleteCharAt(text.length()-1);
            text.append(token.getText()).append("+");
            i++;
        }

        text.deleteCharAt(text.length()-1);
        return text.toString();
    }
}
