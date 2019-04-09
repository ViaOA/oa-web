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
import java.util.*;

/** used with OAToggleButtons to make sure that only one button is selected at a time. 
    @see OAToggleButton
*/
public class OAButtonGroup {
    private static final long serialVersionUID = 1L;
    protected Vector vec = new Vector(5,5);

    public OAToggleButton[] getButtons() {
        int x = vec.size();
        OAToggleButton[] t = new OAToggleButton[x];
        vec.copyInto(t);
        return t;
    }
    public void add(OAToggleButton tog) {
        if (tog != null && !vec.contains(tog)) {
            vec.addElement(tog);
            if (tog.getSelected() || vec.size() == 1) setSelected(tog);
            tog.setButtonGroup(this);
        }
    }

    public void remove(OAToggleButton tog) {
        if (tog != null && vec.contains(tog)) {
            vec.removeElement(tog);
        }
    }
    
    protected int getSelectedIndex() {
        int x = vec.size();
        for (int i=0; i<x; i++) {
            OAToggleButton tog = (OAToggleButton) vec.elementAt(i);
            if (tog.getSelected()) return i;
        }
        if (x > 0) {
            setSelectedIndex(0);
            return 0;
        }
        return -1;
    }
    protected void setSelectedIndex(int pos) {
        int x = vec.size();
        if (pos < x) {
            for (int i=0; i<x; i++) {
                OAToggleButton tog = (OAToggleButton) vec.elementAt(i);
                tog.setSelected( (i == pos) );
            }
        }
    }

    public void setSelected(OAToggleButton cmd) {
        int x = vec.size();
        for (int i=0; i<x; i++) {
            OAToggleButton tog = (OAToggleButton) vec.elementAt(i);
            boolean b = (tog == cmd);
            if (tog.getSelected() != b) tog.setSelected(b);
        }
    }
    
}
