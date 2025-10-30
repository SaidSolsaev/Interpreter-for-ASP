package no.uio.ifi.asp.parser;

import no.uio.ifi.asp.runtime.RuntimeReturnValue;
import no.uio.ifi.asp.runtime.RuntimeScope;
import no.uio.ifi.asp.runtime.RuntimeValue;
import no.uio.ifi.asp.scanner.Scanner;

public class AspExprStmt extends AspSmallStmt {
    AspExpr aspExpr;

    AspExprStmt(int n) {
        super(n);
    }

    static AspExprStmt parse(Scanner s){
        AspExprStmt aes = new AspExprStmt(s.curLineNum()); 
        enterParser("expr stmt");

        aes.aspExpr = AspExpr.parse(s);

        leaveParser("expr stmt");

        return aes;
    }

    @Override
    public void prettyPrint(){
        aspExpr.prettyPrint();
    }

    //basert på forklaring på forelesning
    @Override
    RuntimeValue eval(RuntimeScope curScope) throws RuntimeReturnValue {
        RuntimeValue v = aspExpr.eval(curScope);
        trace(v.showInfo());
        return null;

        
    }
    
}
