package no.uio.ifi.asp.parser;

import no.uio.ifi.asp.runtime.RuntimeReturnValue;
import no.uio.ifi.asp.runtime.RuntimeScope;
import no.uio.ifi.asp.runtime.RuntimeValue;
import no.uio.ifi.asp.scanner.Scanner;
import no.uio.ifi.asp.scanner.TokenKind;

public class AspNotTest extends AspSyntax {
    AspComparison ac;
    Boolean n = false;

    AspNotTest(int n) {
        super(n);
        //TODO Auto-generated constructor stub
    }

    static AspNotTest parse(Scanner s) {
        enterParser("not test");

        AspNotTest ae = new AspNotTest(s.curLineNum());

        if(s.curToken().kind == TokenKind.notToken){
            skip(s, TokenKind.notToken);
            ae.n = true;
        }
        

        ae.ac = AspComparison.parse(s);

        leaveParser("not test");

       
        return ae;
    }
    

    @Override
    void prettyPrint() {
        if (n != null && n == true){
            prettyWrite("not ");
        }
        ac.prettyPrint();
    }

    //fra forelesning
    @Override
    RuntimeValue eval(RuntimeScope curScope) throws RuntimeReturnValue { 
        RuntimeValue v = ac.eval(curScope); //svar fra det comparison sin eval ga oss
        if(n){ //n
            v =  v.evalNot(this); //evalNot vil snu verdien i v, som er en logisk verdi
        }
        return v;
    }
    
}
