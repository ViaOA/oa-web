/*  Copyright 1999 Vince Via vvia@viaoa.com
    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at http://www.apache.org/licenses/LICENSE-2.0
    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.
*/
package com.viaoa.jsp;


import java.lang.reflect.*;
import java.util.Vector;
import com.viaoa.hub.*;
import com.viaoa.util.*;

/** Button that has two states: Selected and Not Selected. Can be used in a OAButtonGroup so that
    only one button in the group is selected at a time.

<pre>
    [Java Code]
    OAToggleButton tog = new OAToggleButton("testUp.gif","testDown.gif");
    tog.setSelected(true);
    buttonGroup.add(tog);
    form.add("togTest", tog);
    ....
    [HTML Code]
    &lt;input type="image" name="&lt;%=form.getToggleButton("togUser").getName()%&gt;" &lt;%=form.getToggleButton("togUser").getSource()%&gt; SRC="testUp.gif" ALT="User Info" border="0"&gt;
    output =&gt;
    &lt;input type="image" name="oacommand_7_togUser" SRC="testUp.gif" src="testUp.gif"&gt;
</pre>
    @see OAButtonGroup
*/
public class OAToggleButton extends OAButton {
    private static final long serialVersionUID = 1L;
    protected OAButtonGroup buttonGroup;
    protected String tooltip;
    
    public OAToggleButton() {
//        bOnClickScript = false; // dont include onclick event handler
    }

    public OAToggleButton(String imageName) {
        this();
//        setImageName(imageName);
    }
    public OAToggleButton(String imageName, String selectedImageName) {
        this();
//        setImageName(imageName);
//        setSelectedImageName(selectedImageName);
    }
    public OAToggleButton(String imageName, String selectedImageName, String mouseOverImage) {
        this();
//        setImageName(imageName);
//        setSelectedImageName(selectedImageName);
//        setMouseOverImageName(mouseOverImage);
    }

    public void setSelected(boolean b) {
        bIsSelected = b;
//        if (getSelected() != b) {
//            super.setSelected(b);
            if (b && buttonGroup != null) buttonGroup.setSelected(this);
//        }
    }

    private boolean bIsSelected;
    public boolean getSelected() {
        return bIsSelected;
    }

    public OAButtonGroup getButtonGroup() {
        return buttonGroup;
    }
    
    public void setButtonGroup(OAButtonGroup bgrp) {
        if (buttonGroup != null) {
            if (buttonGroup == bgrp) return;
            bgrp.remove(this);
        }
        buttonGroup = bgrp;
        if (bgrp != null) bgrp.add(this);
        
    }
    
    @Override
    public String onSubmit(String forwardUrl) {
        setSelected(true);
        return super.onSubmit(forwardUrl);
    }
    

}

