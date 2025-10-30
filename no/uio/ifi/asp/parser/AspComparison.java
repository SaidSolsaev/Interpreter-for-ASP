package no.uio.ifi.asp.parser;

import java.util.ArrayList;

import no.uio.ifi.asp.main.Main;
import no.uio.ifi.asp.runtime.RuntimeReturnValue;
import no.uio.ifi.asp.runtime.RuntimeScope;
import no.uio.ifi.asp.runtime.RuntimeValue;
import no.uio.ifi.asp.scanner.Scanner;
import no.uio.ifi.asp.scanner.TokenKind;


public class AspComparison extends AspSyntax {

    ArrayList<AspCompOpr> compOpr = new ArrayList<>();
    ArrayList<AspTerm> term = new ArrayList<>();

    AspComparison(int n) {
        super(n);
        
    }

    static AspComparison parse(Scanner s){
        AspComparison ac = new AspComparison(s.curLineNum());

        enterParser("comparison");

         while(true){
            ac.term.add(AspTerm.parse(s));

            if(!s.isCompOpr()){
                break;
            }
            
            else{
                ac.compOpr.add(AspCompOpr.parse(s));
            }
        }
        leaveParser("comparison");
        return ac;
    }
    

    @Override
    void prettyPrint() {
        int p = 0;

        for (AspTerm x : term){
            int i = 0;
            
            if (p > 0){
                compOpr.get(i).prettyPrint();
                i++;
            }
            x.prettyPrint();
            p++;
        }
    }

    @Override
    RuntimeValue eval(RuntimeScope curScope) throws RuntimeReturnValue {
        RuntimeValue v = term.get(0).eval(curScope);

        for (int i = 1; i < term.size(); i++){
            TokenKind k = compOpr.get(i-1).kind;
            v = term.get(i-1).eval(curScope);

            switch(k){
                case lessToken:
                    v = v.evalLess(term.get(i).eval(curScope), this);
                    break;
                case greaterToken:
                    v = v.evalGreater(term.get(i).eval(curScope), this); 
                    break;
                case doubleEqualToken:
                    v = v.evalEqual(term.get(i).eval(curScope), this); 
                    break;
                case greaterEqualToken:
                    v = v.evalGreaterEqual(term.get(i).eval(curScope), this); 
                    break;
                case lessEqualToken:
                    v = v.evalLessEqual(term.get(i).eval(curScope), this); 
                    break;
                case notEqualToken:
                    v = v.evalNotEqual(term.get(i).eval(curScope), this); 
                    break;
                default:
                    Main.panic("Illegal comparison operator: " + k + "!");
                
            }
            if (!v.getBoolValue("Term comparsion", this)){
                return v;
            }
        }
        return v;
    }
    
}
