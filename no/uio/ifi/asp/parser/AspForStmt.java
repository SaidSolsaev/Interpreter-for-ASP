package no.uio.ifi.asp.parser;
import java.util.ArrayList;
import no.uio.ifi.asp.runtime.RuntimeListValue;
import no.uio.ifi.asp.runtime.RuntimeReturnValue;
import no.uio.ifi.asp.runtime.RuntimeScope;
import no.uio.ifi.asp.runtime.RuntimeValue;
import no.uio.ifi.asp.scanner.Scanner;
import no.uio.ifi.asp.scanner.TokenKind;

public class AspForStmt  extends AspCompoundStmt{
    AspName aname;
    AspExpr test;
    AspSuite body;
    
    AspForStmt(int n) {
        super(n);
        //TODO Auto-generated constructor stub
    }

    static AspForStmt parse(Scanner s){
        AspForStmt afs = new AspForStmt(s.curLineNum());
        enterParser("for stmt");
        
        skip(s, TokenKind.forToken);
        afs.aname = AspName.parse(s);

        skip(s, TokenKind.inToken);
        afs.test = AspExpr.parse(s);

        skip(s, TokenKind.colonToken);
        
        afs.body = AspSuite.parse(s);

        leaveParser("for stmt");

        return afs;

    }

    @Override
    public void prettyPrint() {
        prettyWrite("for ");
        aname.prettyPrint();
        prettyWrite(" in ");
        test.prettyPrint();
        prettyWrite(": ");
        
        prettyIndent();
        body.prettyPrint();
        prettyDedent();
    }


    @Override
    public RuntimeValue eval(RuntimeScope curScope) throws RuntimeReturnValue {
        RuntimeValue v = test.eval(curScope);

        if (v instanceof RuntimeListValue) {
            //RuntimeListValue listValue = (RuntimeListValue) v;
            ArrayList<RuntimeValue> listArray = ((RuntimeListValue) v).getList();
            
            for (int i = 0; i < listArray.size(); i++) {
                
                curScope.assign(aname.name, listArray.get(i)); 
                
                trace("for #" + (i+1) + ": " + aname.name + " = " + listArray.get(i).showInfo());
                v = body.eval(curScope);
            }
        }
        return v;
    }
    
}
