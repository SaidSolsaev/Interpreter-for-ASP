package no.uio.ifi.asp.runtime;

import java.util.HashMap;

import no.uio.ifi.asp.parser.AspSyntax;

public class RuntimeDictValue extends RuntimeValue {
    HashMap<RuntimeStringValue, RuntimeValue> dict;
    
    public RuntimeDictValue (HashMap<RuntimeStringValue, RuntimeValue> v) {
        dict = v;
    }

    @Override
    public String typeName() {
        return "dictionary";
    }

    @Override
    public String toString(){
        String utskriv = "{ ";
        int i = 1;
        for (RuntimeStringValue s: dict.keySet()){
            utskriv += s + " : " + dict.get(s);

            if (i < dict.size()){
                utskriv += ", ";
            }
            i++;
        }
        utskriv += " }";
        return utskriv;
    }

    @Override
    public boolean getBoolValue(String what, AspSyntax where){
        if (dict.isEmpty()){
            return false;
        }
        else{
            return true;
        }
        
    }

    @Override 
    public RuntimeIntValue evalLen(AspSyntax where){
        return new RuntimeIntValue(dict.size());
    }

    @Override
    public RuntimeValue evalSubscription(RuntimeValue v, AspSyntax where){
        RuntimeValue x = null;

        for (RuntimeStringValue s : dict.keySet()){

            if (v.getStringValue("String", where).equals(s.getStringValue("String", where))){
                x = dict.get(s);
            }
        }

        if (x == null){
            runtimeError("Keyerror for dictionary, Could not find value for: " + v.showInfo(), where);
            return null;
        }
        return x;
    }

    @Override
    public RuntimeValue evalNot(AspSyntax where){
        if (dict.size() == 0){
            return new RuntimeBoolValue(true);
        }
        return new RuntimeBoolValue(false);
    }

    
    //For del 4
    @Override
    public void evalAssignElem(RuntimeValue inx, RuntimeValue val, AspSyntax where){
        
        if (inx instanceof RuntimeStringValue){
            dict.put((RuntimeStringValue) inx, val);
        } else{
            runtimeError("Error! Assigning element to an element not allowed for type: " + typeName() + "!", where);
        }
    }
}
