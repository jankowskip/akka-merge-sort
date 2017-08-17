package app;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;
import app.akka.ListMessage;
import app.akka.MasterActor;
import com.google.common.collect.ImmutableList;

public class Start {

    private static final String MERGE_SORT_SYSTEM = "MERGE_SORT_SYSTEM";

    public static void main(String[] args) {
        ActorSystem exampleSystem = ActorSystem.create(MERGE_SORT_SYSTEM);
        ActorRef master = exampleSystem.actorOf(Props.create(MasterActor.class, MasterActor::new));
        Integer[] arrayToSort = {2, 1, 3, 5, 7, 8, 12, 76, 3224, 12, 123, 65, 43, 1};
        ImmutableList<Integer> listToSort = ImmutableList.copyOf(arrayToSort);
        master.tell(new ListMessage(listToSort), ActorRef.noSender());
    }

}
