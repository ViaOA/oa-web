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
package com.viaoa.web.servlet;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;

public class TestServlet extends HttpServlet {
    private Thread thread;
    
    @Override
    public void init() throws ServletException {
        super.init();
        
        thread = new Thread(new Runnable() {
            @Override
            public void run() {
                // qqqqqqqqq call your code here
            }
        });
        thread.start();  //qqqqqq run in your own thread
    }
}
