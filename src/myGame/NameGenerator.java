/*
 *  NameGenerator.java
 *
 *  Class used to generate randomised names for cities.
 *
 */

package myGame;

import java.io.*;
import java.util.Vector;
import java.util.Random;

public class NameGenerator {

    private Vector<String> syllables;
    private Vector<String> prefixes;
    private Vector<String> suffixes;
    private Random gen = new Random();

    // Constructor
    public NameGenerator() {
        // Retrieve syllables list.
        this.syllables = this.loadTextFile("data/syllables.txt");
        this.prefixes = this.loadTextFile("data/prefixes.txt");
        this.suffixes = this.loadTextFile("data/suffixes.txt");
    }

    // Private constructor code.
    private Vector<String> loadTextFile(String filename) {
        try {

            // Creates reader to retrieve data.
            FileReader reader = new FileReader(filename);
            BufferedReader input = new BufferedReader(reader);

            return readingCode(input);

        }
        catch (FileNotFoundException error) {   // In case file does not exist.
            System.out.println(error);
            return null;
        }  
    }

    private Vector<String> readingCode(BufferedReader in_reader) {

        // Creates vector to store read in values.
        Vector<String> all_text = new Vector<String>();   

        try {
            String newString;
            // Loops through file and loads all the data into the vector.
            while ( (newString=in_reader.readLine()) != null) {
                if (!all_text.contains(newString)) {
                    all_text.add(newString);
                }
            }
        }
        catch (IOException error) {     // In case of a readLine error.
            System.out.println(error); 
        }

        // Vector is returned.
        return all_text;

    }

    // Generation code.
    public String newName(int inLength) {

        String name = "";
        int index;

        // Loops for number of length.
        for (int i = 0; i < inLength; ++i) {

            // Generate index.
            index = gen.nextInt(this.syllables.size());

            // Add syllable to the name appropriately.
            if (i == 0) {
                if (this.syllables.get(index).length() > 1) {
                    name += this.syllables.get(index).substring(0,1).toUpperCase();
                    name += this.syllables.get(index).substring(1);
                } else {
                    name += this.syllables.get(index).toUpperCase();
                }
            } else {
                name += this.syllables.get(index);
            }

        }

        // Add on a prefix.
        int chance = gen.nextInt(10);
        if (chance == 0) {

            // Generate index.
            index = gen.nextInt(this.prefixes.size());

            // Adds prefix to the name.
            name = this.prefixes.get(index) + " " + name;

        }
        
     // Add on a suffix.
        chance = gen.nextInt(20);
        if (chance == 0) {

            // Generate index.
            index = gen.nextInt(this.suffixes.size());

            // Adds prefix to the name.
            name = name + this.suffixes.get(index);

        }

        // Returns the final name.
        return name;

    }

    // Mutators
    public void setRandomGenerator(Random in) {
        this.gen = in;
    }

}