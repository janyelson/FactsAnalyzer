package lexicalAnalyzer;

import Utils.HTMLParser;

import java.util.ArrayList;
import java.util.List;

import static java.util.Arrays.asList;

public class Token
{
    /*PROPERTIES*/
    private String text;
    private Integer lineNumber;
    private String tag;
    private String category;
    private String specification;
    private String synonimous;
    private ArrayList<String> possiblesCategories;

    public Token(String text, String tag, Integer lineNumber)
    {
        this.text = text;
        this.tag = tag;
        this.lineNumber = lineNumber + 1;

        setCategory(tag);
        setPossiblesCategories();
        //setSynonimous();
    }

    private Token(String text, String tag)
    {
        this(text, tag, 0);
    }

    private Token(String text)
    {
        this(text, "UNC");
    }

    public Token()
    {
        this("", "");
    }

    public String getText()
    {
        return text;
    }

    public Integer getLineNumber()
    {
        return lineNumber;
    }

    public String getTagger()
    {
        return this.tag;
    }

    @Override
    public String toString()
    {
        return "TOKEN: " + text
                + '\n' + "TAG: " + tag
                + '\n' + "CLASSIFICATION: " + category
                + '\n' + "LINE: " + lineNumber;
    }

    public boolean hasPossibleCategory(String category) {

        if(category.equals("verbo"))
        {
            if(this.category.equals("determinante")) return false;
            if(this.category.equals("substantivo")) return false;
        }

        if(possiblesCategories.contains(category))
        {
            this.category = category;
            return true;
        }
        return false;
    }

    public String getCategory()
    {
        return this.category;
    }

    public boolean hasCategory(String category)
    {
        return this.category.equals(category);
    }

    private void setCategory(String tag)
    {
        switch(tag)
        {
            case "ART":
                this.category = "determinante";
                this.specification = "artigo";
                break;

            case "NUM":
                this.category = "determinante";
                this.specification = "numeral";
                break;

            case "ADJ": case "ADJ|EST":
                this.category = "adjetivo";
                this.specification = "adjetivo";
                break;

            case "PROADJ":
                this.category = "adjetivo";
                this.specification = "pronome";
                break;

            case "PCP":
                this.category = "participio";
                this.specification = "n.classificado";
                break;

            case "N": case "NPROP": case "N|EST":  case "N|DAT": case "N|HOR":
                this.category = "substantivo";
                this.specification = "proprio";
                break;

            case "PROSUB": case "PROPESS":
                this.category = "substantivo";
                this.specification = "pronome";
                break;

            case "ADV":
                this.category = "adverbio";
                this.specification = "adverbio";
                break;

            case "KC": case "KS":
                this.category = "conectivo";
                this.specification = "n.classificado";
                break;

            case "ADV-KS": case "ADV-KS-RE":
                this.category = "conectivo";
                this.specification = "adverbio";
                break;

            case "PRO-KS": case "PRO-KS-REL":
                this.category = "conectivo";
                this.specification = "pronome";
                break;

            case "PREP": case "PREP|+":
                this.category = "preposicao";
                this.specification = "preposicao";
                break;

            case "IN":
                this.category = "interjeicao";
                this.specification = "s.valor";
                break;

            case "V": case "VAUX": case "V|+":
                this.category = "verbo";
                this.specification = "verbo";
                break;

            case "PDEN":
                this.category = "denotativo";
                this.specification = "s.valor";
                break;

            case "CUR":
                this.category = "simbolo";
                this.specification = "simbolo";
                break;

            case ".": case "?": case "!":
                this.category = "p.final";
                this.specification = "point";
                break;

            case ",": case ";":
                this.category = "p.intermediario";
                this.specification = "point";
                break;

            default:
                this.category = "n.classificado";
                this.specification = "n.classificado";
        }
    }

    private void setPossiblesCategories()
    {
        if(category.equals("p.intermediario") || category.equals("p.final") || text.equals("$"))
        {
            possiblesCategories  = new ArrayList<String>();
            return;
        }

        possiblesCategories = new HTMLParser().getPartOfSpeech(this.text.toLowerCase());

        if(!possiblesCategories.isEmpty())
        {
            if(possiblesCategories.get(0).equals("preposicao"))category = possiblesCategories.get(0);
            if(possiblesCategories.get(0).equals("verbo") && category.equals("preposicao")) category = "verbo";
        }
    }

    private void setSynonimous()
    {
        if(category.equals("verbo") || tag.equals("N")) synonimous = new HTMLParser().getFirstSynonymous(text);
    }

    public boolean hasPossibleNominal()
    {
        return (hasPossibleCategory("determinante") || hasPossibleCategory("substantivo")
                || hasPossibleCategory("numeral") || hasPossibleCategory("adjetivo"));
    }

    public boolean isNominal()
    {
        return (this.category.equals("determinante") || this.category.equals("substantivo")
                || this.category.equals("numeral") || this.category.equals("adjetivo"));
    }

    public String getSpecification()
    {
        return specification;
    }

    public void switchText()
    {
        if(synonimous == null) return;

        String a = synonimous;
        synonimous = text;
        text = a;
    }

    public String getAll()
    {
        return category + "|" + specification;
    }


}
