package no.uio.ifi.asp.parser;

import no.uio.ifi.asp.runtime.RuntimeNoneValue;
import no.uio.ifi.asp.runtime.RuntimeReturnValue;
import no.uio.ifi.asp.runtime.RuntimeScope;
import no.uio.ifi.asp.runtime.RuntimeValue;
import no.uio.ifi.asp.scanner.Scanner;
import no.uio.ifi.asp.scanner.TokenKind;

public class AspNoneLiteral extends AspAtom {

    AspNoneLiteral(int n) {
        super(n);
        //TODO Auto-generated constructor stub
    }

    static AspNoneLiteral parse(Scanner s){
        AspNoneLiteral anl = new AspNoneLiteral(s.curLineNum());

        enterParser("none literal");

        skip(s, TokenKind.noneToken);
    
        leaveParser("none literal");

        return anl;

    }

    @Override
    void prettyPrint() {
        prettyWrite(" None ");;
    }

    @Override
    RuntimeValue eval(RuntimeScope curScope) throws RuntimeReturnValue {
        return new RuntimeNoneValue();
    }
}
