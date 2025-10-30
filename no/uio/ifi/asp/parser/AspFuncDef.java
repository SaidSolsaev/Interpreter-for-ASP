package no.uio.ifi.asp.parser;
import java.util.ArrayList;
import no.uio.ifi.asp.runtime.RuntimeFunc;
import no.uio.ifi.asp.runtime.RuntimeReturnValue;
import no.uio.ifi.asp.runtime.RuntimeScope;
import no.uio.ifi.asp.runtime.RuntimeValue;
import no.uio.ifi.asp.scanner.Scanner;
import no.uio.ifi.asp.scanner.TokenKind;

public class AspFuncDef extends AspCompoundStmt {
    public ArrayList<AspName> name = new ArrayList<>();
    public AspSuite suite;
    AspName n;

    AspFuncDef(int n) {
        super(n);
    }

    static AspFuncDef parse(Scanner s) {
        AspFuncDef afd = new AspFuncDef(s.curLineNum());
        enterParser("func def");

        skip(s, TokenKind.defToken);
        afd.n = AspName.parse(s);
        // afd.name.add(AspName.parse(s));
    
        skip(s, TokenKind.leftParToken);

        while (s.curToken().kind == TokenKind.nameToken || s.curToken().kind == TokenKind.commaToken) {
            afd.name.add(AspName.parse(s));

            if (s.curToken().kind == TokenKind.commaToken) {
                skip(s, TokenKind.commaToken);
            }

        }
        skip(s, TokenKind.rightParToken);
        skip(s, TokenKind.colonToken);

        afd.suite = AspSuite.parse(s);

        leaveParser("func def");
        return afd;

    }

    @Override
    void prettyPrint() {
        prettyWrite("def ");
        // name.get(0).prettyPrint();
        n.prettyPrint();
        prettyWrite(" (");

        int p = 0;

        if (1 < name.size()) {
            for (int i = 1; i < name.size(); i++) {
                if (p > 0) {
                    prettyWrite(", ");
                }
                name.get(i).prettyPrint();
                p++;
            }
        }

        prettyWrite("):");

        prettyIndent();
        suite.prettyPrint();
        prettyDedent();
    }

    @Override
    RuntimeValue eval(RuntimeScope curScope) throws RuntimeReturnValue {
        
        trace("def " + n.name);
        RuntimeFunc def_funk = new RuntimeFunc(this, curScope, n.name);
        curScope.assign(n.name, def_funk);
        
        return def_funk;
    }

   
    

}
