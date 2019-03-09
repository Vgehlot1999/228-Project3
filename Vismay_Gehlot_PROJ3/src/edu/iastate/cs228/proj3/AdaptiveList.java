package edu.iastate.cs228.proj3;

/*
 *  @author Vismay Gehlot
 *
 *
 *  An implementation of List<E> based on a doubly-linked list 
 *  with an array for indexed reads/writes
 *
 */

import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.NoSuchElementException;
import java.util.Objects;

import org.w3c.dom.Node;

public class AdaptiveList<E> implements List<E>
{
  public class ListNode 
  {                     
    public E data;        
    public ListNode next; 
    public ListNode prev; 
    
    public ListNode(E item)
    {
      data = item;
      next = prev = null;
    }
  }
  
  public ListNode head;  // dummy node made public for testing.
  public ListNode tail;  // dummy node made public for testing.
  private int numItems;  // number of data items
  private boolean linkedUTD; // true if the linked list is up-to-date.

  public E[] theArray;  // the array for storing elements
  private boolean arrayUTD; // true if the array is up-to-date.

  public AdaptiveList()
  {
    clear();
  }

  @Override
  public void clear()
  {
    head = new ListNode(null);
    tail = new ListNode(null);
    head.next = tail;
    tail.prev = head;
    numItems = 0;
    linkedUTD = true;
    arrayUTD = false;
    theArray = null;
  }

  public boolean getlinkedUTD()
  {
    return linkedUTD;
  }

  public boolean getarrayUTD()
  {
    return arrayUTD;
  }

  public AdaptiveList(Collection<? extends E> c)
  {
    clear();
    addAll(c);
  }

  // Removes the node from the linked list.
  // This method should be used to remove a node 
  // from the linked list.
  private void unlink(ListNode toRemove)
  {
    if ( toRemove == head || toRemove == tail )
      throw new RuntimeException("An attempt to remove head or tail");
    toRemove.prev.next = toRemove.next;
    toRemove.next.prev = toRemove.prev;
  }

  // Inserts new node toAdd right after old node current.
  // This method should be used to add a node to the linked list.
  private void link(ListNode current, ListNode toAdd)
  {
    if ( current == tail )
      throw new RuntimeException("An attempt to chain after tail");
    if ( toAdd == head || toAdd == tail )
      throw new RuntimeException("An attempt to add head/tail as a new node");
    toAdd.next = current.next;
    toAdd.next.prev = toAdd;
    toAdd.prev = current;
    current.next = toAdd;
  }

  private void updateArray() // makes theArray up-to-date.
  {
    if ( numItems < 0 )
      throw new RuntimeException("numItems is negative: " + numItems);
    if ( ! linkedUTD )
      throw new RuntimeException("linkedUTD is false");
    
    toArray();
    arrayUTD = true;
  }

  private void updateLinked() // makes the linked list up-to-date.
  {
    if ( numItems < 0 )
      throw new RuntimeException("numItems is negative: " + numItems);
    if ( ! arrayUTD )
      throw new RuntimeException("arrayUTD is false");

    if ( theArray == null || theArray.length < numItems )
      throw new RuntimeException("theArray is null or shorter");

    head.next = tail;
    tail.prev = head;
   
    for (int i = 0; i < theArray.length; i++)
    {
    	add(i, theArray[i]);
    }
    
    linkedUTD = true;
  }

  @Override
  public int size()
  {
	  return numItems;
  }

  @Override
  public boolean isEmpty()
  {
	  if (numItems == 0)
	  {
		  return true;
	  }
	  return false;
  }

  @Override
  public boolean add(E obj)
  {
	  if (!linkedUTD)
	  {
		  updateLinked();
	  }
	  
	  checkNode(findNode(numItems - 1));
	  link(findNode(numItems - 1), new ListNode(obj));
	  numItems++;
	  arrayUTD = false;
	  
	  return true; 
  }

  @Override
  public boolean addAll(Collection< ? extends E> c)
  {
	
	  	for(E temp : c)
	  	{
	  		add(temp);
	  	}
	
	  	return true; 
  }

