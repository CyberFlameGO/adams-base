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
 * PartialLeastSquaresTab.java
 * Copyright (C) 2016 University of Waikato, Hamilton, NZ
 */

package adams.gui.tools.wekainvestigator.tab;

import adams.core.Index;
import adams.core.MessageCollection;
import adams.core.Properties;
import adams.core.Range;
import adams.core.base.BaseRegExp;
import adams.data.instancesanalysis.PLS;
import adams.data.instancesanalysis.PLS.Algorithm;
import adams.data.sequence.XYSequence;
import adams.data.sequence.XYSequencePoint;
import adams.data.sequence.XYSequencePointComparator.Comparison;
import adams.data.spreadsheet.Row;
import adams.data.spreadsheet.SpreadSheet;
import adams.data.weka.WekaAttributeRange;
import adams.flow.sink.sequenceplotter.SequencePlotterPanel;
import adams.gui.core.BaseSplitPane;
import adams.gui.core.BaseTabbedPane;
import adams.gui.core.NumberTextField;
import adams.gui.core.NumberTextField.Type;
import adams.gui.core.ParameterPanel;
import adams.gui.event.WekaInvestigatorDataEvent;
import adams.gui.tools.wekainvestigator.InvestigatorPanel;
import adams.gui.tools.wekainvestigator.data.DataContainer;
import adams.gui.tools.wekainvestigator.job.InvestigatorTabJob;
import adams.gui.visualization.core.AxisPanel;
import adams.gui.visualization.core.axis.FancyTickGenerator;
import adams.gui.visualization.core.plot.Axis;
import adams.gui.visualization.sequence.LinePaintlet;
import adams.gui.visualization.sequence.XYSequenceContainer;
import adams.gui.visualization.sequence.XYSequenceContainerManager;
import adams.gui.visualization.stats.scatterplot.AbstractScatterPlotOverlay;
import adams.gui.visualization.stats.scatterplot.Coordinates;
import adams.gui.visualization.stats.scatterplot.ScatterPlot;
import adams.gui.visualization.stats.scatterplot.action.ViewDataClickAction;
import weka.core.Instances;

import javax.swing.ComboBoxModel;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Visualizes the PLS loadings and PLS space calculated from the selected
 * dataset.
 *
 * @author FracPete (fracpete at waikato dot ac dot nz)
 * @version $Revision$
 */
