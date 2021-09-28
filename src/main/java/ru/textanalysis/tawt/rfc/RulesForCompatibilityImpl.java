package ru.textanalysis.tawt.rfc;

import lombok.extern.slf4j.Slf4j;
import ru.textanalysis.tawt.ms.grammeme.MorfologyParameters.Case;
import ru.textanalysis.tawt.ms.grammeme.MorfologyParameters.Gender;
import ru.textanalysis.tawt.ms.grammeme.MorfologyParameters.Numbers;
import ru.textanalysis.tawt.ms.model.sp.Word;
import ru.textanalysis.tawt.rfc.model.Rule;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static ru.textanalysis.tawt.ms.grammeme.MorfologyParameters.TypeOfSpeech.*;

@Slf4j
public class RulesForCompatibilityImpl implements RulesForCompatibility {

	private final List<Rule> rules = new ArrayList<>();
	private final List<Rule> rulesForPretext = new ArrayList<>();

	public RulesForCompatibilityImpl() {
	}

	@Override
	public void init() {
		rules.add(new Rule(
			NOUN_PRONOUN,
			NOUN,
			(byte) 0,
			(byte) 127));
		rules.add(new Rule(
			NOUN,
			NOUN_PRONOUN,
			(byte) 0,
			(byte) 2));
		rules.add(new Rule(
			NOUN,
			ADJECTIVE_FULL,
			(byte) 2,
			(byte) 2,
			((formM, formD) ->
				formM.getMorfCharacteristicsByIdentifier(Case.IDENTIFIER) == formD.getMorfCharacteristicsByIdentifier(Case.IDENTIFIER)
					&& formM.getMorfCharacteristicsByIdentifier(Numbers.IDENTIFIER) == formD.getMorfCharacteristicsByIdentifier(Numbers.IDENTIFIER)
					&& (formM.getMorfCharacteristicsByIdentifier(Numbers.IDENTIFIER) == Numbers.PLURAL || formM.getMorfCharacteristicsByIdentifier(Gender.IDENTIFIER) == formD.getMorfCharacteristicsByIdentifier(Gender.IDENTIFIER))
			)));
		rules.add(new Rule(
			NOUN,
			ADJECTIVE_SHORT,
			(byte) 2,
			(byte) 2,
			((formM, formD) ->
				formM.getMorfCharacteristicsByIdentifier(Case.IDENTIFIER) == formD.getMorfCharacteristicsByIdentifier(Case.IDENTIFIER)
					&& formM.getMorfCharacteristicsByIdentifier(Numbers.IDENTIFIER) == formD.getMorfCharacteristicsByIdentifier(Numbers.IDENTIFIER)
					&& (formM.getMorfCharacteristicsByIdentifier(Numbers.IDENTIFIER) == Numbers.PLURAL || formM.getMorfCharacteristicsByIdentifier(Gender.IDENTIFIER) == formD.getMorfCharacteristicsByIdentifier(Gender.IDENTIFIER))
			)
		));
		rules.add(new Rule(
			NOUN,
			VERB,
			(byte) 0,
			(byte) 2)
		);
		rules.add(new Rule(
			VERB,
			NOUN,
			(byte) 0,
			(byte) 4)
		);

		rulesForPretext.add(new Rule(
			PRETEXT,
			NOUN,
			(byte) 0,
			(byte) 127)
		);
		rulesForPretext.add(new Rule(
			PARTICLE,
			NOUN,
			(byte) 0,
			(byte) 127)
		);

		log.debug("RFC is initialized!");
	}

	@Override
	public boolean establishRelation(Word main, Word dep) {
		return rules.stream().anyMatch(rule -> rule.searchCompatibility(main, dep));
	}

	@Override
	public boolean establishRelation(int distance, Word main, Word dep) {
		return rules.stream().anyMatch(rule -> rule.searchCompatibility(distance, main, dep));
	}

	@Override
	public boolean establishRelationForPretext(Word pretext, Word word) {
		return rulesForPretext.stream().anyMatch(rule -> rule.searchCompatibility(pretext, word));
	}

	@Override
	public void addRules(Collection<Rule> rules) {
		this.rules.addAll(rules);
	}

	@Override
	public void addRulesForPretext(Collection<Rule> rulesForPretext) {
		this.rulesForPretext.addAll(rulesForPretext);
	}
}
