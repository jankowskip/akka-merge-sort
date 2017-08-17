package app.akka;

import com.google.common.collect.ImmutableList;

public class SortedListMessage {

    private ImmutableList<Integer> sortedList;

    public SortedListMessage(ImmutableList<Integer> sortedList) {
        this.sortedList = sortedList;
    }

    public ImmutableList<Integer> getSortedList() {
        return sortedList;
    }
}
