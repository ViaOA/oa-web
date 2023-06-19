package com.viaoa.web.html.input;

import java.util.List;

import com.viaoa.web.html.HtmlFormElement;
import com.viaoa.web.html.OAHtmlComponent.InputType;

public class InputColor extends HtmlFormElement {

    public InputColor(String id) {
        super(id, InputType.Color);
    }

    public String getValue() {
        return oaHtmlComponent.getValue();
    }
    public void setValue(String value) {
        oaHtmlComponent.setValue(value);
    }

//qqqqqqqqqq datalist of "#AA00BB" colors    
    // Id of datalist element
    public String getList() {
        return oaHtmlComponent.getList();
    }

    public void setList(String listId) {
        oaHtmlComponent.setList(listId);
    }
    
    public List<String> getDataList() {
        return oaHtmlComponent.getDataList();
    }

    public void setDataList(List<String> lst) {
        oaHtmlComponent.setDataList(lst);
    }

}
