///////////////////////////////////////////////////////////////////////////////
//
// Main Class File:  ImageLoopEditor.java
// File:             LinkedLoop.java
// Semester:         CS 367 Fall 2017
//
// Author:           Mike Huang		miked.huang@gmail.com
// CS Login:         zhuang335
// Lecturer's Name:  Charles N. Fischer
//
// Pair Partner:     Sam Adams
// Email:            sajadams@gmail.com
// CS Login:         sjadams3
// Lecturer's Name:  Charles N. Fischer
//
//////////////////////////// 80 columns wide //////////////////////////////////

import java.util.Iterator;

/**
 * Implementation of LoopADT using circular doubly-linked list.
 *
 * <p>Bugs: none known
 *
 * @author Mike Huang
 */
public class LinkedLoop<E> implements LoopADT<E>, Iterable<E> 
{
	
	/** Private doubly-linked node */
	private DblListnode<E> items;
	
	/** Private integer storing the total number of nodes in the list */
	private int numItems;

	/**
	 * Constructs an linked loop
	 */
	public LinkedLoop() 
	{
		items = null;
		numItems = 0;
	}

	/**
	 * Add a new DblListnode to the front of the current linked loop
	 * 
	 * @param item A DblListnode to add to the list          
	 */
	@Override
	public void add(E item) 
	{
		
		DblListnode<E> newNode = new DblListnode<E>(item);
		// create the first node and link it to itself
		if (items == null) 
		{
			items = newNode;
			items.setNext(items);
			items.setPrev(items);
		}
		// if there's already a node, link it in front of the current node
		else 
		{
			
			// update the newNode's pointers
			newNode.setPrev(items.getPrev());
			newNode.setNext(items);
			
			// update pointers to the new Node
			items.getPrev().setNext(newNode);
			items.setPrev(newNode);
			
			// update current node to the added node
			items = newNode;
		}
		numItems++;
	}

	/**
	 * Return the current item from the loop
	 * 
	 * @return current item
	 */
	@Override
	public E getCurrent() throws EmptyLoopException 
	{
		if (items == null) 
		{
			throw new EmptyLoopException();
		}
		return items.getData();
	}

	/**
	 * Return the current object from the loop and remove. Make the one after
	 * current.
	 * 
	 * @return removed current object
	 */
	@Override
	public E removeCurrent() throws EmptyLoopException 
	{
		if (items == null) 
		{
			throw new EmptyLoopException();
		}
		// store current item so it can be displayed after being removed
		DblListnode<E> temp = items;
		// if this is the last item set items node to null
		if (numItems == 1) 
		{
			items = null;
		}
		// connect nodes around the current node to remove it
		else 
		{
			items = temp.getPrev();
			temp.getNext().setPrev(items);
			items.setNext(temp.getNext());
		}
		numItems--; 
		return temp.getData();
	}

	/**
	 * Move to the next node
	 */
	@Override
	public void next() 
	{
		items = items.getNext();
	}

	/**
	 * Move to the previous node
	 */
	@Override
	public void previous() 
	{
		items = items.getPrev();
	}

	/**
	 * Check if list is empty
	 * 
	 * @return true if the list is empty and false if it's not empty
	 */
	@Override
	public boolean isEmpty() 
	{
		if (numItems == 0) 
		{
			return true;
		}
		return false;
	}

	/**
	 * Return the total number items in the list
	 * 
	 * @return total number of items
	 */
	@Override
	public int size() 
	{
		return numItems;
	}

	/**
	 * Creates an iterator for the circular doubly-linked list
	 * 
	 * @return circular doubly-linked list iterator
	 */
	@Override
	public Iterator<E> iterator() 
	{
		return new LinkedLoopIterator<E>(items, numItems);
	}

}