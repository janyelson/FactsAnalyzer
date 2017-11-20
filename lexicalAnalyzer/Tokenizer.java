package lexicalAnalyzer;

import com.sun.deploy.util.StringUtils;
import opennlp.tools.postag.POSModel;
import opennlp.tools.postag.POSTaggerME;
import opennlp.tools.tokenize.TokenizerME;
import opennlp.tools.tokenize.TokenizerModel;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class Tokenizer
{
    /*PROPERTIES*/
    private List<String> lines;
    private ArrayList<Token> tokens;
    private Integer lineNum;

    /*STRING*/
    private StringBuilder sb;
    private StringUtils su = new StringUtils();

    private TokenizerME tokenizerME;
    private POSTaggerME posTagger;


    public Tokenizer(List<String> lines, boolean DEBUG_MODE) throws IllegalArgumentException
    {
        if (lines == null)
        {
            throw new IllegalArgumentException("\"lines\" cannot be null.");
        }

        this.lines = lines;
        this.tokens = new ArrayList<Token>();
        this.lineNum = 0;

        try {
            InputStream tokenModelIn = new FileInputStream("C:\\apache-opennlp-1.8.3\\models\\pt-token.bin");
            InputStream posModelIn = new FileInputStream("C:\\apache-opennlp-1.8.3\\models\\AeliusMaxentOpenNLP.bin");
            TokenizerModel tokenModel = new TokenizerModel(tokenModelIn);
            tokenizerME = new TokenizerME(tokenModel);
            POSModel posModel = new POSModel(posModelIn);
            posTagger = new POSTaggerME(posModel);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Tokenizer(List<String> lines)
    {
        this(lines, false);
    }

    public Tokenizer()
    {
        this(new ArrayList<String>());
    }

    public void parse()
    {
        //Parsing
        for (String line : lines) {
            if (line.isEmpty()) {
                lineNum += 1;
                continue;
            }
            parseLine(line);
            lineNum += 1;
        }

    }

    private void parseLine(String line)
    {
        String tokensME[] = tokenizerME.tokenize(line);
        String tags[] = posTagger.tag(tokensME);

        for(int i = 0; i < tokensME.length; i++)
        {
            tokens.add(new Token(tokensME[i], tags[i], lineNum));
        }

    }

    public ArrayList<Token> getTokens() {
        return tokens;
    }

}
