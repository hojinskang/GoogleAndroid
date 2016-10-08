package com.google.engedu.ghost;

import android.util.Log;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Random;


public class TrieNode {
    private HashMap<String, TrieNode> children;
    //public HashSet<String> allWords = new HashSet<String>();
    private boolean isWord;

    public TrieNode() {
        children = new HashMap<>();
        isWord = false;
    }

    public void add(String s) {
        TrieNode tn = this;
        for (int i = 0; i < s.length(); i++) {
            if (tn.children.containsKey(s.substring(0,i+1))) {
                tn = tn.children.get(s.substring(0,i+1));
            } else {
                tn.children.put(s.substring(0,i+1), new TrieNode());
                tn = tn.children.get(s.substring(0,i+1));
                //allWords.add(s.substring(0, i + 1));
            }
        }
        tn.isWord = true;
    }

    public boolean isWord(String s) {
        TrieNode tn = this;
        for (int i = 0; i < s.length(); i++) {
            if (tn.children.containsKey(s.substring(0, i + 1))) {
                //Log.d("TRIENODE ISWORD", s.substring(0, i+1));
                tn = tn.children.get(s.substring(0, i + 1));
                //Log.d("TRIENODE ISWORD", String.valueOf(tn.isWord));
            } else {
                return false;
            }
        }
        return tn.isWord;
        //return allWords.contains(s);
    }

    public String getAnyWordStartingWith(String s) {
        TrieNode tn = this;
        for (int i = 0; i < s.length(); i++) {
            //Log.d("TRIENODE GETANY", tn.children.keySet().toString());
            if (tn.children.containsKey(s.substring(0,i+1))) {
                //Log.d("TRIENODE GETANY", s.substring(0,i+1));
                tn = tn.children.get(s.substring(0,i+1));
            } else {
                //Log.d("TRIENODE GETANY", "NULL1");
                return null;
            }
        }
        Map.Entry<String, TrieNode> entry = null;
        while (!tn.isWord) {
            if (tn.children.entrySet().iterator().hasNext()) {
                entry = tn.children.entrySet().iterator().next();
                //Log.d("TRIENODE GETANY", entry.getKey());
            } else {
                //Log.d("TRIENODE GETANY", "NULL2");
                return null;
            }
            tn = tn.children.get(entry.getKey());
        }
        return entry.getKey();
    }

    public String getGoodWordStartingWith(String s) {
        TrieNode tn = this;
        for (int i = 0; i < s.length(); i++) {
            //Log.d("TRIENODE GET GOOD", tn.children.keySet().toString());
            if (tn.children.containsKey(s.substring(0,i+1))) {
                //Log.d("TRIENODE GET GOOD", s.substring(0,i+1));
                tn = tn.children.get(s.substring(0,i+1));
            } else {
                //Log.d("TRIENODE GET GOOD", "NULL");
                return null;
            }
        }
        if (allCompleteWords(tn.children)) {
            return getRandomWord(tn.children, true);
        } else {
            return getRandomWord(tn.children, false);
        }
    }

    private boolean allCompleteWords(HashMap<String, TrieNode> hm) {
        Iterator it = hm.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry<String, TrieNode> pair = (Map.Entry<String, TrieNode>)it.next();
            if (!pair.getValue().isWord) {
                //Log.d("TRIENODE CHECK WORDS", "FALSE");
                return false;
            }
        }
        //Log.d("TRIENODE CHECK WORDS", "TRUE");
        return true;
    }

    private String getRandomWord(HashMap<String, TrieNode> hm, boolean complete) {
        Iterator it = hm.entrySet().iterator();
        Map.Entry<String, TrieNode> pair = null;

        HashSet<String> completeWords = new HashSet<String>();
        HashSet<String> incompleteWords = new HashSet<String>();

        while (it.hasNext()) {
            pair = (Map.Entry<String, TrieNode>)it.next();
            if (complete) {
                completeWords.add(pair.getKey());
            } else {
                incompleteWords.add(pair.getKey());
            }
        }

        Random random = new Random();
        int ranIndex;
        if (complete) {
            //Log.d("TRIENODE GET WORD", completeWords.toString());
            if (completeWords.isEmpty()) return null;
            ranIndex = random.nextInt(completeWords.size());
            it = completeWords.iterator();
        } else {
            //Log.d("TRIENODE GET WORD", incompleteWords.toString());
            if (incompleteWords.isEmpty()) return null;
            ranIndex = random.nextInt(incompleteWords.size());
            it = incompleteWords.iterator();
        }
        String str = "";
        while (it.hasNext() && ranIndex >= 0) {
            str = it.next().toString();
            ranIndex -= 1;
        }
        //Log.d("TRIENODE GET WORD RET", str);

        return str;
    }
}
