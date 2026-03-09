package com.viaoa.jsp;

import org.junit.Test;
import static org.junit.Assert.*;
import com.viaoa.OAUnitTest;
import com.viaoa.object.OAObject;
import com.viaoa.template.OATemplate;

import test.hifive.model.oa.Employee;


public class OATemplateTest extends OAUnitTest {

    @Test
    public void test() {
        Employee emp = new Employee();
        emp.setLastName("Smith");
        
        OATemplate<Employee> temp = new OATemplate<Employee>() {
            @Override
            protected Object getProperty(OAObject oaObj, String propertyName) {
                if (propertyName.equalsIgnoreCase("test")) {
                    return "Works";
                }
                return super.getProperty(oaObj, propertyName);
            }
        };
        temp.setTemplate("<%=lastName%>, <%=test%>");
        String s = temp.process(emp);
        assertEquals("Smith, Works", s);
    }
    
}

/*

* can now use propertyPaths with hubs in them, the results will be comma separated string

        <%=ifnot CustomItem%>
            <%=item.name%>
        <%=ifnotend CustomItem%>

        <%=if description%>
            <%=description, "38L."%>
        <%=ifend description%>
        <%=ifnot description%>
            <%=item.description, "38L."%>
        <%=ifnotend description%>


      <%=if item.imageStore.bytes%>
      <tr valign="top">
        <td>
            &nbsp;
        </td>
        <td>
            &nbsp;
        </td>
        <td colspan=5>
            <img src="oaproperty://com.cdi.model.oa.ImageStore/bytes?id=<%=item.imageStore.id%>&mh=1100&mw=1100&x=<%=$seq%>">
        </td>
      </tr>
      <%=ifend item.imageStore.bytes%>


      <%=foreach SalesOrderItems%>
      <%=foreachend SalesOrderItems%>   or   <%=foreachend%>   or <%=end%>

        <td style="text-align:right">
            <%=count$, "R,"%>
        </td>

        <!-- this is intercepted by callback -->
        <nobr><%=split$location%></nobr>


*/

/*
    <br>Tags that are supported:
 *  <ul>
 *  <li><%=prop[,width||fmt]%>  to use value from OAProperties, or one of the values from setProperty()
 *
 *  <li><%=foreach [prop]%>  to loop through a list of values (hub elements). Note: all tag properties in the scope of for loop will be based on this object.
 *  <li><%=end%>
 *
 *  <li><%=if prop%>  true if value is not null and length > 0, is 0 or false
 *  <li><%=end%>
 *
 *  <li><%=if !prop%>  true if value is not null and length > 0
 *  <li><%=ifnot prop%>  true if value is not null and length > 0
 *  <li><%=end%>
 *
 *  <li><%=if prop == "value to match"%>
 *  <li><%=ifequals prop "value to match"%>
 *  <li><%=end%>
 *
 *  <li><%=if prop > 99%>
 *  <li><%=ifgt prop 99%>
 *  <li><%=end%>
 *  <li><%=if prop >= 99%>
 *  <li><%=ifgte prop 99%>
 *  <li><%=end%>
 *
 *  <li><%=if prop < 99%>
 *  <li><%=iflt prop 99%>
 *  <li><%=end%>
 *  <li><%=if prop <= 99%>
 *  <li><%=iflte prop 99%>
 *  <li><%=end%>
 *
 *  <li><%=format[X],'12 L'%>  where X can be used as a unique identifier, so that there can be multiple embedded formats.
 *  <li><%=end%>
 *
 *  <li><%=include name%> include another file in the same directory   ex: <%=include include%>
 *  </ul>
 *
 *  <ul>Aggregate commands, works with current/most recent "foreach"
 *  <li><%=#counter [propName], fmt%> current counter
 *  <li><%=#sum [propName], propName fmt%> sum of listed properties
 *  <li><%=#count [propName], fmt%> count of listed properties
 *  </ul>
 *
 *  Note: tags are case insensitive
 *
 *  Other special tag attributes:
 *  <tr header='true'>  used by first row of a table, that will be printed as heading when table spans multiple pages.
 *  <div pagebreak='no'>  block tag to disable page breaks.
 *
 *  <div pagebreak='yes'>  block tag to force a page breaks.
 *
 *  OAHTMLReport will automatically set property values for $DATE, $TIME, $PAGE parameters
 *  <br>
 * The html code uses special tags "<%= ? %>", where "?" is the property name, or property path to use.
 *
 * By using setProperties and setObject, you can set the root object where the data is retrieved from.
 *
 * NOTE: Use a "$" prefix (ex: $PAGE) for tag names that use the value from the setProperties name/value pairs.
 * Otherwise, the value of the tag will be taken from the object, using the name as the property path.
 *
 *
* @see #getProperty(OAObject, String) that can be overwritten to handle custom/dynamic values.
 */

/**
 * Dynamically converts text with custom property [paths] and processing tags into pure html text, by using a supplied OAObject or Hub to
 * plug into the text.
 * <p>
 * Used for producing html, reports, web pages, emails, UI components like tooltips, autocomplete, renderers, and more.
 */
