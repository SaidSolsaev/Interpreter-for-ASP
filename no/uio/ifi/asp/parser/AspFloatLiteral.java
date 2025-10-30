package no.uio.ifi.asp.parser;

import no.uio.ifi.asp.runtime.RuntimeFloatValue;
import no.uio.ifi.asp.runtime.RuntimeReturnValue;
import no.uio.ifi.asp.runtime.RuntimeScope;
import no.uio.ifi.asp.runtime.RuntimeValue;
import no.uio.ifi.asp.scanner.Scanner;
import no.uio.ifi.asp.scanner.TokenKind;

public class AspFloatLiteral extends AspAtom {
    double tall;

    AspFloatLiteral(int n) {
        super(n);
        //TODO Auto-generated constructor stub
    }

    static AspFloatLiteral parse(Scanner s){
        enterParser("float literal");
        AspFloatLiteral afl = new AspFloatLiteral(s.curLineNum());

        afl.tall = s.curToken().floatLit;
        skip(s, TokenKind.floatToken);
        
        leaveParser("float literal");
        return afl;
    }

    @Override
    void prettyPrint() {
        prettyWrite(Double.toString(tall));
        
    }

    @Override
    RuntimeValue eval(RuntimeScope curScope) throws RuntimeReturnValue {
        return new RuntimeFloatValue(tall);
    }
    
}
