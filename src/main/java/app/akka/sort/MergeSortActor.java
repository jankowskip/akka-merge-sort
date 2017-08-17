package app.akka.sort;

import akka.actor.AbstractActor;
import akka.actor.ActorRef;
import akka.actor.Props;
import app.akka.ListMessage;
import app.akka.SortedListMessage;
import app.akka.sort.merge.MergeListsActor;
import app.akka.sort.merge.MergeListsMessage;
import com.google.common.collect.ImmutableList;

public class MergeSortActor extends AbstractActor {

    private AbstractActor.Receive idle;
    private AbstractActor.Receive waitingForFirstListToMerge;
    private AbstractActor.Receive waitingForSecondListToMerge;
    private AbstractActor.Receive waitingForMergedSortedList;
    private ImmutableList<Integer> firstSortedList;

    public MergeSortActor() {

        idle =
                receiveBuilder()
                        .match(ListMessage.class, (ListMessage listMessage) -> {
                            ImmutableList<Integer> messageList = listMessage.getList();
                            if (messageList.size() > 1) {
                                sendListChunkToNewMergeSortActor(messageList.subList(0, messageList.size() / 2));
                                sendListChunkToNewMergeSortActor(messageList.subList(messageList.size() / 2, messageList.size()));
                                getContext().become(waitingForFirstListToMerge);
                            } else {
                                getSender().tell(new SortedListMessage(listMessage.getList()), getSelf());
                                getContext().stop(getSelf());
                            }
                        })
                        .build();

        waitingForFirstListToMerge =
                receiveBuilder()
                        .match(SortedListMessage.class, (SortedListMessage sortedListMessage) -> {
                            this.firstSortedList = sortedListMessage.getSortedList();
                            getContext().become(waitingForSecondListToMerge);
                        })
                        .build();

        waitingForSecondListToMerge =
                receiveBuilder()
                        .match(SortedListMessage.class, (SortedListMessage sortedListMessage) -> {
                            ActorRef actorRef = getContext().actorOf(Props.create(MergeListsActor.class, MergeListsActor::new));
                            actorRef.tell(new MergeListsMessage(this.firstSortedList, sortedListMessage.getSortedList()), getSelf());
                            getContext().become(waitingForMergedSortedList);
                        })
                        .build();

        waitingForMergedSortedList =
                receiveBuilder()
                        .match(SortedListMessage.class, (SortedListMessage sortedListMessage) -> {
                            getContext().getParent().tell(sortedListMessage, getSelf());
                            getContext().stop(getSelf());
                        })
                        .build();
    }

    @Override
    public Receive createReceive() {
        return idle;
    }

    private void sendListChunkToNewMergeSortActor(ImmutableList<Integer> listChunk) {
        ActorRef actorRef = getContext().actorOf(Props.create(MergeSortActor.class, MergeSortActor::new));
        actorRef.tell(new ListMessage(listChunk), getSelf());
    }

}
