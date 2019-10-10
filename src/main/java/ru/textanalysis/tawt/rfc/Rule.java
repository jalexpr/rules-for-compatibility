package ru.textanalysis.tawt.rfc;

import ru.textanalysis.tawt.ms.internal.sp.OmoFormSP;
import ru.textanalysis.tawt.ms.internal.sp.WordSP;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.BiPredicate;

import static ru.textanalysis.tawt.rfc.Utils.installCompatibility;

public class Rule {
    protected final byte maxDistanceLeft;
    protected final byte maxDistanceRight;
    protected final Set<Byte> typeOfSpeechsForMainWord;
    protected final Set<Byte> typeOfSpeechsForDependentWord;
    protected final BiPredicate<OmoFormSP, OmoFormSP> predicate;

    //todo реализовать механизм по автопереводу из стринга в бинарное представление части речи
    public Rule(byte typeOfSpeechForMainWord, byte typeOfSpeechForDependentWord,
                byte maxDistanceLeft, byte maxDistanceRight) {
        this.typeOfSpeechsForMainWord = new HashSet<>();
        this.typeOfSpeechsForMainWord.add(typeOfSpeechForMainWord);
        this.typeOfSpeechsForDependentWord = new HashSet<>();
        this.typeOfSpeechsForDependentWord.add(typeOfSpeechForDependentWord);
        this.maxDistanceLeft = maxDistanceLeft;
        this.maxDistanceRight = maxDistanceRight;
        this.predicate = ((omoFormSP1, omoFormSP2) -> true);
    }

    public Rule(byte typeOfSpeechForMainWord, byte typeOfSpeechForDependentWord,
                byte maxDistanceLeft, byte maxDistanceRight, BiPredicate<OmoFormSP, OmoFormSP> predicate) {
        this.typeOfSpeechsForMainWord = new HashSet<>();
        this.typeOfSpeechsForMainWord.add(typeOfSpeechForMainWord);
        this.typeOfSpeechsForDependentWord = new HashSet<>();
        this.typeOfSpeechsForDependentWord.add(typeOfSpeechForDependentWord);
        this.maxDistanceLeft = maxDistanceLeft;
        this.maxDistanceRight = maxDistanceRight;
        this.predicate = predicate;
    }

    //todo реализовать механизм по автопереводу из стринга в бинарное представление части речи
    public Rule(Set<Byte> typeOfSpeechsForMainWord, Set<Byte> typeOfSpeechsForDependentWord,
                byte maxDistanceLeft, byte maxDistanceRight) {
        this.typeOfSpeechsForMainWord = typeOfSpeechsForMainWord;
        this.typeOfSpeechsForDependentWord = typeOfSpeechsForDependentWord;
        this.maxDistanceLeft = maxDistanceLeft;
        this.maxDistanceRight = maxDistanceRight;
        this.predicate = ((omoFormSP1, omoFormSP2) -> true);
    }

    //передать чтобы выборка была по статистике
    public boolean isCompatibility(int distance, WordSP leftWord, WordSP rightWord) {
        if (distance <= 0) {
            throw new RuntimeException("Ошибка! distance должна быть больше 0, distance = " + distance);
        }

        boolean isCompatibility = false;
        if (distance <= maxDistanceLeft) {
            isCompatibility = searchCompatibility(rightWord, leftWord);
        }
        if (distance <= maxDistanceRight) {
            isCompatibility = searchCompatibility(leftWord, rightWord);
        }

        return isCompatibility;
    }

    private boolean searchCompatibility(WordSP mainWord, WordSP depWord) {
        AtomicBoolean isCompatibility = new AtomicBoolean(false);
        mainWord.applyConsumer(mainOmoForm -> {
            if (typeOfSpeechsForMainWord.contains(mainOmoForm.getToS())) {
                depWord.applyConsumer(depOmoForm -> {
                    if (!depOmoForm.haveMain() && typeOfSpeechsForDependentWord.contains(depOmoForm.getToS())
                    && predicate.test(mainOmoForm, depOmoForm)) {
                        installCompatibility(mainWord, depWord, mainOmoForm, depOmoForm);
                        isCompatibility.set(true);
                    }
                });
                //todo добавить возможн отключать удаление
                if (isCompatibility.get()) {
                    depWord.cleanNotRelation();
                }
            }
        });
        //todo добавить возможн отключать удаление
        if (isCompatibility.get()) {
            mainWord.cleanNotRelation();
        }
        return isCompatibility.get();
    }
}
