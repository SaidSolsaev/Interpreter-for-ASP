
package no.uio.ifi.asp.parser;

import java.util.ArrayList;

import no.uio.ifi.asp.runtime.*;
import no.uio.ifi.asp.scanner.*;

public class AspExpr extends AspSyntax {
    // -- Must be changed in part 2:
   
    ArrayList<AspAndTest> andTests = new ArrayList<>();
    AspExpr(int n) {
        super(n);
    }

    public static AspExpr parse(Scanner s) {
        enterParser("expr");

        // -- Must be changed in part 2:
        AspExpr ae = new AspExpr(s.curLineNum());

        while(true){
            ae.andTests.add(AspAndTest.parse(s));

            if(s.curToken().kind != TokenKind.orToken){ //nothing more to check
                break;
            }else{
                skip(s, TokenKind.orToken);
            }}

        leaveParser("expr");

        return ae;
    }

    @Override
    public void prettyPrint() {
        // -- Must be changed in part 2:
        int p = 0;

        for (AspAndTest x : andTests){
            if (p > 0){
                prettyWrite(" or ");
            }
            x.prettyPrint();
            p++;
        }
    }

    @Override
    public RuntimeValue eval(RuntimeScope curScope) throws RuntimeReturnValue {
        
        RuntimeValue v = andTests.get(0).eval(curScope);
        

        for (int i = 1; i < andTests.size(); i++){
            if (v.getBoolValue("or operand", this)){
                
                return v;
            }
            v = andTests.get(i).eval(curScope);
        }
        
        return v;
    }
}
