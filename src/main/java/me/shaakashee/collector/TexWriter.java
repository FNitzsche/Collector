package me.shaakashee.collector;

import me.shaakashee.collector.model.Etikett;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

public class TexWriter {

    public static boolean writeCollectionToTex(ArrayList<Etikett> etiketts, String path){

        String tex = createTexString(etiketts);

        createFile(path);
        try {
            FileWriter myWriter = new FileWriter(path+".tex");
            myWriter.write(tex);
            myWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return true;
    }

    public static boolean createFile(String path){
        try {
            File myObj = new File(path + ".tex");
            if (myObj.createNewFile()) {
                System.out.println("File created: " + myObj.getName());
            } else {
                System.out.println("File already exists.");
            }
        } catch (IOException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
            return false;
        }
        return true;
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
                "\\usepackage[usestackEOL]{stackengine}\n" +
                "\\usepackage{hyperref}\n");

        ret.append("\\begin{document}\n");

        for (Etikett e: etiketts){
            ret.append(etikettToString(e));
        }

        ret.append("\\end{document}");

        return ret.toString();
    }

    public static String etikettToString(Etikett etikett){
        StringBuilder ret = new StringBuilder();

        ret.append("\\vspace*{\\fill}\n");
        ret.append("{\\raggedleft\\vspace*{\\fill}\\Longstack[l]{\n");

        ret.append("\\textbf{Familie: } ");
        ret.append((etikett.getFam().length() > 35? "\\\\\n":"") + etikett.getFam() + "\\\\\n");

        ret.append("\\textbf{Gattung: } ");
        ret.append((etikett.getGattung().length() > 35? "\\\\\n":"") + etikett.getGattung() + "\\\\\n");

        ret.append("\\textbf{Art: } ");
        ret.append((etikett.getArt().length() > 35? "\\\\\n":"") + etikett.getArt() + "\\\\\n");

        ret.append("\\textbf{Autor: } ");
        ret.append((etikett.getAutor().length() > 35? "\\\\\n":"") + etikett.getAutor() + "\\\\\n");

        ret.append("\\textbf{Name: } ");
        ret.append((etikett.getName().length() > 35? "\\\\\n":"") + etikett.getName() + "\\\\\n");

        ret.append("\\textbf{Fundort: } ");
        ret.append((etikett.getFundort().length() > 35? "\\\\\n":"") + etikett.getFundort() + "\\\\\n");

        ret.append("\\textbf{Standort: } ");
        ret.append((etikett.getStandort().length() > 35? "\\\\\n":"") + etikett.getStandort() + "\\\\\n");

        ret.append("\\textbf{Leg: } ");
        ret.append((etikett.getLeg().length() > 35? "\\\\\n":"") + etikett.getLeg() + "\\\\\n");

        ret.append("\\textbf{Det: } ");
        ret.append((etikett.getDet().length() > 35? "\\\\\n":"") + etikett.getDet() + "\\\\\n");

        ret.append("\\textbf{Datum: } ");
        ret.append((etikett.getDate().length() > 35? "\\\\\n":"") + etikett.getDate() + "\\\\\n");

        ret.append("}\\par\n" +
                "}\n" +
                "\\newpage\n");

        return ret.toString();
    }

}