  @Override
  public boolean remove(Object obj)
  {
	  remove(indexOf(obj));
	  return true;
  }

  private void checkIndex(int pos) // a helper method
  {
    if ( pos >= numItems || pos < 0 )
     throw new IndexOutOfBoundsException(
       "Index: " + pos + ", Size: " + numItems);
  }

  private void checkIndex2(int pos) // a helper method
  {
    if ( pos > numItems || pos < 0 )
     throw new IndexOutOfBoundsException(
       "Index: " + pos + ", Size: " + numItems);
  }

  private void checkNode(ListNode cur) // a helper method
  {
    if ( cur == null || cur == tail )
     throw new RuntimeException(
      "numItems: " + numItems + " is too large");
  }

  private ListNode findNode(int pos)   // a helper method
  {
    ListNode tempNode = head;
    for ( int i = 0; i < pos; i++ )
    {
      checkNode(tempNode);
      tempNode = tempNode.next;
    }
    checkNode(tempNode);
    return tempNode;
  }

  @Override
  public void add(int pos, E obj)
  {
	  checkIndex2(pos);
	  
	  if (!linkedUTD)
	  {
		  updateLinked();
	  }
	  
	  if (Objects.isNull(obj))
	  {
		  return;
	  }
	  
	  link(findNode(pos), new ListNode(obj));
	  arrayUTD = false;
  }

  @Override
  public boolean addAll(int pos, Collection< ? extends E> c)
  {
	  checkIndex2(pos);
	  if (c.isEmpty())
	  {
		  return false;
	  }
	  int i = pos;
	  
	  for (E e : c)
	  {
		  add(i, e);
		  i++;
	  }
	  
	  return true; 
  }

  @Override
  public E remove(int pos)
  {
	  if (!linkedUTD)
	  {
		  updateLinked();
	  }
    
	  checkIndex(pos);
	  ListNode tempNode = findNode(pos + 1);
	  
	  E temp = tempNode.data;
	  unlink(tempNode);
	  numItems = numItems - 1;
	  arrayUTD = false;
	  return temp; 
  }

  @Override
  public E get(int pos)
  {
	  checkIndex(pos);
	  ListNode tempNode = findNode(pos + 1);
	  updateArray();
	  return tempNode.data;
  }

  @Override
  public E set(int pos, E obj)
  {
	  if (!linkedUTD)
	  {
		  updateLinked();
	  }
	  checkIndex(pos);
	  ListNode tempNode = findNode(pos + 1);
	  
	  E temp = tempNode.data;
	  tempNode.data = obj;
	  return temp;
  } 

  /**
   *  If the number of elements is at most 1, 
   *  the method returns false. Otherwise, it 
   *  reverses the order of the elements in the 
   *  array without using any additional array, 
   *  and returns true. Note that if the array 
   *  is modified, then linkedUTD needs to be set 
   *  to false.
   */
  public boolean reverse()
  {
	  if (size() <= 1)
	  {
		  return false;
	  }
	  
	  E temp;
	  if (numItems<= 1)
	  {
		  return false;
	  }
	  
	  for (int i = 0; i < (size()/2); i++)
	  {
		  temp = theArray[numItems - 1];
		  theArray[numItems - 1] = theArray[i];
		  theArray[i] = temp;
	  }
	  linkedUTD = false;
	  
	  return true;
  }

  
  /** 
   *  If the number of elements is at most 1, 
   *  the method returns false. Otherwise, it 
   *  swaps the items positioned at even index 
   *  with the subsequent one in odd index without 
   *  using any additional array, and returns true.
   *  Note that if the array is modified, then 
   *  linkedUTD needs to be set to false. 
   */
  public boolean reorderOddEven()
  {
	  if (this.size() < 2)
	  {
		  return false;
	  }
	  
	  if (!arrayUTD)
	  {
		  updateArray();
	  }
	  
	  linkedUTD = false;
	  E temp;
	  
	  for (int i = 0; i < size() / 2; i++)
	  {
		  temp = theArray[i];
		  theArray[i] = theArray[i + 1];
		  theArray[i + 1] = temp;
	  }
	  return true;
  }
  
