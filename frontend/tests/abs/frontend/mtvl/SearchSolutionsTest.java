/**
 * Copyright (c) 2009-2011, The HATS Consortium. All rights reserved. 
 * This file is licensed under the terms of the Modified BSD License.
 */
package abs.frontend.mtvl;

import static org.junit.Assert.assertTrue;

import java.io.FileOutputStream;
import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

import abs.frontend.FrontendTest;
import abs.frontend.ast.Model;


public class SearchSolutionsTest extends FrontendTest {
    
    static private boolean checkSol(ChocoSolver s, Model m, String prod) {
        Map<String,Integer> guess = new HashMap<String,Integer>();
        if (m.getSolution(prod,guess))
            return s.checkSolution(guess,m);
        else return false;
    }
    
    static private String helloprogram =
        " module Helloworld;" +
        " product P1 (English);" +
        " product P2 (French);" +
        " product P3 (French, Repeat{times=10});" +
        " product P4 (English, Repeat{times=6});" +
        " root MultiLingualHelloWorld {" +
        "   group allof {" +
        "      Language {" +
        "        group oneof { English, Dutch, French, German }" +
        "      }," +
        "      opt Repeat {" +
        "        Int times in [0..10];" +
        "        ifin: times > 0; " +
        "      } " +
        "    } " +
        " }" +
        " extension English {" +
        "    ifin: Repeat ->" +
        "          (Repeat.times >= 2 && Repeat.times <= 5);" +
        " }";


    @Test
    public void SearchSolutions() {
        Model model       = assertParseOk(helloprogram);
        model.setNullPrintStream();
                
        ChocoSolver s = model.getCSModel();

        assertTrue(s.countSolutions() == 78);
        assertTrue(checkSol(s,model,"Helloworld.P1"));
        assertTrue(checkSol(s,model,"Helloworld.P2"));
        assertTrue(checkSol(s,model,"Helloworld.P3"));
        assertTrue(!checkSol(s,model,"Helloworld.P4"));        
    }

    @Test
    public void SearchSolutionsNoAttr() {
        Model model       = assertParseOk(helloprogram);
        model.dropAttributes();
        
        ChocoSolver s = model.getCSModel();
        
        assertTrue(s.countSolutions() == 8);
    }
}