package mcc.decisiondome.selector;

import mcc.decisiondome.DecisionField;

@FunctionalInterface
public interface FieldSelector {
    
    int select(DecisionField[] fields);

}
