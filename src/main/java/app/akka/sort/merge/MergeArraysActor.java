package app.akka.sort.merge;

import akka.actor.AbstractActor;
import app.akka.SortedArrayMessage;

public class MergeArraysActor extends AbstractActor {

    @Override
    public Receive createReceive() {
        return receiveBuilder()
                .match(MergeArraysMessage.class, (MergeArraysMessage mergeArraysMessage) -> {
                    int[] sortedArray = merge(mergeArraysMessage.getFirstArray(), mergeArraysMessage.getSecondArray());
                    getSender().tell(new SortedArrayMessage(sortedArray), getSelf());
                })
                .build();
    }

    private int[] merge(int[] firstArray, int[] secondArray) {
        int[] result = new int[firstArray.length + secondArray.length];
        int i = 0, j = 0, k = 0;

        while (i < firstArray.length && j < secondArray.length)
            result[k++] = firstArray[i] < secondArray[j] ? firstArray[i++] : secondArray[j++];

        while (i < firstArray.length)
            result[k++] = firstArray[i++];

        while (j < secondArray.length)
            result[k++] = secondArray[j++];
        return result;
    }

}
