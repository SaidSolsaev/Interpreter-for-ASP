package no.uio.ifi.asp.parser;

import no.uio.ifi.asp.runtime.RuntimeReturnValue;
import no.uio.ifi.asp.runtime.RuntimeScope;
import no.uio.ifi.asp.runtime.RuntimeValue;
import no.uio.ifi.asp.scanner.Scanner;
import no.uio.ifi.asp.scanner.TokenKind;

public class AspName extends AspAtom {
    public String name;

    AspName(int n) {
        super(n);
        //TODO Auto-generated constructor stub
    }
    
    static AspName parse(Scanner s){
        AspName an = new AspName(s.curLineNum());

        enterParser("name");
        
        an.name = s.curToken().name;
        skip(s, TokenKind.nameToken);

        leaveParser("name");


        return an;
}

    @Override
    void prettyPrint() {
        prettyWrite(name);
    }

    @Override
    RuntimeValue eval(RuntimeScope curScope) throws RuntimeReturnValue {
        return curScope.find(name, this);
    }

    
}
