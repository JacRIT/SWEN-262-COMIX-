package UI.Concrete;

import java.util.Iterator;

import Model.ModifiedQueue;

public class QBackwardIterator<T> implements Iterator<T> {

  private ModifiedQueue<T> queue;

  public QBackwardIterator(ModifiedQueue<T> queue) {
    this.queue = queue;
  }

  @Override
  public boolean hasNext() {
    int size = this.queue.size();
    int index = this.queue.getIndex();

    return size >= 1 && index >= 0;

    // if (size == 0)
    // return false;

    // if (index == -1)
    // return false;

    // return true;
  }

  @Override
  public T next() {
    T object = this.queue.getCurrent();
    if (this.queue.getIndex() > 0)
      this.queue.moveBackward();
    return object;
  }

}