public class PartialLeastSquaresTab
  extends AbstractInvestigatorTab {

  private static final long serialVersionUID = -4106630131554796889L;

  public static final String KEY_LEFTPANELWIDTH = "leftpanelwidth";

  public static final String KEY_DATASET = "dataset";

  public static final String KEY_RANGE = "range";

  public static final String KEY_ALGORITHM = "algorithm";

  public static final String KEY_COMPONENTS = "components";

  /** the split pane. */
  protected BaseSplitPane m_SplitPane;

  /** the left panel. */
  protected JPanel m_PanelLeft;

  /** the right panel. */
  protected JPanel m_PanelRight;

  /** the parameter panel. */
  protected ParameterPanel m_PanelParameters;

  /** the datasets model. */
  protected DefaultComboBoxModel<String> m_ModelDatasets;

  /** the datasets. */
  protected JComboBox<String> m_ComboBoxDatasets;

  /** the attribute range. */
  protected JTextField m_TextAttributeRange;

  /** the algorithm. */
  protected JComboBox<Algorithm> m_ComboBoxAlgorithm;

  /** the number of components. */
  protected NumberTextField m_TextNumComponents;

  /** the button to start PLS. */
  protected JButton m_ButtonStart;

  /** the button to stop PLS. */
  protected JButton m_ButtonStop;

  /** the tabbed pane for the plots. */
  protected BaseTabbedPane m_TabbedPanePlots;

  /** the loadings plot. */
  protected ScatterPlot m_PanelLoadings;

  /** the scores plot. */
  protected ScatterPlot m_PanelScores;

  /** the plot of the loadings. */
  protected SequencePlotterPanel m_PanelWeights;

  /**
   * Initializes the members.
   */
  @Override
  protected void initialize() {
    super.initialize();

    m_ModelDatasets = new DefaultComboBoxModel<>();
  }

  /**
   * Initializes the widgets.
   */
  @Override
  protected void initGUI() {
    Properties 		props;
    JPanel		panelOptions;
    JPanel		panelButtons;
    AxisPanel		axis;
    FancyTickGenerator	tick;

    super.initGUI();

    props = InvestigatorPanel.getProperties();

    m_SplitPane = new BaseSplitPane(BaseSplitPane.HORIZONTAL_SPLIT);
    m_SplitPane.setDividerLocation(props.getInteger("PartialLeastSquares.LeftPanelWidth", 200));
    m_SplitPane.setOneTouchExpandable(true);
    m_ContentPanel.add(m_SplitPane, BorderLayout.CENTER);

    m_PanelLeft = new JPanel(new BorderLayout());
    m_PanelRight = new JPanel(new BorderLayout());
    m_SplitPane.setLeftComponent(m_PanelLeft);
    m_SplitPane.setRightComponent(m_PanelRight);

    panelOptions = new JPanel(new BorderLayout());
    m_PanelLeft.add(panelOptions, BorderLayout.NORTH);

    m_PanelParameters = new ParameterPanel();
    panelOptions.add(m_PanelParameters, BorderLayout.CENTER);

    m_ComboBoxDatasets = new JComboBox<>(m_ModelDatasets);
    m_PanelParameters.addParameter("Dataset", m_ComboBoxDatasets);

    m_TextAttributeRange = new JTextField(20);
    m_TextAttributeRange.setText(Range.ALL);
    m_TextAttributeRange.getDocument().addDocumentListener(new DocumentListener() {
      @Override
      public void insertUpdate(DocumentEvent e) {
	updateButtons();
      }
      @Override
      public void removeUpdate(DocumentEvent e) {
	updateButtons();
      }
      @Override
      public void changedUpdate(DocumentEvent e) {
	updateButtons();
      }
    });
    m_PanelParameters.addParameter("Range", m_TextAttributeRange);

    m_ComboBoxAlgorithm = new JComboBox<>(Algorithm.values());
    m_ComboBoxAlgorithm.setSelectedItem(Algorithm.valueOf(props.getProperty("PartialLeastSquares.Algorithm", "SIMPLS")));
    if (m_ComboBoxAlgorithm.getSelectedIndex() == -1)
      m_ComboBoxAlgorithm.setSelectedIndex(0);
    m_ComboBoxAlgorithm.addActionListener((ActionEvent e) -> {
      m_PanelLoadings.setXRegExp(new BaseRegExp(m_ComboBoxAlgorithm.getSelectedItem() + "_1"));
      m_PanelLoadings.setYRegExp(new BaseRegExp(m_ComboBoxAlgorithm.getSelectedItem() + "_2"));
    });
    m_PanelParameters.addParameter("Algorithm", m_ComboBoxAlgorithm);

    m_TextNumComponents = new NumberTextField(Type.INTEGER, 20);
    m_TextNumComponents.setValue(props.getInteger("PartialLeastSquares.NumComponents", 20));
    m_PanelParameters.addParameter("Components", m_TextNumComponents);

    // buttons
    panelButtons = new JPanel(new FlowLayout(FlowLayout.LEFT));
    panelOptions.add(panelButtons, BorderLayout.SOUTH);

    m_ButtonStart = new JButton("Start");
    m_ButtonStart.addActionListener((ActionEvent e) -> startExecution());
    panelButtons.add(m_ButtonStart);

    m_ButtonStop = new JButton("Stop");
    m_ButtonStop.addActionListener((ActionEvent e) -> stopExecution());
    panelButtons.add(m_ButtonStop);

    // the plots
    m_TabbedPanePlots = new BaseTabbedPane();
    m_PanelRight.add(m_TabbedPanePlots, BorderLayout.CENTER);

    m_PanelLoadings = new ScatterPlot();
    m_PanelLoadings.setXRegExp(new BaseRegExp(m_ComboBoxAlgorithm.getSelectedItem() + "_1"));
    m_PanelLoadings.setYRegExp(new BaseRegExp(m_ComboBoxAlgorithm.getSelectedItem() + "_2"));
    m_PanelLoadings.getPlot().getAxis(Axis.LEFT).setTopMargin(0.01);
    m_PanelLoadings.getPlot().getAxis(Axis.LEFT).setBottomMargin(0.01);
    m_PanelLoadings.getPlot().getAxis(Axis.BOTTOM).setTopMargin(0.01);
    m_PanelLoadings.getPlot().getAxis(Axis.BOTTOM).setBottomMargin(0.01);
    m_PanelLoadings.setMouseClickAction(new ViewDataClickAction());
    m_PanelLoadings.setOverlays(new AbstractScatterPlotOverlay[]{
      new Coordinates()
    });
    m_TabbedPanePlots.addTab("Loadings", m_PanelLoadings);

    m_PanelWeights = new SequencePlotterPanel("Weights");
    m_PanelWeights.setPaintlet(new LinePaintlet());
    // x
    axis = m_PanelWeights.getPlot().getAxis(Axis.BOTTOM);
    axis.setAxisName("attribute");
    axis.setNumberFormat("0");
    tick = new FancyTickGenerator();
    tick.setNumTicks(40);
    axis.setTickGenerator(tick);
    axis.setNthValueToShow(4);
    // y
    axis = m_PanelWeights.getPlot().getAxis(Axis.LEFT);
    axis.setAxisName("weight");
    axis.setNumberFormat("0.000");
    tick = new FancyTickGenerator();
    tick.setNumTicks(20);
    axis.setTickGenerator(tick);
    axis.setNthValueToShow(2);
    m_TabbedPanePlots.addTab("Weights", m_PanelWeights);

    m_PanelScores = new ScatterPlot();
    m_PanelScores.setXIndex(new Index("1"));
    m_PanelScores.setYIndex(new Index("2"));
    m_PanelScores.getPlot().getAxis(Axis.LEFT).setTopMargin(0.01);
    m_PanelScores.getPlot().getAxis(Axis.LEFT).setBottomMargin(0.01);
    m_PanelScores.getPlot().getAxis(Axis.BOTTOM).setTopMargin(0.01);
    m_PanelScores.getPlot().getAxis(Axis.BOTTOM).setBottomMargin(0.01);
    m_PanelScores.setMouseClickAction(new ViewDataClickAction());
    m_PanelScores.setOverlays(new AbstractScatterPlotOverlay[]{
      new Coordinates()
    });
    m_TabbedPanePlots.addTab("Scores", m_PanelScores);
  }

  /**
   * finishes the initialization.
   */
  @Override
  protected void finishInit() {
    super.finishInit();
    updateButtons();
  }

  /**
   * Returns the title of this table.
   *
   * @return		the title
   */
  @Override
  public String getTitle() {
    return "PLS";
  }

  /**
   * Returns the icon name for the tab icon.
   *
   * @return		the icon name, null if not available
   */
  public String getTabIcon() {
    return "scatterplot.gif";
  }

  /**
   * Determines the index of the old dataset name in the current dataset model.
   *
   * @param oldDataset	the old dataset to look for
   * @return		the index, -1 if not found
   */
  protected int indexOfDataset(String oldDataset) {
    int 		result;
    int			i;
    DataContainer data;

    result = -1;

    if (oldDataset != null)
      oldDataset = oldDataset.replaceAll("^[0-9]+: ", "");
    for (i = 0; i < getOwner().getData().size(); i++) {
      data = getOwner().getData().get(i);
      if ((oldDataset != null) && data.getData().relationName().equals(oldDataset)) {
	result = i;
	break;
      }
    }

    return result;
  }

  /**
   * Checks whether the data has changed and the model needs updating.
   *
   * @param newDatasets		the new list of datasets
   * @param currentModel	the current model
   * @return			true if changed
   */
  protected boolean hasDataChanged(List<String> newDatasets, ComboBoxModel<String> currentModel) {
    boolean	result;
    int		i;
    Set<String> setDatasets;
    Set<String>	setModel;

    setDatasets = new HashSet<>(newDatasets);
    setModel    = new HashSet<>();
    for (i = 0; i < currentModel.getSize(); i++)
      setModel.add(currentModel.getElementAt(i));

    result = (setDatasets.size() != setModel.size())
      || !(setDatasets.containsAll(setModel) && setModel.containsAll(setDatasets));

    return result;
  }

  /**
   * Generates the list of datasets for a combobox.
   *
   * @return		the list
   */
  protected List<String> generateDatasetList() {
    List<String> 	result;
    int			i;
    DataContainer 	data;

    result = new ArrayList<>();
    for (i = 0; i < getOwner().getData().size(); i++) {
      data = getOwner().getData().get(i);
      result.add((i + 1) + ": " + data.getData().relationName());
    }

    return result;
  }

  /**
   * Notifies the tab that the data changed.
   *
   * @param e		the event
   */
  @Override
  public void dataChanged(WekaInvestigatorDataEvent e) {
    List<String>	datasets;
    int			index;

    if (e.getType() == WekaInvestigatorDataEvent.ROW_ACTIVATED) {
      m_ComboBoxDatasets.setSelectedIndex(e.getRows()[0]);
      return;
    }

    datasets = generateDatasetList();
    index    = indexOfDataset((String) m_ComboBoxDatasets.getSelectedItem());
    if (hasDataChanged(datasets, m_ModelDatasets)) {
      m_ModelDatasets = new DefaultComboBoxModel<>(datasets.toArray(new String[datasets.size()]));
      m_ComboBoxDatasets.setModel(m_ModelDatasets);
      if ((index == -1) && (m_ModelDatasets.getSize() > 0))
	m_ComboBoxDatasets.setSelectedIndex(0);
      else if (index > -1)
	m_ComboBoxDatasets.setSelectedIndex(index);
    }
    updateButtons();
  }

  /**
   * Returns whether the tab is busy.
   *
   * @return		true if busy
   */
  public boolean isBusy() {
    return (m_Worker != null);
  }

  /**
   * Updates the buttons.
   */
  protected void updateButtons() {
    String	rangeStr;
    Instances	data;
    int 	algorithm;

    algorithm = m_ComboBoxAlgorithm.getSelectedIndex();
    rangeStr = m_TextAttributeRange.getText();
    if (m_ComboBoxDatasets.getSelectedIndex() > -1)
      data = getData().get(m_ComboBoxDatasets.getSelectedIndex()).getData();
    else
      data = null;

    m_ButtonStart.setEnabled(
      !isBusy()
	&& (algorithm > -1)
	&& (data != null)
	&& (data.classIndex() > -1)
	&& !rangeStr.isEmpty()
	&& Range.isValid(rangeStr, data.numAttributes()));
    m_ButtonStop.setEnabled(isBusy());
  }

  /**
   * Generates PLS visualization.
   */
  protected void startExecution() {
    startExecution(new InvestigatorTabJob(this, "PLS visualization") {
      @Override
      protected void doRun() {
	try {
	  DataContainer datacont = getData().get(m_ComboBoxDatasets.getSelectedIndex());
	  PLS pls = new PLS();
	  pls.setAttributeRange(new WekaAttributeRange(m_TextAttributeRange.getText()));
	  pls.setAlgorithm((Algorithm) m_ComboBoxAlgorithm.getSelectedItem());
	  pls.setNumComponents(m_TextNumComponents.getValue().intValue());
	  String msg = pls.analyze(datacont.getData());
	  if (msg != null) {
	    logError(msg, "PLS Error");
	  }
	  else {
	    // loadings (scatter)
	    SpreadSheet loadings = pls.getLoadings();
	    m_PanelLoadings.setData(loadings);
	    m_PanelLoadings.reset();
	    // loadings (weights)
	    XYSequenceContainerManager manager = m_PanelWeights.getContainerManager();
	    double min = Double.POSITIVE_INFINITY;
	    double max = Double.NEGATIVE_INFINITY;
	    manager.clear();
	    manager.startUpdate();
	    for (int c = 0; c < loadings.getColumnCount(); c++) {
	      XYSequence seq = new XYSequence();
	      seq.setComparison(Comparison.X_AND_Y);
	      seq.setID(loadings.getColumnName(c));
	      XYSequenceContainer cont = manager.newContainer(seq);
	      manager.add(cont);
	      for (int r = 0; r < loadings.getRowCount(); r++) {
		Row row = loadings.getRow(r);
		double value = row.getCell(c).toDouble();
		min = Math.min(min, value);
		max = Math.max(max, value);
		XYSequencePoint point = new XYSequencePoint("" + seq.size(), seq.size(), value);
		seq.add(point);
	      }
	    }
	    manager.finishUpdate();
	    // scores (scatter)
	    m_PanelScores.setData(pls.getScores());
	    m_PanelScores.reset();
	  }
	}
	catch (Throwable t) {
	  logError("Failed to perform PLS!", t, "PLS error");
	}
      }
    });
  }

  /**
   * Hook method that gets called after successfully starting a job.
   *
   * @param job		the job that got started
   */
  @Override
  protected void postStartExecution(InvestigatorTabJob job) {
    super.postStartExecution(job);
    updateButtons();
  }

  /**
   * Hook method that gets called after stopping a job.
   */
  @Override
  protected void postStopExecution() {
    super.postStopExecution();
    logMessage("Stopped PLS visualization");
    updateButtons();
  }

  /**
   * Hook method that gets called after stopping a job.
   */
  @Override
  protected void postExecutionFinished() {
    super.postExecutionFinished();
    updateButtons();
  }

  /**
   * Returns the objects for serialization.
   *
   * @return		the mapping of the objects to serialize
   */
  protected Map<String,Object> doSerialize() {
    Map<String,Object>	result;

    result = super.doSerialize();
    result.put(KEY_LEFTPANELWIDTH, m_SplitPane.getDividerLocation());
    result.put(KEY_DATASET, m_ComboBoxDatasets.getSelectedIndex());
    result.put(KEY_RANGE, m_TextAttributeRange.getText());
    result.put(KEY_ALGORITHM, m_ComboBoxAlgorithm.getSelectedIndex());
    result.put(KEY_COMPONENTS, m_TextNumComponents.getValue().intValue());

    return result;
  }

  /**
   * Restores the objects.
   *
   * @param data	the data to restore
   * @param errors	for storing errors
   */
  protected void doDeserialize(Map<String,Object> data, MessageCollection errors) {
    super.doDeserialize(data, errors);
    if (data.containsKey(KEY_LEFTPANELWIDTH))
      m_SplitPane.setDividerLocation((int) data.get(KEY_LEFTPANELWIDTH));
    if (data.containsKey(KEY_DATASET))
      m_ComboBoxDatasets.setSelectedIndex((int) data.get(KEY_DATASET));
    if (data.containsKey(KEY_RANGE))
      m_TextAttributeRange.setText((String) data.get(KEY_RANGE));
    if (data.containsKey(KEY_ALGORITHM))
      m_ComboBoxAlgorithm.setSelectedIndex((int) data.get(KEY_ALGORITHM));
    if (data.containsKey(KEY_COMPONENTS))
      m_TextNumComponents.setValue((int) data.get(KEY_COMPONENTS));
  }
}
