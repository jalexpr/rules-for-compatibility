package ru.textanalysis.tfwwt.rules.compatibility;

import ru.textanalysis.tfwwt.morphological.structures.grammeme.MorfologyParameters;
import ru.textanalysis.tfwwt.morphological.structures.internal.sp.WordSP;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

public class RelationshipHandler {
    private final List<Rule> rules;

    public RelationshipHandler() {
        this.rules = new ArrayList<>();
        rules.add(new Rule(MorfologyParameters.TypeOfSpeech.NOUN,
                MorfologyParameters.TypeOfSpeech.VERB,
                (byte) 0,
                (byte) 2));
    }

    public boolean establishRelation(int distance, WordSP leftWord, WordSP rightWord) {
        AtomicBoolean isCompatibility = new AtomicBoolean(false);
        rules.forEach(rule -> {
            if (rule.isCompatibility(distance, leftWord, rightWord)) {
                isCompatibility.set(true);
            }
        });
        return isCompatibility.get();
    }
//
//    private RefOmoFormList getFormOfIterator(ListIterator<RefOmoFormList> mainFormIterator) throws Exception {
//        if(mainFormIterator.hasNext()) {
//            RefOmoFormList mianForm = mainFormIterator.next();
//            mainFormIterator.previous();
//            return mianForm;
//        }
//        if(mainFormIterator.hasPrevious()) {
//            RefOmoFormList mianForm = mainFormIterator.previous();
//            mainFormIterator.next();
//            return mianForm;
//        }
//
//        throw new Exception("Опорный оборот состоит из одного слова");
//    }
//
//    Попытка установить связь с соседними совами
//    private void establishRelationshipWithNeighbors(OmoFormList mainWord, ListIterator<OmoFormList> mainFormIterator, int depth, boolean right) {
//        if(right) {
//            for(int i = 0; i < depth; i++) {
//                establishRelationshipAndRemoveFormsWithoutRelationsForTwoWord(mainWord, mainFormIterator.next());
//            }
//        } else {
//            for(int i = 0; i < depth; i++) {
//                establishRelationshipAndRemoveFormsWithoutRelationsForTwoWord(mainWord, mainFormIterator.previous());
//            }
//        }
//    }
//
//    public boolean establishRelationshipAndRemoveFormsWithoutRelationsForTwoWord(OmoFormList mainWord, OmoFormList dependentWord) {
//        boolean isEstablishedRelationship = establishRelationship(mainWord, dependentWord);
//
//        if (isEstablishedRelationship) {
//            removeFormsWithoutRelations(mainWord);
//            removeFormsWithoutRelations(dependentWord);
//        }
//
//        return isEstablishedRelationship;
//    }
//
//    private boolean establishRelationship(OmoFormList mainFormList, OmoFormList dependentFormList) {
//        boolean isEstablishedRelationship = false;
//
//        for (OmoForm mainForm : mainFormList) {
//            for (OmoForm dependentForm : dependentFormList) {
//                if (isCombinedOmoForm(mainForm, dependentForm)) {
//                    mainForm.addDependentForm(dependentForm);
//                    isEstablishedRelationship = true;
//                }
//            }
//        }
//
//        return isEstablishedRelationship;
//    }
//
//    private void removeFormsWithoutRelations(OmoFormList formList) {
//        Iterator<OmoForm> iterForm = formList.iterator();
//        while (iterForm.hasNext()) {
//            if (iterForm.next().haveCommunication()) {
//                iterForm.remove();
//            }
//        }
//    }
//
//    public boolean isCombinedOmoForm(OmoForm mainOmoForm, OmoForm dependentOmoForm) {
//        use Rule
//        return false;
//    }

}
