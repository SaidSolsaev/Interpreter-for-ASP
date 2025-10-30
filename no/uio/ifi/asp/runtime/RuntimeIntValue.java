package no.uio.ifi.asp.runtime;

import no.uio.ifi.asp.parser.AspSyntax;

public class RuntimeIntValue extends RuntimeValue {
    long intValue;

    public RuntimeIntValue(long intValue) {
        this.intValue = intValue;
    }

    @Override
    String typeName() {
        return "int";
    }

    @Override
    public String toString() {
        return Long.toString(intValue);
    }

    @Override
    public String showInfo() {
        return Long.toString(intValue);
    }

    @Override
    public long getIntValue(String what, AspSyntax where) {
        return intValue;
    }

    @Override
    public double getFloatValue(String what, AspSyntax where) {
        return (double) intValue;
    }

    @Override
    public boolean getBoolValue(String what, AspSyntax where) {
        return (intValue != 0);
    }

    @Override
    public RuntimeValue evalPositive(AspSyntax where) {
        return new RuntimeIntValue(+intValue);
    }

    @Override
    public RuntimeValue evalNegate(AspSyntax where) {
        return new RuntimeIntValue(-intValue);
    }

    // fra prekoden
    @Override
    public RuntimeValue evalAdd(RuntimeValue v, AspSyntax where) {

        if (v instanceof RuntimeIntValue) { // er v en runtimeIntValue?
            return new RuntimeIntValue(intValue + v.getIntValue("+ operand", where));
        } else if (v instanceof RuntimeFloatValue) { // er v en runtimeFloatValue?

            return new RuntimeFloatValue(intValue + v.getFloatValue("+ operand", where));
        }
        // Sjekk for om det er string
        else if (v instanceof RuntimeStringValue) {
            runtimeError("TypeError: unsupported operand type(s) for +: 'int' and 'str'", where);
        }
        // hvis ingen av dem return feilmedling
        runtimeError("Type error for +!", where);

        return null; // Required by the compiler.
    }

    @Override
    public RuntimeValue evalSubtract(RuntimeValue v, AspSyntax where) {
        if (v instanceof RuntimeIntValue) { // er v en runtimeIntValue?
            return new RuntimeIntValue(intValue - v.getIntValue("- operand", where));
        } else if (v instanceof RuntimeFloatValue) { // er v en runtimeFloatValue?

            return new RuntimeFloatValue(intValue - v.getFloatValue("- operand", where));
        } else if (v instanceof RuntimeStringValue) {
            runtimeError("TypeError: unsupported operand type(s) for -: 'int' and 'str'", where);
        }
        // hvis ingen av dem return feilmedling
        runtimeError("Type error for -.", where);

        return null; // Required by the compiler.
    }

    @Override
    public RuntimeValue evalMultiply(RuntimeValue v, AspSyntax where) {
        if (v instanceof RuntimeIntValue) { // er v en runtimeIntValue?
            return new RuntimeIntValue(intValue * v.getIntValue("* operand", where));
        } else if (v instanceof RuntimeFloatValue) {
            return new RuntimeFloatValue(intValue * v.getFloatValue("* operand", where));
        }
        // hvis ingen av dem return feilmedling
        runtimeError("Type error for *.", where);

        return null; // Required by the compiler.
    }

    @Override
    public RuntimeValue evalDivide(RuntimeValue v, AspSyntax where) {
        if (v instanceof RuntimeIntValue) { // er v en runtimeIntValue?
            return new RuntimeIntValue(intValue / v.getIntValue("/ operand", where));
        } else if (v instanceof RuntimeFloatValue) {
            return new RuntimeFloatValue(intValue / v.getFloatValue("/ operand", where));
        }
        // hvis ingen av dem return feilmedling
        runtimeError("Type error for /.", where);

        return null; // Required by the compiler.
    }

    @Override
    public RuntimeValue evalIntDivide(RuntimeValue v, AspSyntax where) {
        if (v instanceof RuntimeIntValue) { // er v en runtimeIntValue?
            return new RuntimeIntValue(Math.floorDiv(intValue, v.getIntValue("// operand", where)));
        } else if (v instanceof RuntimeFloatValue) {
            return new RuntimeFloatValue(Math.floor(intValue / v.getFloatValue("// operand", where)));
        }
        // hvis ingen av dem return feilmedling
        runtimeError("Type error for //.", where);

        return null; // Required by the compiler.
    }

