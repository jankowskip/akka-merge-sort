package app.akka.sort.merge;

import akka.actor.AbstractActor;
import app.akka.SortedListMessage;
import com.google.common.collect.ImmutableList;

import static java.util.stream.Collectors.toList;
import static java.util.stream.Stream.concat;

public class MergeListsActor extends AbstractActor {

    @Override
    public Receive createReceive() {
        return receiveBuilder()
                .match(MergeListsMessage.class, (MergeListsMessage mergeListsMessage) -> {
                    ImmutableList<Integer> sortedList = merge(mergeListsMessage.getFirstList(), mergeListsMessage.getSecondList());
                    getSender().tell(new SortedListMessage(sortedList), getSelf());
                    getContext().stop(getSelf());
                })
                .build();
    }

    private ImmutableList<Integer> merge(ImmutableList<Integer> firstList, ImmutableList<Integer> secondList) {
        return ImmutableList.copyOf(concat(firstList.stream(), secondList.stream())
                .sorted()
                .collect(toList()));
    }
}
