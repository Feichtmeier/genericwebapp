package org.feichtmeier.genericwebapp.view.util;

public class JavaScripts {

    public static final String USE_SYSTEM_THEME_SCRIPT = "let mm = window.matchMedia('(prefers-color-scheme: dark)');function apply() { document.documentElement.setAttribute(\"theme\",mm.matches?\"dark\":\"\");}mm.addListener(apply);apply();";
    
}
