package note.model;

import note.interfaces.INote;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Note implements INote {
    static final String DATE_FORMAT = "dd.MM.yyyy HH:mm:ss";
    private int id;
    private String head;
    private String body;
    private long create;
    private long update;
    private long delete;

    public Note(int id, String head, String body) {
        this(id, head, body, new Date().getTime(), 0);
    }

    protected Note(int id, String head, String body, long create, long update) {
        this.id = id;
        this.head = head;
        this.body = body;
        this.create = create;
        this.update = update;
    }

    @Override
    public int getId() {
        return id;
    }

    @Override
    public String getHead() {
        return head;
    }

    @Override
    public String getBody() {
        return body;
    }

    @Override
    public void setBody(String text) {
        this.body = text;
        this.update = new Date().getTime();
        this.delete = 0;
    }

    @Override
    public long getCreate() {
        return create;
    }

    @Override
    public long getUpdate() {
        return update;
    }

    @Override
    public long getDelete() {
        return delete;
    }

    @Override
    public void setDelete() {
        delete = new Date().getTime();
    }

    @Override
    public boolean isDelete() {
        return delete != 0;
    }

    @Override
    public String view() {
        StringBuilder sb = new StringBuilder();
        sb.append("Запись: ");
        sb.append("Идентификтор: ").append(id).append("\n");
        sb.append("Заголовок: ").append(head).append("\n");
        sb.append("Текст: ").append(body).append("\n");
        sb.append("Дата создания: ").append((new SimpleDateFormat(DATE_FORMAT)).format(create)).append("\n");
        sb.append("Дата изменения: ");
        if (update != 0) sb.append((new SimpleDateFormat(DATE_FORMAT)).format(update));
        else sb.append("не изменялась");
        sb.append("\n");
        sb.append("Дата удаления: ");
        if (delete != 0) sb.append((new SimpleDateFormat(DATE_FORMAT)).format(delete));
        else sb.append("не удалена");
        sb.append("\n");
        return sb.toString();
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("№ ").append(id).append("; ");
        sb.append(head).append("; ");
        if(body.length()>20)
            sb.append(body.substring(0, 20)).append(" ...; ");
        else sb.append(body).append("; ");
        sb.append((new SimpleDateFormat(DATE_FORMAT)).format(create));
        if (update != 0) sb.append("; ").append((new SimpleDateFormat(DATE_FORMAT)).format(update));
        if (delete != 0) sb.append("; Удалена.");
        sb.append("\n");
        return sb.toString();
    }
}
