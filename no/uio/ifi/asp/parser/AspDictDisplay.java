package no.uio.ifi.asp.parser;

import java.util.ArrayList;
import java.util.HashMap;

import no.uio.ifi.asp.runtime.RuntimeDictValue;
import no.uio.ifi.asp.runtime.RuntimeReturnValue;
import no.uio.ifi.asp.runtime.RuntimeScope;
import no.uio.ifi.asp.runtime.RuntimeStringValue;
import no.uio.ifi.asp.runtime.RuntimeValue;
import no.uio.ifi.asp.scanner.Scanner;
import no.uio.ifi.asp.scanner.TokenKind;

public class AspDictDisplay extends AspAtom{

    ArrayList<AspStringLiteral> asl = new ArrayList<>();
    ArrayList<AspExpr> expr = new ArrayList<>();

    AspDictDisplay(int n) {
        super(n);
        
    }

    static AspDictDisplay parse(Scanner s){

        enterParser("dict display");
        AspDictDisplay addi = new AspDictDisplay(s.curLineNum());
        skip(s, TokenKind.leftBraceToken);

        while(s.curToken().kind != TokenKind.rightBraceToken){

            addi.asl.add(AspStringLiteral.parse(s));

            skip(s, TokenKind.colonToken);

            addi.expr.add(AspExpr.parse(s));

            if (s.curToken().kind == TokenKind.rightBraceToken){
                break;
            }
            skip(s, TokenKind.commaToken);
        }
        
        skip(s, TokenKind.rightBraceToken);
        

        leaveParser("dict display");

        return addi;
    }

    @Override
    void prettyPrint() {
        prettyWrite("{");
    
        for (int i = 0; i < expr.size(); i++){
            asl.get(i).prettyPrint();
            prettyWrite(":");
            expr.get(i).prettyPrint();
            prettyWrite(", ");
        }
        prettyWrite("}");
    }

    @Override
    RuntimeValue eval(RuntimeScope curScope) throws RuntimeReturnValue {
        HashMap<RuntimeStringValue, RuntimeValue> hash = new HashMap<>();

        for (int i = 0; i<asl.size(); i++){
            RuntimeStringValue s = (RuntimeStringValue) asl.get(i).eval(curScope);
            RuntimeValue e = expr.get(i).eval(curScope);
            hash.put(s, e);
        }
        
        return new RuntimeDictValue(hash);
    }
    
}
