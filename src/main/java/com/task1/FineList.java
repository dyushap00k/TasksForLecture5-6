package com.task1;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.ArrayList;
import java.util.List;
@XmlRootElement(name = "fines")
public class FineList {

    private List<Fine> list;
    public FineList(){
        list = new ArrayList<>();
    }
    @XmlElement(name = "fine")
    public List<Fine> getList() {
        return list;
    }

    public void setList(List<Fine> list) {
        this.list = list;
    }
}
