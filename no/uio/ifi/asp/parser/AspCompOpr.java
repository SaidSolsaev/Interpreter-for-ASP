package no.uio.ifi.asp.parser;

import no.uio.ifi.asp.runtime.RuntimeReturnValue;
import no.uio.ifi.asp.runtime.RuntimeScope;
import no.uio.ifi.asp.runtime.RuntimeValue;
import no.uio.ifi.asp.scanner.Scanner;
import no.uio.ifi.asp.scanner.TokenKind;

public class AspCompOpr extends AspSyntax {
    String tegn;
    
    
    TokenKind kind;

    AspCompOpr(int n) {
        super(n);
        //TODO Auto-generated constructor stub
    }

    static AspCompOpr parse(Scanner s){
        enterParser("comp opr");
        AspCompOpr aco = new AspCompOpr(s.curLineNum());

        if (s.curToken().kind == TokenKind.doubleEqualToken){
            aco.tegn = " == ";
            aco.kind = TokenKind.doubleEqualToken;
            skip(s, TokenKind.doubleEqualToken);
        }
        else if(s.curToken().kind == TokenKind.greaterToken){
            aco.tegn = " > ";
            aco.kind = TokenKind.greaterToken;
            skip(s, TokenKind.greaterToken);
            
        } else if (s.curToken().kind == TokenKind.lessToken){
            aco.tegn = " < ";
            aco.kind = TokenKind.lessToken;
            skip(s, TokenKind.lessToken);

        }else if (s.curToken().kind == TokenKind.greaterEqualToken){
            aco.tegn = " >= ";
            aco.kind = TokenKind.greaterEqualToken;
            skip(s, TokenKind.greaterEqualToken);

        }else if (s.curToken().kind == TokenKind.lessEqualToken){
            aco.tegn = " <= ";
            aco.kind = TokenKind.lessEqualToken;
            skip(s, TokenKind.lessEqualToken);

        } else if (s.curToken().kind == TokenKind.notEqualToken){
            aco.tegn = " != ";
            aco.kind = TokenKind.notEqualToken;
            skip(s, TokenKind.notEqualToken);
        }
        else{
            parserError("Expected an expression comp opr, but found a " + s.curToken().kind + "!", s.curLineNum());
        }
        
        leaveParser("comp opr");
        return aco;
    }

    @Override
    void prettyPrint() {
        prettyWrite(tegn);
    }

    @Override
    RuntimeValue eval(RuntimeScope curScope) throws RuntimeReturnValue {
        return null; //aldri brukt 
    }
    
}
