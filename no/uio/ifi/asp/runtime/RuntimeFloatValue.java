package no.uio.ifi.asp.runtime;

import no.uio.ifi.asp.parser.AspSyntax;

public class RuntimeFloatValue extends RuntimeValue{

    double floatValue;

    public RuntimeFloatValue(double floatValue) {
	    this.floatValue = floatValue;
    }

    @Override
    String typeName() {
        return "Float";
        
    }
     @Override
    public boolean getBoolValue(String what, AspSyntax where) {
        if (floatValue > 0) {
            return true;
        } else {
            return false;
        }
    }
    @Override
    public String toString() {
        return Double.toString(floatValue);
    }
    @Override
    public String showInfo() {
        return Double.toString(floatValue);
    }

    @Override
    public double getFloatValue(String what, AspSyntax where) {
        return (double) floatValue;
    }
    

    @Override
    public String getStringValue(String what, AspSyntax where) {
        return String.valueOf(floatValue);
    }


    @Override
    public long getIntValue(String what, AspSyntax where) {
        return (long) floatValue;
    }

    @Override 
    public RuntimeValue evalNegate(AspSyntax where) {
        return new RuntimeFloatValue(-floatValue); 

    }
    @Override 
    public RuntimeValue evalPositive(AspSyntax where) {
        return new RuntimeFloatValue(floatValue); 

    }

    @Override
    public RuntimeValue evalNot(AspSyntax where){
        if (floatValue == 0){
            return new RuntimeBoolValue(true);
        }

        return new RuntimeBoolValue(false);
    }


//fra forelesningslides 
    @Override
    public RuntimeValue evalAdd(RuntimeValue v, AspSyntax where) {
        if (v instanceof RuntimeIntValue) {
            return new RuntimeFloatValue(floatValue + v.getIntValue("+", where));
        } 
        else if (v instanceof RuntimeFloatValue) {
            return new RuntimeFloatValue(floatValue + v.getFloatValue("+", where));
        }
        else if (v instanceof RuntimeStringValue){
            runtimeError("TypeError: unsupported operand type(s) for +: 'float' and 'str'", where);
        }
        runtimeError("Type error for '+'" + typeName() + "!", where);
        return null;
    }
    @Override
    public RuntimeValue evalSubtract(RuntimeValue v, AspSyntax where) {
        if (v instanceof RuntimeIntValue) {
            return new RuntimeFloatValue(floatValue - v.getIntValue("-", where));
        } 
        else if (v instanceof RuntimeFloatValue) {
            return new RuntimeFloatValue(floatValue - v.getFloatValue("-", where));
        }
        else if (v instanceof RuntimeStringValue){
            runtimeError("TypeError: unsupported operand type(s) for -: 'float' and 'str'", where);
        }
        runtimeError("Type error for '-'" + typeName() + "!", where);
        return null;
    }
    @Override
    public RuntimeValue evalMultiply(RuntimeValue v, AspSyntax where){
        if (v instanceof RuntimeIntValue) { //er v en runtimeIntValue?
            return new RuntimeFloatValue(floatValue * v.getIntValue("* operand",where));  
        }
        else if (v instanceof RuntimeFloatValue) {
            return new RuntimeFloatValue(floatValue * v.getFloatValue("* operand", where));
        }
        //hvis ingen av dem return feilmedling
        runtimeError("Type error for *.", where);
        
        return null; // Required by the compiler.
    }
    @Override
    public RuntimeValue evalDivide(RuntimeValue v, AspSyntax where){
        if (v instanceof RuntimeIntValue) { //er v en runtimeIntValue?
            return new RuntimeFloatValue(floatValue / v.getIntValue("/ operand",where));  
        }
        else if (v instanceof RuntimeFloatValue) {
            return new RuntimeFloatValue(floatValue / v.getFloatValue("/ operand", where));
        }
        //hvis ingen av dem return feilmedling
        runtimeError("Type error for /.", where);
        
        return null; // Required by the compiler.
    }
    @Override
    public RuntimeValue evalIntDivide(RuntimeValue v, AspSyntax where){
        if (v instanceof RuntimeIntValue) { //er v en runtimeIntValue?
            return new RuntimeFloatValue(Math.floor(floatValue / v.getIntValue("// operand",where)));
        }
        else if (v instanceof RuntimeFloatValue) {
            return new RuntimeFloatValue(Math.floor(floatValue / v.getFloatValue("// operand", where)));
        }
        //hvis ingen av dem return feilmedling
        runtimeError("Type error for //.", where);
        
        return null; // Required by the compiler.
    }

