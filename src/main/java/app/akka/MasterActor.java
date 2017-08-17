package app.akka;

import akka.actor.AbstractActor;
import akka.actor.ActorRef;
import akka.actor.Props;
import akka.japi.pf.ReceiveBuilder;
import app.akka.sort.MergeSortActor;

public class MasterActor extends AbstractActor {

    @Override
    public Receive createReceive() {
        return new ReceiveBuilder()
                .match(ListMessage.class, listMessage -> {
                    ActorRef actorRef = getContext().actorOf(Props.create(MergeSortActor.class, MergeSortActor::new));
                    actorRef.tell(listMessage, getSelf());
                })
                .match(SortedListMessage.class, (SortedListMessage sortedListMessage) -> {
                    sortedListMessage.getSortedList().forEach(System.out::println);
                    getContext().getSystem().terminate();
                })
                .matchAny(this::unhandled)
                .build();
    }
}