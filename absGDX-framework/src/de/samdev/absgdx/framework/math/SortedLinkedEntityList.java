package de.samdev.absgdx.framework.math;

import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;

import de.samdev.absgdx.framework.entities.Entity;

/**
 * This is an Implementation of an (always) sorted LinkedList
 * 
 * The elements are put into the right position when added
 * 
 * Elements with the same z-layer are ordered by the order of adding
 *
 */
public class SortedLinkedEntityList implements List<Entity> {

	private LinkedList<Entity> list; // ArrayList is faster on Insert by a factor of [4] - (and its nowhere slower) :-/
	
	private final static Comparator<Entity> layerComparator = new Comparator<Entity>() {
		@Override
		public int compare(Entity e0, Entity e1) {
			return Integer.compare(-e0.zlayer, -e1.zlayer);
		}
	};
	 
	/**
	 * Creates a new SortedLinkedEntityList
	 */
	public SortedLinkedEntityList() {
		super();
		
		list = new LinkedList<Entity>();
	}

	@Override
	public boolean add(Entity e) {
		ListIterator<Entity> it = list.listIterator();
		
		while (it.hasNext()) {
			if (it.next().zlayer <= e.zlayer) {
				it.previous();
				it.add(e);
				return true;
			}
		}

		it.add(e);
		return true;
	}

	@Override
	public Iterator<Entity> iterator() {
		return list.iterator();
	}

	@Override
	@Deprecated
	public void add(int index, Entity element) {
		throw new UnsupportedOperationException();
	}

	@Override
	public boolean addAll(Collection<? extends Entity> c) {
		for (Entity e : c) {
			add(e);
		}
		return true;
	}

	@Override
	@Deprecated
	public boolean addAll(int index, Collection<? extends Entity> c) {
		throw new UnsupportedOperationException();
	}

	@Override
	public void clear() {
		list.clear();
	}

	@Override
	public boolean contains(Object o) {
		return list.contains(o);
	}

	@Override
	public boolean containsAll(Collection<?> c) {
		return list.containsAll(c);
	}

	@Override
	public Entity get(int index) {
		return list.get(index);
	}

	@Override
	public int indexOf(Object o) {
		return list.indexOf(o);
	}

	@Override
	public boolean isEmpty() {
		return list.isEmpty();
	}

	@Override
	public int lastIndexOf(Object o) {
		return list.lastIndexOf(o);
	}

	@Override
	public ReadOnlyListIterator<Entity> listIterator() {
		return new ReadOnlyListIterator<Entity>(list.listIterator());
	}

	@Override
	public ReadOnlyListIterator<Entity> listIterator(int index) {
		return new ReadOnlyListIterator<Entity>(list.listIterator(index));
	}

	@Override
	public boolean remove(Object o) {
		return list.remove(o);
	}

	@Override
	public Entity remove(int index) {
		return list.remove(index);
	}

	@Override
	public boolean removeAll(Collection<?> c) {
		return list.removeAll(c);
	}

	@Override
	@Deprecated
	public boolean retainAll(Collection<?> c) {
		throw new UnsupportedOperationException();
	}

	@Override
	@Deprecated
	public Entity set(int index, Entity element) {
		throw new UnsupportedOperationException();
	}

	@Override
	public int size() {
		return list.size();
	}

	@Override
	public List<Entity> subList(int fromIndex, int toIndex) {
		return list.subList(fromIndex, toIndex);
	}

	@Override
	public Object[] toArray() {
		return list.toArray();
	}

	@Override
	public <T> T[] toArray(T[] a) {
		return list.toArray(a);
	}
	
	/**
	 * Returns an iterator over the elements in this list in reverse sequential order. The elements will be returned in order from last (tail) to first (head).
	 * 
	 * @return an iterator over the elements in this list in reverse sequence
	 */
	public Iterator<Entity> descendingIterator() {
		return list.descendingIterator();
	}

	/**
	 * Checks if the list is still sorted (or if some z-Layer values have changed)
	 * Sorts the list again if needed
	 */
	public void testIntegrity() {
		ListIterator<Entity> it = list.listIterator();
		
		if (it.hasNext()) {
			int last = it.next().zlayer;
			
			while (it.hasNext()) {
				int current = it.next().zlayer;
				
				if (current > last) {
					Collections.sort(list, layerComparator);
					
					return;
				}
				last = current;
			}
		}
	}
	
	/**
	 *  removes all Entities with alive=false in O(n) time
	 */
	public void removeDeadEntities() {
		ListIterator<Entity> it = list.listIterator();
		
		while (it.hasNext()) {
			Entity e = it.next();
			if (! e.alive) {
				it.remove();
				
				boolean succ = e.collisionOwner.removeGeometries(e.listCollisionGeometries());
				
				assert succ; //TODO Remove asserts ?? 
			}
		}
	}
}
