package no.uio.ifi.asp.parser;
import java.util.ArrayList;
import no.uio.ifi.asp.runtime.RuntimeReturnValue;
import no.uio.ifi.asp.runtime.RuntimeScope;
import no.uio.ifi.asp.runtime.RuntimeValue;
import no.uio.ifi.asp.scanner.Scanner;
import no.uio.ifi.asp.scanner.TokenKind;

public class AspAssignment extends AspSmallStmt {
    AspName name;
    AspExpr expr;
    ArrayList<AspSubscription> sub = new ArrayList<>();

    AspAssignment(int n) {
        super(n);

    }

    static AspAssignment parse(Scanner s) {
        AspAssignment aam = new AspAssignment(s.curLineNum());

        enterParser("assignment");
        aam.name = AspName.parse(s);

        while (s.curToken().kind != TokenKind.equalToken) {
            aam.sub.add(AspSubscription.parse(s));
        }
        skip(s, TokenKind.equalToken);

        aam.expr = AspExpr.parse(s);

        leaveParser("assignment");

        return aam;
    }

    @Override
    public void prettyPrint() {
        name.prettyPrint();
        for (AspSubscription x : sub) {
            x.prettyPrint();
        }
        prettyWrite(" = ");
        expr.prettyPrint();
    }

    @Override
    RuntimeValue eval(RuntimeScope curScope) throws RuntimeReturnValue {
        RuntimeValue exp = expr.eval(curScope);

        if (sub.isEmpty()) {
            String id = name.name;
            curScope.assign(id, exp);
            trace(id + " is assigned to: " + exp.toString());
        } else {
            RuntimeValue exp1 = name.eval(curScope);

            for (int i = 0; i < sub.size() - 1; i++) {
                RuntimeValue exp2 = sub.get(i).eval(curScope);
                exp1 = exp1.evalSubscription(exp2, this);
            }

            AspSubscription lastSubsc = sub.get(sub.size() - 1);
            RuntimeValue index = lastSubsc.eval(curScope);
            trace(name.name + "[ " + index.showInfo() + " ] is assigned to " + exp.toString());
            exp1.evalAssignElem(index, exp, this);
        }
        
        return null;
    }

}
