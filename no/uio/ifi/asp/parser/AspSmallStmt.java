package no.uio.ifi.asp.parser;


import no.uio.ifi.asp.scanner.Scanner;

public abstract class AspSmallStmt extends AspSyntax{

    AspSmallStmt(int n) {
        super(n);
        //TODO Auto-generated constructor stub
    }

    static AspSmallStmt parse(Scanner s){
        AspSmallStmt ass = null;
        
        enterParser("small stmt");
           
        if (s.anyEqualToken()){
            ass = AspAssignment.parse(s);  
        }

        else{
        switch(s.curToken().kind){
            case passToken:
                ass = AspPassStmt.parse(s);
                break;
            case globalToken:
                ass = AspGlobalStmt.parse(s);
                break;
            case returnToken:
                ass = AspReturnStmt.parse(s);
                break;
            default:
                ass = AspExprStmt.parse(s);
                break;
                //parserError("Expected an expression small stmt but found a " + s.curToken().kind + " ! ", s.curLineNum());
        }}

        leaveParser("small stmt");
        return ass;
    }
    
}
