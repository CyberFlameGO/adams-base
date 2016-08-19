/*
 *   This program is free software: you can redistribute it and/or modify
 *   it under the terms of the GNU General Public License as published by
 *   the Free Software Foundation, either version 3 of the License, or
 *   (at your option) any later version.
 *
 *   This program is distributed in the hope that it will be useful,
 *   but WITHOUT ANY WARRANTY; without even the implied warranty of
 *   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *   GNU General Public License for more details.
 *
 *   You should have received a copy of the GNU General Public License
 *   along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

/**
 * WekaInvestigatorPreferencesPanel.java
 * Copyright (C) 2016 University of Waikato, Hamilton, New Zealand
 */
package adams.gui.application;

import adams.data.weka.classattribute.LastAttribute;
import adams.env.Environment;
import adams.env.WekaInvestigatorDefinition;
import adams.gui.core.PropertiesParameterPanel.PropertyType;
import adams.gui.goe.GenericArrayEditorPanel;
import adams.gui.goe.GenericObjectEditorPanel;
import adams.gui.goe.WekaGenericObjectEditorPanel;

/**
 * Preferences for the WEKA Investigator.
 * 
 * @author  fracpete (fracpete at waikato dot ac dot nz)
 * @version $Revision$
 */
public class WekaInvestigatorPreferencesPanel
  extends AbstractPropertiesPreferencesPanel {

  /** for serialization. */
  private static final long serialVersionUID = 3895159356677639564L;
  
  @Override
  protected void initGUI() {
    super.initGUI();

    // general
    addPropertyType("General.DefaultTabs", PropertyType.COMMA_SEPARATED_LIST);
    addPropertyType("General.DefaultDataTableHeight", PropertyType.INTEGER);
    addPropertyType("General.ClassAttributeHeuristic", PropertyType.OBJECT_EDITOR);
    setChooser("General.ClassAttributeHeuristic", new GenericObjectEditorPanel(adams.data.weka.classattribute.AbstractClassAttributeHeuristic.class, new LastAttribute(), true));

    // preprocessing
    addPropertyType("Preprocess.Filter", PropertyType.OBJECT_EDITOR);
    setChooser("Preprocess.Filter", new WekaGenericObjectEditorPanel(weka.filters.Filter.class, new weka.filters.AllFilter(), true));
    addPropertyType("Preprocess.ReplaceDatasets", PropertyType.BOOLEAN);
    addPropertyType("Preprocess.KeepName", PropertyType.BOOLEAN);
    addPropertyType("Preprocess.BatchFilter", PropertyType.BOOLEAN);

    // classify
    addPropertyType("Classify.Classifier", PropertyType.OBJECT_EDITOR);
    setChooser("Classify.Classifier", new WekaGenericObjectEditorPanel(weka.classifiers.Classifier.class, new weka.classifiers.rules.ZeroR(), true));
    addPropertyType("Classify.LeftPanelWidth", PropertyType.INTEGER);
    addPropertyType("Classify.NumFolds", PropertyType.INTEGER);
    addPropertyType("Classify.CrossValidationFinalModel", PropertyType.BOOLEAN);
    addPropertyType("Classify.Seed", PropertyType.INTEGER);
    addPropertyType("Classify.TrainPercentage", PropertyType.DOUBLE);
    addPropertyType("Classify.PreserveOrder", PropertyType.BOOLEAN);
    addPropertyType("Classify.DefaultOutputGenerators", PropertyType.ARRAY_EDITOR);
    setChooser("Classify.DefaultOutputGenerators", new GenericArrayEditorPanel(new adams.gui.tools.wekainvestigator.tab.classifytab.output.AbstractOutputGenerator[0]));
    setArrayClass("Classify.DefaultOutputGenerators", adams.gui.tools.wekainvestigator.tab.classifytab.output.AbstractOutputGenerator.class);
    setArraySeparator("Classify.DefaultOutputGenerators", " ");

    // clusterer
    addPropertyType("Cluster.Clusterer", PropertyType.OBJECT_EDITOR);
    setChooser("Cluster.Clusterer", new WekaGenericObjectEditorPanel(weka.clusterers.Clusterer.class, new weka.clusterers.SimpleKMeans(), true));
    addPropertyType("Cluster.LeftPanelWidth", PropertyType.INTEGER);
    addPropertyType("Cluster.NumFolds", PropertyType.INTEGER);
    addPropertyType("Cluster.CrossValidationFinalModel", PropertyType.BOOLEAN);
    addPropertyType("Cluster.Seed", PropertyType.INTEGER);
    addPropertyType("Cluster.TrainPercentage", PropertyType.DOUBLE);
    addPropertyType("Cluster.PreserveOrder", PropertyType.BOOLEAN);
    addPropertyType("Cluster.DefaultOutputGenerators", PropertyType.ARRAY_EDITOR);
    setChooser("Cluster.DefaultOutputGenerators", new GenericArrayEditorPanel(new adams.gui.tools.wekainvestigator.tab.clustertab.output.AbstractOutputGenerator[0]));
    setArrayClass("Cluster.DefaultOutputGenerators", adams.gui.tools.wekainvestigator.tab.clustertab.output.AbstractOutputGenerator.class);
    setArraySeparator("Cluster.DefaultOutputGenerators", " ");

    setPreferences(Environment.getInstance().read(WekaInvestigatorDefinition.KEY));
  }
  
  /**
   * The title of the preferences.
   * 
   * @return		the title
   */
  @Override
  public String getTitle() {
    return "WEKA Investigator";
  }

  /**
   * Returns whether the panel requires a wrapper scrollpane/panel for display.
   * 
   * @return		true if wrapper required
   */
  @Override
  public boolean requiresWrapper() {
    return false;
  }

  /**
   * Activates the settings.
   * 
   * @return		null if successfully activated, otherwise error message
   */
  @Override
  public String activate() {
    if (Environment.getInstance().write(WekaInvestigatorDefinition.KEY, getPreferences()))
      return null;
    else
      return "Failed to save Weka Investigator setup!";
  }
}
