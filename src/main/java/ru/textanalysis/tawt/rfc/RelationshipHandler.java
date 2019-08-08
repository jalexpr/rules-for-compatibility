package ru.textanalysis.tawt.rfc;

import ru.textanalysis.tawt.ms.grammeme.MorfologyParameters;
import ru.textanalysis.tawt.ms.internal.sp.WordSP;

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
}
