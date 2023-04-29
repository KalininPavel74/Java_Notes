package note.util;

import note.interfaces.ICSV;
import note.interfaces.IFactoryUtil;

public class FactoryUtil implements IFactoryUtil {
    @Override
    public ICSV create() { return new CSV(); };
}
