package claimAnalyzer;

import java.util.ArrayList;

public class Snippets
{
    private ArrayList<Snippet> snippets;
    private int results;

    public Snippets(ArrayList<Snippet> snippets, int results)
    {
        this.snippets = snippets;
        this.results = results;
    }

    public Snippets(ArrayList<Snippet> snippets)
    {
        this(snippets, 0);
    }

    public Snippets()
    {
        this(new ArrayList<Snippet>(), 0);
    }

    public Snippet getSnippet(int index)
    {
        return snippets.get(index);
    }

    public int getResults()
    {
        return results;
    }

    public void show()
    {
        System.out.println("QUANTITY: " + results);
        System.out.println("RESULTS: ");
        for(Snippet snippet : snippets)
        {
            System.out.println(snippet.toString());
        }
    }
}
