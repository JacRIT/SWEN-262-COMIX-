package UI.Interfaces;

public interface Factory<T> {

  /**
   * Instantiate the correct algorithim based upon the input given
   * 
   * @param input String representation of the name of the algorithim to be
   *              created
   * @return Selected Algorithim of type T
   */
  public T createAlgorithim(String input);

}
