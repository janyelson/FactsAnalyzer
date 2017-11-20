package claimAnalyzer;

import lexicalAnalyzer.Token;

import java.util.ArrayList;

public class RulesTable
{
    private String rule1 = "substantivo|pronome verbo|verbo";
    private String rule2 = "substantivo|proprio adjetivo|adjetivo";
    private String all;

    public void transform(ArrayList<Token> tokens)
    {
        StringBuilder allBuilder = new StringBuilder();
        for(Token token : tokens)
        {
            allBuilder.append(token.getAll()).append(" ");
        }

        all = allBuilder.toString();

        if(all.contains(rule1)) applyRule(tokens, 1);
        if(all.contains(rule2)) applyRule(tokens, 2);
    }

    private void applyRule(ArrayList<Token> tokens, int rule)
    {
        switch (rule)
        {
            case 1:
                if(all.indexOf("substantivo|pronome") == 0) tokens.remove(0);
                break;
            case 2:
                switchTokens(tokens.get(all.indexOf("substantivo|proprio")), tokens.get(all.indexOf("adjetivo|adjetivo")));
                break;
        }

    }

    private void switchTokens(Token tokenA, Token tokenB)
    {
        Token auxToken = tokenA;
        tokenA = tokenB;
        tokenB = auxToken;
    }

}
