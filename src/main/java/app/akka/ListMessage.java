package app.akka;

import com.google.common.collect.ImmutableList;

public class ListMessage {

    private ImmutableList<Integer> list;

    public ListMessage(ImmutableList<Integer> list) {
        this.list = list;
    }

    public ImmutableList<Integer> getList() {
        return list;
    }
}
