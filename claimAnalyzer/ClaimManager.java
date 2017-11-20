package claimAnalyzer;

import Utils.HTMLParser;
import Utils.StringUtils;
import lexicalAnalyzer.Token;

import java.util.ArrayList;

public class ClaimManager
{
    private ArrayList<Token> tokens;
    private Snippets originalSnippets;
    private Snippets modifiedSnippets;
    private StringUtils stringUtils = StringUtils.getInstance();

    private RulesTable rulesTable;

    public ClaimManager(ArrayList<Token> tokens)
    {
        this.tokens = tokens;
        rulesTable = new RulesTable();
    }

    public void getSnippets()
    {
        if(tokens.isEmpty())
        {
            System.out.println("No claims to search snippets!");
            return;
        }

        originalSnippets = new HTMLParser().getSnippets(stringUtils.tokensToString(tokens));

        System.out.println("\nORIGINAL SNIPPETS: \n");
        if(originalSnippets.getResults() > 0) originalSnippets.show();
        else System.out.println("0 results, no snippets!");

        if(originalSnippets.getResults() < 3)
        {
            System.out.println("\nMODIFIED SNIPPETS: \n");
            modifiedSnippets = new HTMLParser().getSnippets(modifierTokens());
            if(modifiedSnippets.getResults() > 0) modifiedSnippets.show();
            else System.out.println("0 results, no snippets!");
        }

    }

    private String modifierTokens()
    {
        System.out.println("\nBefore: " + stringUtils.tokensToString(tokens));
        rulesTable.transform(tokens);
        System.out.println("\nAfter: " + stringUtils.tokensToString(tokens));
        for(Token token : tokens)
        {
            token.switchText();
        }

        return stringUtils.tokensToString(tokens);
    }

}
