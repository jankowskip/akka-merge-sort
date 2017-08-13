

import akka.actor.AbstractActor;
import akka.actor.ActorRef;
import akka.actor.Props;

import java.util.Arrays;

public class MergeSortActor extends AbstractActor {

    private AbstractActor.Receive idle;
    private AbstractActor.Receive waitingForArrays;
    private int[] sortedArray;

    public MergeSortActor() {

        idle =
                receiveBuilder()
                        .match(ArrayMessage.class, (ArrayMessage arrayMessage) -> {
                            int[] messageArray = arrayMessage.getArray();
                            if (messageArray.length > 1) {
                                passArrayChunkToSort(messageArray, 0, messageArray.length / 2);
                                passArrayChunkToSort(messageArray, messageArray.length / 2, messageArray.length);

                            } else {
                                getSender().tell(new SortedArrayMessage(arrayMessage.getArray()), getSelf());
                            }
                            getContext().become(waitingForArrays);
                        })
                        .build();

        waitingForArrays =
                receiveBuilder()
                        .match(SortedArrayMessage.class, (SortedArrayMessage sortedArrayMessage) -> {
                            if (this.sortedArray == null) {
                                this.sortedArray = sortedArrayMessage.getSortedArray();
                            } else {
                                ActorRef actorRef = getContext().actorOf(Props.create(MergeArraysActor.class, MergeArraysActor::new));
                                actorRef.tell(new MergeArraysMessage(this.sortedArray, sortedArrayMessage.getSortedArray()), getSelf());
                            }

                        })
                        .match(MergeSortMessage.class, (MergeSortMessage mergeSortMessage) -> {
                            getContext().parent().tell(new SortedArrayMessage(mergeSortMessage.getSortedArray()), getSelf());
                        })
                        .build();
    }


    private void passArrayChunkToSort(int[] originalArray, int from, int to) {
        ActorRef actorRef = getContext().actorOf(Props.create(MergeSortActor.class, MergeSortActor::new));
        int[] arrayChunk = Arrays.copyOfRange(originalArray, from, to);
        actorRef.tell(new ArrayMessage(arrayChunk), getSelf());
    }

    @Override
    public Receive createReceive() {
        return idle;
    }
}
