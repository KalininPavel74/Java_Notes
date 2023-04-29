package note.interfaces;

public interface IFactoryView {
    IView create(String title, String charset, char[] exitLetters);
}
