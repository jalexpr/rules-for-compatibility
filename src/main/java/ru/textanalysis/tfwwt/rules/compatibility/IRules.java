package ru.textanalysis.tfwwt.rules.compatibility;

import ru.textanalysis.tfwwt.morphological.structures.storage.OmoFormList;

public interface IRules {
    public boolean isCompatibility(OmoFormList rightForms, OmoFormList leftForms);
}
