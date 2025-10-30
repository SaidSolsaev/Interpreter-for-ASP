package no.uio.ifi.asp.parser;
import java.util.ArrayList;
import no.uio.ifi.asp.runtime.RuntimeReturnValue;
import no.uio.ifi.asp.runtime.RuntimeScope;
import no.uio.ifi.asp.runtime.RuntimeValue;
import no.uio.ifi.asp.scanner.Scanner;
import no.uio.ifi.asp.scanner.TokenKind;

public class AspGlobalStmt extends AspSmallStmt{
    ArrayList<AspName> name = new ArrayList<>();;

    AspGlobalStmt(int n) {
        super(n);
    }

    static AspGlobalStmt parse(Scanner s){
        enterParser("global stmt");
        AspGlobalStmt ags = new AspGlobalStmt(s.curLineNum());
        
        skip(s, TokenKind.globalToken);
        ags.name.add(AspName.parse(s));
        
        while(s.curToken().kind == TokenKind.commaToken){
            skip(s, TokenKind.commaToken);
            ags.name.add(AspName.parse(s));  
        }


        leaveParser("global stmt");
        return ags;
        
    }
    @Override
    void prettyPrint() {
        prettyWrite("global ");
        name.get(0).prettyPrint();

        int p = 1;
        if (p < name.size()){
            prettyWrite(", ");
           
            while (p < name.size()){
                name.get(p).prettyPrint();
            }
        }
        
    }

    @Override
    RuntimeValue eval(RuntimeScope curScope) throws RuntimeReturnValue {
        // Registrere alle globale variabler.
        for (AspName n : name){
            //n.eval(curScope);
            curScope.registerGlobalName(n.name);
        }
        return null;
    }
    
}
