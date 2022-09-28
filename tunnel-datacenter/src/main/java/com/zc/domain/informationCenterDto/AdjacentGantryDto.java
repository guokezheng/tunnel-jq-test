package com.zc.domain.informationCenterDto;



/**
 * 相邻门架
 */
public class AdjacentGantryDto {

    //收费站 或 门架 id
    private String id;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "AdjacentGantryDto{" +
                "id='" + id + '\'' +
                '}';
    }
}
