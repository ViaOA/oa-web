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
 * @author vvia
 */
public class OAToolbarOptionsDelegate {
	
	
	
	public static void config(
			OAToolbarOptions tboTop, 
			OAToolbarOptions tboBottom,
			OAToolbarOptions.Type tboType
		) 
	{
		if (tboType == null) return;
		if (tboTop == null && tboBottom == null) return;
		OAToolbarOptions to;
		
		if (tboType == tboType.List || tboType == tboType.ListWithEdit || tboType == tboType.ListForMaster || tboType == tboType.SearchList || tboType == tboType.SearchListWithEdit) {
			if (tboTop != null) {
				if (tboBottom == null) {
					// all on top
					to = tboTop;
					to.add(
						to.Icon | to.Label | to.Goto |  
						to.Find |
						to.Refresh | to.PlusNav | to.PlusMove | to.Save | to.New |
						to.Add | to.Insert | to.Wizard | to.Remove | to.Delete |
						to.HubSearch | to.Report | to.Download | to.Custom
					);
				}
				else {
					// split
					to = tboTop;
					to.add(to.Icon | to.Label | to.Find);
					
					to = tboBottom;
					to.add(
						to.Goto | 
						to.Refresh | to.PlusNav | to.PlusMove | to.Save | to.New |
						to.Add | to.Insert | to.Wizard | to.Remove | to.Delete |
						to.HubSearch | to.Report | to.Download |
						to.Custom | to.HubCalc
					);
				}
			}
			else {
				// all on bottom
				to = tboBottom;
				to.add(
					to.Icon | to.Label | to.Goto |  
					to.Find |
					to.Refresh | to.PlusNav | to.PlusMove | to.Save | to.New |
					to.Add | to.Insert | to.Wizard | to.Remove | to.Delete |
					to.HubSearch | to.Report | to.Download |
					to.Custom | to.HubCalc
				);
			}

			to = tboTop;
			if (to != null && (tboType == tboType.SearchList || tboType == tboType.SearchListWithEdit)) {
				to.add(to.Search);
				to.remove(to.HubSearch);
			}
			
			
			if (tboType == tboType.ListWithEdit) {
				to = tboTop;
				if (to != null) {
					to.remove(to.Icon | to.Label | to.PlusNav);
				}
				to = tboBottom;
				if (to != null) {
					to.remove(to.Icon | to.Label | to.PlusNav);
				}
			}
			else if (tboType == tboType.SearchListWithEdit || tboType == tboType.SearchList) {
				to = tboTop;
				if (to != null) {
					to.remove(to.Icon | to.Label | to.PlusNav | to.Find);
				}
				to = tboBottom;
				if (to != null) {
					to.remove(to.Icon | to.Label | to.PlusNav | to.Find);
				}
			}
			else if (tboType == tboType.ListForMaster) {
				to = tboTop;
				if (to != null) {
					to.remove(to.Icon | to.Label | to.PlusNav);
				}
				to = tboBottom;
				if (to != null) {
					to.remove(to.Icon | to.Label | to.PlusNav);
				}
			}
			
			
		}
		else {
			if (tboType == tboType.Edit) {
				if (tboTop != null) {
					if (tboBottom == null) {
						// all on top
						to = tboTop;
						to.add(
							to.Icon | to.Label |  
							to.Refresh | to.Save | to.New |
							to.Wizard | to.Delete |
							to.Report | to.Custom
						);
					}
					else {
						// split
						to = tboTop;
						to.add(to.Icon | to.Label);
						
						to = tboBottom;
						to.add(
							to.Refresh | to.Save | to.New |
							to.Wizard | to.Delete |
							to.Report |
							to.Custom
						);
					}
				}
				else {
					// all on bottom
					to = tboBottom;
					to.add(
						to.Icon | to.Label |  
						to.Refresh | to.Save | to.New |
						to.Wizard | to.Delete |
						to.Report | to.Custom
					);
				}
			}
			else { // EditWithList
				// none
			}
		}
	}
	
	
}
