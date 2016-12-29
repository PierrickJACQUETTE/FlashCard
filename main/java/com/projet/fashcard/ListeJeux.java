package com.projet.fashcard;

import java.util.ArrayList;

public class ListeJeux {

    private ArrayList<String> list;

    private ListeJeux(){
        list = new ArrayList();
        for(int i = 0; i < 15; i++){
            list.add("TEST - Jeu n°"+i);
        }
    }

    private static ListeJeux LISTJEUX = null;

    public static ListeJeux getInstance(){
        if(LISTJEUX == null) LISTJEUX = new ListeJeux();
        return LISTJEUX;
    }

    public void ajout(String str){
        list.add(str);
    }

    public String get(int i){
        return list.get(i);
    }

    public ArrayList<String> recup(){
        return list;
    }
}
