package UI.Concrete;

import java.util.Iterator;

import Model.ModifiedQueue;

public class QForwardIteator<T> implements Iterator<T> {

  private ModifiedQueue<T> queue;

  public QForwardIteator(ModifiedQueue<T> queue) {
    this.queue = queue;
  }

  @Override
  public boolean hasNext() {
    int size = this.queue.size();
    int index = this.queue.getIndex();

    if (size - 1 > index)
      return true;

    return false;
  }

  @Override
  public T next() {
    T object = this.queue.getCurrent();
    this.queue.moveForward();
    return object;
  }

}
