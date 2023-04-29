package note.model;

import note.interfaces.IFactoryNotes;
import note.interfaces.INotes;
import note.util.ExceptionProg;

public class FactoryNotes implements IFactoryNotes {
    @Override
    public INotes create(String[][] ss) throws ExceptionProg {
        return new Notes(ss);
    }
}
