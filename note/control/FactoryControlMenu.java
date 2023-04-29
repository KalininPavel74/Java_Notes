package note.control;

import note.interfaces.*;

public class FactoryControlMenu implements IFactoryControl {
    @Override
    public IControl create(IView view, INotes notes, IFactoryNote fNote, ICSV csv, String dbFile, String charset) {
        return new ControlMenu(view, notes, fNote, csv, dbFile, charset);
    }
}
