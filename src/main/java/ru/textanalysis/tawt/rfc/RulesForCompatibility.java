package ru.textanalysis.tawt.rfc;

import ru.textanalysis.tawt.ms.grammeme.MorfologyParameters.Case;
import ru.textanalysis.tawt.ms.grammeme.MorfologyParameters.Gender;
import ru.textanalysis.tawt.ms.grammeme.MorfologyParameters.Numbers;
import ru.textanalysis.tawt.ms.grammeme.MorfologyParameters.TypeOfSpeech;
import ru.textanalysis.tawt.ms.internal.sp.WordSP;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

public class RulesForCompatibility {
    private final List<Rule> rules;
    private final List<Rule> rulesForPretext;

    public RulesForCompatibility() {
        this.rules = new ArrayList<>();
        rules.add(new Rule(TypeOfSpeech.NOUNPRONOUN,
                TypeOfSpeech.NOUN,
                (byte) 0,
                (byte) 127));
        rules.add(new Rule(TypeOfSpeech.NOUN,
                TypeOfSpeech.NOUNPRONOUN,
                (byte) 0,
                (byte) 2));
        rules.add(new Rule(TypeOfSpeech.NOUN,
                TypeOfSpeech.ADJECTIVEFULL,
                (byte) 2,
                (byte) 2,
                ((formM, formD) ->
                        formM.getMorf(Case.IDENTIFIER) == formD.getMorf(Case.IDENTIFIER)
                                && formM.getMorf(Numbers.IDENTIFIER) == formD.getMorf(Numbers.IDENTIFIER)
                                && (formM.getMorf(Numbers.IDENTIFIER) == Numbers.PLURAL || formM.getMorf(Gender.IDENTIFIER) == formD.getMorf(Gender.IDENTIFIER))
                )));
        rules.add(new Rule(TypeOfSpeech.NOUN,
                TypeOfSpeech.ADJECTIVESHORT,
                (byte) 2,
                (byte) 2,
                ((formM, formD) ->
                        formM.getMorf(Case.IDENTIFIER) == formD.getMorf(Case.IDENTIFIER)
                                && formM.getMorf(Numbers.IDENTIFIER) == formD.getMorf(Numbers.IDENTIFIER)
                                && (formM.getMorf(Numbers.IDENTIFIER) == Numbers.PLURAL || formM.getMorf(Gender.IDENTIFIER) == formD.getMorf(Gender.IDENTIFIER))
                )
        ));
        rules.add(new Rule(TypeOfSpeech.NOUN,
                TypeOfSpeech.VERB,
                (byte) 0,
                (byte) 2));
        rules.add(new Rule(TypeOfSpeech.VERB,
                TypeOfSpeech.NOUN,
                (byte) 0,
                (byte) 4));

        this.rulesForPretext = new ArrayList<>();
        rulesForPretext.add(new Rule(TypeOfSpeech.PRETEXT,
                TypeOfSpeech.NOUN,
                (byte) 0,
                (byte) 127));
        rulesForPretext.add(new Rule(TypeOfSpeech.PARTICLE,
                TypeOfSpeech.NOUN,
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
        return rulesForPretext.stream()
                .filter(rule -> rule.isCompatibility(1, pretext, word))
                .count() > 0;
    }
}