    @Override
    public RuntimeValue evalModulo(RuntimeValue v, AspSyntax where) {
        if (v instanceof RuntimeIntValue) { // er v en runtimeIntValue?
            return new RuntimeIntValue(Math.floorMod(intValue, v.getIntValue("% operand", where)));
        } else if (v instanceof RuntimeFloatValue) {
            return new RuntimeFloatValue((intValue - v.getFloatValue("% operand", where))
                    * Math.floor(intValue / v.getFloatValue("% operand", where)));
        }
        // hvis ingen av dem return feilmedling
        runtimeError("Type error for %.", where);

        return null; // Required by the compiler.
    }

    public RuntimeValue evalEqual(RuntimeValue v, AspSyntax where) {
        if (v instanceof RuntimeIntValue) { // er v en runtimeIntValue?
            return new RuntimeBoolValue(intValue == v.getIntValue("== operand", where));
        } else if (v instanceof RuntimeFloatValue) {
            return new RuntimeBoolValue(intValue == v.getFloatValue("== operand", where));
        }
        // hvis ingen av dem return feilmedling
        runtimeError("Type error for ==.", where);

        return null; // Required by the compiler.
    }

    public RuntimeValue evalGreater(RuntimeValue v, AspSyntax where) {
        if (v instanceof RuntimeIntValue) { // er v en runtimeIntValue?
            return new RuntimeBoolValue(intValue > v.getIntValue("> operand", where));
        } else if (v instanceof RuntimeFloatValue) {
            return new RuntimeBoolValue(intValue > v.getFloatValue("> operand", where));
        }
        // hvis ingen av dem return feilmedling
        runtimeError("Type error for >.", where);

        return null; // Required by the compiler.
    }

    @Override
    public RuntimeValue evalNot(AspSyntax where) {
        if (intValue == 0) {
            return new RuntimeBoolValue(true);
        }
        return new RuntimeBoolValue(false);
    }

    public RuntimeValue evalGreaterEqual(RuntimeValue v, AspSyntax where) {
        if (v instanceof RuntimeIntValue) { // er v en runtimeIntValue?
            return new RuntimeBoolValue(intValue >= v.getIntValue(">= operand", where));
        } else if (v instanceof RuntimeFloatValue) {
            return new RuntimeBoolValue(intValue >= v.getFloatValue(">= operand", where));
        }
        // hvis ingen av dem return feilmedling
        runtimeError("Type error for >=.", where);

        return null; // Required by the compiler.
    }

    public RuntimeValue evalLess(RuntimeValue v, AspSyntax where) {
        if (v instanceof RuntimeIntValue) { // er v en runtimeIntValue?
            return new RuntimeBoolValue(intValue < v.getIntValue("< operand", where));
        } else if (v instanceof RuntimeFloatValue) {
            return new RuntimeBoolValue(intValue < v.getFloatValue("< operand", where));
        }
        // hvis ingen av dem return feilmedling
        runtimeError("Type error for <.", where);

        return null; // Required by the compiler.
    }

    public RuntimeValue evalLessEqual(RuntimeValue v, AspSyntax where) {
        if (v instanceof RuntimeIntValue) { // er v en runtimeIntValue?
            return new RuntimeBoolValue(intValue <= v.getIntValue("<= operand", where));
        } else if (v instanceof RuntimeFloatValue) {
            return new RuntimeBoolValue(intValue <= v.getFloatValue("<= operand", where));
        }
        // hvis ingen av dem return feilmedling
        runtimeError("Type error for <=.", where);

        return null; // Required by the compiler.
    }

    public RuntimeValue evalNotEqual(RuntimeValue v, AspSyntax where) {
        if (v instanceof RuntimeIntValue) { // er v en runtimeIntValue?
            return new RuntimeBoolValue(intValue != v.getIntValue("!= operand", where));
        } else if (v instanceof RuntimeFloatValue) {
            return new RuntimeBoolValue(intValue != v.getFloatValue("!= operand", where));
        }
        // hvis ingen av dem return feilmedling
        runtimeError("Type error for !=.", where);

        return null; // Required by the compiler.
    }

    // Done
}