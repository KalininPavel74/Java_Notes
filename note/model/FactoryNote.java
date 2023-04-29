package note.model;

import note.interfaces.IFactoryNote;
import note.interfaces.IGenerator;
import note.interfaces.INote;

public class FactoryNote implements IFactoryNote {
    private final IGenerator generator;

    public FactoryNote(IGenerator generator) {
        this.generator = generator;
    }
    @Override
    public INote cteate(String head, String body) {
        int id = generator.getNewId();
        return new Note(id, head, body);
    }

    static protected INote cteate(int id, String head, String body, long create, long update) {
        return new Note(id, head, body, create, update);
    }

}
