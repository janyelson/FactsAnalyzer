package claimAnalyzer;

public class Snippet
{
    private String text;
    private String font;

    public Snippet(String text, String font)
    {
        this.text = text;
        this.font = font;
    }

    public Snippet(String text)
    {
        this(text, "");
    }

    public Snippet()
    {
        this("");
    }

    public String getText()
    {
        return text;
    }

    public String getFont()
    {
        return font;
    }

    public String toString()
    {
        return "SNIPPET: " + text + '\n'
                + "FONT: " + font + '\n';
    }
}
