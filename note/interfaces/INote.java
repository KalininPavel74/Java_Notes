package note.interfaces;

public interface INote {
    int getId();
    String getHead();
    String getBody();
    void setBody(String text);
    long getCreate();
    long getUpdate();
    long getDelete();
    void setDelete();
    boolean isDelete();
    String view();
}
