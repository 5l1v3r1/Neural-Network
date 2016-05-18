import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created by jshan on 3/15/2016.
 */
public class Test {
    static class Thing{
        String thing = "thing";

        public void setThing(String thing)
        {
            this.thing = thing;
        }

        public String getThing(){
            return thing;
        }
    }

    public static void main(String args[])
    {
        Thing thing1 = new Thing();
        Thing thing2 = new Thing();
        Thing thing3 = new Thing();

        thing1.setThing("thing1");
        thing2.setThing("thing2");
        thing3.setThing("thing3");

        List<Thing> thingList = new ArrayList<>();
        thingList.add(thing1);
        thingList.add(thing2);
        thingList.add(thing3);

        Iterator<Thing> it = thingList.iterator();

        while (it.hasNext())
        {
            Thing itThing = it.next();
            thingList.add(new Thing());
            it.remove();
            Thing newThing = itThing;
            System.out.println(newThing.getThing());
        }

        System.out.println(thingList.size());
    }
}
