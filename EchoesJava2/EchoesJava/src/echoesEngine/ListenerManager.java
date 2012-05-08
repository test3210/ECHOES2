package echoesEngine;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import utils.Interfaces.*;
import utils.Enums.*;

@SuppressWarnings("rawtypes")
public class ListenerManager implements IAgentListener, IRenderingListener
{
  private ChildModelListenerImpl childModelListener;
  private RenderingListenerImpl renderingListener;
  private AgentListenerImpl agentListener;
  private PauseListenerImpl pauseListener;
  private EventListenerImpl eventListener;
  private TouchListenerImpl touchListener;

  private static ListenerManager theManager = new ListenerManager();
  
  public static ListenerManager GetInstance()
  {
    return theManager;
  }
  
  private ListenerManager()
  {
    childModelListener = new ChildModelListenerImpl();
    renderingListener = new RenderingListenerImpl();
    agentListener = new AgentListenerImpl();
    eventListener = new EventListenerImpl();
    pauseListener = new PauseListenerImpl();
    touchListener = new TouchListenerImpl();
  }
  
  public void Subscribe(Object listener)
  {
    if (listener instanceof IChildModelListener)
      childModelListener.subscribe(listener);
    if (listener instanceof IRenderingListener)
      renderingListener.subscribe(listener);
    else if (listener instanceof IPauseListener)
      pauseListener.subscribe(listener);
    else if (listener instanceof IEventListener)
      eventListener.subscribe(listener);
    else if (listener instanceof IAgentListener)
      agentListener.unsubscribe(listener);
    else if (listener instanceof ITouchListener)
      touchListener.subscribe(listener);
  }
  
  public void Unsubscribe(Object listener)
  {
    if (listener instanceof IChildModelListener)
      childModelListener.unsubscribe(listener);
    if (listener instanceof IRenderingListener)
      renderingListener.unsubscribe(listener);
    else if (listener instanceof IPauseListener)
      pauseListener.unsubscribe(listener);
    else if (listener instanceof IEventListener)
      eventListener.unsubscribe(listener);
    else if (listener instanceof IAgentListener)
      agentListener.unsubscribe(listener);
    else if (listener instanceof ITouchListener)
      touchListener.unsubscribe(listener);
  }
  
  public void registerForEvents(Object listener, ListenerType type)
  {
    switch (type)
    {
    case childModel:
      childModelListener.subscribe(listener);
      break;
    case renderer:
      renderingListener.subscribe(listener);
      break;
    case agent:
      agentListener.subscribe(listener);
      break;
    case event:
      eventListener.subscribe(listener);
      break;
    case pause:
      pauseListener.subscribe(listener);
      break;
    case touch:
      touchListener.subscribe(listener);
      break;
    }
  }
  
  public void unregisterForEvents(Object listener, ListenerType type)
  {
    switch (type)
    {
    case childModel:
      //childModelListener.unsubscribe(listener);
      break;
    case agent:
      agentListener.unsubscribe(listener);
      break;
    case event:
      eventListener.unsubscribe(listener);
      break;
    case pause:
      pauseListener.unsubscribe(listener);
      break;
    }
  }
  
  public Object retrieve(ListenerType type)
  {
    switch (type)
    {
    case childModel:
      return childModelListener;
    case renderer:
      return renderingListener;
    case agent:
      return agentListener;
    case event:
      return eventListener;
    case pause:
      return pauseListener;
    case touch:
      return touchListener;
    }
    return null;
  }
  
  // IAgentListener
  @Override
  public void agentActionStarted(String agent, String object, List<String> details)
  {
    agentListener.agentActionStarted(agent,  object,  details);
  }
   
  @Override
  public void agentActionCompleted(String agent, String object, List<String> details)
  {
    agentListener.agentActionCompleted(agent,  object,  details);
  }
  
  @Override
  public void agentActionFailed (String agentId, String action, List<String> details, String reason)
  {
    agentListener.agentActionFailed(agentId,  action,  details, reason);
  }
  
  // IRenderongListener
  
  @Override
  public void objectAdded(String objId, Map<String, String> props)
  {
    renderingListener.objectAdded(objId, props);
  }

  @Override
  public void objectRemoved(String objId)
  {
    renderingListener.objectRemoved(objId);
  }

  @Override
  public void objectPropertyChanged(String objId, String propName, String propValue)
  {
    renderingListener.objectPropertyChanged(objId, propName, propValue);
  }

  @Override
  public void userStarted(String name)
  {
    renderingListener.userStarted(name);
  }

