/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tcg.generator.cards.hackertcg;

/**
 *
 * @author mmain
 */
public enum EffectType {
    EXE  ("[EXE]",  "[bold:EXE]", "This effect executes every time the card has enough cycles."),
    CONT ("[CONT]", "[bold:CONT]", "This effect is in effect as long as this card is running and unhalted."),
    COND ("[COND]", "[bold:COND]", "This effect fires when the specified condition is met."),
    FLIP ("[FLIP]", "[bold:FLIP]", "This effect fires when the card is flipped from face down to face up.");
    
    protected String tag;
    protected String markup;
    protected String desc;
    
    public String getTag() {
        return tag;
    }
    
    public String getMarkup() {
        return markup;
    }
    
    public String getDesc() {
        return desc;
    }
    
    EffectType(String tag, String markup, String desc) {
        this.tag = tag;
        this.markup = markup;
        this.desc = desc;
    }
}
