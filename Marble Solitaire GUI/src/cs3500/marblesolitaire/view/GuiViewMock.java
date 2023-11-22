package cs3500.marblesolitaire.view;

import java.util.List;

import cs3500.marblesolitaire.controller.Features;

import srcLab.cs3500.marblesolitaire.view.MarbleSolitaireGuiView;

/**
 * represents a mock GUI view class for testing
 */
public class GuiViewMock implements MarbleSolitaireGuiView {
  private final List<String> log;

  /**
   * creates a mock swing gui view with a list to log processed methods
   *
   * @param log list of methods being applied
   */
  public GuiViewMock(List<String> log) {
    this.log = log;
  }

  @Override
  public void refresh() {
    this.log.add(String.format("refresh()"));
  }

  @Override
  public void renderMessage(String message) {
    this.log.add(String.format("renderMessage(%s)", message));
  }

  //@Override
  public void setFeatures(Features callBack) {
    this.log.add(String.format("called setFeatures()"));
  }
}