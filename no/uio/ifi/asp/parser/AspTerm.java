package no.uio.ifi.asp.parser;

import java.util.ArrayList;

import no.uio.ifi.asp.main.Main;
import no.uio.ifi.asp.runtime.RuntimeReturnValue;
import no.uio.ifi.asp.runtime.RuntimeScope;
import no.uio.ifi.asp.runtime.RuntimeValue;
import no.uio.ifi.asp.scanner.Scanner;
import no.uio.ifi.asp.scanner.TokenKind;

public class AspTerm extends AspSyntax{
    ArrayList<AspFactor> fct = new ArrayList<>();
    ArrayList<AspTermOpr> terms = new ArrayList<>();

    AspTerm(int n) {
        super(n);
        //TODO Auto-generated constructor stub
    }

    static AspTerm parse(Scanner s){
        enterParser("term");
        AspTerm at = new AspTerm(s.curLineNum());

        while (true){
            at.fct.add(AspFactor.parse(s));

            if (s.isTermOpr()){
                at.terms.add(AspTermOpr.parse(s));
            } 
            else{
                break;
            }
        }

        leaveParser("term");
        return at;
    }

    @Override
    void prettyPrint() {
        int p = 0;

        for (AspFactor x : fct){
            x.prettyPrint();
            if (p < terms.size() && terms.size() != 0){
                terms.get(p).prettyPrint();
            }
            p++;
        }
        
    }

    //fra prekoden
    @Override
    RuntimeValue eval(RuntimeScope curScope) throws RuntimeReturnValue {

        RuntimeValue v = fct.get(0).eval(curScope); //gir første faktor

        //hvis det er flere går vi inn i listen:
        for (int i = 1; i < fct.size(); i++) {
            TokenKind k = terms.get(i-1).kind; //en forskøvet så da blir det i-1

            switch (k) { //sjekker om det er minus eller pluss
                case minusToken: 
                    
                    v = v.evalSubtract(fct.get(i).eval(curScope), this); //sender med this for feilmeldingskyld
                    break;
                case plusToken:
                    
                    v = v.evalAdd(fct.get(i).eval(curScope), this);
                    break;
                default: //hvis det verken er pluss eller minus så er det feil
                    Main.panic("Illegal term operator: " + k + "!");
            }
        }
        trace(v.toString());
        
        return v;
        
    }
    
}
