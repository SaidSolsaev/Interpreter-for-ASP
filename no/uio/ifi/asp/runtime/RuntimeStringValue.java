package no.uio.ifi.asp.runtime;
import no.uio.ifi.asp.parser.AspSyntax;


public class RuntimeStringValue extends RuntimeValue{
    String sVal;
    
    public RuntimeStringValue(String s) {
	    sVal = s;
    }

    @Override
    String typeName(){
        return "String";
    }

    @Override
    //Kode hentet fra forelesning
    public String showInfo(){
        if (sVal.indexOf("\'") >= 0){
            return "\"" + sVal + "\"";
        }
        return "\'" + sVal + "\'";
    }

    @Override
    public String toString(){
        return sVal;
    }

    @Override
    public String getStringValue(String what, AspSyntax where){
        return sVal;
    }
    
    @Override
    public boolean getBoolValue(String what, AspSyntax where){
        if (sVal == ""){
            return false;
        }
        else{
            return true;
        }
    }

    @Override
    public RuntimeValue evalLen(AspSyntax where){
        return new RuntimeIntValue((sVal.length()));
    }

    // For part 3:
    @Override
    public RuntimeValue evalAdd(RuntimeValue v, AspSyntax where) {
	    if (v instanceof RuntimeValue){
            return new RuntimeStringValue(sVal + v.getStringValue("+ oprand", where));
        }
        runtimeError("Type error for '+'"  + typeName() + "!", where);
	    return null;  // Required by the compiler!
    }

    
    @Override
    public RuntimeValue evalEqual(RuntimeValue v, AspSyntax where) {
        if (v instanceof RuntimeStringValue){

            if (sVal.equals(v.getStringValue("== oprand", where))){
                return new RuntimeBoolValue(true); 
            }
            
            return new RuntimeBoolValue(false);
        }
    	runtimeError("Type error for '=='" + typeName() + "!", where);
	    return null;  // Required by the compiler!
    }

    @Override
    public RuntimeValue evalGreater(RuntimeValue v, AspSyntax where) {
        if (v instanceof RuntimeStringValue){

            if (sVal.equals(v.getStringValue("> oprand", where))){
                return new RuntimeBoolValue(true); 
            }
            
            return new RuntimeBoolValue(false);
            
        }
	    runtimeError("Type error for '>'" + typeName() + "!", where);
	    return null;  // Required by the compiler!
    }

    @Override
    public RuntimeValue evalNot(AspSyntax where){
        return new RuntimeBoolValue(sVal == "");
    }

    @Override
    public RuntimeValue evalGreaterEqual(RuntimeValue v, AspSyntax where) {
        if (v instanceof RuntimeStringValue){
            if (sVal.equals(v.getStringValue(">= oprand", where))){
                return new RuntimeBoolValue(true); 
            }
            
            return new RuntimeBoolValue(false);
            
        }
	    runtimeError("Type error for '>='" + typeName() + "!", where);
	    return null;  // Required by the compiler!
    }

    @Override
    public RuntimeValue evalLess(RuntimeValue v, AspSyntax where) {
        if (v instanceof RuntimeStringValue){
            if (sVal.equals(v.getStringValue("< oprand", where))){
                return new RuntimeBoolValue(true); 
            }
            
            return new RuntimeBoolValue(false);
            
        }
	    runtimeError("Type error for '<'" + typeName() + "!", where);
	    return null;  // Required by the compiler!
    }

    @Override
    public RuntimeValue evalLessEqual(RuntimeValue v, AspSyntax where) {
        if (v instanceof RuntimeStringValue){
            if (sVal.equals(v.getStringValue("<= oprand", where))){
                return new RuntimeBoolValue(true); 
            }
            
            return new RuntimeBoolValue(false);
            
        }
	    runtimeError("Type error for '<='" + typeName() + "!", where);
	    return null;  // Required by the compiler!
    }

    @Override
    public RuntimeValue evalMultiply(RuntimeValue v, AspSyntax where) {
        if (v instanceof RuntimeIntValue){
            return new RuntimeStringValue(sVal.repeat((int)v.getIntValue("*", where)));
        }
	    runtimeError("Type error for '*'" + typeName() + "!", where);
	    return null;  // Required by the compiler!
    }

    @Override
    public RuntimeValue evalNotEqual(RuntimeValue v, AspSyntax where) {
        if (v instanceof RuntimeStringValue){
            if (!sVal.equals(v.getStringValue("!= oprand", where))){
                return new RuntimeBoolValue(true); 
            }
            
            return new RuntimeBoolValue(false);
        }

	    runtimeError("Type error for '!='" + typeName() + "!", where);
	    return null;  // Required by the compiler!
    }

    @Override
    public RuntimeValue evalSubscription(RuntimeValue v, AspSyntax where) {
        if (v instanceof RuntimeIntValue) {
            long k = v.getIntValue(sVal, where);
            if (k >= sVal.length() || k < 0) {
                runtimeError("Index out of range.", where);
            }
            char h = sVal.charAt((int) k);
            String s = String.valueOf(h);
            RuntimeStringValue val = new RuntimeStringValue(s);
            return val;
        }
        runtimeError("Type error for subscription.", where);
        return null;
    }
}