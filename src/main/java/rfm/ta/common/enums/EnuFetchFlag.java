package rfm.ta.common.enums;

import java.util.Hashtable;

/**
 * 取对账明细标记;[0]-未开始;[1]-获取中;[2]-已经获取
 */
public enum EnuFetchFlag {
    FETCH_FLAG0("0","未开始"),
    FETCH_FLAG1("1","获取中"),
    FETCH_FLAG2("2","已经获取");

    private String code = null;
    private String title = null;
    private static Hashtable<String, EnuFetchFlag> aliasEnums;

    EnuFetchFlag(String code, String title){
        this.init(code, title);
    }

    @SuppressWarnings("unchecked")
    private void init(String code, String title) {
        this.code = code;
        this.title = title;
        synchronized (this.getClass()) {
            if (aliasEnums == null) {
                aliasEnums = new Hashtable();
            }
        }
        aliasEnums.put(code, this);
        aliasEnums.put(title, this);
    }

    public static EnuFetchFlag getValueByKey(String alias) {
        return aliasEnums.get(alias);
    }

    public String getCode() {
        return this.code;
    }

    public String getTitle() {
        return this.title;
    }
}
