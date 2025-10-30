package no.uio.ifi.asp.parser;

import java.util.ArrayList;

import no.uio.ifi.asp.runtime.RuntimeListValue;
import no.uio.ifi.asp.runtime.RuntimeReturnValue;
import no.uio.ifi.asp.runtime.RuntimeScope;
import no.uio.ifi.asp.runtime.RuntimeValue;
import no.uio.ifi.asp.scanner.Scanner;
import no.uio.ifi.asp.scanner.TokenKind;

public class AspListDisplay extends AspAtom {
    ArrayList<AspExpr> expr = new ArrayList<>();

    AspListDisplay(int n) {
        super(n);
        //TODO Auto-generated constructor stub
    }

    static AspListDisplay parse(Scanner s){
        enterParser("list display");

        AspListDisplay ald = new AspListDisplay(s.curLineNum());

        skip(s, TokenKind.leftBracketToken);
        
        if (s.curToken().kind != TokenKind.rightBracketToken){
            while(true){
                ald.expr.add(AspExpr.parse(s));

                if (s.curToken().kind != TokenKind.commaToken){
                    break;
                }
                skip(s,TokenKind.commaToken);
            }
        }

        skip(s, TokenKind.rightBracketToken);

        leaveParser("list display");
        return ald;
    } 

    @Override
    void prettyPrint() {
        prettyWrite("[");

        if (expr.size() < 0){
            prettyWrite("]");
        } else{
            int p = 0;

            for (AspExpr x : expr){
                if (p > 0){
                    prettyWrite(",");
                }
                x.prettyPrint();
                p++;
            }
        }
        prettyWrite("]");
    }

    @Override
    RuntimeValue eval(RuntimeScope curScope) throws RuntimeReturnValue {
        RuntimeListValue list = new RuntimeListValue(new ArrayList<RuntimeValue>());

        for (AspExpr x : expr){
            list.addElement(x.eval(curScope));
        }

        return list;
    }


    
}
