package no.uio.ifi.asp.parser;
import java.util.ArrayList;
import no.uio.ifi.asp.runtime.RuntimeReturnValue;
import no.uio.ifi.asp.runtime.RuntimeScope;
import no.uio.ifi.asp.runtime.RuntimeValue;
import no.uio.ifi.asp.scanner.Scanner;
import no.uio.ifi.asp.scanner.TokenKind;

public class AspIfStmt extends AspCompoundStmt {
    ArrayList<AspExpr> test = new ArrayList<>();
    ArrayList<AspSuite> body = new ArrayList<>();
    
    AspIfStmt(int n) {
        super(n);
        //TODO Auto-generated constructor stub
    }


    static AspIfStmt parse(Scanner s) {

        enterParser("if stmt");
        AspIfStmt ais = new AspIfStmt(s.curLineNum());
        
        skip(s, TokenKind.ifToken);
        ais.test.add(AspExpr.parse(s));
        skip(s, TokenKind.colonToken);
        ais.body.add(AspSuite.parse(s));

        //Så lenge det er elif tokens ->
        //While her siden det kan være flere elif.
        while(s.curToken().kind == TokenKind.elifToken){
            skip(s, TokenKind.elifToken);
            ais.test.add(AspExpr.parse(s));
            skip(s, TokenKind.colonToken);
            ais.body.add(AspSuite.parse(s));
        }
        
        //Om det er en else token ->
        if (s.curToken().kind == TokenKind.elseToken){
            skip(s, TokenKind.elseToken);
             // må fjernes
            skip(s, TokenKind.colonToken);
            ais.body.add(AspSuite.parse(s));
        }

        leaveParser("if stmt");
        return ais;
    }

    @Override
    public void prettyPrint() {
        prettyWrite("if ");
        test.get(0).prettyPrint();
        prettyWrite(":");
        body.get(0).prettyPrint();
        

        int i = 1;
        for (int j = i; j < test.size(); j++){
            prettyWrite("elif ");
            test.get(i).prettyPrint();
            prettyWrite(":");
            prettyIndent();
            body.get(i).prettyPrint();
            prettyDedent();
            i = j;
        }

        if (body.size() > test.size()){
            prettyWrite("else");
            prettyWrite(":");
            prettyIndent();
            body.get(i).prettyPrint();
            prettyDedent();
        }
    }

    
    @Override
    RuntimeValue eval(RuntimeScope curScope) throws RuntimeReturnValue {
        int i = 0;
        RuntimeValue v = null;

        for (AspExpr aspExpr : test) {
            
            RuntimeValue t = aspExpr.eval(curScope);
            
            if(t.getBoolValue("eval if stmt", this)){ //hvis sann utfør suite
                trace("If True: ");
                v = body.get(i).eval(curScope); //suite
                return v;
            }
            i++;
        }
        //hvis expr liste er mindre enn suite liste da er det else
        if (body.size() > test.size()){
            trace("else: ...");
            v = body.get(body.size()-1).eval(curScope);
            return v;
        } 
        return null;
    
    } 
    
    }