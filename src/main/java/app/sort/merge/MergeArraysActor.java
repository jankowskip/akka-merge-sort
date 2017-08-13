package app.sort.merge;

import akka.actor.AbstractActor;
import app.sort.MergeSortMessage;

public class MergeArraysActor extends AbstractActor {

    @Override
    public Receive createReceive() {
        return receiveBuilder()
                .match(MergeArraysMessage.class, (MergeArraysMessage mergeArraysMessage) -> {
                    int[] sortedArray = merge(mergeArraysMessage.getArrayOne(), mergeArraysMessage.getArrayTwo());
                    getSender().tell(new MergeSortMessage(sortedArray), getSelf());
                })
                .build();
    }


    public static int[] merge (int[] firstArray, int[] secondArray){
            int[] answer = new int[firstArray.length + secondArray.length];
            int i = 0, j = 0, k = 0;

            while (i < firstArray.length && j < secondArray.length)
                answer[k++] = firstArray[i] < secondArray[j] ? firstArray[i++] : secondArray[j++];

            while (i < firstArray.length)
                answer[k++] = firstArray[i++];

            while (j < secondArray.length)
                answer[k++] = secondArray[j++];
            return answer;


    }
}
