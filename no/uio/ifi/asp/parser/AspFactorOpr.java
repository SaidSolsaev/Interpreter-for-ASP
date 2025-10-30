package no.uio.ifi.asp.parser;

import no.uio.ifi.asp.runtime.RuntimeReturnValue;
import no.uio.ifi.asp.runtime.RuntimeScope;
import no.uio.ifi.asp.runtime.RuntimeValue;
import no.uio.ifi.asp.scanner.Scanner;
import no.uio.ifi.asp.scanner.TokenKind;

public class AspFactorOpr extends AspSyntax {
    String opr;
    TokenKind kind;

    AspFactorOpr(int n) {
        super(n);
        //TODO Auto-generated constructor stub
    }

    static AspFactorOpr parse(Scanner s){
        enterParser("factor opr");

        AspFactorOpr afo = new AspFactorOpr(s.curLineNum());

        if (s.curToken().kind == TokenKind.slashToken){
            afo.opr = " / ";
            afo.kind = s.curToken().kind;
            
            skip(s,TokenKind.slashToken);
        } else if ( s.curToken().kind == TokenKind.doubleSlashToken){
            afo.opr = " // ";
            afo.kind = s.curToken().kind;
            skip(s,TokenKind.doubleSlashToken);
        }else if ( s.curToken().kind == TokenKind.astToken){
            afo.opr = " * ";
            afo.kind = s.curToken().kind;
            skip(s,TokenKind.astToken);
        } else if ( s.curToken().kind == TokenKind.percentToken){
            afo.opr = " % ";
            afo.kind = s.curToken().kind;
            skip(s, TokenKind.percentToken);
        }
        
        leaveParser("factor opr");
        return afo;
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
