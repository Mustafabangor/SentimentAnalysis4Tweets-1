/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dhamacher.sentimentanalysis4tweets.servlet;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.script.Bindings;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;
import javax.script.SimpleBindings;

/**
 *
 * @author daniel
 */
public class SentimentScriptEngine {

    private ScriptEngine engine;
    private Bindings bindings;

    public SentimentScriptEngine() {
        ScriptEngine engine =
                new ScriptEngineManager().getEngineByName("javascript");
        bindings = new SimpleBindings();
        if (engine == null) {
            System.out.println("No Suitable javascript engine found");
        }
    }

    public void addBinding(String key, Object obj) {
        try {
            bindings.put(key, obj);
            FileReader fr = new FileReader(key);
            engine.eval(fr, bindings);
        } catch (FileNotFoundException ex) {
            Logger.getLogger(SentimentScriptEngine.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ScriptException ex) {
            Logger.getLogger(SentimentScriptEngine.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
