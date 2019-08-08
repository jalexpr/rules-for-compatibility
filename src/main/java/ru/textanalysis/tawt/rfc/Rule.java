package ru.textanalysis.tawt.rfc;

import ru.textanalysis.tawt.ms.internal.sp.CursorToFormInWord;
import ru.textanalysis.tawt.ms.internal.sp.WordSP;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.atomic.AtomicBoolean;

public class Rule {
    protected final byte maxDistanceLeft;
    protected final byte maxDistanceRight;
    protected final Set<Byte> typeOfSpeechsForMainWord;
    protected final Set<Byte> typeOfSpeechsForDependentWord;

    //todo реализовать механизм по автопереводу из стринга в бинарное представление части речи
    public Rule(byte typeOfSpeechForMainWord, byte typeOfSpeechForDependentWord,
                byte maxDistanceLeft, byte maxDistanceRight) {
        this.typeOfSpeechsForMainWord = new HashSet<>();
        this.typeOfSpeechsForMainWord.add(typeOfSpeechForMainWord);
        this.typeOfSpeechsForDependentWord = new HashSet<>();
        this.typeOfSpeechsForDependentWord.add(typeOfSpeechForDependentWord);
        this.maxDistanceLeft = maxDistanceLeft;
        this.maxDistanceRight = maxDistanceRight;
    }

    //todo реализовать механизм по автопереводу из стринга в бинарное представление части речи
    public Rule(Set<Byte> typeOfSpeechsForMainWord, Set<Byte> typeOfSpeechsForDependentWord,
                byte maxDistanceLeft, byte maxDistanceRight) {
        this.typeOfSpeechsForMainWord = typeOfSpeechsForMainWord;
        this.typeOfSpeechsForDependentWord = typeOfSpeechsForDependentWord;
        this.maxDistanceLeft = maxDistanceLeft;
        this.maxDistanceRight = maxDistanceRight;
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
                    if (typeOfSpeechsForDependentWord.contains(depOmoForm.getToS())) {
                        mainOmoForm.addDependentCursors(new CursorToFormInWord(depWord, depOmoForm.hashCode()));
                        depOmoForm.setMainCursors(new CursorToFormInWord(mainWord, mainOmoForm.hashCode()));
                        isCompatibility.set(true);
                    }
                });
            }
        });
        return isCompatibility.get();
    }

}
