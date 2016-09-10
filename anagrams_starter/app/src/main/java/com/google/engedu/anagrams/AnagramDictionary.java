package com.google.engedu.anagrams;

import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Random;

public class AnagramDictionary {

    private static final int MIN_NUM_ANAGRAMS = 5;
    private static final int DEFAULT_WORD_LENGTH = 3;
    private static final int MAX_WORD_LENGTH = 7;
    private Random random = new Random();

    private ArrayList<String> wordList;

    private HashSet<String> wordSet;
    private HashMap<String, Collection<String>> lettersToWord;

    private HashMap<Integer, Collection<String>> sizeToWords;
    private int wordLength;

    private static final boolean TWO_LETTER_MODE = true;
    private static final boolean TWO_WORD_MODE = true;

    public AnagramDictionary(InputStream wordListStream) throws IOException {
        BufferedReader in = new BufferedReader(new InputStreamReader(wordListStream));
        String line;

        wordList = new ArrayList<String>();
        wordSet = new HashSet<String>();
        lettersToWord = new HashMap<String, Collection<String>>();
        sizeToWords = new HashMap<Integer, Collection<String>>();
        wordLength = DEFAULT_WORD_LENGTH;

        while((line = in.readLine()) != null) {
            String word = line.trim();

            if (!wordList.contains(word)) {
                wordList.add(word);
            }

            wordSet.add(word);

            String sortedWord = sortLetters(word);

            if (!lettersToWord.containsKey(sortedWord)) {
                Collection<String> words = new ArrayList<String>();
                words.add(word);
                lettersToWord.put(sortedWord, words);
            } else {
                if (!lettersToWord.get(sortedWord).contains(word)) {
                    Collection<String> temp = lettersToWord.get(sortedWord);
                    temp.add(word);
                    lettersToWord.put(sortedWord, temp);
                }
            }

            int wordSize = word.length();
            if (!sizeToWords.containsKey(wordSize)) {
                Collection<String> words = new ArrayList<String>();
                words.add(word);
                sizeToWords.put(wordSize, words);
            } else {
                if (!sizeToWords.get(wordSize).contains(word)) {
                    Collection<String> temp = sizeToWords.get(wordSize);
                    temp.add(word);
                    sizeToWords.put(wordSize, temp);
                }
            }
        }
    }

    public boolean isGoodWord(String word, String base) {
        return wordSet.contains(word) && !word.contains(base);
    }

    public ArrayList<String> getAnagrams(String targetWord) {
        ArrayList<String> result = new ArrayList<String>();
        String sortedWord = sortLetters(targetWord);
        if (lettersToWord.containsKey(sortedWord)) {
            for (String str : lettersToWord.get(sortedWord)) {
                result.add(str);
            }
        }

        result.addAll(getAnagramsWithOneMoreLetter(targetWord));

        if (TWO_LETTER_MODE) {
            result.addAll(getAnagramsWithTwoMoreLetters(targetWord));
        }

        return result;
    }

    public String sortLetters(String word) {
        char[] charArr = word.toCharArray();
        Arrays.sort(charArr);
        return new String(charArr);
    }

    public ArrayList<String> getAnagramsWithTwoMoreLetters(String word) {
        ArrayList<String> result = new ArrayList<String>();
        for (char alphabet = 'a'; alphabet <= 'z';alphabet++) {
            for (char alphabet2 = 'a'; alphabet2 <= 'z';alphabet2++) {
                String temp = word + alphabet + alphabet2;
                String sortedTemp = sortLetters(temp);
                if (lettersToWord.containsKey(sortedTemp)) {
                    for (String str : lettersToWord.get(sortedTemp)) {
                        result.add(str);
                    }
                }
            }
        }
        return result;
    }

    public ArrayList<String> getAnagramsWithOneMoreLetter(String word) {
        ArrayList<String> result = new ArrayList<String>();
        for (char alphabet = 'a'; alphabet <= 'z';alphabet++) {
            String temp = word + alphabet;
            String sortedTemp = sortLetters(temp);
            if (lettersToWord.containsKey(sortedTemp)) {
                for (String str : lettersToWord.get(sortedTemp)) {
                    result.add(str);
                }
            }
        }
        return result;
    }

    public String pickGoodStarterWord() {
        for (int i = wordLength; i < MAX_WORD_LENGTH; i++) {
            Collection<String> temp = sizeToWords.get(i);
            if (!temp.isEmpty()) {
                ArrayList<String> tempList = new ArrayList<String>(temp);
                while (!tempList.isEmpty()) {
                    int randomIndex = (int) (Math.random() * (tempList.size() - 1));
                    String str = tempList.remove(randomIndex);
                    if (lettersToWord.get(sortLetters(str)).size() > MIN_NUM_ANAGRAMS) {
                        return tempList.remove(randomIndex).toString();
                    }
                }
            }
        }
        return null;
    }
}
