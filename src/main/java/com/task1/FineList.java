package com.task1;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.ArrayList;
import java.util.List;
@XmlRootElement(name = "fines")
public class FineList {

    private List<task3.Fine> list;
    public FineList(){
        list = new ArrayList<>();
    }
    @XmlElement(name = "fine")
    public List<task3.Fine> getList() {
        return list;
    }

    public void setList(List<task3.Fine> list) {
        this.list = list;
    }
}
