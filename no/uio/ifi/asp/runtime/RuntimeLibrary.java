
package no.uio.ifi.asp.runtime;
import java.util.ArrayList;
import java.util.Scanner;
import no.uio.ifi.asp.parser.AspSyntax;

public class RuntimeLibrary extends RuntimeScope {
    private Scanner keyboard = new Scanner(System.in);

    public RuntimeLibrary() {
        // -- Must be changed in part 4:
        // Bibliotek: (len, print, input, str, int, float, range)
        // 1. Kodebit hentet fra kompendiet s. 66 punkt 3.5.5
        assign("len", new RuntimeFunc("len") {
            @Override
            public RuntimeValue evalFuncCall(ArrayList<RuntimeValue> actualParams, AspSyntax where) {
                checkNumParams(actualParams, 1, "len", where);
                return actualParams.get(0).evalLen(where);
            }
        });

        // print
        assign("print", new RuntimeFunc("print") {
            @Override
            public RuntimeValue evalFuncCall(ArrayList<RuntimeValue> actualParams, AspSyntax where) {
                String s = ""; //For pen utskrift.
                for (int i = 0; i < actualParams.size(); i++) {
                    s += actualParams.get(i);
                    s += " ";
                }

                System.out.println(s);

                return new RuntimeNoneValue();
            }
        });

        // float
        assign("float", new RuntimeFunc("float") {
            @Override
            public RuntimeValue evalFuncCall(ArrayList<RuntimeValue> actualParams, AspSyntax where) {

                checkNumParams(actualParams, 1, "float", where);
                float rTall = Float.parseFloat(actualParams.get(0).getStringValue("string", where)); 

                return new RuntimeFloatValue(rTall);
            }
        });

        // int
        assign("int", new RuntimeFunc("int") {
            @Override
            public RuntimeValue evalFuncCall(ArrayList<RuntimeValue> actualParams, AspSyntax where) {
                checkNumParams(actualParams, 1, "int", where);
                
                //caster input string til int
                int rTall = Integer.parseInt(actualParams.get(0).getStringValue("string", where)); 
        
                return new RuntimeIntValue(rTall);

            }
        });

        // string
        assign("str", new RuntimeFunc("str") {
            @Override
            public RuntimeValue evalFuncCall(ArrayList<RuntimeValue> actualParams, AspSyntax where) {
                checkNumParams(actualParams, 1, "str", where);
                return new RuntimeStringValue(String.valueOf(actualParams.get(0).getIntValue("int", where)));
            }
        });

        // input
        assign("input", new RuntimeFunc("input") {
            @Override
            public RuntimeValue evalFuncCall(ArrayList<RuntimeValue> actualParams, AspSyntax where) {
                checkNumParams(actualParams, 1, "input", where);
                System.out.println(actualParams.get(0));
                return new RuntimeStringValue(keyboard.nextLine());
            }
        });

        // range
        assign("range", new RuntimeFunc("range") {
            @Override
            public RuntimeValue evalFuncCall(ArrayList<RuntimeValue> actualParams, AspSyntax where) {
                checkNumParams(actualParams, 2, "range", where);

                int start = (int) actualParams.get(0).getIntValue("int", where);
                int slutt;

                if ((int) actualParams.get(1).getIntValue("int", where) == 1){
                    slutt = (int) actualParams.get(1).getIntValue("int", where);
                }
                else {
                     slutt = (int) actualParams.get(1).getIntValue("int", where) -1;
                }


                // returnere en liste med verdiene fra start til slutt
                RuntimeListValue liste = new RuntimeListValue(new ArrayList<RuntimeValue>());

                // hvis det er lovlig range
                if (start <= slutt) {
                    
                    // fra start til og med slutt legg til i listen
                    for (long i = start; i <= slutt; i++) {
                        liste.addElement(new RuntimeIntValue(i));
                    }
                } else {
                    
                    runtimeError("range: invalid start parameter", where);
                }
                return liste;

            }
        });

    }

    private void checkNumParams(ArrayList<RuntimeValue> actArgs, int nCorrect, String id, AspSyntax where) {
        if (actArgs.size() != nCorrect)
            RuntimeValue.runtimeError("Wrong number of parameters to " + id + "!", where);
    }
}
