package no.uio.ifi.asp.runtime;
import java.util.ArrayList;
import no.uio.ifi.asp.parser.AspSyntax;

public class RuntimeListValue extends RuntimeValue {
    public ArrayList<RuntimeValue> listVal = new ArrayList<>();

    public RuntimeListValue(ArrayList<RuntimeValue> list){
        listVal = list;
    }

	@Override
	public String typeName() {
        return "list";
	}

    @Override
    public String toString(){
        return listVal.toString();
    }

    @Override
    public String showInfo(){
        return listVal.toString();
    }


    @Override
    public RuntimeValue evalEqual(RuntimeValue v, AspSyntax where){
        if(v instanceof RuntimeListValue){
            if(this.getElements(where).size() == v.getElements(where).size()){
                for(int i=0; i<this.getElements(where).size() -1; i++){
                    if(getElements(where).get(i) != v.getElements(where).get(i)){
                        return new RuntimeBoolValue(false);
                    }
                }
                return new RuntimeBoolValue(true);
            }else{
                return new RuntimeBoolValue(false);
            }
        }
        return new RuntimeBoolValue(false);
    }

    @Override
    public RuntimeValue evalNotEqual(RuntimeValue v, AspSyntax where){
        if(v instanceof RuntimeListValue){
            if(this.getElements(where).size() == v.getElements(where).size()){
                for(int i=0; i<this.getElements(where).size() -1; i++){
                    if(getElements(where).get(i) != v.getElements(where).get(i)){
                        return new RuntimeBoolValue(true);
                    }
                }
                return new RuntimeBoolValue(false);
            }else{
                return new RuntimeBoolValue(true);
            }
        }
        return new RuntimeBoolValue(true);
    }


    @Override
    public RuntimeValue evalMultiply(RuntimeValue v, AspSyntax where){
        if (v instanceof RuntimeIntValue){
            long tall = v.getIntValue("* Operand", where);
            RuntimeListValue nyListe = new RuntimeListValue(new ArrayList<RuntimeValue>());

            for (int i = 0; i < tall; i++){
                nyListe.addElements(listVal);
            }
            return nyListe;
        }
        runtimeError("'*' undefined for " + typeName() + " and " + v.typeName() + "!", where);
        return null;
    }

    @Override
    public RuntimeValue evalLen(AspSyntax where){
        return new RuntimeIntValue(listVal.size());
    }

    @Override
    public RuntimeValue evalNot(AspSyntax where){
        return new RuntimeBoolValue(listVal.size() == 0);
    }

    @Override
    public RuntimeValue evalSubscription(RuntimeValue v, AspSyntax where){
        int index = 0;
        
        if (v instanceof RuntimeIntValue){
            index = (int) v.getIntValue("Subscribtion", where);
        } else if (v instanceof RuntimeFloatValue){
            runtimeError("TypeError: list indices must be integers or slices, not float", where);
        }

        if (index > listVal.size()-1){
          runtimeError("IndexError: list index out of range " + typeName() + "!", where);
        } 
        
        return listVal.get(index);
    }

    //For del 4
    @Override
    public void evalAssignElem(RuntimeValue inx, RuntimeValue val, AspSyntax where){
        if (listVal.size()-1 < inx.getIntValue("Assignment", where)){
            runtimeError("TypeError: Index out of bounds for " + typeName() + "!", where);
        }else{
            listVal.set((int) inx.getIntValue("Assignmet",where), val);
        }
    }

    //hjelpemetode for å hente alle elementer
    @Override
    public ArrayList<RuntimeValue> getElements(AspSyntax where) {
        return listVal;
    }

    //Hjelpemetode for å hente et element
    public RuntimeValue getElement(int i){
        return listVal.get(i);
    }

    //Hjelpemetode for å legge til elm i lista
    public void addElements(ArrayList<RuntimeValue> elements){
        listVal.addAll(elements);
    }

    //Hjelpemetode for å legge til 1 element i lista
    public void addElement(RuntimeValue elmt){
        listVal.add(elmt);
    }
     public ArrayList<RuntimeValue> getList(){
        return listVal;
    }


    
}