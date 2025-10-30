package no.uio.ifi.asp.parser;

import java.util.ArrayList;

import no.uio.ifi.asp.runtime.RuntimeReturnValue;
import no.uio.ifi.asp.runtime.RuntimeScope;
import no.uio.ifi.asp.runtime.RuntimeValue;
import no.uio.ifi.asp.scanner.Scanner;
import no.uio.ifi.asp.scanner.TokenKind;

public class AspSmallStmtList extends AspStmt {
    ArrayList<AspSmallStmt> stmts = new ArrayList<>();

    AspSmallStmtList(int n) {
        super(n);
        //TODO Auto-generated constructor stub
    }

    static AspSmallStmtList parse(Scanner s){
        enterParser("small stmt list");
        AspSmallStmtList assl = new AspSmallStmtList(s.curLineNum());

        while (true){

            if (!(s.curToken().kind != TokenKind.newLineToken)){
                break;
            }
            assl.stmts.add(AspSmallStmt.parse(s));
            

            if (s.curToken().kind == TokenKind.semicolonToken){
                skip(s, TokenKind.semicolonToken);
                
            }
        }
        skip(s, TokenKind.newLineToken);
        
        

        leaveParser("small stmt list");
        return assl;
    }

    @Override
    void prettyPrint() {
        int p = 0;

        for (AspSmallStmt x : stmts){
            if (p > 0){
                prettyWrite("; ");
            }
            x.prettyPrint();
            p++;
        }
        prettyWriteLn();
    }

    @Override
    RuntimeValue eval(RuntimeScope curScope) throws RuntimeReturnValue {
       
        RuntimeValue v = null;
        for (AspSmallStmt stmt : stmts) {
            v = stmt.eval(curScope);
        }
        return v;
        
    }
    
}
