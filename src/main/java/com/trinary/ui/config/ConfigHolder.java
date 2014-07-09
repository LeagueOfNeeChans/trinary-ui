/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.trinary.ui.config;

import java.util.HashMap;

/**
 *
 * @author Michael
 */
public final class ConfigHolder {
    static private HashMap<String, String> config = new HashMap<>();
    
    static {
        config.put("rootDirectory", "");
    }
    
    public static void setConfig(String key, String value) {
        config.put(key, value);
    }
    
    public static String getConfig(String key) {
        return config.get(key);
    }
}
