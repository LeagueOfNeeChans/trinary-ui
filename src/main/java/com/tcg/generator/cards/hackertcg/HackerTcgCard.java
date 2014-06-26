/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tcg.generator.cards.hackertcg;

import com.tcg.generator.cards.reflect.Card;
import java.util.ArrayList;

/**
 *
 * @author mmain
 */
public class HackerTcgCard extends Card {
    public ArrayList<String> effects = new ArrayList<>();
    public String flavorText = null;
    
    public HackerTcgCard() {}
    
    public HackerTcgCard(String cardName) {
        this.cardName = cardName;
    }

    public void setFlavorText(String flavorText) {
        this.flavorText = "[flavor:" + flavorText + "]";
    }
    
    public void addEffect(EffectType type, String text) {
        effects.add(new Effect(type, text).toString());
    }
    
    public void addEffect(Effect effect) {
        effects.add(effect.toString());
    }
}
