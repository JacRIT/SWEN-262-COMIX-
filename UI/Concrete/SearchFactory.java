package UI.Concrete;

import Model.Search.SearchAlgorithm;
import Model.Search.ConcreteSearches.ExactKeywordSearch;
import Model.Search.ConcreteSearches.ExactNumberSearch;
import Model.Search.ConcreteSearches.GapSearch;
import Model.Search.ConcreteSearches.PartialKeywordSearch;
import Model.Search.ConcreteSearches.RunSearch;
import Model.Search.ConcreteSearches.ValueSearch;
import UI.Interfaces.Factory;

public class SearchFactory implements Factory<SearchAlgorithm> {

  public SearchFactory() {
  }

  @Override
  public SearchAlgorithm createAlgorithim(String input) {

    if (input == null || input.equals("partial-search") || input.equals("partial") || input.length() == 0) {
      return new PartialKeywordSearch();
    }
    if (input.equals("exact-search") || input.equals("exact")) {
      return new ExactKeywordSearch();
    }
    if (input.equals("exact-number")) {
      return new ExactNumberSearch();
    }
    if (input.equals("value")) {
      return new ValueSearch();
    }

    if (input.equals("run-search"))
      return new RunSearch();

    if (input.equals("gap-search"))
      return new GapSearch();

    return null;
  }

}
