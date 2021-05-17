package me.shaakashee.collector.utils.texUtils;

import me.shaakashee.collector.model.Etikett;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;

public class TexWriter {

    public static final String NOTHING = "---";

    public static File writeCollectionToTex(ArrayList<Etikett> etiketts, String path){

        String tex = createTexString(etiketts);

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

    public static String createTexString(ArrayList<Etikett> etiketts){
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
                "\\usepackage[left=2cm,right=2cm,top=2cm,bottom=2cm]{geometry}\n" +
                "\\usepackage{qrcode}\n");

        ret.append("\\begin{document}\n");

        for (Etikett e: etiketts){
            ret.append(etikettToString(e));
        }

        ret.append("\\end{document}");

        return ret.toString();
    }

    public static String etikettToString(Etikett etikett){
        StringBuilder ret = new StringBuilder();

        ret.append("\\begin{multicols*}{2}\n" +
                "\\vfill\\null\n" +
                "\\begin{flushright}\n");
        if (etikett.getText() != null && etikett.getUrl() != null && etikett.getPageDate() != null) {
            ret.append("\\qrcode{" + etikett.getUrl() + "}\n");
        }
        ret.append("\\end{flushright}\n" +
                "\n" +
                "\\columnbreak\\vspace*{\\fill}\n" +
                "\\noindent");

        ret.append("\\textbf{Familie: } ");
        ret.append((etikett.getFam()!= null? etikett.getFam():NOTHING) + "\\\\\n");

        ret.append("\\textbf{Gattung: } ");
        ret.append((etikett.getGattung()!= null? etikett.getGattung():NOTHING) + "\\\\\n");

        ret.append("\\textbf{Art: } ");
        ret.append((etikett.getArt()!= null? etikett.getArt():NOTHING) + "\\\\\n");

        ret.append("\\textbf{Autor: } ");
        ret.append((etikett.getAutor()!= null? etikett.getAutor():NOTHING) + "\\\\\n");

        ret.append("\\textbf{Name: } ");
        ret.append((etikett.getName()!= null? etikett.getName():NOTHING) + "\\\\\n");

        ret.append("\\textbf{Fundort: } ");
        ret.append((etikett.getFundort()!= null? etikett.getFundort():NOTHING) + "\\\\\n");

        ret.append("\\textbf{Standort: } ");
        ret.append((etikett.getStandort()!= null? etikett.getStandort():NOTHING) + "\\\\\n");

        ret.append("\\textbf{Leg: } ");
        ret.append((etikett.getLeg()!= null? etikett.getLeg():NOTHING) + "\\\\\n");

        ret.append("\\textbf{Det: } ");
        ret.append((etikett.getDet()!= null? etikett.getDet():NOTHING) + "\\\\\n");

        ret.append("\\textbf{Datum: } ");
        ret.append( (etikett.getDate()!= null? etikett.getDate():NOTHING) + "\\\\\n");

        if (etikett.getText() != null && etikett.getUrl() != null && etikett.getPageDate() != null) {
            ret.append("{\\footnotesize ");
            ret.append(etikett.getText().trim());
            ret.append("\\footnote{" + formatURL(etikett.getUrl()) + " , von " + etikett.getPageDate() + "}\n}");
        }

        ret.append("\\end{multicols*}\n" +
                "\\newpage");

        return ret.toString();
    }

    public static String formatURL(String url){
        String formatted = url.replace("%", "\\%")
                .replace("_", "\\_");
        return formatted;
    }

}
