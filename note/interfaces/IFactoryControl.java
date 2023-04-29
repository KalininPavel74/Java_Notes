package note.interfaces;

public interface IFactoryControl {
    IControl create(IView view, INotes notes, IFactoryNote fNote, ICSV csv, String dbFile, String charset);
}