  @Override
  public void userTouchedObject(String objId)
  {
    renderingListener.userTouchedObject(objId);
  }

  @Override
  public void userTouchedAgent(String agentId)
  {
    renderingListener.userTouchedAgent(agentId);
  }

  @Override
  public void agentAdded(String agentId, Map<String, String> props)
  {
    renderingListener.agentAdded(agentId, props);
  }

  @Override
  public void agentRemoved(String agentId)
  {
    renderingListener.agentRemoved(agentId);
  }

  @Override
  public void agentPropertyChanged(String agentId, String propName,String propValue)
  {
    renderingListener.agentPropertyChanged(agentId, propName, propValue);
  }

  @Override
  public void worldPropertyChanged(String propName, String propValue)
  {
    renderingListener.worldPropertyChanged(propName, propValue);
  }

  @Override
  public void scenarioStarted(String name)
  {
    renderingListener.scenarioStarted(name);
  }

  @Override
  public void scenarioEnded(String name)
  {
    renderingListener.scenarioEnded(name);
  }
 
  private class ChildModelListenerImpl implements IChildModelListener
  {
    private Map<Integer, IChildModelListener> childModelListeners = new HashMap<Integer, IChildModelListener> ();
  
    @Override
    public void engagementEstimate(Object engagement, double confidence)
    {
      Iterator<?> it = childModelListeners.entrySet().iterator();
      while (it.hasNext()) 
      {
        Map.Entry pairs = (Map.Entry)it.next();
        IChildModelListener nextListener = (IChildModelListener)pairs.getValue();
        nextListener.engagementEstimate(engagement, confidence);
      }
    }
    
    public void subscribe(Object listener)
    {
      Integer key = new Integer(listener.hashCode());
      if (!childModelListeners.containsKey(key))
        childModelListeners.put(key, (IChildModelListener)listener);
    }
    
    public void unsubscribe(Object listener)
    {
      Integer key = new Integer(listener.hashCode());
      if (childModelListeners.containsKey(key))
        childModelListeners.remove(key);
    }
  }
  
  private class RenderingListenerImpl implements IRenderingListener
  {
    private Map<Integer, IRenderingListener> renderingListeners = new HashMap<Integer, IRenderingListener> ();

    @Override
    public void objectAdded(String objId, Map<String, String> props)
    {
      Iterator<?> it = renderingListeners.entrySet().iterator();
      while (it.hasNext()) 
      {
        Map.Entry pairs = (Map.Entry)it.next();
        IRenderingListener nextListener = (IRenderingListener)pairs.getValue();
        nextListener.objectAdded(objId, props);
      }
    }

    @Override
    public void objectRemoved(String objId)
    {
      Iterator<?> it = renderingListeners.entrySet().iterator();
      while (it.hasNext()) 
      {
        Map.Entry pairs = (Map.Entry)it.next();
        IRenderingListener nextListener = (IRenderingListener)pairs.getValue();
        nextListener.objectRemoved(objId);
      }
    }

    @Override
    public void objectPropertyChanged(String objId, String propName, String propValue)
    {
      Iterator<?> it = renderingListeners.entrySet().iterator();
      while (it.hasNext()) 
      {
        Map.Entry pairs = (Map.Entry)it.next();
        IRenderingListener nextListener = (IRenderingListener)pairs.getValue();
        nextListener.objectPropertyChanged(objId, propName, propValue);
      }
    }

    @Override
    public void userStarted(String name)
    {
      Iterator<?> it = renderingListeners.entrySet().iterator();
      while (it.hasNext()) 
      {
        Map.Entry pairs = (Map.Entry)it.next();
        IRenderingListener nextListener = (IRenderingListener)pairs.getValue();
        nextListener.userStarted(name);
      }
    }

    @Override
    public void userTouchedObject(String objId)
    {
      Iterator<?> it = renderingListeners.entrySet().iterator();
      while (it.hasNext()) 
      {
        Map.Entry pairs = (Map.Entry)it.next();
        IRenderingListener nextListener = (IRenderingListener)pairs.getValue();
        nextListener.userTouchedObject(objId);
      }
    }

    @Override
    public void userTouchedAgent(String agentId)
    {
      Iterator<?> it = renderingListeners.entrySet().iterator();
      while (it.hasNext()) 
      {
        Map.Entry pairs = (Map.Entry)it.next();
        IRenderingListener nextListener = (IRenderingListener)pairs.getValue();
        nextListener.userTouchedAgent(agentId);
      }
    }

