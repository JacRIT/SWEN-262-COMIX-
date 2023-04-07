package UI.Concrete;

import Model.Search.SortAlgorithm;
import Model.Search.ConcreteSorts.DateSort;
import Model.Search.ConcreteSorts.DefaultSort;
import Model.Search.ConcreteSorts.IssueNumberSort;
import Model.Search.ConcreteSorts.TitleSort;
import Model.Search.ConcreteSorts.VolumeSort;
import UI.Interfaces.Factory;

public class SortFactory implements Factory<SortAlgorithm> {

  @Override
  public SortAlgorithm createAlgorithim(String input) {

    if (input == null || input.length() == 0) {
      return new DefaultSort();
    }
    if (input.equals("title")) {
      return new TitleSort();
    }
    if (input.equals("publication")) {
      return new DateSort();
    }
    if (input.equals("issue")) {
      return new IssueNumberSort();
    }
    if (input.equals("volume")) {
      return new VolumeSort();
    }
    return null;

  }

}
