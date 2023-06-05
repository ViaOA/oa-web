package com.viaoa.web.ui;

import com.viaoa.web.ui.base.BaseComponent;
import com.viaoa.web.ui.base.BaseForm;

public class OAForm extends BaseForm {

    public OAForm() {
    }
    public OAForm(String id) {
        super(id);
    }
    public OAForm(String id, String url) {
        super(id, url);
    }

    
    public OALabel getLabel(String id) {
        BaseComponent comp = getComponent(id, OALabel.class);
        if (comp instanceof OALabel) return (OALabel) comp;
        return null;
    }
    public OATextField getTextField(String id) {
        BaseComponent comp = getComponent(id, OATextField.class);
        if (comp instanceof OATextField) return (OATextField) comp;
        return null;
    }
    public OAPassword getPassword(String id) {
        BaseComponent comp = getComponent(id, OAPassword.class);
        if (comp instanceof OAPassword) return (OAPassword) comp;
        return null;
    }
    public OAButton getButton(String id) {
        BaseComponent comp = getComponent(id, OAButton.class);
        if (comp instanceof OAButton) return (OAButton) comp;
        return null;
    }
    public OAFileInput getFileInput(String id) {
        BaseComponent comp = getComponent(id, OAButton.class);
        if (comp instanceof OAFileInput) return (OAFileInput) comp;
        return null;
    }
    
    public OAButtonList getButtonList(String id) {
        BaseComponent comp = getComponent(id, OAButtonList.class);
        if (comp instanceof OAButtonList) return (OAButtonList) comp;
        return null;
    }
    public OAHtmlElement getHtmlElement(String id) {
        BaseComponent comp = getComponent(id, OAHtmlElement.class);
        if (comp instanceof OAHtmlElement) return (OAHtmlElement) comp;
        return null;
    }
    public OATextArea getTextArea(String id) {
        BaseComponent comp = getComponent(id, OATextArea.class);
        if (comp instanceof OATextArea) return (OATextArea) comp;
        return null;
    }
    public OACombo getCombo(String id) {
        BaseComponent comp = getComponent(id, OACombo.class);
        if (comp instanceof OACombo) return (OACombo) comp;
        return null;
    }
    public OATable getTable(String id) {
        BaseComponent comp = getComponent(id, OATable.class);
        if (comp instanceof OATable) return (OATable) comp;
        return null;
    }
    public OALink getLink(String id) {
        BaseComponent comp = getComponent(id, OALink.class);
        if (comp instanceof OALink) return (OALink) comp;
        return null;
    }
    public OACheckBox getCheckBox(String id) {
        BaseComponent comp = getComponent(id, OACheckBox.class);
        if (comp instanceof OACheckBox) return (OACheckBox) comp;
        return null;
    }
    public OAGrid getGrid(String id) {
        BaseComponent comp = getComponent(id, OAGrid.class);
        if (comp instanceof OAGrid) return (OAGrid) comp;
        return null;
    }
    public OAHtmlSelect getSelect(String id) {
        BaseComponent comp = getComponent(id, OAHtmlSelect.class);
        if (comp instanceof OAHtmlSelect) return (OAHtmlSelect) comp;
        return null;
    }
    public OAImage getImage(String id) {
        BaseComponent comp = getComponent(id, OAImage.class);
        if (comp instanceof OAImage) return (OAImage) comp;
        return null;
    }
    public OARadio getRadio(String id) {
        BaseComponent comp = getComponent(id, OARadio.class);
        if (comp instanceof OARadio) return (OARadio) comp;
        return null;
    }
    public OAServletImage getServletImage(String id) {
        BaseComponent comp = getComponent(id, OAServletImage.class);
        if (comp instanceof OAServletImage) return (OAServletImage) comp;
        return null;
    }
    public OAList getList(String id) {
        BaseComponent comp = getComponent(id, OAList.class);
        if (comp instanceof OAList) return (OAList) comp;
        return null;
    }
    public OADialog getDialog(String id) {
        BaseComponent comp = getComponent(id, OADialog.class);
        if (comp instanceof OADialog) return (OADialog) comp;
        return null;
    }
    public OAPopup getPopup(String id) {
        BaseComponent comp = getComponent(id, OAPopup.class);
        if (comp instanceof OAPopup) return (OAPopup) comp;
        return null;
    }
    public OAPopupList getPopupList(String id) {
        BaseComponent comp = getComponent(id, OAPopupList.class);
        if (comp instanceof OAPopupList) return (OAPopupList) comp;
        return null;
    }
}
