package syntaxAnalyzer;

import lexicalAnalyzer.Token;

import java.util.ArrayList;

public class SyntaxAnalyzer
{
    private ArrayList<Token> tokens;
    private Token currentToken;
    private static int count = -1;

    private int sentenceCout = 0;
    private ArrayList<Integer> discartedSetences;

    public SyntaxAnalyzer(ArrayList<Token> tokens)
    {
        this.tokens = tokens;
        discartedSetences = new ArrayList<>();
    }

    public SyntaxAnalyzer()
    {
        this(new ArrayList<>());
    }

    public void run() {
        System.out.println("Analyzing syntax...\n");

        while (hasToken()){
            try {
                textA();
            } catch (SyntaxException e) {
                System.out.println(e.getMessage());
                utilFinalPoint();
                discartedSetences.add(sentenceCout);
            }
        }
        System.out.println("\nFinhish analyzer Syntax.\n");

    }

    private void textA() throws SyntaxException
    {
        sentenceCout++;
        sentenceA();
        textB();
    }

    private void textB() throws SyntaxException
    {
        if(!hasToken() || currentToken.getText().equals("$")) return;

        currentToken = getNextToken();
        if(!currentToken.hasCategory("p.final"))
        {
            syntaxError("without final point");
        }
    }

    private void sentenceA() throws SyntaxException
    {
        conectivo();
        syntagmaNominalA();
        sentenceB();
    }

    private void sentenceB() throws SyntaxException
    {
        currentToken = getNextToken();
        if(currentToken.hasCategory("conectivo"))
        {
            syntagmaNominalA();
            sentenceB();
        }
        else
        {
            count--;
            syntagmaVerbalA();
            if(nextCategoryIs("conectivo")) sentenceB();
        }
    }

    private void syntagmaNominalA() throws SyntaxException
    {
        currentToken = getNextToken();
        if(currentToken.hasCategory("preposicao"))
        {
            currentToken = getNextToken();
            if(!currentToken.isNominal())
            {
                if(!currentToken.hasPossibleNominal()) syntaxError("in syntagma nominal structure");
            }
        }
        else if(!currentToken.isNominal())
        {
            count--;
            return;
        }

        count--;
        syntagmaNominalB();
    }

    private void syntagmaNominalB() throws SyntaxException
    {
        modifier();

        currentToken = getNextToken();
        if(currentToken.hasCategory("determinante"))
        {
            modifier();

            if(nextCategoryIs("adjetivo"))
            {
                qualifier();
                if(nextCategoryIs("substantivo"))
                {
                    noun();
                }
            }
            else
            {
                qualifier();
                noun();
            }

            qualifier();
        }
        else if(currentToken.hasCategory("substantivo"))
        {
            count--;
            noun();
            qualifier();
        }
        else if(currentToken.hasCategory("adjetivo"))
        {

            qualifier();
            conectivo();

            if(nextCategoryIs("substantivo"))
            {
                noun();
            }

            qualifier();
        }
        else
        {
            if(!(currentToken.hasPossibleNominal()))
            {
                syntaxError("in syntagma nominal structure");
            }

            syntagmaNominalB();

        }

        modifier();
    }

    private void syntagmaVerbalA() throws SyntaxException
    {
        verb();
        syntagmaVerbalB();
    }

    private void syntagmaVerbalB() throws SyntaxException
    {
        if(nextCategoryIs("p.final") || nextCategoryIs("$") || nextCategoryIs("verbo")) return;

        currentToken = getNextToken();
        if(!currentToken.getText().equals(",")) count--;
        conectivo();
        syntagmaNominalA();
    }

    private void verb() throws SyntaxException
    {
        modifier();

        currentToken = getNextToken();
        if(!(currentToken.hasCategory("verbo")))
        {
            if(!currentToken.hasPossibleCategory("verbo")) syntaxError("without verb");
        }

        currentToken = getNextToken();
        if(!(currentToken.hasCategory("verbo") || currentToken.hasCategory("participio"))) count--;

        modifier();
    }

    private void qualifier() throws SyntaxException
    {

        modifier();

        currentToken = getNextToken();
        if(!(currentToken.hasCategory("adjetivo"))) count--;

        currentToken = getNextToken();
        if(!(currentToken.hasCategory("adjetivo") || currentToken.hasCategory("participio"))) count--;

        modifier();
    }

    private void noun() throws SyntaxException
    {
        currentToken = getNextToken();
        if(!currentToken.hasCategory("substantivo"))
        {
            if(!currentToken.hasPossibleCategory("substantivo")) syntaxError("without noun");
        }


        if(currentToken.getSpecification().equals("proprio"))
        {
            qualifier();
            if(nextSpecificationIs("pronome")) return;

            int ret = nounConective();

            if(!nextSpecificationIs("proprio"))
            {
                count -= ret;
                return;
            }

            noun();
        }
    }

    private void modifier() throws SyntaxException
    {

        currentToken = getNextToken();
        if(!currentToken.hasCategory("adverbio"))
        {
            count--;
            return;
        }

        modifier();
    }

    private void conectivo() throws SyntaxException
    {
        currentToken = getNextToken();
        if(!currentToken.hasCategory("conectivo"))
        {
            count--;
            return;
        }

        if(currentToken.getSpecification().equals("pronome")) count--;

        modifier();
    }

    private int nounConective() throws SyntaxException
    {
        int ret = 0;

        currentToken = getNextToken();
        if(currentToken.hasCategory("conectivo"))
        {
            ret++;
        }
        else if(currentToken.hasCategory("preposicao"))
        {
            currentToken = getNextToken();
            if(currentToken.hasCategory("determinante")) ret++;
            else count--;
            ret++;
        }
        else
        {
            count--;
        }

        return ret;
    }

    private Token getNextToken()
    {
        count++;
        if (count >= tokens.size())
        {
            return (new Token("$", "$", currentToken.getLineNumber()));
        }

        return tokens.get(count);
    }

    private boolean nextCategoryIs(String category) throws SyntaxException
    {
        currentToken = getNextToken();
        count--;
        return currentToken.hasCategory(category);
    }

    private boolean nextSpecificationIs(String specification) throws SyntaxException
    {
        currentToken = getNextToken();
        count--;
        return currentToken.getSpecification().equals(specification);
    }

    private boolean hasToken()
    {
        return (count + 1) < tokens.size();
    }

    private void utilFinalPoint()
    {
        while(!(getNextToken().hasCategory("p.final") || getNextToken().getText().equals("$"))) {}
    }

    public ArrayList<Token> getResultTokens()
    {
        ArrayList<Token> t = new ArrayList<Token>();
        int i = 1;

        for(Token token : tokens)
        {
            if(!discartedSetences.contains(i)) t.add(token);
            if(token.hasCategory("p.final")) i++;
        }

        return t;
    }

    private void syntaxError(String errorMsg) throws SyntaxException
    {
        String str = "Syntax Error! Line " + currentToken.getLineNumber() + ": " +
                "\"" + currentToken.getText() + "\" " +
                errorMsg;
        throw new SyntaxException(str);
    }

}
