package km.lmy.searchview;

/**
 * Created by yeshenggen on 2019/5/29.
 */

public class SearchBean {
    private String name;
    private Integer id;

    public SearchBean() {
    }

    public SearchBean(String name, Integer id) {
        this.name = name;
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }
}