  @Override
  public boolean contains(Object obj)
  {
	  AdaptiveListIterator x = new AdaptiveListIterator();
	  for (int i = 0; i < numItems; i++)
	  {
		  if (x.next() == obj)
		  {
			  return true;
		  }
	  }
	  return false;
  }

  @Override
  public boolean containsAll(Collection< ? > c)
  {  
	  for (Object e : c)
	  {
		  if (contains(e))
		  {
			  return true;
		  }
	  }
	  return false;
  }


  @Override
  public int indexOf(Object obj)
  {
	  if (this.isEmpty())
	  {
		  return -1;
	  }
	  AdaptiveListIterator x = new AdaptiveListIterator();
	  int count = 0;
	  
	  while (x.hasNext())
	  {
		  if(!x.next().equals(obj))
		  {
			  count++;
		  }
		  else 
		  {
              return count;
          }
	  }
	  return -1;
  }

  @Override
  public int lastIndexOf(Object obj)
  {
	  int finalIndex = -1;
	  
	  if (this.isEmpty())
	  {
		  return finalIndex;
	  }
	  
	  int count = 0;
	  AdaptiveListIterator x = new AdaptiveListIterator();
	  
	  while (x.hasNext())
	  {
		  if (x.next().equals(obj))
		  {
			  count++;
		  }
		  finalIndex++;
	  }
	  return finalIndex;
  }

  @Override
  public boolean removeAll(Collection<?> c)
  {
	  AdaptiveListIterator x = new AdaptiveListIterator();
	  boolean isChanged = false;
	  E temp;
	  
	  while(x.hasNext())
	  {
		  temp = x.next();
		  
		  if (c.contains(temp))
		  {
			  x.remove();
			  isChanged = true;
		  }
	  }
	  return isChanged;
  }

  @Override
  public boolean retainAll(Collection<?> c)
  {
	  AdaptiveListIterator x = new AdaptiveListIterator();
	  boolean isChanged = false;
	  E temp;
	  
	  while (x.hasNext())
	  {
		  temp = x.next();
		  if(!c.contains(temp))
		  {
			  x.remove();
			  isChanged = true;
		  }
	  }
	  if (c == null)
	  {
		  throw new NullPointerException();
	  }
	  return isChanged; 
  }

  @Override
  public Object[] toArray()
  {
	  if(!linkedUTD)
	  {
		  updateLinked();
	  }
	  Object[] tempArr = new Object[numItems];
	  AdaptiveListIterator x = new AdaptiveListIterator();
	  for(int i = 0; i < numItems; ++i)
	  {
		  tempArr[i] = x.next();
	  }
	  return tempArr;
  }
  
  
  /**
   * In here you are allowed to use only 
   * java.util.Arrays.copyOf method.
   */
  @Override
  public <T> T[] toArray(T[] arr)
  {
    // TODO
    return null; 
  }

  @Override
  public List<E> subList(int fromPos, int toPos)
  {
    throw new UnsupportedOperationException();
  }

  private class AdaptiveListIterator implements ListIterator<E>
  {
    private int    index;  // index of next node;
    private ListNode cur;  // node at index - 1
    private ListNode last; // node last visited by next() or previous()

    public AdaptiveListIterator()
    {
    	if ( ! linkedUTD ) updateLinked();
    	index = 0;
    	cur = head;
    	last = null;
    }
    public AdaptiveListIterator(int pos)
    {
    	if ( ! linkedUTD ) updateLinked();
    	index = pos;
    	last = null;
    	if (pos == 0)
    	{
    		cur = head;
    	}
    	else
    	{
    		cur = findNode(pos);
    	}
    }

    @Override
    public boolean hasNext()
    {
    	if (cur.next == tail)
    	{
    		return false;
    	}
    	return true;
    }

    @Override
    public E next()
    {
    	cur = cur.next;
    	last = cur;
    	return last.data;
    } 

    @Override
    public boolean hasPrevious()
    {
      if (cur.prev == head)
      {
    	  return false;
      }
      return true; 
    }

