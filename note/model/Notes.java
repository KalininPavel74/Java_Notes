package note.model;

import note.interfaces.IGenerator;
import note.interfaces.INote;
import note.interfaces.INotes;
import note.util.ExceptionProg;

import java.util.*;
import java.util.logging.Logger;

public class Notes implements INotes {

    private static final Logger logger = Logger.getLogger(Notes.class.getName());

    private final ArrayList<INote> notes;
    private final IGenerator generator;
    private long saveDate;

    public Notes(String[][] ss) throws ExceptionProg {
        this.notes = new ArrayList<INote>();
        if (ss == null || ss.length == 0) {
            this.generator = new Generator(0);
        } else {
            Set<Integer> set = new HashSet<Integer>();
            int maxId = 0;
            for (String[] arS : ss) {
                try {
                    Integer id = Integer.valueOf(arS[0]);
                    if (id > maxId) maxId = id;
                    boolean b = set.add(id);
                    if (!b) throw new ExceptionProg("В записях присутствуют дубли по номеру. " + id);
                    this.notes.add(FactoryNote.cteate(id, arS[1], arS[2], Long.valueOf(arS[3]), Long.valueOf(arS[4])));
                } catch (NumberFormatException e) {
                    throw new ExceptionProg("Ошибка в номере записи или датах" + e.getMessage());
                }
            }
            if (set.size() != ss.length)
                throw new ExceptionProg("В записях присутствуют дубли по номеру. " + set.size() + " <> " + ss.length);
            this.generator = new Generator(maxId);
        }

    }

    @Override
    public IGenerator getGenerator() {
        return generator;
    }

    @Override
    public List<INote> getListNotes() {
        return notes;
    }

    @Override
    public void add(INote note) {
        notes.add(note);
    }

    @Override
    public boolean update(int id, String body) {
        INote note = search(id);
        if (note != null) {
            note.setBody(body);
            return true;
        }
        return false;
    }

    @Override
    public List<INote> search(long beginDate, long endDate) {
        ArrayList<INote> al = new ArrayList<>();
        for (INote note : notes) {
            if(note.getDelete() == 0 || note.getDelete() > saveDate) {
                long create = note.getCreate();
                if (create >= beginDate && create <= endDate) {
                    al.add(note);
                }
            }
        }
        return al;
    }

    @Override
    public String search2(long beginDate, long endDate) {
        List<INote> list = search(beginDate, endDate);
        StringBuilder sb = new StringBuilder();
        for(INote note: list) {
            if(note.getDelete() == 0 || note.getDelete() > saveDate)
                sb.append(note.view()).append("\n");
        }
        return sb.toString();
    }

    @Override
    public INote search(int id) {
        for (INote note : notes)
            if (note.getId() == id) {
                return note;
            }
        return null;
    }

    @Override
    public String search2(int id) {
        INote note;
        note = search(id);
        if (note != null)
            note.view();
        return "Запись не найдена.";
    }

    @Override
    public String view(int id) {
        INote note = search(id);
        if (note != null)
            return note.view();
        return "Запись не найдена.";
    }

    @Override
    public boolean delete(int id) {
        INote note = search(id);
        if (note != null) {
            note.setDelete();
            return true;
        }
        return false;
    }

    @Override
    public String getLastNotes(int n) {
        StringBuilder sb = new StringBuilder();
        int i = notes.size()-1;
        int count = n;
        while (i >= 0 && count > 0) {
            if(notes.get(i).getDelete() == 0 || notes.get(i).getDelete() > saveDate) {
                sb.append(notes.get(i).toString());
                count--;
            }
            i--;
        }
        return sb.toString();
    }

    @Override
    public String[][] getArrayNotes() {
        int qty = 0;
        for(INote note: notes)
            if(!note.isDelete()) qty++;
        if(qty == 0) return null;

        String[][] ss = new String[qty][5];
        int i = 0;
        for(INote note: notes)
            if (!note.isDelete()) {
                ss[i][0] = String.valueOf(note.getId());
                ss[i][1] = String.valueOf(note.getHead());
                ss[i][2] = String.valueOf(note.getBody());
                ss[i][3] = String.valueOf(note.getCreate());
                ss[i][4] = String.valueOf(note.getUpdate());
                i++;
            }
        return ss;
    }

    @Override
    public void setSaveDate(long saveDate) {
        this.saveDate = saveDate;
        logger.info("время сохранения"+saveDate);
    }


}
