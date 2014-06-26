/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tcg.generator.tests;

import com.tcg.generator.cards.GenericCard;
import com.tcg.generator.config.ConfigHolder;
import java.io.File;

/**
 *
 * @author Michael
 */
public class Test {
    public static void main(String[] args) {
        ConfigHolder.setConfig("rootDirectory", "src/main/resources/");
        GenericCard vanguard = new GenericCard();
        vanguard.setCardName("Fluttershy")
                .setLayout(new File("src/main/resources/layouts/vanguard.layout"))
                .setArtwork(new File("src/main/resources/art/fluttershy-art.png"))
                .setCardData(
                "{" +
                    "\"grade\": 0," +
                    "\"trigger\": \"heal\"," +
                    "\"shield\": 10000," +
                    "\"power\": 10000," +
                    "\"clan\": \"Equestria\"," +
                    "\"race\": \"Pony\"," +
                    "\"effects\": [" +
                        "\"<img src='cont' /> If you have a unit named <b>Twilight Sparkle</b> on <img src='vanguard' />, then <b>Fluttershy</b> gains <img src='sword' />+5000 until end of turn.\"" +
                    "]" +
                "}");
        vanguard.draw(new File("card.png"));
    }
}