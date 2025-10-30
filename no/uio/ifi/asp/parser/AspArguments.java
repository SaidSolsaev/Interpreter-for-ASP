package no.uio.ifi.asp.parser;
import java.util.ArrayList;
import no.uio.ifi.asp.runtime.RuntimeReturnValue;
import no.uio.ifi.asp.runtime.RuntimeScope;
import no.uio.ifi.asp.runtime.RuntimeValue;
import no.uio.ifi.asp.scanner.Scanner;
import no.uio.ifi.asp.scanner.TokenKind;

public class AspArguments extends AspPrimarySuffix {
    ArrayList<AspExpr> expr = new ArrayList<>();


    AspArguments(int n) {
        super(n);
        //TODO Auto-generated constructor stub
    }

    static AspArguments parse(Scanner s){
        enterParser("arguments");

        AspArguments aas = new AspArguments(s.curLineNum());

        skip(s, TokenKind.leftParToken);

        if (s.curToken().kind != TokenKind.rightParToken){
            while(true){
                aas.expr.add(AspExpr.parse(s));

                if (s.curToken().kind != TokenKind.commaToken){
                    break;
                }
                skip(s,TokenKind.commaToken);
                
            }
        }

        skip(s, TokenKind.rightParToken);
        
        leaveParser("arguments");
        return aas;
    }

    @Override
    void prettyPrint() {
        prettyWrite("(");
        int p = 0;

        for (AspExpr x : expr){
            if (p > 0){
                prettyWrite(", ");
            }
            x.prettyPrint();
            p++;
        }
        prettyWrite(")");
    }

    @Override
    RuntimeValue eval(RuntimeScope curScope) throws RuntimeReturnValue {

        RuntimeValue v = expr.get(0).eval(curScope); 
        //går inn i listen
        for (int i = 1; i < expr.size(); ++i) {
         
            if (! v.getBoolValue("arguments operand", this)) {
                return v;
            }
           
            v = expr.get(i).eval(curScope);
        }
    
        
        return v;
        
        
    }
    
    
}
