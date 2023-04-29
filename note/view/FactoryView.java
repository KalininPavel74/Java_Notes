package note.view;

import note.interfaces.IFactoryView;
import note.interfaces.IView;

public class FactoryView implements IFactoryView {
    public IView create(String title, String charset, char[] exitLetters) {
        return new ViewConsole(title, charset, exitLetters);
    }

}