    @Override
    public void agentAdded(String agentId, Map<String, String> props)
    {
      Iterator<?> it = renderingListeners.entrySet().iterator();
      while (it.hasNext()) 
      {
        Map.Entry pairs = (Map.Entry)it.next();
        IRenderingListener nextListener = (IRenderingListener)pairs.getValue();
        nextListener.agentAdded(agentId, props);
      }
    }

    @Override
    public void agentRemoved(String agentId)
    {
      Iterator<?> it = renderingListeners.entrySet().iterator();
      while (it.hasNext()) 
      {
        Map.Entry pairs = (Map.Entry)it.next();
        IRenderingListener nextListener = (IRenderingListener)pairs.getValue();
        nextListener.agentRemoved(agentId);
      }
    }

    @Override
    public void agentPropertyChanged(String agentId, String propName, String propValue)
    {
      Iterator<?> it = renderingListeners.entrySet().iterator();
      while (it.hasNext()) 
      {
        Map.Entry pairs = (Map.Entry)it.next();
        IRenderingListener nextListener = (IRenderingListener)pairs.getValue();
        nextListener.agentPropertyChanged(agentId, propName, propValue);
      }
    }

    @Override
    public void worldPropertyChanged(String propName, String propValue)
    {
      Iterator<?> it = renderingListeners.entrySet().iterator();
      while (it.hasNext()) 
      {
        Map.Entry pairs = (Map.Entry)it.next();
        IRenderingListener nextListener = (IRenderingListener)pairs.getValue();
        nextListener.worldPropertyChanged(propName, propValue);
      }
    }

    @Override
    public void scenarioStarted(String name)
    {
      Iterator<?> it = renderingListeners.entrySet().iterator();
      while (it.hasNext()) 
      {
        Map.Entry pairs = (Map.Entry)it.next();
        IRenderingListener nextListener = (IRenderingListener)pairs.getValue();
        nextListener.scenarioStarted(name);
      }
    }

    @Override
    public void scenarioEnded(String name)
    {
      Iterator<?> it = renderingListeners.entrySet().iterator();
      while (it.hasNext()) 
      {
        Map.Entry pairs = (Map.Entry)it.next();
        IRenderingListener nextListener = (IRenderingListener)pairs.getValue();
        nextListener.scenarioEnded(name);
      }
    }
    
    public void subscribe(Object listener)
    {
      Integer key = new Integer(listener.hashCode());
      if (!renderingListeners.containsKey(key))
        renderingListeners.put(key, (IRenderingListener)listener);
    }
    
    public void unsubscribe(Object listener)
    {
      Integer key = new Integer(listener.hashCode());
      if (renderingListeners.containsKey(key))
        renderingListeners.remove(key);
    }
  }
  
  private class AgentListenerImpl implements IAgentListener
  {
    private Map<Integer, IAgentListener> agentListeners = new HashMap<Integer, IAgentListener> ();
    
    @Override
    public void agentActionStarted(String agentId, String action, List<String> details)
    {
      Iterator<?> it = agentListeners.entrySet().iterator();
      while (it.hasNext()) 
      {
        Map.Entry pairs = (Map.Entry)it.next();
        IAgentListener nextListener = (IAgentListener)pairs.getValue();
        nextListener.agentActionStarted(agentId, action, details);
      }
    }

    @Override
    public void agentActionCompleted(String agentId, String action, List<String> details)
    {
      Iterator<?> it = agentListeners.entrySet().iterator();
      while (it.hasNext()) 
      {
        Map.Entry pairs = (Map.Entry)it.next();
        IAgentListener nextListener = (IAgentListener)pairs.getValue();
        nextListener.agentActionCompleted(agentId, action, details);
      }
    }

    @Override
    public void agentActionFailed(String agentId, String action, List<String> details, String reason)
    {
      Iterator<?> it = agentListeners.entrySet().iterator();
      while (it.hasNext()) 
      {
        Map.Entry pairs = (Map.Entry)it.next();
        IAgentListener nextListener = (IAgentListener)pairs.getValue();
        nextListener.agentActionFailed(agentId, action, details, reason);
      }
    }
    
    public void subscribe(Object listener)
    {
      Integer key = new Integer(listener.hashCode());
      if (!agentListeners.containsKey(key))
        agentListeners.put(key, (IAgentListener)listener);
    }
    
    public void unsubscribe(Object listener)
    {
      Integer key = new Integer(listener.hashCode());
      if (agentListeners.containsKey(key))
        agentListeners.remove(key);
    }
  }
  
  private class PauseListenerImpl implements IPauseListener
  {
    private Map<Integer, IPauseListener> pauseListeners = new HashMap<Integer, IPauseListener> ();
    private boolean paused;
  
