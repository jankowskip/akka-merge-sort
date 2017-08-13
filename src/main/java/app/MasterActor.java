package app;

import akka.actor.AbstractActor;
import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;
import akka.japi.pf.ReceiveBuilder;
import app.sort.MergeSortActor;

import java.util.Arrays;

public class MasterActor extends AbstractActor {

    private static final String MERGE_SORT_SYSTEM = "MergeSortSystem";

    public static void main(String[] args) {
        ActorSystem exampleSystem = ActorSystem.create(MERGE_SORT_SYSTEM);
        ActorRef master = exampleSystem.actorOf(Props.create(MasterActor.class, MasterActor::new));
        int[] arrayToSort = {2, 5, 4, 3, 2,1};
        master.tell(arrayToSort, ActorRef.noSender());
    }

    @Override
    public Receive createReceive() {
        return new ReceiveBuilder()
                .match(int[].class, array -> {
                    ActorRef actorRef = getContext().actorOf(Props.create(MergeSortActor.class, MergeSortActor::new));
                    actorRef.tell(new ArrayMessage(array), getSelf());
                })
                .match(SortedArrayMessage.class, (SortedArrayMessage sortedArrayMessage) -> {
                        System.out.println(Arrays.toString(sortedArrayMessage.getSortedArray()));
                })
                .matchAny(this::unhandled).build();
    }
}