    @Override
    public E previous()
    {
    	last = cur;
    	cur = cur.prev;
    	return last.data;
    }
    
    @Override
    public int nextIndex()
    {
    	return index; 
    }

    @Override
    public int previousIndex()
    {
    	return index - 1; 
    }

    @Override
    public void remove()
    {
    	unlink(last);
    }

    @Override
    public void add(E obj)
    { 
    	if (obj == tail || obj == head)
    	{
    		throw new RuntimeException();
    	}
    	
    	
    	if (cur == tail)
    	{
    		throw new RuntimeException();
    	}
    	
    	ListNode tempNode = new ListNode(obj);
    	
    	tempNode.next = cur.next;
    	tempNode.next.prev = tempNode;
    	tempNode.prev = cur;
    	cur.next = tempNode;
    } 

    @Override
    public void set(E obj)
    {
      if (last != null)
      {
    	  last.data = obj;
      }
    }
  } // AdaptiveListIterator
  
  @Override
  public boolean equals(Object obj)
  {
    if ( ! linkedUTD ) updateLinked();
    if ( (obj == null) || ! ( obj instanceof List<?> ) )
      return false;
    List<?> list = (List<?>) obj;
    if ( list.size() != numItems ) return false;
    Iterator<?> iter = list.iterator();
    for ( ListNode tmp = head.next; tmp != tail; tmp = tmp.next )
    {
      if ( ! iter.hasNext() ) return false;
      Object t = iter.next();
      if ( ! (t == tmp.data || t != null && t.equals(tmp.data) ) )
         return false;
    }
    if ( iter.hasNext() ) return false;
    return true;
  }

  @Override
  public Iterator<E> iterator()
  {
    return new AdaptiveListIterator();
  }

  @Override
  public ListIterator<E> listIterator()
  {
    return new AdaptiveListIterator();
  }

  @Override
  public ListIterator<E> listIterator(int pos)
  {
    checkIndex2(pos);
    return new AdaptiveListIterator(pos);
  }

  // Adopted from the List<E> interface.
  @Override
  public int hashCode()
  {
    if ( ! linkedUTD ) updateLinked();
    int hashCode = 1;
    for ( E e : this )
       hashCode = 31 * hashCode + ( e == null ? 0 : e.hashCode() );
    return hashCode;
  }

  // You should use the toString*() methods to see if your code works as expected.
  @Override
  public String toString()
  {
   // Other options System.lineSeparator or
   // String.format with %n token...
   // Not making data field.
   String eol = System.getProperty("line.separator");
   return toStringArray() + eol + toStringLinked();
  }

  public String toStringArray()
  {
    String eol = System.getProperty("line.separator");
    StringBuilder strb = new StringBuilder();
    strb.append("A sequence of items from the most recent array:" + eol );
    strb.append('[');
    if ( theArray != null )
      for ( int j = 0; j < theArray.length; )
      {
        if ( theArray[j] != null )
           strb.append( theArray[j].toString() );
        else
           strb.append("-");
        j++;
        if ( j < theArray.length )
           strb.append(", ");
      }
    strb.append(']');
    return strb.toString();
  }

  public String toStringLinked()
  {
    return toStringLinked(null);
  }

  // iter can be null.
  public String toStringLinked(ListIterator<E> iter)
  {
    int cnt = 0;
    int loc = iter == null? -1 : iter.nextIndex();

    String eol = System.getProperty("line.separator");
    StringBuilder strb = new StringBuilder();
    strb.append("A sequence of items from the most recent linked list:" + eol );
    strb.append('(');
    for ( ListNode cur = head.next; cur != tail; )
    {
      if ( cur.data != null )
      {
        if ( loc == cnt )
        {
          strb.append("| ");
          loc = -1;
        }
        strb.append(cur.data.toString());
        cnt++;

        if ( loc == numItems && cnt == numItems )
        {
          strb.append(" |");
          loc = -1;
        }
      }
      else
         strb.append("-");
      
      cur = cur.next;
      if ( cur != tail )
         strb.append(", ");
    }
    strb.append(')');
    return strb.toString();
  }
}
