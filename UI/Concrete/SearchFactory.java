package UI.Concrete;

import Model.Search.SearchAlgorithm;
import Model.Search.ConcreteSearches.ExactKeywordSearch;
import Model.Search.ConcreteSearches.ExactNumberSearch;
import Model.Search.ConcreteSearches.PartialKeywordSearch;
import Model.Search.ConcreteSearches.ValueSearch;
import UI.Interfaces.Factory;

public class SearchFactory implements Factory<SearchAlgorithm> {

  public SearchFactory() {
  }

  @Override
  public SearchAlgorithm createAlgorithim(String input) {

    if (input == null || input.equals("partial-search") || input.length() == 0) {
      return new PartialKeywordSearch();
    }
    if (input.equals("exact-search")) {
      return new ExactKeywordSearch();
    }
    if (input.equals("exact-number")) {
      return new ExactNumberSearch();
    }
    if (input.equals("value")) {
      return new ValueSearch();
    }
    return null;
  }

}
