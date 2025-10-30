package no.uio.ifi.asp.parser;

import java.util.ArrayList;

import no.uio.ifi.asp.runtime.RuntimeReturnValue;
import no.uio.ifi.asp.runtime.RuntimeScope;
import no.uio.ifi.asp.runtime.RuntimeValue;
import no.uio.ifi.asp.scanner.Scanner;
import no.uio.ifi.asp.scanner.TokenKind;

public class AspSuite extends AspSyntax {
    ArrayList<AspStmt> stmts = new ArrayList<>();
    AspSmallStmtList assl;

    AspSuite(int n) {
        super(n);
        
    }

    static AspSuite parse(Scanner s){
        enterParser("suite");
        AspSuite asu = new AspSuite(s.curLineNum());

        if (s.curToken().kind == TokenKind.newLineToken){
            skip(s, TokenKind.newLineToken);
            skip(s, TokenKind.indentToken);
    
            while (s.curToken().kind != TokenKind.dedentToken){
                asu.stmts.add(AspStmt.parse(s));
            }
            skip(s, TokenKind.dedentToken);
        } else {
            asu.assl = AspSmallStmtList.parse(s);
        }

        leaveParser("suite");
        return asu;
    }

    @Override
    void prettyPrint() {
        if (assl == null){
            prettyWriteLn();
            prettyIndent();

            for (AspStmt x : stmts){
                x.prettyPrint();
            }
            
            prettyDedent();
        } else{
            assl.prettyPrint();
        }
    }

    @Override
    public RuntimeValue eval(RuntimeScope curScope) throws RuntimeReturnValue {
        
        if (assl == null){
            for (AspStmt as : stmts){
                as.eval(curScope);
            }
        } else if (assl != null){
            assl.eval(curScope);
        }
        
        return null;
    }
    
}
