package ru.textanalysis.tawt.rfc;

import ru.textanalysis.tawt.ms.grammeme.MorfologyParameters;
import ru.textanalysis.tawt.ms.internal.sp.WordSP;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

public class RelationshipHandler {
    private final List<Rule> rules;
    private final List<Rule> rulesForPretext;

    public RelationshipHandler() {
        this.rules = new ArrayList<>();
        rules.add(new Rule(MorfologyParameters.TypeOfSpeech.NOUNPRONOUN,
                MorfologyParameters.TypeOfSpeech.NOUN,
                (byte) 0,
                (byte) 127));
        rules.add(new Rule(MorfologyParameters.TypeOfSpeech.NOUN,
                MorfologyParameters.TypeOfSpeech.NOUNPRONOUN,
                (byte) 0,
                (byte) 2));
        rules.add(new Rule(MorfologyParameters.TypeOfSpeech.NOUN,
                MorfologyParameters.TypeOfSpeech.VERB,
                (byte) 0,
                (byte) 2));
        rules.add(new Rule(MorfologyParameters.TypeOfSpeech.VERB,
                MorfologyParameters.TypeOfSpeech.NOUN,
                (byte) 0,
                (byte) 4));

        this.rulesForPretext = new ArrayList<>();
        rules.add(new Rule(MorfologyParameters.TypeOfSpeech.PRETEXT,
                MorfologyParameters.TypeOfSpeech.NOUN,
                (byte) 0,
                (byte) 127));
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

    //пробегаемся по всем словам, которые стоят правея
    public boolean establishRelationForPretext(WordSP pretext, WordSP word) {
        return rulesForPretext.stream().filter(rule -> rule.isCompatibility(1, pretext, word)).count() > 0;
    }
}