    @Override
    public void setPaused(boolean value)
    {
      paused = value;
      
      Iterator<?> it = pauseListeners.entrySet().iterator();
      while (it.hasNext()) 
      {
        Map.Entry pairs = (Map.Entry)it.next();
        IPauseListener nextListener = (IPauseListener)pairs.getValue();
        nextListener.setPaused(paused);
      }
    }
    
    public void subscribe(Object listener)
    {
      Integer key = new Integer(listener.hashCode());
      if (!pauseListeners.containsKey(key))
        pauseListeners.put(key, (IPauseListener)listener);
    }
    
    public void unsubscribe(Object listener)
    {
      Integer key = new Integer(listener.hashCode());
      if (pauseListeners.containsKey(key))
        pauseListeners.remove(key);
    }
  }
  
  private class EventListenerImpl implements IEventListener
  {
    private Map<Integer, IEventListener> eventListeners = new HashMap<Integer, IEventListener> ();
   
    @Override
    public void userGazeEvent(String details, long msec)
    {
      Iterator<?> it = eventListeners.entrySet().iterator();
      while (it.hasNext()) 
      {
        Map.Entry pairs = (Map.Entry)it.next();
        IEventListener nextListener = (IEventListener)pairs.getValue();
        nextListener.userGazeEvent(details, msec);
      }
    }

    @Override
    public void userTouchEvent(String objId)
    {
      Iterator<?> it = eventListeners.entrySet().iterator();
      while (it.hasNext()) 
      {
        Map.Entry pairs = (Map.Entry)it.next();
        IEventListener nextListener = (IEventListener)pairs.getValue();
        nextListener.userTouchEvent(objId);
      }
    }

    @Override
    public void userAction(UserActionType action)
    {
      Iterator<?> it = eventListeners.entrySet().iterator();
      while (it.hasNext()) 
      {
        Map.Entry pairs = (Map.Entry)it.next();
        IEventListener nextListener = (IEventListener)pairs.getValue();
        nextListener.userAction(action);
      }
    }
  
    public void subscribe(Object listener)
    {
      Integer key = new Integer(listener.hashCode());
      if (!eventListeners.containsKey(key))
        eventListeners.put(key, (IEventListener)listener);
    }
    
    public void unsubscribe(Object listener)
    {
      Integer key = new Integer(listener.hashCode());
      if (eventListeners.containsKey(key))
        eventListeners.remove(key);
    }
  }

  private class TouchListenerImpl implements ITouchListener
  {
    private Map<Integer, ITouchListener> touchListeners = new HashMap<Integer, ITouchListener> ();

    @Override
    public void click(int x, int y, int width, int height)
    { 
      Iterator<?> it = touchListeners.entrySet().iterator();
      while (it.hasNext()) 
      {
        Map.Entry pairs = (Map.Entry)it.next();
        ITouchListener nextListener = (ITouchListener)pairs.getValue();
        nextListener.click(x, y, width, height);
      }
    }

    @Override
    public void pointDown(int id, int x, int y, int width, int height)
    {
      Iterator<?> it = touchListeners.entrySet().iterator();
      while (it.hasNext()) 
      {
        Map.Entry pairs = (Map.Entry)it.next();
        ITouchListener nextListener = (ITouchListener)pairs.getValue();
        nextListener.pointDown(id, x, y, width, height);
      }
    }

    @Override
    public void pointMoved(int id, int newX, int newY, int newWidth, int newHeight)
    {
      Iterator<?> it = touchListeners.entrySet().iterator();
      while (it.hasNext()) 
      {
        Map.Entry pairs = (Map.Entry)it.next();
        ITouchListener nextListener = (ITouchListener)pairs.getValue();
        nextListener.pointMoved(id, newX, newY, newWidth, newHeight);
      }
    }

    @Override
    public void pointUp(int id)
    {
      Iterator<?> it = touchListeners.entrySet().iterator();
      while (it.hasNext()) 
      {
        Map.Entry pairs = (Map.Entry)it.next();
        ITouchListener nextListener = (ITouchListener)pairs.getValue();
        nextListener.pointUp(id);
      }
    }
    
    public void subscribe(Object listener)
    {
      Integer key = new Integer(listener.hashCode());
      if (!touchListeners.containsKey(key))
        touchListeners.put(key, (ITouchListener)listener);
    }
    
    public void unsubscribe(Object listener)
    {
      Integer key = new Integer(listener.hashCode());
      if (touchListeners.containsKey(key))
        touchListeners.remove(key);
    }
  }
}
