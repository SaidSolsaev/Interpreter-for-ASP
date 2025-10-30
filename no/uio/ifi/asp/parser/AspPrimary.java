package no.uio.ifi.asp.parser;

import java.util.ArrayList;

import no.uio.ifi.asp.runtime.RuntimeDictValue;
import no.uio.ifi.asp.runtime.RuntimeFunc;
import no.uio.ifi.asp.runtime.RuntimeListValue;
import no.uio.ifi.asp.runtime.RuntimeReturnValue;
import no.uio.ifi.asp.runtime.RuntimeScope;
import no.uio.ifi.asp.runtime.RuntimeStringValue;
import no.uio.ifi.asp.runtime.RuntimeValue;
import no.uio.ifi.asp.scanner.Scanner;
import no.uio.ifi.asp.scanner.TokenKind;

public class AspPrimary extends AspSyntax {
    AspAtom test;
    ArrayList<AspPrimarySuffix> body = new ArrayList<>();

    AspPrimary(int n) {
        super(n);
        // TODO Auto-generated constructor stub
    }

    static AspPrimary parse(Scanner s) {
        enterParser("primary");
        AspPrimary aspP = new AspPrimary(s.curLineNum());

        aspP.test = AspAtom.parse(s);

        while (s.curToken().kind == TokenKind.leftParToken || s.curToken().kind == TokenKind.leftBracketToken) {
            aspP.body.add(AspPrimarySuffix.parse(s));
        }

        leaveParser("primary");
        return aspP;
    }

    @Override
    void prettyPrint() {
        test.prettyPrint();
        if (body.size() != 0) {
            for (AspPrimarySuffix x : body) {
                x.prettyPrint();
            }
        }
    }

    @Override
    RuntimeValue eval(RuntimeScope curScope) throws RuntimeReturnValue {
        RuntimeValue v = test.eval(curScope);
        

        for (AspPrimarySuffix apsf : body) {
            if (v instanceof RuntimeListValue || v instanceof RuntimeStringValue || v instanceof RuntimeDictValue) {
                v = v.evalSubscription(apsf.eval(curScope), this);
                
                
            } else if (apsf instanceof AspArguments) {
                
                if (test instanceof AspName) {
                    AspName navn = (AspName) test;
                    RuntimeValue funksjon = curScope.find(navn.name, this);

                    if (funksjon instanceof RuntimeFunc) {
                        RuntimeFunc func = (RuntimeFunc) funksjon;
                        AspArguments args = (AspArguments) apsf;
                        ArrayList<RuntimeValue> par = new ArrayList<>();
                        
                        String s = "(";

                        for (AspExpr x : args.expr) {
                            RuntimeValue val = x.eval(curScope);
                            par.add(val);

                            s += val.showInfo();
                            if (x != args.expr.get(args.expr.size() - 1)) {
                                s += ", ";
                            }
                            
                        }

                        trace("Function: " + navn.name + s + "):");
                
                        // System.out.println("liste" + par);
                        // System.out.println("parameter: " + v);

                        v = func.evalFuncCall(par, this);
                    }

                }
            }
        }

        return v;
    }

}
