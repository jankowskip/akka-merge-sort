package app.akka.sort.merge;

import com.google.common.collect.ImmutableList;

public class MergeListsMessage {

    private ImmutableList<Integer> firstList;
    private ImmutableList<Integer> secondList;

    public MergeListsMessage(ImmutableList<Integer> firstList, ImmutableList<Integer> secondList) {
        this.firstList = firstList;
        this.secondList = secondList;
    }

    ImmutableList<Integer> getFirstList() {
        return firstList;
    }

    ImmutableList<Integer> getSecondList() {
        return secondList;
    }
}
