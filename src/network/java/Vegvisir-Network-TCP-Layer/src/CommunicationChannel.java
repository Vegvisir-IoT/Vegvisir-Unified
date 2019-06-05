import java.util.concurrent.LinkedBlockingDeque;



public class CommunicationChannel {

    private LinkedBlockingDeque<Integer> comQueue = null;
    public CommunicationChannel()
    {
        // initialize the communication queue
        comQueue = new LinkedBlockingDeque<Integer>();

    }

    public void add(Integer in_item)
    {
        comQueue.add(in_item);
    }

    public Integer poll()
    {
        return comQueue.poll();
    }

    public Integer size()
    {
        return comQueue.size();
    }




}
