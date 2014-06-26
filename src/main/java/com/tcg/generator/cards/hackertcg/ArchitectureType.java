/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tcg.generator.cards.hackertcg;

/**
 *
 * @author mmain
 */
public enum ArchitectureType {
    X86_64 ("x86_64", "64-bit x86 Processor");
    
    protected String abbrev, desc;
    
    public String getName() {
        return abbrev;
    }
    
    public String getDesc() {
        return desc;
    }
    
    ArchitectureType(String abbrev, String desc) {
        this.abbrev = abbrev;
        this.desc = desc;
    }
}
