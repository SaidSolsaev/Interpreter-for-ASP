
package no.uio.ifi.asp.parser;

import java.util.ArrayList;

import no.uio.ifi.asp.runtime.*;
import no.uio.ifi.asp.scanner.*;
import static no.uio.ifi.asp.scanner.TokenKind.*;

public class AspProgram extends AspSyntax {
    ArrayList<AspStmt> stmts = new ArrayList<>();

    AspProgram(int n) {
        super(n);
    }

    public static AspProgram parse(Scanner s) {
        enterParser("program");

        AspProgram ap = new AspProgram(s.curLineNum());

        while (s.curToken().kind != eofToken) {
            ap.stmts.add(AspStmt.parse(s));
        }

        leaveParser("program");
        return ap;
    }

    @Override
    public void prettyPrint() {
        // -- Must be changed in part 2:
        for (AspStmt x : stmts){
            x.prettyPrint();
        }
    }

    @Override
    public RuntimeValue eval(RuntimeScope curScope) throws RuntimeReturnValue {
        // -- Must be changed in part 4:
        // Denne koden er fra forelesning
        trace("<--- Program Start --->");
        for (AspStmt stmt : stmts){
            try {
                stmt.eval(curScope);
            } catch (RuntimeReturnValue returnErr) {
                RuntimeValue.runtimeError("Return outside of function! On Line: ", returnErr.lineNum);
            }
        }
        
        trace("<--- Program End --->");
        return null;
    }
}
