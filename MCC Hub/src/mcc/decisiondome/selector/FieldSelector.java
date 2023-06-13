package mcc.decisiondome.selector;

import mcc.decisiondome.DecisionField;

@FunctionalInterface
public interface FieldSelector {
    
    /**
     * Calculates and returns the index of the field which should be selected.
     * @param fields the array of all available fields
     * @return the index of the field which should be selected
     */
    int select(DecisionField[] fields);

}
