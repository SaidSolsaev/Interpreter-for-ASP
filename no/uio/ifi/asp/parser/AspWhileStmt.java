package no.uio.ifi.asp.parser;



import no.uio.ifi.asp.runtime.RuntimeReturnValue;
import no.uio.ifi.asp.runtime.RuntimeScope;
import no.uio.ifi.asp.runtime.RuntimeValue;
import no.uio.ifi.asp.scanner.Scanner;
import no.uio.ifi.asp.scanner.TokenKind;

public class AspWhileStmt extends AspCompoundStmt{
    AspExpr expr;
    AspSuite body;

    AspWhileStmt(int n) {
        super(n);
        //TODO Auto-generated constructor stub
    }

    static AspWhileStmt parse(Scanner s){
        enterParser("while stmt");
        AspWhileStmt aws = new AspWhileStmt(s.curLineNum());
        skip(s, TokenKind.whileToken);
        aws.expr = AspExpr.parse(s);
        skip(s, TokenKind.colonToken);
        aws.body = AspSuite.parse(s);

        leaveParser("while stmt");
        return aws;
    }

    @Override
    void prettyPrint() {
        prettyWrite("while ");
        expr.prettyPrint();
        prettyWrite(":");
        prettyIndent();
        body.prettyPrint();
        prettyDedent();
    }
    //fra forelesning
    @Override
    RuntimeValue eval(RuntimeScope curScope) throws RuntimeReturnValue {
        while(true){
            RuntimeValue t = expr.eval(curScope);
            if (! t.getBoolValue("while loop test", this)) {
                break;
            }
            trace("while True: ... ");
            body.eval(curScope);
        }
        trace("while False: ");
        return null;
    }
    
}
