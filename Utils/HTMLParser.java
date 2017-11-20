package Utils;

import claimAnalyzer.Snippet;
import claimAnalyzer.Snippets;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;
import java.util.ArrayList;

public class HTMLParser
{
    private final String DICIO_URL = "https://www.dicio.com.br/";
    private final String GOOGLE_URL = "http://www.google.com/search?q=";
    private final String SYNONYMOUS_URL = "https://www.sinonimos.com.br/";

    private Document doc = null;

    public ArrayList<String> getPartOfSpeech(String word)
    {
        String content = "";
        String searchedWord = "";
        try
        {
            doc = Jsoup.connect(DICIO_URL + word).get();
            content = doc.body().getElementsByClass("adicional").text();
            searchedWord = doc.getElementsByClass("tit-section").text();
        } catch (IOException e) {
            System.out.println("Request to dicio for \"" + word + "\" was not a success");
        }

        if(!searchedWord.toLowerCase().contains(word)) return new ArrayList<String>();


        return translateCategories(content);
    }

    public Snippets getSnippets(String text)
    {
        int resultsQuantity = 0;
        String results = "";
        ArrayList<String> texts = null;
        ArrayList<String> fonts = null;
        ArrayList<Snippet> snippets = new ArrayList<>();

        try
        {
            doc = Jsoup.connect(GOOGLE_URL + "\"" + text + "\"").get();
            results = doc.getElementsByClass("med").text();

            if(!(results.contains("nenhum documento") || results.contains("Nenhum resultado")))
            {
                resultsQuantity = StringUtils.getInstance().getIntFromString(doc.getElementById("resultStats").text());
                texts = (ArrayList<String>) doc.getElementsByClass("st").eachText();
                fonts = (ArrayList<String>) doc.getElementsByClass("_Rm").eachText();

                for(int i = 0; i < fonts.size(); i++)
                {
                    snippets.add(new Snippet(texts.get(i), fonts.get(i)));
                }
            }

            return new Snippets(snippets, resultsQuantity);

        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Request to google/search was not a success");
        }

        return new Snippets(snippets, resultsQuantity);
    }

    private ArrayList<String> translateCategories(String content)
    {
        ArrayList<String> arrayList = new ArrayList<>();
        if(content.contains("substantivo")) arrayList.add("substantivo");
        if(content.contains("adjetivo")) arrayList.add("adjetivo");
        if(content.contains("adverbio")) arrayList.add("adverbio");
        if(content.contains("artigo")) arrayList.add("determinante");
        if(content.contains("pronome")) arrayList.add("substantivo");
        if(content.contains("verbo")) arrayList.add("verbo");
        if(content.contains("numeral")) arrayList.add("numeral");
        if(content.contains("interjeção")) arrayList.add("interjecao");
        if(content.contains("preposição")) arrayList.add("preposicao");
        if(content.contains("contração")) arrayList.add("preposicao");
        if(content.contains("símb")) arrayList.add("simbolo");
        if(content.contains("conjução")) arrayList.add("conectivo");
        return arrayList;

    }

    public String getFirstSynonymous(String word)
    {
        String synonymous = "";

        try
        {
            doc = Jsoup.connect(SYNONYMOUS_URL + word.toLowerCase()).get();
            synonymous = doc.body().getElementsByClass("sinonimo").get(0).text();
        } catch (IOException e) {
            System.out.println("HTML Parser ERROR: Request to sinonimos for \"" + word + "\" was not a success");
        }

        return synonymous;
    }
}
