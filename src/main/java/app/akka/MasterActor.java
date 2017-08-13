package app.akka;

import akka.actor.AbstractActor;
import akka.actor.ActorRef;
import akka.actor.Props;
import akka.japi.pf.ReceiveBuilder;
import app.akka.sort.MergeSortActor;

import java.util.Arrays;

public class MasterActor extends AbstractActor {

    @Override
    public Receive createReceive() {
        return new ReceiveBuilder()
                .match(int[].class, array -> {
                    ActorRef actorRef = getContext().actorOf(Props.create(MergeSortActor.class, MergeSortActor::new));
                    actorRef.tell(new ArrayMessage(array), getSelf());
                })
                .match(SortedArrayMessage.class, (SortedArrayMessage sortedArrayMessage) -> {
                    System.out.println(Arrays.toString(sortedArrayMessage.getSortedArray()));
                    getContext().stop(getSender());
                })
                .matchAny(this::unhandled)
                .build();
    }
}