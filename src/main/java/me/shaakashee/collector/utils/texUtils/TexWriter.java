package me.shaakashee.collector.utils.texUtils;

import me.shaakashee.collector.model.Etikett;
import me.shaakashee.collector.model.Group;
import me.shaakashee.collector.utils.collectionUtils.CollectionSorter;
import me.shaakashee.collector.utils.wikiUtils.WikiConnector;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;

public class TexWriter {

    public static final String NOTHING = "\\line(1,0){150}\n \\end{spacing}";

    public static File writeCollectionToTex(ArrayList<Etikett> etiketts, String path, String documentclass, String borders,
                                            Boolean showTableOfContents, Boolean generateQRCodes, Boolean showFamilie, Boolean showGattung,
                                            Boolean showPageNumbers){

        String tex = createTexString(etiketts, documentclass, borders, showTableOfContents, generateQRCodes, showFamilie, showGattung, showPageNumbers);

        File ret = createFile(path);

        try {
            Writer out = new BufferedWriter(new OutputStreamWriter(
                    new FileOutputStream(path), "UTF-8"));
            out.write(tex);
            out.close();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return ret;
    }

    public static File createFile(String path){
        try {
            File myObj = new File(path);
            if (myObj.createNewFile()) {
                System.out.println("File created: " + myObj.getName());
            } else {
                System.out.println("File already exists.");
            }
            return myObj;
        } catch (IOException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
            return null;
        }
    }

    public static String createTexString(ArrayList<Etikett> etiketts, String documentclass, String borders,
                                         Boolean showTableOfContents, Boolean generateQRCodes, Boolean showFamilie, Boolean showGattung,
                                         Boolean showPageNumbers){
        StringBuilder ret = new StringBuilder();

        ret.append("\\documentclass[12pt,a4paper]{article}\n" +
                "\\usepackage[utf8]{inputenc}\n" +
                "\\usepackage[german]{babel}\n" +
                "\\usepackage[T1]{fontenc}\n" +
                "\\usepackage{amsmath}\n" +
                "\\usepackage{amsfonts}\n" +
                "\\usepackage{amssymb}\n" +
                "\\usepackage{makeidx}\n" +
                "\\usepackage{graphicx}\n" +
                "\\usepackage{lmodern}\n" +
                "\\usepackage{kpfonts}\n" +
                "\\usepackage{multicol}\n" +
                "\\usepackage{setspace}\n" +
                "\\usepackage[" + documentclass + ", " + borders + "]{geometry}\n" +
                "\\usepackage{qrcode}\n");

        ret.append("\\begin{document}\n");

        if (!showPageNumbers){
            ret.append("\\pagestyle{empty}\n");
        }

        if (showTableOfContents) {
            ret.append("\\tableofcontents\n");
            ret.append("\\newpage\n");
        }

        ret.append(collectionToString(etiketts, generateQRCodes, showFamilie, showGattung));

        ret.append("\\end{document}");

        return ret.toString();
    }

    public static String collectionToString(ArrayList<Etikett> etiketts, Boolean generateQRCodes, Boolean showFamilie, Boolean showGattung){
        Group root = CollectionSorter.sortCollection(etiketts);

        StringBuilder collectionString = new StringBuilder();

        for (Group fam: root.getSortedChildren()){
            if (showFamilie) {
                collectionString.append("\\section{Familie: " + fam.name + "}\n");
                fam.requestText();
                if (fam.text != null && fam.url != null && fam.pagedate != null) {
                    collectionString.append(" \\glqq " + formatURL(fam.text.trim()) + " \\grqq ");
                    collectionString.append("\\footnote{woertlich von " + formatURL(WikiConnector.wiki + fam.url).trim() + " , von " + fam.pagedate.trim() + "}\n");
                }
            }
            for (Group gattung: fam.getSortedChildren()){
                if (showGattung) {
                    collectionString.append("\\subsection{Gattung: " + gattung.name + "}\n");
                    gattung.requestText();
                    if (gattung.text != null && gattung.url != null && gattung.pagedate != null) {
                        collectionString.append(" \\glqq " + formatURL(gattung.text.trim()) + " \\grqq ");
                        collectionString.append("\\footnote{woertlich von " + formatURL(WikiConnector.wiki + gattung.url).trim() + " , von " + gattung.pagedate.trim() + "}\n");
                    }
                }
                collectionString.append("\\newpage\n");
                for (Etikett e: gattung.getSortedLeaves()){
                    collectionString.append(etikettToString(e, generateQRCodes));
                }
            }
        }

        collectionString.append(createBib(root));

        return collectionString.toString();
    }

    public static String etikettToString(Etikett etikett,  Boolean generateQRCodes){
        StringBuilder ret = new StringBuilder();

        ret.append("\\begin{multicols*}{2}\n" +
                "\\vfill\\null\n" +
                "\\begin{flushright}\n");

        ret.append((etikett.getFundort()!= null?"":"\\begin{spacing}{2}\n"));
        ret.append("\\textbf{Fundort: } ");
        ret.append((etikett.getFundort()!= null? etikett.getFundort() + "\\\\":NOTHING) + "\n");

        ret.append((etikett.getStandort()!= null?"":"\\begin{spacing}{2}\n"));
        ret.append("\\textbf{Standort: } ");
        ret.append((etikett.getStandort()!= null? etikett.getStandort() + "\\\\":NOTHING) + "\n");

        ret.append((etikett.getLeg()!= null?"":"\\begin{spacing}{2}\n"));
        ret.append("\\textbf{Leg: } ");
        ret.append((etikett.getLeg()!= null? etikett.getLeg() + "\\\\":NOTHING) + "\n");

        ret.append((etikett.getDet()!= null?"":"\\begin{spacing}{2}\n"));
        ret.append("\\textbf{Det: } ");
        ret.append((etikett.getDet()!= null? etikett.getDet() + "\\\\":NOTHING) + "\n");

        ret.append((etikett.getDate()!= null?"":"\\begin{spacing}{2}\n"));
        ret.append("\\textbf{Datum: } ");
        ret.append( (etikett.getDate()!= null? etikett.getDate() + "\\\\":NOTHING) + "\n");

        ret.append("\\vspace{1em} \n");

        if (generateQRCodes && etikett.getText() != null && etikett.getUrl() != null && etikett.getPageDate() != null && etikett.getPageTitle() != null) {
            ret.append("\\qrcode{" + etikett.getUrl() + "}\n");
        }
        ret.append("\\end{flushright}\n" +
                "\n" +
                "\\columnbreak\\vspace*{\\fill}\n" +
                "\\noindent");

        ret.append((etikett.getFam()!= null?"":"\\begin{spacing}{2}\n"));
        ret.append("\\textbf{Familie: } ");
        ret.append((etikett.getFam()!= null? etikett.getFam() + "\\\\":NOTHING) + "\n");

        ret.append((etikett.getGattung()!= null?"":"\\begin{spacing}{2}\n"));
        ret.append("\\textbf{Gattung: } ");
        ret.append((etikett.getGattung()!= null? etikett.getGattung() + "\\\\":NOTHING) + "\n");

        ret.append((etikett.getArt()!= null?"":"\\begin{spacing}{2}\n"));
        ret.append("\\textbf{Art: } ");
        ret.append((etikett.getArt()!= null? etikett.getArt() + "\\\\":NOTHING) + "\n");

        ret.append((etikett.getAutor()!= null?"":"\\begin{spacing}{2}\n"));
        ret.append("\\textbf{Autor: } ");
        ret.append((etikett.getAutor()!= null? etikett.getAutor() + "\\\\":NOTHING) + "\n");

        ret.append((etikett.getName()!= null?"":"\\begin{spacing}{2}\n"));
        ret.append("\\textbf{Name: } ");
        ret.append((etikett.getName()!= null? etikett.getName() + "\\\\":NOTHING) + "\n");

        if (etikett.getText() != null && etikett.getUrl() != null && etikett.getPageDate() != null && etikett.getPageTitle() != null) {
            ret.append("{\\footnotesize \n ");
            ret.append("\\begin{spacing}{0.5} \n");
            ret.append(" \\glqq " + formatURL(etikett.getText().trim()) + " \\grqq ");
            ret.append("\\footnote{woertlich von " + formatURL(etikett.getUrl().trim()) + " , von " + etikett.getPageDate().trim() + "}\n");
            ret.append("\\end{spacing}} \n");
        }

        ret.append("\\end{multicols*}\n" +
                "\\newpage\n");

        return ret.toString();
    }

    public static String formatURL(String url){
        String formatted = url.replace("%", "\\%")
                .replace("_", "\\_");
        return formatted;
    }

    public static String createBib(Group root){
        ArrayList<Source> bib = new ArrayList<>();

        for (Group fam: root.getSortedChildren()){
            if (fam.text != null && fam.url != null && fam.pagedate != null) {
                Source source = new Source();
                source.url = WikiConnector.wiki + fam.url;
                source.date = fam.pagedate;
                source.title = fam.name;
                bib.add(source);
            }
            for (Group gattung: fam.getSortedChildren()){
                if (gattung.text != null && gattung.url != null && gattung.pagedate != null) {
                    Source source = new Source();
                    source.url = WikiConnector.wiki + gattung.url;
                    source.date = gattung.pagedate;
                    source.title = gattung.name;
                    bib.add(source);
                }

                for (Etikett e: gattung.getSortedLeaves()){
                    if (e.getText() != null && e.getPageTitle() != null && e.getPageDate() != null && e.getUrl() != null){
                        Source source = new Source();
                        source.url = e.getUrl();
                        source.date = e.getPageDate();
                        source.title = e.getPageTitle();
                        bib.add(source);
                    }
                }
            }
        }

        StringBuilder ret = new StringBuilder();

        ret.append("\\section{Literaturverzeichnis}\n");
        ret.append("\\begin{enumerate}\n");

        bib.stream().sorted((s1, s2) -> s1.title.compareTo(s2.title)).forEach(s -> {
            ret.append("\\item ");
            ret.append("\\textit{" + formatURL(s.title) + "}, ");
            ret.append(formatURL(s.url) + "\\\\\n aufgerufen am ");
            ret.append(s.date + "\\\\\n");
        });

        ret.append("\\end{enumerate}");

        return ret.toString();
    }

    static class Source{
        public String url;
        public String date;
        public String title;
    }

}
