package com.viaoa.web.oldversion;

import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.viaoa.object.OAObject;
import com.viaoa.web.swing.ComponentInterface;

public class OATextFieldFilter implements OAJspComponent, OATableEditor, OAJspRequirementsInterface, ComponentInterface {

    public OATextFieldFilter(String pTitle) {
        // TODO Auto-generated constructor stub
    }

    @Override
    public String[] getRequiredCssNames() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public String[] getRequiredJsNames() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public String getTableEditorHtml() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public boolean isChanged() {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public String getId() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void reset() {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void setForm(OAForm form) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public OAForm getForm() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public boolean _beforeFormSubmitted() {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public boolean _onFormSubmitted(HttpServletRequest req, HttpServletResponse resp, HashMap<String, String[]> hashNameValue) {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public void _beforeOnSubmit() {
        // TODO Auto-generated method stub
        
    }

    @Override
    public String _onSubmit(String forwardUrl) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public String onSubmit(String forwardUrl) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public String _afterFormSubmitted(String forwardUrl) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public String afterFormSubmitted(String forwardUrl) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public String getScript() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public String getVerifyScript() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public String getAjaxScript() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void setEnabled(boolean b) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public boolean getEnabled() {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public void setVisible(boolean b) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public boolean getVisible() {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public String getForwardUrl() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public String getEditorHtml(OAObject obj) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public String getRenderHtml(OAObject obj) {
        // TODO Auto-generated method stub
        return null;
    }

}
