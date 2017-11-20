import claimAnalyzer.ClaimManager;
import lexicalAnalyzer.Token;
import lexicalAnalyzer.Tokenizer;
import syntaxAnalyzer.SyntaxAnalyzer;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class Test {
    public static void main(String args[])
    {

        Tokenizer tokenizer = null;
        SyntaxAnalyzer syntaxAnalyzer = null;
        ClaimManager claimManager = null;

        try {
            tokenizer = new Tokenizer(Files.readAllLines(Paths.get("Test")), true);
            tokenizer.parse();

            for (Token t : tokenizer.getTokens())
            {
                System.out.println(t + "\n");
            }

            syntaxAnalyzer = new SyntaxAnalyzer(tokenizer.getTokens());
            syntaxAnalyzer.run();

            //claimManager = new ClaimManager(syntaxAnalyzer.getResultTokens());
            //claimManager.getSnippets();

        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
