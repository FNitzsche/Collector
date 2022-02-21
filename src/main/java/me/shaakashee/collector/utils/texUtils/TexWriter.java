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
    public static final String NOTHINGSMALL = "\\line(1,0){75}\n \\end{spacing}";
    public static final String a6 = "a6paper";
    public static final String a5 = "a5paper";

    public static File writeCollectionToTex(ArrayList<Etikett> etiketts, String path, String documentclass, String borders,
                                            Boolean showTableOfContents, Boolean generateQRCodes, Boolean showFamilie, Boolean showGattung,
                                            Boolean showPlantDescription, Boolean showPageNumbers){

        String tex = createTexString(etiketts, documentclass, borders, showTableOfContents, generateQRCodes, showFamilie, showGattung, showPlantDescription, showPageNumbers);

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
                                         Boolean showPlantDescription, Boolean showPageNumbers){
        boolean small = documentclass.contains(a6) || documentclass.contains(a5);

        StringBuilder ret = new StringBuilder();

        ret.append("\\documentclass[12pt,a4paper]{article}\n" +
                "\\usepackage[utf8]{inputenc}\n" +
                "\\usepackage{fontspec}\n" +
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
                "\\usepackage{microtype}\n" +
                "\\usepackage[" + documentclass + ", " + borders + "]{geometry}\n" +
                "\\usepackage[hyphens]{url}\n" +
                "\\usepackage{qrcode}\n" +
                "\\setmainfont{DejaVu Serif}\n");

        ret.append("\\begin{document}\n");

        if (!showPageNumbers){
            ret.append("\\pagestyle{empty}\n");
        }

        if (showTableOfContents) {
            ret.append("\\tableofcontents\n");
            ret.append("\\newpage\n");
        }

        ret.append(collectionToString(small, etiketts, generateQRCodes, showFamilie, showGattung, showPlantDescription));

        ret.append("\\end{document}");

        return ret.toString();
    }

    public static String collectionToString(boolean small, ArrayList<Etikett> etiketts, Boolean generateQRCodes, Boolean showFamilie, Boolean showGattung, Boolean showPlantDescription){
        Group root = CollectionSorter.sortCollection(etiketts);

        StringBuilder collectionString = new StringBuilder();

        for (Group fam: root.getSortedChildren()){
            if (showFamilie) {
                collectionString.append("\\section{Familie: " + fam.name + "}\n");
                fam.requestText();
                if (fam.text != null && fam.url != null && fam.pagedate != null) {
                    collectionString.append(" \\glqq " + formatURL(fam.text.trim()) + " \\grqq ");
                    collectionString.append("\\footnote{woertlich von \\url{" + formatURL(WikiConnector.wiki + fam.url).trim() + "} , von " + fam.pagedate.trim() + "}\n");
                }
            }
            for (Group gattung: fam.getSortedChildren()){
                if (showGattung) {
                    collectionString.append("\\subsection{Gattung: " + gattung.name + "}\n");
                    gattung.requestText();
                    if (gattung.text != null && gattung.url != null && gattung.pagedate != null) {
                        collectionString.append(" \\glqq " + formatURL(gattung.text.trim()) + " \\grqq ");
                        collectionString.append("\\footnote{woertlich von \\url{" + formatURL(WikiConnector.wiki + gattung.url).trim() + "} , von " + gattung.pagedate.trim() + "}\n");
                    }
                }
                collectionString.append("\\newpage\n");
                for (Etikett e: gattung.getSortedLeaves()){
                    collectionString.append(etikettToString(small, e, generateQRCodes, showPlantDescription));
                }
            }
        }

        collectionString.append(createBib(root));

        return collectionString.toString();
    }

    public static String etikettToString(boolean small, Etikett etikett,  Boolean generateQRCodes, Boolean showPlantDescription){
        StringBuilder ret = new StringBuilder();

        String spacing = (small? "1":"2");
        String length = (small?NOTHINGSMALL:NOTHING);

        ret.append("\\begin{multicols*}{2}\n" +
                "\\vfill\\null\n" +
                "\\begin{flushright}\n");

        if (small){
            ret.append("\\begin{small}\n");
        }
        ret.append((etikett.getFundort()!= null?"":"\\begin{spacing}{" + spacing + "}\n"));
        ret.append("\\textbf{Fundort: \\\\} ");
        ret.append((etikett.getFundort()!= null? etikett.getFundort().replace("\"", "''") + "\\\\":length) + "\n");

        ret.append((etikett.getStandort()!= null?"":"\\begin{spacing}{" + spacing + "}\n"));
        ret.append("\\textbf{Standort: \\\\} ");
        ret.append((etikett.getStandort()!= null? etikett.getStandort().replace("\"", "''") + "\\\\":length) + "\n");

        ret.append((etikett.getLeg()!= null?"":"\\begin{spacing}{" + spacing + "}\n"));
        ret.append("\\textbf{Leg: \\\\} ");
        ret.append((etikett.getLeg()!= null? etikett.getLeg() + "\\\\":length) + "\n");

        ret.append((etikett.getDet()!= null?"":"\\begin{spacing}{" + spacing + "}\n"));
        ret.append("\\textbf{Det: \\\\} ");
        ret.append((etikett.getDet()!= null? etikett.getDet() + "\\\\":length) + "\n");

        ret.append((etikett.getDate()!= null?"":"\\begin{spacing}{" + spacing + "}\n"));
        ret.append("\\textbf{Datum: \\\\} ");
        ret.append( (etikett.getDate()!= null? etikett.getDate() + "\\\\":length) + "\n");

        ret.append("\\vspace{1em} \n");

        if (generateQRCodes && etikett.getText() != null && etikett.getUrl() != null && etikett.getPageDate() != null && etikett.getPageTitle() != null) {
            ret.append("\\qrcode{" + etikett.getUrl() + "}\n");
        }

        if (small){
            ret.append("\\end{small}\n");
        }

        ret.append("\\end{flushright}\n" +
                "\n" +
                "\\columnbreak\\vspace*{\\fill}\n" +
                "\\noindent");

        if (small){
            ret.append("\\begin{small}\n");
        }

        ret.append((etikett.getFam()!= null?"":"\\begin{spacing}{" + spacing + "}\n"));
        ret.append("\\textbf{Familie: \\\\} ");
        ret.append((etikett.getFam()!= null? etikett.getFam() + "\\\\":length) + "\n");

        ret.append((etikett.getGattung()!= null?"":"\\begin{spacing}{" + spacing + "}\n"));
        ret.append("\\textbf{Gattung: \\\\} ");
        ret.append((etikett.getGattung()!= null? etikett.getGattung() + "\\\\":length) + "\n");

        ret.append((etikett.getArt()!= null?"":"\\begin{spacing}{" + spacing + "}\n"));
        ret.append("\\textbf{Art: \\\\} ");
        ret.append((etikett.getArt()!= null? etikett.getArt() + "\\\\":length) + "\n");

        ret.append((etikett.getAutor()!= null?"":"\\begin{spacing}{" + spacing + "}\n"));
        ret.append("\\textbf{Autor: \\\\} ");
        ret.append((etikett.getAutor()!= null? etikett.getAutor() + "\\\\":length) + "\n");

        ret.append((etikett.getName()!= null?"":"\\begin{spacing}{" + spacing + "}\n"));
        ret.append("\\textbf{Name: \\\\} ");
        ret.append((etikett.getName()!= null? etikett.getName() + "\\\\":length) + "\n");

        if (small){
            ret.append("\\end{small}\n");
        }

        if (showPlantDescription && etikett.getText() != null && etikett.getUrl() != null && etikett.getPageDate() != null && etikett.getPageTitle() != null) {
            ret.append("{\\" + (small?"scriptsize":"footnotesize") + "\n ");
            ret.append("\\begin{spacing}{0.5} \n");
            ret.append(" \\glqq " + formatURL(etikett.getText().trim()) + " \\grqq ");
            ret.append("\\footnote{woertlich von \\url{" + formatURL(etikett.getUrl().trim()) + "} , von " + etikett.getPageDate().trim() + "}\n");
            ret.append("\\end{spacing}} \n");
        } else if (generateQRCodes && etikett.getText() != null && etikett.getUrl() != null && etikett.getPageDate() != null && etikett.getPageTitle() != null) {
            ret.append("\\vspace{5em}\n");
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
        ret.append("\\sloppy\n");
        ret.append("\\begin{enumerate}\n");

        bib.stream().sorted((s1, s2) -> s1.title.compareTo(s2.title)).forEach(s -> {
            ret.append("\\item ");
            ret.append("\\textit{" + formatURL(s.title) + "}, \\\\\n" +
                    " ");
            ret.append("\\url{" + formatURL(s.url) + "}\\\\\n aufgerufen am ");
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
