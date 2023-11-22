import cs3500.marblesolitaire.controller.Features;
import cs3500.marblesolitaire.controller.GUIController;
import cs3500.marblesolitaire.model.hw02.EnglishSolitaireModel;
import cs3500.marblesolitaire.model.hw02.MarbleSolitaireModel;
import srcLab.cs3500.marblesolitaire.view.MarbleSolitaireGuiView;
import srcLab.cs3500.marblesolitaire.view.SwingGuiView;

public class GUIDriver {


  //Run the game here.
  public static void main(String[] args) {
    MarbleSolitaireModel model = new EnglishSolitaireModel();
    MarbleSolitaireGuiView view = new SwingGuiView(model);
    Features controller = new GUIController(model, view);
  }
}
