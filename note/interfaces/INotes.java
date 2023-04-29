package note.interfaces;

import java.util.List;
public interface INotes {
    IGenerator getGenerator();
    List<INote> getListNotes();
    void add(INote note);
    boolean update(int id, String body);
    INote search(int id);
    String search2(int id);
    List<INote> search(long beginDate, long endDate);
    String search2(long beginDate, long endDate);
    String view(int id);
    boolean delete(int id);
    String getLastNotes(int n);
    String[][] getArrayNotes();
    void setSaveDate(long saveDate);
}
