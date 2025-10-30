package no.uio.ifi.asp.parser;

import java.util.ArrayList;

import no.uio.ifi.asp.main.Main;
import no.uio.ifi.asp.runtime.RuntimeReturnValue;
import no.uio.ifi.asp.runtime.RuntimeScope;
import no.uio.ifi.asp.runtime.RuntimeValue;
import no.uio.ifi.asp.scanner.Scanner;
import no.uio.ifi.asp.scanner.TokenKind;

public class AspFactor extends AspSyntax {
    ArrayList<AspFactorPrefix> facPreList = new ArrayList<>();
    ArrayList<AspPrimary> primList = new ArrayList<>();
    ArrayList<AspFactorOpr> facOprList = new ArrayList<>();

    AspFactor(int n) {
        super(n);
        //TODO Auto-generated constructor stub
    }

    static AspFactor parse(Scanner s){
        enterParser("factor");
        AspFactor aspF = new AspFactor(s.curLineNum());
        
        while(true){
            if (s.isFactorPrefix()){
                aspF.facPreList.add(AspFactorPrefix.parse(s));
            }
            else{
                aspF.facPreList.add(null);
            }

            aspF.primList.add(AspPrimary.parse(s));

            if (s.isFactorOpr()){
                aspF.facOprList.add(AspFactorOpr.parse(s));
            } else{
                break;
            }
        }

        leaveParser("factor");
        return aspF;
    }

    @Override
    void prettyPrint() {
        
        for(int p = 0; p < primList.size(); p++){
            if (facPreList.get(p) != null){
                facPreList.get(p).prettyPrint();
            }
            primList.get(p).prettyPrint();

            if (p < facOprList.size()){
                facOprList.get(p).prettyPrint();
            }
        
        }
    }

    @Override
    RuntimeValue eval(RuntimeScope curScope) throws RuntimeReturnValue {
        RuntimeValue v = primList.get(0).eval(curScope);
        
        if (facPreList.size() != 0){
            if (facPreList.get(0) != null){
                
                TokenKind k = facPreList.get(0).kind;

                if (k == TokenKind.minusToken){
                    v = v.evalNegate(this);
                } else if (k == TokenKind.plusToken){
                    v = v.evalPositive(this);
                } else{
                    Main.panic("Illegal factor operator" + k  + "!");
                } 
            }
        } 
        
        for (int i = 1; i < primList.size(); i++){
            TokenKind k = facOprList.get(i-1).kind;
            RuntimeValue n = primList.get(i).eval(curScope);

            if (k == TokenKind.astToken){
                v = v.evalMultiply(n, this);
            } 
            else if (k == TokenKind.percentToken){
                v = v.evalModulo(n, this);
            } 
            else if(k == TokenKind.slashToken){
                v = v.evalDivide(n, this);
            } 
            else if(k == TokenKind.doubleSlashToken){
                v = v.evalIntDivide(n, this);
            } else{
                Main.panic("illegal factor operator " + k  + "!");
            }

            if (facPreList.get(i) != null){
                TokenKind nextK = facPreList.get(i).kind;

                if (nextK == TokenKind.minusToken){
                    n = n.evalNegate(this);
                } else if (nextK == TokenKind.plusToken){
                    n = n.evalPositive(this);
                } else{
                    Main.panic("Illegal factor operator" + k  + "!");
                }
            }
        }
        
        return v;
    }
    
}
