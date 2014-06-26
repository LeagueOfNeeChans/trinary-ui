/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tcg.generator.cards.hackertcg;

/**
 *
 * @author mmain
 */
public class Effect {
    protected EffectType type;
    protected String text;
    
    public Effect() {}
    
    public Effect(EffectType type, String text) {
        this.type = type;
        this.text = text;
    }
    
    public EffectType getType() {
        return type;
    }
    
    public String getText() {
        return text;
    }
    
    @Override
    public String toString() {
        return type.getMarkup() + " " + text;
    }
}
