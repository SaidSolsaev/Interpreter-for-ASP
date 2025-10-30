package no.uio.ifi.asp.parser;

import no.uio.ifi.asp.runtime.RuntimeReturnValue;
import no.uio.ifi.asp.runtime.RuntimeScope;
import no.uio.ifi.asp.runtime.RuntimeValue;
import no.uio.ifi.asp.scanner.Scanner;
import no.uio.ifi.asp.scanner.TokenKind;

public class AspTermOpr extends AspSyntax {
    String opr;
    
    //for eval i term klassen
    TokenKind kind;

    AspTermOpr(int n) {
        super(n);
        
    }

    static AspTermOpr parse(Scanner s){
        enterParser("term opr");

        AspTermOpr ato = new AspTermOpr(s.curLineNum());

        if (s.curToken().kind == TokenKind.plusToken){
            ato.opr = " + ";
            ato.kind = s.curToken().kind;
            skip(s, TokenKind.plusToken);
        } else if (s.curToken().kind == TokenKind.minusToken){
            ato.opr = " - ";
            ato.kind = s.curToken().kind;
            skip(s, TokenKind.minusToken);
        }

        leaveParser("term opr");

        return ato;
    }

    @Override
    void prettyPrint() {
        prettyWrite(opr);
    }

    @Override
    RuntimeValue eval(RuntimeScope curScope) throws RuntimeReturnValue {
        return null;
    }
    
}
