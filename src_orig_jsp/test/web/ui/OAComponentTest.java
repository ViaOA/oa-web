package com.viaoa.web.ui;

import org.junit.Test;
import static org.junit.Assert.*;

import com.viaoa.web.html.OAHtmlComponent;
import com.viaoa.web.html.form.OAForm;
import com.viaoa.web.ui.form.*;
import com.viaoa.OAUnitTest;

public class OAComponentTest extends OAUnitTest {

    @Test
    public void test() {
        OAHtmlComponent comp = new OAHtmlComponent("id");
        assertEquals("id", comp.getId());
        
        assertNull(comp.getForm());
        OAForm form = new OAForm();
        assertEquals(form, comp.getForm());
        
        assertNull(comp.getName());
        
        
    }
    
    public void testSubmit() {
        
    }
    
    
}
