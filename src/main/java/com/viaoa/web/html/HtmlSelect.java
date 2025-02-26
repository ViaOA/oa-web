package com.viaoa.web.html;

import java.util.*;

import com.viaoa.util.*;
import com.viaoa.web.html.OAHtmlComponent.FormElementType;

/**
 * Html Select element that has collection of options to match html structure.
 */
public class HtmlSelect extends HtmlFormElement {
    // Note: html select element does not support readonly, use disabled
    // Note: to find selected, loop through getOptions and check for "selected" property

    public HtmlSelect(String selector) {
        this(selector, FormElementType.Select);
    }

    public HtmlSelect(String selector, FormElementType ft) {
        super(selector, ft);
    }
    
    /**
     * Size attribute is the rows to display. 
     * Use 1 for a combo/dropdown
     */
    public int getSize() {
        return htmlComponent.getSize();
    }

    public void setSize(int val) {
        htmlComponent.setSize(val);
    }

    /**
     * Html "rows" attribute for the number of rows displayed. 
     */
    public int getDisplayRows() {
        return htmlComponent.getSize();
    }

    public void setDisplayRows(int rowsToDisplay) {
        htmlComponent.setSize(rowsToDisplay);
    }
    
 
    public boolean getMultiple() {
        return htmlComponent.getMultiple();
    }
    public void setMultiple(boolean b) {
        htmlComponent.setMultiple(b);
    }

    public void add(HtmlOption option) {
        if (option != null) htmlComponent.add(option);
    }
    public void clearOptions() {
        htmlComponent.clearOptions();
    }
    public List<HtmlOption> getOptions() {
        return htmlComponent.getOptions();
    }
    
    /** 
     * Note: for multiselect, this will be the most recent selected option only.
     * To find all, look at options.selected 
     */
    public String getValue() {
        return htmlComponent.getValue();
    }
    public void setValue(String value) {
        htmlComponent.setValue(value);
    }

    public boolean getRequired() {
        return htmlComponent.getRequired();
    }

    public boolean isRequired() {
        return htmlComponent.getRequired();
    }

    public void setRequired(boolean req) {
        htmlComponent.setRequired(req);
    }

    
    private static Set<String> hsSupported = new HashSet<>();  // lowercase
    static {
        hsSupported.add("size");
        hsSupported.add("multiple");
        hsSupported.add("required");
        hsSupported.add("value");
    }
    public boolean isSupported(String name) {
        if (name == null) return false;
        return super.isSupported(name) || hsSupported.contains(name.toLowerCase());
    }
    
    @Override
    public void onClientEvent(final String type, final Map<String, String> map) {
        super.onClientEvent(type, map);
        
        if (OAStr.isNotEqual(type, Event_Change)) return;
        
        String s = map.get("selectedIndexes");
        if (s == null) return;
        int[] indexes;
        if (s.trim().length() == 0) {
            indexes = new int[0];
        }
        else {
            String[] ss = s.split(",");
            indexes = new int[ss.length];
            for (int i=0; i<ss.length; i++) {
                indexes[i] = OAConv.toInt(ss[i]);
            }
        }
        onClientChangeEvent(indexes);
    }
    
    protected void onClientChangeEvent(int[] selectIndexes) {
        int x = getOAHtmlComponent().getOptions().size();
        String newValue = "";
        for (int i=0; i<x; i++) {
            boolean b = false;
            for (int j : selectIndexes) {
                if (j == i) {
                    b = true;
                    break;
                }
            }
            HtmlOption ho = getOAHtmlComponent().getOptions().get(i);
            ho.setSelected(b);
            newValue = ho.getValue();
        }
        getOAHtmlComponent().setValue(newValue);
        getOAHtmlComponent().setValueChanged(false);
    }
}