    @Override
    public RuntimeValue evalModulo(RuntimeValue v, AspSyntax where){
        if (v instanceof RuntimeIntValue) { //er v en runtimeIntValue?
            return new RuntimeFloatValue(Math.floor(floatValue % v.getIntValue("% operand",where)));
        }
        else if (v instanceof RuntimeFloatValue) {
            return new RuntimeFloatValue(Math.floor(floatValue % v.getFloatValue("% operand", where)) * Math.floor(floatValue / v.getFloatValue("% operand", where)));
        }
        //hvis ingen av dem return feilmedling
        runtimeError("Type error for %.", where);
        
        return null; // Required by the compiler.
    }

    
    public RuntimeValue evalEqual(RuntimeValue v, AspSyntax where){
        if (v instanceof RuntimeIntValue) { //er v en runtimeIntValue?
            return new RuntimeBoolValue(floatValue == v.getIntValue("== operand", where));
        }
        else if (v instanceof RuntimeFloatValue) {
            return new RuntimeBoolValue(floatValue == v.getIntValue("== operand", where));
        }
        //hvis ingen av dem return feilmedling
        runtimeError("Type error for ==.", where);
        
        return null; // Required by the compiler.
    }

    public RuntimeValue evalGreater(RuntimeValue v, AspSyntax where){
        if (v instanceof RuntimeIntValue) { //er v en runtimeIntValue?
            return new RuntimeBoolValue(floatValue > v.getIntValue("> operand", where));
        }
        else if (v instanceof RuntimeFloatValue) {
            return new RuntimeBoolValue(floatValue > v.getIntValue("> operand", where));
        }
        //hvis ingen av dem return feilmedling
        runtimeError("Type error for >.", where);
        
        return null; // Required by the compiler.
    }

    public RuntimeValue evalGreaterEqual(RuntimeValue v, AspSyntax where){
        if (v instanceof RuntimeIntValue) { //er v en runtimeIntValue?
            return new RuntimeBoolValue(floatValue >= v.getIntValue(">= operand", where));
        }
        else if (v instanceof RuntimeFloatValue) {
            return new RuntimeBoolValue(floatValue >= v.getIntValue(">= operand", where));
        }
        //hvis ingen av dem return feilmedling
        runtimeError("Type error for >=.", where);
        
        return null; // Required by the compiler.
    }

    public RuntimeValue evalLess(RuntimeValue v, AspSyntax where){
        if (v instanceof RuntimeIntValue) { //er v en runtimeIntValue?
            return new RuntimeBoolValue(floatValue < v.getIntValue("< operand", where));
        }
        else if (v instanceof RuntimeFloatValue) {
            return new RuntimeBoolValue(floatValue < v.getIntValue("< operand", where));
        }
        //hvis ingen av dem return feilmedling
        runtimeError("Type error for <.", where);
        
        return null; // Required by the compiler.
    }

    public RuntimeValue evalLessEqual(RuntimeValue v, AspSyntax where){
        if (v instanceof RuntimeIntValue) { //er v en runtimeIntValue?
            return new RuntimeBoolValue(floatValue <= v.getIntValue("<= operand", where));
        }
        else if (v instanceof RuntimeFloatValue) {
            return new RuntimeBoolValue(floatValue <= v.getIntValue("<= operand", where));
        }
        //hvis ingen av dem return feilmedling
        runtimeError("Type error for <=.", where);
        
        return null; // Required by the compiler.
    }
    

    public RuntimeValue evalNotEqual(RuntimeValue v, AspSyntax where){
        if (v instanceof RuntimeIntValue) { //er v en runtimeIntValue?
            return new RuntimeBoolValue(floatValue != v.getIntValue("!= operand", where));
        }
        else if (v instanceof RuntimeFloatValue) {
            return new RuntimeBoolValue(floatValue != v.getIntValue("!= operand", where));
        }
        //hvis ingen av dem return feilmedling
        runtimeError("Type error for !=.", where);
        
        return null; // Required by the compiler.
    }








}