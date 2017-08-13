package app.akka;

public class SortedArrayMessage {

    private int[] sortedArray;

    public SortedArrayMessage(int[] sortedArray) {
        this.sortedArray = sortedArray;
    }

    public int[] getSortedArray() {
        return sortedArray;
    }
}
