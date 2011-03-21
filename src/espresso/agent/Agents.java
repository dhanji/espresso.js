package espresso.agent;

import com.google.common.collect.MapMaker;
import org.mozilla.javascript.Context;
import org.mozilla.javascript.Function;
import org.mozilla.javascript.Scriptable;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author dhanji@gmail.com (Dhanji R. Prasanna)
 */
public class Agents {
  private static final ConcurrentMap<String, AgentDescriptor> agents = new MapMaker().makeMap();
  private static final ExecutorService executor = Executors.newSingleThreadExecutor();

  public static synchronized void registerAgent(String name, Scriptable thisObj,
                                                Function callback) {
    agents.putIfAbsent(name, new AgentDescriptor(thisObj, callback));
  }

  public static void sendMessage(final String name, final Scriptable msg) {
//    agents.get(name).messages.add(msg);

    // Trigger executor (later delink the executor's queue from the message queue,
    // so we can load balance the agents over our thread pools)
    executor.execute(new Runnable() {
      @Override
      public void run() {
        AgentDescriptor agent = agents.get(name);

        dispatch(msg, agent.thisObj, agent.callback);
      }
    });
  }

  public static void dispatch(Scriptable msg, Scriptable thisObj,
                               Function callback) {
    // Deferred execution in background thread.
    Context context = Context.getCurrentContext();
    boolean shouldClose = false;
    if (null == context) {
      shouldClose = true;
      context = Context.enter();
    }
    callback.call(context, context.initStandardObjects(), thisObj,
        msg == null ? null : new Object[] { msg });

    if (shouldClose)
      Context.exit();
  }

  public static synchronized void close(String name) throws InterruptedException {
    // this is super broken, so fix it!
    AgentDescriptor agent = agents.remove(name);

    // TODO wait for queue to drain.
    //...

    if (agents.isEmpty()) {
      executor.shutdown();
    }
  }

  public static class AgentDescriptor {
    private final Scriptable thisObj;
    private final Function callback;
    private final Queue<Scriptable> messages = new ConcurrentLinkedQueue<Scriptable>();

    public AgentDescriptor(Scriptable thisObj, Function callback) {
      this.thisObj = thisObj;
      this.callback = callback;
    }
  }
}
