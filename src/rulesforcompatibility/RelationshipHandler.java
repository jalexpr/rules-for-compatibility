package rulesforcompatibility;

import java.util.Iterator;
import java.util.ListIterator;
import morphologicalstructures.OmoForm;
import storagestructures.OmoFormList;
import storagestructures.WordList;

public class RelationshipHandler {

    public static boolean establishRelationshipAndRemoveFormsWithoutRelationsForWordAndBearingPhrase(WordList sentence, ListIterator<OmoFormList> mainFormIterator) throws Exception {
        OmoFormList mainForm = getFormOfIterator(mainFormIterator);
        establishRelationshipWithNeighbors(mainForm, mainFormIterator, 0, true);
        establishRelationshipWithNeighbors(mainForm, mainFormIterator, 0, false);
        
    }

    private static OmoFormList getFormOfIterator(ListIterator<OmoFormList> mainFormIterator) throws Exception {
        if(mainFormIterator.hasNext()) {
            OmoFormList mianForm = mainFormIterator.next();
            mainFormIterator.previous();
            return mianForm;
        }
        if(mainFormIterator.hasPrevious()) {
            OmoFormList mianForm = mainFormIterator.previous();
            mainFormIterator.next();
            return mianForm;
        }
        
        throw new Exception("Опорный оборот состоит из одно слова");
    }
    
    //Попытка установить связь с соседними совами
    private static boolean establishRelationshipWithNeighbors(OmoFormList mainWord, ListIterator<OmoFormList> mainFormIterator, int depth, boolean right) {
        if(right) {
            for(int i = 0; i < depth; i++) {
                establishRelationshipAndRemoveFormsWithoutRelationsForTwoWord(mainWord, mainFormIterator.next());
            }
        } else {
            for(int i = 0; i < depth; i++) {
                establishRelationshipAndRemoveFormsWithoutRelationsForTwoWord(mainWord, mainFormIterator.previous());
            }
        }
    }
    
    public static boolean establishRelationshipAndRemoveFormsWithoutRelationsForTwoWord(OmoFormList mainWord, OmoFormList dependentWord) {
        boolean isEstablishedRelationship = establishRelationship(mainWord, dependentWord);

        if (isEstablishedRelationship) {
            removeFormsWithoutRelations(mainWord);
            removeFormsWithoutRelations(dependentWord);
        }

        return isEstablishedRelationship;
    }

    private static boolean establishRelationship(OmoFormList mainFormList, OmoFormList dependentFormList) {
        boolean isEstablishedRelationship = false;

        for (OmoForm mainForm : mainFormList) {
            for (OmoForm dependentForm : dependentFormList) {
                if (isCombinedOmoForm(mainForm, dependentForm)) {
                    mainForm.addDependentForm(dependentForm);
                    isEstablishedRelationship = true;
                }
            }
        }

        return isEstablishedRelationship;
    }

    private static void removeFormsWithoutRelations(OmoFormList formList) {
        Iterator<OmoForm> iterForm = formList.iterator();
        while (iterForm.hasNext()) {
            if (iterForm.next().haveCommunication()) {
                iterForm.remove();
            }
        }
    }

    public static boolean isCombinedOmoForm(OmoForm mainOmoForm, OmoForm dependentOmoForm) {
        //use Rules
        return false;
    }

}
