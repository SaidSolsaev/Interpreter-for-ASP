
package no.uio.ifi.asp.parser;

import no.uio.ifi.asp.scanner.*;

abstract class AspStmt extends AspSyntax {

    AspCompoundStmt acs;
    AspSmallStmtList assl;

    AspStmt(int n) {
        super(n);
    }

    static AspStmt parse(Scanner s) {
        AspStmt as = null;
        
        enterParser("stmt");
        
        if(s.isCompoundStmt()){

            as = AspCompoundStmt.parse(s);
        }
        else {
            as = AspSmallStmtList.parse(s);
        }

        leaveParser("stmt");

        return as;
    }
}
