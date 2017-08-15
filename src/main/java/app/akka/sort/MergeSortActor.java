package app.akka.sort;

import akka.actor.AbstractActor;
import akka.actor.ActorRef;
import akka.actor.Props;
import app.akka.ArrayMessage;
import app.akka.SortedArrayMessage;
import app.akka.sort.merge.MergeArraysActor;
import app.akka.sort.merge.MergeArraysMessage;

import static java.util.Arrays.copyOfRange;
import static java.util.Objects.isNull;

public class MergeSortActor extends AbstractActor {

    private AbstractActor.Receive idle;
    private AbstractActor.Receive waitingForArraysToMerge;
    private AbstractActor.Receive waitingForMergedSortedArray;
    private AbstractActor.Receive terminated;
    private int[] firstSortedArray;

    public MergeSortActor() {

        idle =
                receiveBuilder()
                        .match(ArrayMessage.class, (ArrayMessage arrayMessage) -> {
                            int[] messageArray = arrayMessage.getArray();
                            if (messageArray.length > 1) {
                                sendArrayChunkToNewMergeSortActor(messageArray, 0, messageArray.length / 2);
                                sendArrayChunkToNewMergeSortActor(messageArray, messageArray.length / 2, messageArray.length);
                                getContext().become(waitingForArraysToMerge);
                            } else {
                                getSender().tell(new SortedArrayMessage(arrayMessage.getArray()), getSelf());
                                getContext().become(terminated);
                            }
                        })
                        .build();

        waitingForArraysToMerge =
                receiveBuilder()
                        .match(SortedArrayMessage.class, (SortedArrayMessage sortedArrayMessage) -> {
                            if (isNull(this.firstSortedArray)) {
                                this.firstSortedArray = sortedArrayMessage.getSortedArray();
                            } else {
                                ActorRef actorRef = getContext().actorOf(Props.create(MergeArraysActor.class, MergeArraysActor::new));
                                actorRef.tell(new MergeArraysMessage(this.firstSortedArray, sortedArrayMessage.getSortedArray()), getSelf());
                                getContext().become(waitingForMergedSortedArray);
                            }
                        })
                        .build();

        waitingForMergedSortedArray =
                receiveBuilder()
                        .match(SortedArrayMessage.class, (SortedArrayMessage sortedArrayMessage) -> {
                            getContext().getParent().tell(sortedArrayMessage, getSelf());
                            getContext().become(terminated);
                        })
                        .build();

        terminated =
                receiveBuilder()
                        .matchAny(this::unhandled)
                        .build();
    }

    @Override
    public Receive createReceive() {
        return idle;
    }

    private void sendArrayChunkToNewMergeSortActor(int[] originalArray, int from, int to) {
        ActorRef actorRef = getContext().actorOf(Props.create(MergeSortActor.class, MergeSortActor::new));
        int[] arrayChunk = copyOfRange(originalArray, from, to);
        actorRef.tell(new ArrayMessage(arrayChunk), getSelf());
    }

}
