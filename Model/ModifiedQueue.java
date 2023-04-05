package Model;

import java.util.ArrayList;
import java.util.List;

public class ModifiedQueue<T> {

  private List<T> list;
  private int index;

  public ModifiedQueue() {
    this.list = new ArrayList<T>();
    this.index = 0;
  }

  public void addToQueue(T object) {
    if (this.list.size() - 1 == this.index) {
      this.list.add(object);
      return;
    }

    for (int i = this.index; i < this.list.size(); i++) {
      this.list.remove(i);
    }

    this.list.add(object);

  }

  public T moveForward() {
    if (this.list.size() - 1 == this.index) {
      return this.list.get(this.index);
    }

    this.index += 1;
    return this.list.get(this.index);
  }

  public T moveBackward() {
    if (this.index <= 0)
      return null;

    this.index -= 1;
    return this.list.get(this.index);
  }

}
