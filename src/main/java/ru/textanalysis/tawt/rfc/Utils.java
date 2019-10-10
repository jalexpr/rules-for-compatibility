package ru.textanalysis.tawt.rfc;

import ru.textanalysis.tawt.ms.internal.sp.CursorToFormInWord;
import ru.textanalysis.tawt.ms.internal.sp.OmoFormSP;
import ru.textanalysis.tawt.ms.internal.sp.WordSP;

//todo подумать куда переложить
public class Utils {
    public static void installCompatibility(WordSP mainWord, WordSP depWord, OmoFormSP mainOmoForm, OmoFormSP depOmoForm) {
        mainOmoForm.addDependentCursors(new CursorToFormInWord(depWord, depOmoForm.hashCode()));
        depOmoForm.setMainCursors(new CursorToFormInWord(mainWord, mainOmoForm.hashCode()));
    }
}
