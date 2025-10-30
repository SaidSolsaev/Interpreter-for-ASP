package no.uio.ifi.asp.runtime;
import java.util.ArrayList;
import no.uio.ifi.asp.parser.AspFuncDef;
import no.uio.ifi.asp.parser.AspSyntax;


public class RuntimeFunc extends RuntimeValue {
    RuntimeScope scope;
    AspFuncDef function;
    String name;

    public RuntimeFunc(AspFuncDef def, RuntimeScope scope, String name) {
        function = def;
        this.scope = scope;
        this.name = name;
    }

    public RuntimeFunc(String name) {
        this.name = name;
    }

    @Override
    public RuntimeValue evalFuncCall(ArrayList<RuntimeValue> actualParams, AspSyntax where) {
       
        if (actualParams.size() != function.name.size()) {
            runtimeError("Wrong parameters " + actualParams + " Should be: " + function.name.size() + "!", where);
        }

        RuntimeScope sc = new RuntimeScope(scope);

        for (int i = 0; i < actualParams.size(); i++) {
            sc.assign(function.name.get(i).name, actualParams.get(i));
        }

        try {
            function.suite.eval(sc);
        } catch (RuntimeReturnValue returnErr) {
            return returnErr.value;
        }

        return new RuntimeNoneValue();
    }

    @Override
    String typeName() {
        return "def Function";
    }

    public String toString(){
        return name;
    }

}
