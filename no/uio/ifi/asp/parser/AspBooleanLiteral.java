package no.uio.ifi.asp.parser;

import no.uio.ifi.asp.runtime.RuntimeBoolValue;
import no.uio.ifi.asp.runtime.RuntimeReturnValue;
import no.uio.ifi.asp.runtime.RuntimeScope;
import no.uio.ifi.asp.runtime.RuntimeValue;
import no.uio.ifi.asp.scanner.Scanner;
import no.uio.ifi.asp.scanner.TokenKind;

public class AspBooleanLiteral extends AspAtom {
    String b; //For prettyPrint
    

    AspBooleanLiteral(int n) {
        super(n);
        //TODO Auto-generated constructor stub
    }

    static AspBooleanLiteral parse(Scanner s){
        enterParser("boolean literal");

        AspBooleanLiteral abl = new AspBooleanLiteral(s.curLineNum());
        
        if (s.curToken().kind == TokenKind.trueToken){
            abl.b = "True";
            skip(s, TokenKind.trueToken);
        } else if(s.curToken().kind == TokenKind.falseToken){
            abl.b = "False";
            skip(s, TokenKind.falseToken);
        }

        leaveParser("boolean literal");
        return abl;
    }

    @Override
    void prettyPrint() {
        prettyWrite(b);
    }

    //fra prekoden (ish)
    @Override
    RuntimeValue eval(RuntimeScope curScope) throws RuntimeReturnValue {
        boolean value;
        if(b == "True"){
            value = true;
        }
        else {
            value = false;
        }
        return new RuntimeBoolValue(value);
    }
    
}
