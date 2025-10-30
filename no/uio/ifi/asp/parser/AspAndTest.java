package no.uio.ifi.asp.parser;
import java.util.ArrayList;
import no.uio.ifi.asp.runtime.RuntimeReturnValue;
import no.uio.ifi.asp.runtime.RuntimeScope;
import no.uio.ifi.asp.runtime.RuntimeValue;
import no.uio.ifi.asp.scanner.Scanner;
import no.uio.ifi.asp.scanner.TokenKind;

public class AspAndTest extends AspSyntax {
    ArrayList<AspNotTest> notTests = new ArrayList<>();
    
    AspAndTest(int n) {
        super(n);
        //TODO Auto-generated constructor stub
    }

    public static AspAndTest parse(Scanner s) {
        enterParser("and test");

        AspAndTest ae = new AspAndTest(s.curLineNum());

        while(true){
            ae.notTests.add(AspNotTest.parse(s));

            if(s.curToken().kind != TokenKind.andToken){ //nothing more to check
                break;
            }else{
                skip(s, TokenKind.andToken);
            }
        
        }

        leaveParser("and test");

       
        return ae;
    }

    @Override
    void prettyPrint() {
        int p = 0;

        for (AspNotTest x : notTests){
            if (p > 0){
                prettyWrite(" and ");
            }
            x.prettyPrint();
            p++;
        }
        
    }

    //kodet basert på forklaring 3.4.2.4
    @Override
    RuntimeValue eval(RuntimeScope curScope) throws RuntimeReturnValue {
        RuntimeValue v = notTests.get(0).eval(curScope); 

        //går inn i listen
        for (int i = 1; i < notTests.size(); ++i) {
            //hvis den ikke er sant, så returner
            if (! v.getBoolValue("and operand", this)) {
                return v;
            }
            //ellers gå til nesten til vi finner noe ikke sant
            v = notTests.get(i).eval(curScope);
        }
        
        return v;
    }
    
}
