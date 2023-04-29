package note.interfaces;

import note.util.ExceptionProg;

import java.util.List;

public interface IFactoryNotes {
    INotes create(String[][] ss) throws ExceptionProg;
}
