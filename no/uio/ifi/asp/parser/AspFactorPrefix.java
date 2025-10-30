package no.uio.ifi.asp.parser;

import no.uio.ifi.asp.runtime.RuntimeReturnValue;
import no.uio.ifi.asp.runtime.RuntimeScope;
import no.uio.ifi.asp.runtime.RuntimeValue;
import no.uio.ifi.asp.scanner.Scanner;
import no.uio.ifi.asp.scanner.TokenKind;

public class AspFactorPrefix extends AspSyntax {
    String f;
    TokenKind kind;

    AspFactorPrefix(int n) {
        super(n);
        //TODO Auto-generated constructor stub
    }

    static AspFactorPrefix parse(Scanner s){
        enterParser("factor prefix");

        AspFactorPrefix afp = new AspFactorPrefix(s.curLineNum());

        if (s.curToken().kind == TokenKind.plusToken){
            afp.f = "+";
            afp.kind = s.curToken().kind;
            skip(s, TokenKind.plusToken);
        } else if (s.curToken().kind == TokenKind.minusToken){
            afp.f = "-";
            afp.kind = s.curToken().kind;
            skip(s, TokenKind.minusToken);
        }

        leaveParser("factor prefix");
        return afp;
    }


    @Override
    void prettyPrint() {
        prettyWrite(f);
    }

    @Override
    RuntimeValue eval(RuntimeScope curScope) throws RuntimeReturnValue {
        return null;
    }
    
}
