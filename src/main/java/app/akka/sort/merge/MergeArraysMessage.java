package app.akka.sort.merge;

public class MergeArraysMessage {

    private int[] firstArray;
    private int[] secondArray;

    public MergeArraysMessage(int[] firstArray, int[] secondArray) {
        this.firstArray = firstArray;
        this.secondArray = secondArray;
    }

    int[] getFirstArray() {
        return firstArray;
    }

    int[] getSecondArray() {
        return secondArray;
    }
}
