/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tcg.generator.cards.hackertcg;

/**
 *
 * @author mmain
 */
public enum ProcessorType {
    INTEL_CORE_I3 ("Intel Core i3", ArchitectureType.X86_64),
    INTEL_CORE_I5 ("Intel Core i5", ArchitectureType.X86_64),
    INTEL_CORE_I7 ("Intel Core i7", ArchitectureType.X86_64),;
    
    protected String name;
    protected ArchitectureType type;
    
    public String getName() {
        return name;
    }
    
    public ArchitectureType getArchitectureType() {
        return type;
    }
    
    ProcessorType(String name, ArchitectureType type) {
        this.name = name;
        this.type = type;
    }
}
