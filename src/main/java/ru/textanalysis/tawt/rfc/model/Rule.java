package ru.textanalysis.tawt.rfc.model;

import ru.textanalysis.tawt.ms.model.jmorfsdk.Form;
import ru.textanalysis.tawt.ms.model.sp.Word;

import java.util.List;
import java.util.Set;
import java.util.function.BiPredicate;
import java.util.stream.Collectors;

public class Rule {

	protected final byte maxDistanceLeft;
	protected final byte maxDistanceRight;
	protected final Set<Byte> typeOfSpeechsForMainWord;
	protected final Set<Byte> typeOfSpeechsForDependentWord;
	protected final BiPredicate<Form, Form> predicate;

	//todo реализовать механизм по автопереводу из стринга в бинарное представление части речи
	public Rule(
		Byte typeOfSpeechForMainWord,
		Byte typeOfSpeechForDependentWord,
		byte maxDistanceLeft,
		byte maxDistanceRight
	) {
		this(
			Set.of(typeOfSpeechForMainWord),
			Set.of(typeOfSpeechForDependentWord),
			maxDistanceLeft, maxDistanceRight,
			((omoFormSP1, omoFormSP2) -> true)
		);
	}

	public Rule(
		Byte typeOfSpeechForMainWord,
		Byte typeOfSpeechForDependentWord,
		byte maxDistanceLeft,
		byte maxDistanceRight,
		BiPredicate<Form, Form> predicate
	) {
		this(
			Set.of(typeOfSpeechForMainWord),
			Set.of(typeOfSpeechForDependentWord),
			maxDistanceLeft, maxDistanceRight,
			predicate
		);
	}

	public Rule(
		Set<Byte> typeOfSpeechesForMainWord,
		Set<Byte> typeOfSpeechesForDependentWord,
		byte maxDistanceLeft,
		byte maxDistanceRight
	) {
		this(
			typeOfSpeechesForMainWord,
			typeOfSpeechesForDependentWord,
			maxDistanceLeft, maxDistanceRight,
			((omoFormSP1, omoFormSP2) -> true)
		);
	}

	//todo реализовать механизм по автопереводу из стринга в бинарное представление части речи
	public Rule(
		Set<Byte> typeOfSpeechesForMainWord,
		Set<Byte> typeOfSpeechesForDependentWord,
		byte maxDistanceLeft,
		byte maxDistanceRight,
		BiPredicate<Form, Form> predicate
	) {
		this.typeOfSpeechsForMainWord = typeOfSpeechesForMainWord;
		this.typeOfSpeechsForDependentWord = typeOfSpeechesForDependentWord;
		this.maxDistanceLeft = maxDistanceLeft;
		this.maxDistanceRight = maxDistanceRight;
		this.predicate = predicate;
	}

	public boolean searchCompatibility(int distance, Word mainWord, Word depWord) {
		if (distance < 0) {
			throw new RuntimeException("Ошибка! Дистанция между словами не может быть отрицательной, distance = " + distance);
		}

		if (distance <= maxDistanceLeft || distance <= maxDistanceRight) {
			return searchCompatibility(depWord, mainWord);
		} else {
			return false;
		}
	}

	public boolean searchCompatibility(Word mainWord, Word depWord) {
		List<Form> mains = mainWord.getForms().stream()
			.filter(form -> typeOfSpeechsForMainWord.contains(form.getTypeOfSpeech()))
			.collect(Collectors.toList());
		List<Form> deps = depWord.getForms().stream()
			.filter(form -> typeOfSpeechsForDependentWord.contains(form.getTypeOfSpeech()))
			.collect(Collectors.toList());

		List<Form> mainsAfterFiler = mains.stream()
			.filter(main -> {
				List<Form> depsAfterFiler = deps.stream()
					.filter(dep -> predicate.test(main, dep))
					.collect(Collectors.toList());
				if (depsAfterFiler.isEmpty()) {
					return false;
				} else {
					deps.clear();
					deps.addAll(depsAfterFiler);
					return true;
				}
			})
			.collect(Collectors.toList());
		if (mainsAfterFiler.isEmpty()) {
			return false;
		} else {
			//todo вынести в сервис
			mainWord.setForms(mainsAfterFiler);
			depWord.addMain(mainWord);

			depWord.setForms(deps);
			mainWord.addDependent(depWord);

			return true;
		}
	}
}
