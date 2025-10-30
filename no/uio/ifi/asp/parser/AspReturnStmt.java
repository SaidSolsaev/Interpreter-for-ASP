package no.uio.ifi.asp.parser;

import no.uio.ifi.asp.runtime.RuntimeReturnValue;
import no.uio.ifi.asp.runtime.RuntimeScope;
import no.uio.ifi.asp.runtime.RuntimeValue;
import no.uio.ifi.asp.scanner.Scanner;
import no.uio.ifi.asp.scanner.TokenKind;

public class AspReturnStmt extends AspSmallStmt {
    AspExpr expr;

    AspReturnStmt(int n) {
        super(n);
        //TODO Auto-generated constructor stub
    }

    static AspReturnStmt parse(Scanner s){
        AspReturnStmt aes = new AspReturnStmt(s.curLineNum()); 
        enterParser("return stmt");

        skip(s, TokenKind.returnToken);

        aes.expr = AspExpr.parse(s);

        leaveParser("return stmt");

        return aes;
    }
    
    @Override
    public void prettyPrint(){
        prettyWrite(" return ");
        expr.prettyPrint();
    }

    @Override
    public RuntimeValue eval(RuntimeScope curScope) throws RuntimeReturnValue {
        // Kode hentet fra forelesning
        RuntimeValue v = expr.eval(curScope);
        trace("Return " + v.showInfo());
        throw new RuntimeReturnValue(v, lineNum);
    }
}
