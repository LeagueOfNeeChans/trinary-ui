/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tcg.generator.cards.hackertcg;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tcg.generator.layouts.CardLayout;
import java.io.File;

/**
 *
 * @author mmain
 */
public class CPUGoddessCard extends HackerTcgCard {
    public Integer cpu = 0;
    public Integer ram = 0;
    public Integer hdd = 0;
    public Integer slt = 0;
    public ProcessorType processor;
    
    protected ObjectMapper mapper = new ObjectMapper();
    
    public CPUGoddessCard() {
    }
    
    public CPUGoddessCard(String name, Integer cpu, Integer ram, Integer hdd, Integer slt, ProcessorType processor, String artworkFile, CardLayout layout) {
        super(name);
        this.cpu = cpu;
        this.ram = ram;
        this.hdd = hdd;
        this.slt = slt;
        this.processor = processor;
        
        setLayout(layout);
        setArtwork(new File(artworkFile));
    }
    
    public CPUGoddessCard(String name, Integer cpu, Integer ram, Integer hdd, Integer slt, ProcessorType processor, String artworkFile, String layoutFile) {
        super(name);
        this.cpu = cpu;
        this.ram = ram;
        this.hdd = hdd;
        this.slt = slt;
        this.processor = processor;
        
        setLayout(layoutFile);
        setArtwork(new File(artworkFile));
    }
    
    public CPUGoddessCard setCpu(Integer cpu) {
        this.cpu = cpu;
        return (CPUGoddessCard)this;
    }
    
    public CPUGoddessCard setRam(Integer ram) {
        this.ram = ram;
        return (CPUGoddessCard)this;
    }
    
    public CPUGoddessCard setHdd(Integer hdd) {
        this.hdd = hdd;
        return (CPUGoddessCard)this;
    }
    
    public CPUGoddessCard setSlt(Integer slt) {
        this.slt = slt;
        return (CPUGoddessCard)this;
    }
    
    public CPUGoddessCard setProcessor(ProcessorType processor) {
        this.processor = processor;
        return (CPUGoddessCard)this;
    }
}