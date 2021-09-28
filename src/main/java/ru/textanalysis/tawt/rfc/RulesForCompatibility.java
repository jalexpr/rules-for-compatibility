package ru.textanalysis.tawt.rfc;

import ru.textanalysis.tawt.ms.interfaces.InitializationModule;
import ru.textanalysis.tawt.ms.model.sp.Word;
import ru.textanalysis.tawt.rfc.model.Rule;

import java.util.Collection;

public interface RulesForCompatibility extends InitializationModule {

	/**
	 * Определение наличия связи между словами
	 */
	boolean establishRelation(Word main, Word dep);

	/**
	 * Определение наличия связи между словами с учетом расстояния между ними
	 */
	boolean establishRelation(int distance, Word main, Word dep);

	/**
	 * Определение наличия связи между предлогом или частицей и словом
	 */
	boolean establishRelationForPretext(Word pretext, Word word);

	/**
	 * Добавить дополнительные правила определения наличия связи между словами
	 *
	 * @param rules коллекция правил
	 */
	void addRules(Collection<Rule> rules);

	/**
	 * Добавить дополнительные правила для установления связи между предлогом и словом
	 *
	 * @param rulesForPretext коллекция правил
	 */
	void addRulesForPretext(Collection<Rule> rulesForPretext);
}
