package no.uio.ifi.asp.parser;

import no.uio.ifi.asp.runtime.RuntimeIntValue;
import no.uio.ifi.asp.runtime.RuntimeReturnValue;
import no.uio.ifi.asp.runtime.RuntimeScope;
import no.uio.ifi.asp.runtime.RuntimeValue;
import no.uio.ifi.asp.scanner.Scanner;
import no.uio.ifi.asp.scanner.TokenKind;

public class AspIntegerLiteral extends AspAtom{

    long number;

    AspIntegerLiteral(int n) {
        super(n);
        //TODO Auto-generated constructor stub
    }

    static AspIntegerLiteral parse(Scanner s){
        AspIntegerLiteral atl = new AspIntegerLiteral(s.curLineNum());

        enterParser("integer literal");
        
        atl.number = s.curToken().integerLit;
        skip(s, TokenKind.integerToken);

        leaveParser("integer literal");

        return atl;

    }

    @Override
    void prettyPrint() {
        prettyWrite(Long.toString(number));
    }

    @Override
    RuntimeValue eval(RuntimeScope curScope) throws RuntimeReturnValue {
        return new RuntimeIntValue(number);
    }
    
}
