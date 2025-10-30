package no.uio.ifi.asp.parser;

import no.uio.ifi.asp.runtime.RuntimeReturnValue;
import no.uio.ifi.asp.runtime.RuntimeScope;
import no.uio.ifi.asp.runtime.RuntimeValue;
import no.uio.ifi.asp.scanner.Scanner;
import no.uio.ifi.asp.scanner.TokenKind;

public class AspSubscription extends AspPrimarySuffix{
    AspExpr expr;

    AspSubscription(int n) {
        super(n);
        //TODO Auto-generated constructor stub
    }

    static AspSubscription parse(Scanner s){

        AspSubscription as = new AspSubscription(s.curLineNum());

        enterParser("subscription");

        skip(s, TokenKind.leftBracketToken);
        as.expr = AspExpr.parse(s);
        skip(s, TokenKind.rightBracketToken);

        leaveParser("subscription");

        return as;

        
    }

    @Override
    void prettyPrint() {
        prettyWrite("[");
        expr.prettyPrint();
        prettyWrite("]");
    }

    @Override
    RuntimeValue eval(RuntimeScope curScope) throws RuntimeReturnValue {
        RuntimeValue v = expr.eval(curScope);
        return v;
    }
    
}
