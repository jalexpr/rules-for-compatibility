package ru.textanalysis.tfwwt.rules.compatibility;

import ru.textanalysis.tfwwt.morphological.structures.storage.OmoFormList;

public class RFC implements IRules {
    IRules iRules =
    @Override
    public boolean isCompatibility(OmoFormList rightForms, OmoFormList leftForms) {
        return false;
    }
}
