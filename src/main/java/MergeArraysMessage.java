public class MergeArraysMessage {

    private int[] arrayOne;
    private int[] arrayTwo;

    public MergeArraysMessage(int[] arrayOne, int[] arrayTwo) {
        this.arrayOne = arrayOne;
        this.arrayTwo = arrayTwo;
    }

    public int[] getArrayOne() {
        return arrayOne;
    }

    public int[] getArrayTwo() {
        return arrayTwo;
    }

}
