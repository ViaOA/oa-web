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
package com.viaoa.web.html.oa;

/**
 * Types of UI components to add to toolbar.
 * @author vvia
 */
public class OAToolbarOptions {
    public static final int Icon = 1 << 1;
    public static final int Label = 1 << 2;
    public static final int Goto = 1 << 3;
    public static final int Details = 1 << 3; // same as goto
    public static final int GoBack = 1 << 4;
    public static final int Refresh = 1 << 5;
    
    public static final int First = 1 << 6;
    public static final int Last = 1 << 7;
    public static final int Prev = 1 << 8;
    public static final int Next = 1 << 9;
    
    public static final int MoveUp = 1 << 10;
    public static final int MoveDown = 1 << 11;
    
    public static final int Save = 1 << 12;
    public static final int New = 1 << 13;
    public static final int Add = 1 << 14;
    public static final int Insert = 1 << 15;
    public static final int Wizard = 1 << 16;
    
    public static final int Remove = 1 << 17;
    public static final int Delete = 1 << 18;
    
    
    public static final int Find = 1 << 20;
    public static final int HubSearch = 1 << 21;
    public static final int Search = 1 << 22;

    public static final int Report = 1 << 24;
    public static final int Download = 1 << 25;

    
    public static final int TableViewChoices = 1 << 28;  // ex: changes table to groupBy
    public static final int TableFilterChoices = 1 << 29;

    public static final int HubCalc = 1 << 30; 
    
    public static final int Custom = 1 << 31;
    
    protected int value;
    
    public OAToolbarOptions() {
    }
    public OAToolbarOptions(int value) {
        this.value = value;
    }
    
    public boolean get(int type) {
        return (value & type) > 0;
    }

    public void add(int value) {
        this.value |= value;
    }
    
    public void remove(int value) {
        this.value ^= value;
    }

    public void addAll() {
        int value = ~0;
        this.add(value);;
    }
    
    public static OAToolbarOptions createTableNorth() {
        int value = 0;
        value |= Icon;
        value |= Label;
        value |= Find;
        //qqqqq   report, table, filter
        return new OAToolbarOptions(value);
    }

    public static OAToolbarOptions createTableSouth() {
        int value = 0;
        value |= Goto; // details
        value |= Save;
        value |= New;
        value |= Add;
        value |= Insert;
        value |= Wizard;
        value |= Remove;
        value |= Delete;
        value |= HubSearch;
        value |= Download;
        return new OAToolbarOptions(value);
    }
    
    public static OAToolbarOptions createEdit() {
        int value = 0;
        value |= GoBack;
        value |= Icon;
        value |= Label;
        value |= Refresh;
        value |= Save;
        value |= New;
        value |= Add;
        value |= Insert;
        value |= Wizard;
        value |= Remove;
        value |= Delete;
        return new OAToolbarOptions(value);
    }
    
    public void addNav() {
        int value = 0;
        value |= Prev;
        value |= Next;
        value |= First;
        value |= Last;
        value |= MoveUp;
        value |= MoveDown;
        add(value);
    }
}
