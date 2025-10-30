package no.uio.ifi.asp.parser;

import no.uio.ifi.asp.scanner.Scanner;

public abstract class AspPrimarySuffix extends AspSyntax {

    AspPrimarySuffix(int n) {
        super(n);
        // TODO Auto-generated constructor stub
    }

    static AspPrimarySuffix parse(Scanner s) {
        enterParser("primary suffix");

        AspPrimarySuffix aspPS = null;

        switch (s.curToken().kind) {
            case leftBracketToken:
                aspPS = AspSubscription.parse(s);
                break;
            case leftParToken:
                aspPS = AspArguments.parse(s);
                break;
            default:
                parserError("Expected an expression primary suffix, but found a " + s.curToken().kind + "!",
                        s.curLineNum());
        }

        leaveParser("primary suffix");
        return aspPS;
    }
}
